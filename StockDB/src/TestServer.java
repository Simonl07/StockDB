import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
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
import org.json.JSONObject;

public class TestServer
{

	private static Connection connection = null;
	
	
	public static void main(String[] args) throws FileNotFoundException, SQLException, IOException
	{
		connection = new DatabaseConnector().getConnection();
		Server server = new Server(80);
		
		ServletHandler handler = new ServletHandler();
		handler.addServletWithMapping(PostServlet.class, "/");
		
		server.setHandler(handler);
		
		try
		{
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
			
			//log.info("MessageServlet ID " + this.hashCode() + " handling GET request.");
		}
		
		
		private Map<String, String> getHeadersInfo(HttpServletRequest request) {

	        Map<String, String> map = new HashMap<String, String>();

	        Enumeration headerNames = request.getHeaderNames();
	        while (headerNames.hasMoreElements()) {
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
			
			
			//log.info("MessageServlet ID " + this.hashCode() + " handling POST request.");
			
			
			BufferedReader reader = request.getReader();
			
			String line = "";
			String content = "";
			while((line = reader.readLine()) != null)
			{
				content += line;
			}
			
			
			JSONObject json = new JSONObject(content);
			
			Map<String, Object> map = json.toMap();
			
			System.out.println(map);
			
			

			String sql = "INSERT INTO cumulative VALUES(null, ?, null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ";
			
			
			PreparedStatement s;
			try
			{
				s = connection.prepareStatement(sql);
				s.setString(1, getID(json.getString("name_short")));
				s.setString(2,  json.getString("name_full"));
				s.setString(3,  json.getString("name_short"));
				s.setFloat(4,  Float.parseFloat(json.getString("price_close")));
				s.setFloat(5, Float.parseFloat(json.getString("price_open")));
				s.setString(6, json.getString("range_day"));
				s.setFloat(7, Float.parseFloat(json.getString("range_day_high")));
				s.setFloat(8, Float.parseFloat(json.getString("range_day_low")));
				s.setString(9, json.getString("range_52w"));
				s.setFloat(10, Float.parseFloat(json.getString("range_52w_high")));
				s.setFloat(11, Float.parseFloat(json.getString("range_52w_low")));
				s.setFloat(12, Long.parseLong(json.getString("volume")));
				s.setFloat(13, Long.parseLong(json.getString("volume_avg")));
				s.setFloat(11, Long.parseLong(json.getString("volume")));
				s.setFloat(11, Long.parseLong(json.getString("market_cap")));
				s.setFloat(11, Float.parseFloat(json.getString("range_52w_low")));
				
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			
			
			System.out.println();
		}

	}
	
	
	private static String getID(String name) throws SQLException{
		String sql = "SELECT * FROM id_name WHERE name_short = ?";
		PreparedStatement s = connection.prepareStatement(sql);
		s.setString(1, name);
		List<List<String>> result = executeQuery(connection, s);
		
		assert result.get(0).size() == 1;
		assert !result.get(0).get(0).equals("");
		
		return result.get(0).get(0);
	}
	
	
	/**
	 * Utility method to execute a sql query, and parse the result into a java
	 * 2D ArrayList that represents the output of the query.
	 * 
	 * !!Need testing. if testing success and stable, move to databaseConnector
	 * class Executor class
	 * 
	 * @param sql: query to execute
	 * @return A List of a list of Strings that represents each cell.
	 * @throws SQLException
	 */
	public static List<List<String>> executeQuery(Connection connection, PreparedStatement sql) throws SQLException
	{
		try (ResultSet results = sql.executeQuery();)
		{
			ResultSetMetaData meta = results.getMetaData();
			int size = meta.getColumnCount();
			List<List<String>> output = new ArrayList<>();
			while (results.next())
			{
				List<String> temp = new ArrayList<>();
				for (int i = 1; i <= size; i++)
				{
					temp.add(results.getString(meta.getColumnName(i)));
				}
				output.add(temp);
			}

			return output;
		}
	}
	

}

