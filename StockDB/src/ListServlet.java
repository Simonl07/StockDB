import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

public class ListServlet extends HttpServlet
{
	private static Connection connection;
	
	public ListServlet(Connection connection){
		this.connection = connection;
	}
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_OK);

		// log.info("MessageServlet ID " + this.hashCode() + " handling GET
		// request.");
		
		JSONObject json = StockInsertionUtils.getStockList(connection);
		PrintWriter writer = response.getWriter();
		writer.write(json.toString());
		
	}
}
