package src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class CrawlTask
{
	private final String ID;
	private final long TIMESTAMP;
	private final int SIZE;
	private final List<String> STOCK_LIST;

	private long end_time;
	private int status;
	private List<String> invalid_stocks;

	public CrawlTask(List<String> stock_list)
	{

		this.TIMESTAMP = System.currentTimeMillis();
		this.SIZE = stock_list.size();
		this.STOCK_LIST = stock_list;
		this.status = 0;
		this.end_time = -1;
		this.ID = Utils.MD5(this.TIMESTAMP + this.SIZE + this.STOCK_LIST.toString() + this.status);
		this.invalid_stocks = new ArrayList<String>();
	}

	public void reportInvalid(Connection connection, String stock)
	{
		this.invalid_stocks.add(stock);
		removeStock(connection, stock);
	}

	public String getID()
	{
		return ID;
	}

	public int getStatus()
	{
		return status;
	}

	public void setEnd_time(long end_time)
	{
		this.end_time = end_time;
	}

	public void updateStatus(int status)
	{
		this.status = status;
	}

	public JSONObject genJSONPackage()
	{
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		for (String stock: STOCK_LIST)
		{
			array.put(stock);
		}

		json.put("id", this.ID);
		json.put("assignment_begin", this.TIMESTAMP);
		json.put("assignment_size", this.SIZE);
		json.put("stocks", array);
		return json;
	}

	public PreparedStatement genSQLArchive(Connection connection)
	{
		String sql = "INSERT INTO records.crawltasks VALUES";
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(sql);
			// TODO

		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return statement;
	}

	public static void removeStock(Connection connection, String name)
	{
		String sql = "DELETE FROM id_name WHERE name_short = ?;";

		try
		{
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, name);
			statement.execute();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}

	}
}
