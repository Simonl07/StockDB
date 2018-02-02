package src;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

public class PriceUpdate extends HttpServlet
{
	private StockIndex priceIndex;
	private Connection connection;
	private static DecimalFormat df = new DecimalFormat("##.##");

	public PriceUpdate(Connection connection)
	{
		priceIndex = new StockIndex();
		this.connection = connection;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);

		PrintWriter writer = response.getWriter();

		writer.write("<html><body>");
		writer.write("<h1> Stock Price Index: </h1>");
		writer.write("<h3> Current update rate: " + df.format(priceIndex.getUpdateRate()) + " stocks/s </h3>");
		writer.write("<h3> Current average latency: " + df.format(priceIndex.getAverageLatency()) + " seconds </h3>");
		writer.write("<strong> Size: " + priceIndex.getStockList().size() + "</strong>");
		
		for (Stock s: priceIndex.getStockList())
		{
			writer.write("<p> <Strong>(" + s.getSYMBOL() + ") " + s.getNAME_FULL() + ":</Strong> <br />" + "&nbsp;&nbsp;&nbsp;&nbsp; Price: " + s.getLatest_price() + "&nbsp;&nbsp;&nbsp;&nbsp; Volume: "
					+ s.getLatest_volume().toString() + "&nbsp;&nbsp;&nbsp;&nbsp; Last updated " + (System.currentTimeMillis() - s.getLast_update())/1000.0 + " seconds ago by " + s.getLastCrawl() + "</p>");
		}
		writer.write("</html></body>");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);

		JSONObject json = Utils.getJSON(request);

		System.out.println(json.get("name_full") + " " +json.get("name_short"));
		//Integer stock_id = json.getInt("id");
		String name_full = json.getString("name_full");
		String name_short = json.getString("name_short");
		Double price = Double.parseDouble(json.getString("price"));
		Long volume = Long.parseLong(json.getString("volume"));
		
		String crawl_id = json.getString("crawl_task_id");
		priceIndex.updatePrice(name_full, name_short, price, volume, crawl_id);
	}

}