import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

public class DeleteServlet extends HttpServlet
{
	private static Connection connection;
	
	public DeleteServlet(Connection connection){
		this.connection = connection;
	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		JSONObject json = StockInsertionUtils.httpRequest2JSON(request);
		StockInsertionUtils.removeStock(connection, json.getString("stock"));
		
	}
}
