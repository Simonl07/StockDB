package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;

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
