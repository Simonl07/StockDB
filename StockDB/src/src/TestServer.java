package src;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.json.JSONObject;

public class TestServer
{

	private static Connection connection = null;

	public static void main(String[] args) throws FileNotFoundException, SQLException, IOException
	{
		connection = new DatabaseConnector().getConnection();
		Server server = new Server(80);

		ServletHandler handler = new ServletHandler();
		handler.addServletWithMapping(new ServletHolder(new PostServlet()), "/");
		handler.addServletWithMapping(new ServletHolder(new PriceUpdate()), "/live");
		handler.addServletWithMapping(new ServletHolder(new ListServlet(connection)), "/list");

		server.setHandler(handler);

		try
		{
			System.out.println("start");
			server.start();
			server.join();
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	
	public static class PostServlet extends HttpServlet
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
		{
			response.setContentType("text/html");
			response.setStatus(HttpServletResponse.SC_OK);

			// log.info("MessageServlet ID " + this.hashCode() + " handling GET
			// request.");
		}

		private Map<String, String> getHeadersInfo(HttpServletRequest request)
		{

			Map<String, String> map = new HashMap<String, String>();

			Enumeration headerNames = request.getHeaderNames();
			while (headerNames.hasMoreElements())
			{
				String key = (String) headerNames.nextElement();
				String value = request.getHeader(key);
				map.put(key, value);
			}

			return map;
		}

		private Logger log = LogManager.getLogger();

		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
		{
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html");
			response.setStatus(HttpServletResponse.SC_OK);
			getHeadersInfo(request);

			JSONObject json = getJSON(request);
			
			StockInsertionUtils.parse(connection, json);
		}

		

	}
	
	public static class PriceUpdate extends HttpServlet
	{
		private StockPriceIndex priceIndex;
		
		public PriceUpdate(){
			priceIndex = new StockPriceIndex();
		}
		
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
		{
			response.setContentType("text/html");
			response.setStatus(HttpServletResponse.SC_OK);
			
			PrintWriter writer = response.getWriter();
			
			writer.write("<html><body>");
			for(Stock s: priceIndex.getStocks()){
				writer.write("<p> <Strong>" + s.getNAME_SHORT() + ":</Strong> <br />"
						+ "&nbsp;&nbsp;&nbsp;&nbsp; Price: " + s.getPrice() 
						+ "&nbsp;&nbsp;&nbsp;&nbsp; Volume: " + s.getVolume() + "</p>");
			}
			writer.write("</html></body>");
		}
		
		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
		{
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html");
			response.setStatus(HttpServletResponse.SC_OK);

			JSONObject json = getJSON(request);
			
			String name_full = json.getString("name_full");
			String name_short = json.getString("name_short");
			Double price = Double.parseDouble(json.getString("price"));
			Double volume = Double.parseDouble(json.getString("volume"));
			
			priceIndex.put(name_full, name_short, price, volume);
		}
		
		
	}
	
	
	private static JSONObject getJSON(HttpServletRequest request)
	{
		BufferedReader reader;
		String content = "";
		try
		{
			reader = request.getReader();
			String line = "";
			content = "";
			while ((line = reader.readLine()) != null)
			{
				content += line;
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		
		return new JSONObject(content);

	}

	/**
	 * ========================================================================
	 * =============================DANGER ZONE================================
	 * =============================DANGER ZONE================================
	 * =============================DANGER ZONE================================
	 * ========================================================================
	 * 
	 */
	/**
	 * WARNING. This is method will delete all rows within the given table and
	 * reset autoIncrement. This is only for testing and debugging purpose. The
	 * method will be removed once full features are build and deployed.
	 * 
	 * @param connection: database connection
	 * @param tablename: table wish to be cleared
	 * @param hasAutoIncrement: if table contains auto increment
	 */
	public static void DELETE_ALL_ROWS(Connection connection, String tablename)
	{
		try
		{
			// System.out.println("TABLE " + tablename + " is about to be
			// deleted");
			// new Scanner(System.in).nextLine();
			connection.createStatement().execute("TRUNCATE TABLE " + tablename);
		} catch (SQLException e)
		{
			System.out.println("Encountered a SQL error while performing deletion");
		}
	}

}
