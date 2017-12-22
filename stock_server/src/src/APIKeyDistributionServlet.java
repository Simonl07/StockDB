package src;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class APIKeyDistributionServlet extends HttpServlet
{
	private Connection dbConnection;
	public static final String PATH = "/api/key";
	private static final Logger LOG = LogManager.getLogger();
	private static final Map<String, Queue<Timestamp>> requestCache = new HashMap<String, Queue<Timestamp>>();
	private static final int MAX_NUM_REQUESTS = 15;
	private static final int TIME_INTERVAL = 20;
	
	
	/**
	 * HousingSearch Servlet constructor, Initialize dbconnection instance from
	 * DatabaseConnector
	 * 
	 * @param connection: a connection object for connecting database
	 */
	public APIKeyDistributionServlet(Connection connection)
	{
		this.dbConnection = connection;
		LOG.info("APIKeyDistributionServlet initialized" + new java.util.Date().toString());
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
		updateCache();
		String ip = request.getRemoteAddr();
		JSONObject json = new JSONObject();
		if(addRequest(ip) == true){
			String key = generateKey(ip);
			recordKey(dbConnection, key);
			json.accumulate("status", 0);
			json.accumulate("key", key);
			System.out.println(json.toString(4));
			response.getWriter().write(json.toString(4));
			response.setContentType("application/json");
		}else{
			json.accumulate("status", 1);
			System.out.println(json.toString(4));
			response.getWriter().write(json.toString());
			response.setContentType("application/json");
		}
	}
	
	
	private void updateCache()
	{
		Timestamp deadline = new Timestamp(System.currentTimeMillis() - TIME_INTERVAL * 1000);
		for(String key: requestCache.keySet()){
			Queue<Timestamp> queue = requestCache.get(key);
			LOG.info("Address: " + key + " requested " + queue.size() + " times.");
			while(queue.peek() != null && queue.peek().compareTo(deadline) == -1){
				queue.remove();
			}
		}
	}
	
	private boolean addRequest(String ip){
		Queue<Timestamp> queue = requestCache.get(ip);
		if(queue == null){
			LinkedList<Timestamp> requests = new LinkedList<Timestamp>();
			requests.add(new Timestamp(System.currentTimeMillis()));
			requestCache.put(ip, requests);
			return true;
		}
		if(queue.size() > MAX_NUM_REQUESTS){
			return false;
		}
		queue.add(new Timestamp(System.currentTimeMillis()));
		return true;
	}
	
	
	private String generateKey(String ip){
		return Utils.MD5(ip + requestCache.size() + System.currentTimeMillis() + "zoog");
	}
	
	private void recordKey(Connection connection, String key){
		String sql = "INSERT INTO api.authorized VALUES(?, now());";
		try
		{
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, key);
			statement.execute();
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
