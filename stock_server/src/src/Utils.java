package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

public class Utils
{
	public static JSONObject getJSON(HttpServletRequest request)
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
