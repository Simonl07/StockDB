import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


import org.json.JSONObject;

public class StockInsertionUtils
{
	
	
	public static void parse(Connection connection, StockPriceIndex priceIndex, JSONObject json){
		String sql = "INSERT INTO cumulative VALUES(null, ?, now(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		PreparedStatement s;
		try
		{
			s = connection.prepareStatement(sql);
			s.setInt(1, Integer.parseInt(getID(connection, json.getString("name_short"))));
			System.out.println(json.getString("name_short"));
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
	
	
	public static void real_time_insert(Connection connection, JSONObject json){
		String sql = "INSERT INTO real_time VALUES(?, ?, now(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		PreparedStatement s;
		try
		{
			s = connection.prepareStatement(sql);
			s.setString(1,  json.getString("name_short"));
			s.setInt(2, Integer.parseInt(getID(connection, json.getString("name_short"))));

			s.setString(3,  json.getString("name_full"));
			
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
			
			
			String finalHash = MD5(json);
			
			System.out.println(finalHash);
			System.out.println(s);
			
			s.setString(25, finalHash);
			
			
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
	
	public static JSONObject getStockList(Connection connection){
		String sql = "SELECT * FROM id_name;";
		List<List<String>> result = null;
		
		try
		{
			PreparedStatement statement = connection.prepareStatement(sql);
			result = executeQuery(connection, statement);
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
	
	public static void updateStock(Connection connection, JSONObject json){
		String nameShort = json.getString("name_short");
		String stockHash = MD5(json);
		if(checkUpdate(connection, nameShort, stockHash)){
			real_time_insert(connection, json);
		}
	}
	
	public static boolean checkUpdate(Connection connection, String name, String hash){
		String sql = "SELECT * FROM stock.real_time WHERE name_short = ? and stock_hash = ?";
		List<List<String>> results = null;
		try
		{
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, name);
			statement.setString(2, hash);
			results = executeQuery(connection, statement);
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return results.size() == 0;
	}
	
	public static String MD5(String input){
		MessageDigest md = null;
		try
		{
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e){}
		
		md.update(input.getBytes());
		byte messageDigest[] = md.digest();

        StringBuffer hexString = new StringBuffer();
        for (int i=0;i<messageDigest.length;i++) {
            String hex=Integer.toHexString(0xFF & messageDigest[i]);
            if(hex.length()==1)
                hexString.append('0');

            hexString.append(hex);
        }
	    return hexString.toString();
	}
	
	public static String MD5(JSONObject json){
		
		Iterator<String> keys= json.keys();
		String total = "";
		while (keys.hasNext()) 
		{
		        String keyValue = (String)keys.next();
		        String valueString = json.getString(keyValue);
		        total += valueString;
		}
		MessageDigest md = null;
		try
		{
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e){}
		
		md.update(total.getBytes());
		byte messageDigest[] = md.digest();

        StringBuffer hexString = new StringBuffer();
        for (int i=0;i<messageDigest.length;i++) {
            String hex=Integer.toHexString(0xFF & messageDigest[i]);
            if(hex.length()==1)
                hexString.append('0');

            hexString.append(hex);
        }
	    return hexString.toString();
	}
	
	
}
