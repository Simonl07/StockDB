package src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
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

	private int crawled_count;
	private long end_time;
	private float success_rate;
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
		this.success_rate = 1;
		this.crawled_count = 0;
	}

	public int getCrawled_count()
	{
		return this.crawled_count;
	}

	public void setCrawled_count(int crawled_count)
	{
		this.crawled_count = crawled_count;
	}

	public void reportInvalid(Connection connection, String stock)
	{
		this.invalid_stocks.add(stock);
		this.success_rate = ((float)this.SIZE - this.invalid_stocks.size())/(float)this.SIZE;
		System.out.println("Success rate decreased to " + this.success_rate);
		removeStock(connection, stock);
	}

	public float getSuccess_rate()
	{
		return success_rate;
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

	public long getTIMESTAMP()
	{
		return TIMESTAMP;
	}
	
	public int getInvalidCount(){
		return invalid_stocks.size();
	}

	public int getSIZE()
	{
		return SIZE;
	}

	public PreparedStatement genSQLArchive(Connection connection)
	{
		String sql = "INSERT INTO records.crawltasks VALUES(?, ?, ?, ?, ?, ?);";
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(sql);
			statement.setString(1, this.ID);
			statement.setTimestamp(2, new Timestamp(this.TIMESTAMP));
			statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
			statement.setInt(4, this.SIZE);
			statement.setInt(5, this.invalid_stocks.size());
			statement.setFloat(6, this.success_rate);
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
