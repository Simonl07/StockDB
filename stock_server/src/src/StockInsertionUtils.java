package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

public class StockInsertionUtils
{
	
	
	public static void parse(Connection connection, JSONObject json){
		String sql = "INSERT INTO cumulative VALUES(null, ?, now(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		PreparedStatement s;
		try
		{
			s = connection.prepareStatement(sql);
			s.setInt(1, Integer.parseInt(getID(connection, json.getString("name_short"))));
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
			s.setLong(12, Long.parseLong(json.getString("volume")));
			s.setLong(13, Long.parseLong(json.getString("volume_avg")));
			s.setLong(14, Long.parseLong(json.getString("market_cap")));
			s.setFloat(15, Float.parseFloat(json.getString("beta")));
			s.setFloat(16, Float.parseFloat(json.getString("pe_ratio")));
			s.setFloat(17, Float.parseFloat(json.getString("eps")));
			s.setString(18, json.getString("earnings_date"));
			s.setDate(19, Date.valueOf((json.getString("earnings_date_begin"))));
			s.setDate(20, Date.valueOf((json.getString("earnings_date_end"))));
			s.setFloat(21, Float.parseFloat(json.getString("dividend")));
			s.setFloat(22, Float.parseFloat(json.getString("dividend_yield")));
			s.setDate(23, Date.valueOf(json.getString("ex_dividend_date")));
			s.setFloat(24, Float.parseFloat(json.getString("target_est_1Y")));
			
			
			
			s.execute();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	

	
	private static String getID(Connection connection, String name) throws SQLException{
		String sql = "SELECT * FROM id_name WHERE name_short = ?";
		PreparedStatement s = connection.prepareStatement(sql);
		s.setString(1, name);
		List<List<String>> result = Utils.executeQuery(connection, s);
		
		assert result.get(0).size() == 1;
		assert !result.get(0).get(0).equals("");
		
		return result.get(0).get(0);
	}
	
	
	
	public static JSONObject getStockListJSON(Connection connection){
		String sql = "SELECT * FROM id_name;";
		List<List<String>> result = null;
		
		try
		{
			PreparedStatement statement = connection.prepareStatement(sql);
			result = Utils.executeQuery(connection, statement);
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject temp = new JSONObject();
		for(List<String> row: result){
			temp.put(row.get(0), row.get(1));
		}
		
		System.out.println(temp.toString());
		return temp;
	}
	
	public static List<String> getStockList(Connection connection){
		String sql = "SELECT * FROM id_name;";
		List<List<String>> result = null;
		
		try
		{
			PreparedStatement statement = connection.prepareStatement(sql);
			result = Utils.executeQuery(connection, statement);
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<String> temp = new ArrayList<String>();
		for(List<String> row: result){
			temp.add(row.get(1));
		}
		
		return temp;
	}
	
	
	
	
	/**
	 * Convert an HTTP request to a JSON object.
	 * 
	 * @param request: HTTP request
	 * @return a JSON object that contains the parameters of the request
	 * @throws IOException
	 */
	public static JSONObject httpRequest2JSON(HttpServletRequest request) throws IOException
	{

		BufferedReader reader = request.getReader();

		String line = "", content = "";
		while ((line = reader.readLine()) != null)
		{
			content += line;
			System.out.println(line);
		}
		JSONObject retVal = new JSONObject(content);

		return retVal;
	}
	
	public static boolean checkUpdate(Connection connection, String name, String hash){
		String sql = "SELECT * FROM stock.real_time WHERE name_short = ? and stock_hash = ?";
		List<List<String>> results = null;
		try
		{
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, name);
			statement.setString(2, hash);
			results = Utils.executeQuery(connection, statement);
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return results.size() == 0;
	}
	
	
	
	
}
