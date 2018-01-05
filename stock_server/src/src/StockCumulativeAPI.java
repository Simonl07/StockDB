package src;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.json.JSONArray;
import org.json.JSONObject;

public class StockCumulativeAPI extends HttpServlet
{

	private Connection dbConnection;
	public static final String PATH = "/search/api";

	/**
	 * HousingSearch Servlet constructor, Initialize dbconnection instance from
	 * DatabaseConnector
	 * 
	 * @param connection: a connection object for connecting database
	 */
	public StockCumulativeAPI(Connection connection)
	{
		this.dbConnection = connection;
		LogManager.getLogger().info("SearchAPIServlet initialized" + new java.util.Date().toString());
	}

	/**
	 * DoGet method, handles get request from client, takes parameters for
	 * status update.
	 * 
	 * //will add later when HTML for the login page is complete.
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String key = request.getParameter("key");
		if(key == null || !authenticateKey(dbConnection, key)){
			JSONObject json = new JSONObject();
			json.accumulate("status", 1);
			response.getWriter().write(json.toString(4));
			return;
		}
		
		JSONObject json = new JSONObject();
		JSONArray results = new JSONArray();
		
		long start = System.currentTimeMillis();
		
		
		String sql = "SELECT * FROM stock.cumulative;";
		List<List<String>> queryResults = null;
		try
		{
			PreparedStatement statement = dbConnection.prepareStatement(sql);
			System.out.println(statement);
			queryResults = Utils.executeQuery(dbConnection, statement);
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(List<String> row: queryResults){
			JSONObject object = new JSONObject();
			object.put("instock_id", row.get(0));
			object.put("stock_id", row.get(0));
			object.put("instock_timestamp", row.get(0));
			
			
		}
		
		long end = System.currentTimeMillis();
		json.put("results", results);
		json.put("time_took", (end-start)/1000.0);
		
		response.getWriter().write(json.toString(4));
		
		
	}
	
	private boolean authenticateKey(Connection connection, String key){
		String sql = "SELECT * FROM api.authorized WHERE `keys` = ?;";
		List<List<String>> results = null;
		try
		{
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, key);
			System.out.println(statement);
			results = Utils.executeQuery(connection, statement);
			
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return results.size() == 1;
	}
	
	
	
	
	
	
	
}
