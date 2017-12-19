package src;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

public class PriceUpdate extends HttpServlet
{
	private StockPriceIndex priceIndex;
	private Connection connection;

	public PriceUpdate(Connection connection)
	{
		priceIndex = new StockPriceIndex();
		this.connection = connection;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);

		PrintWriter writer = response.getWriter();

		writer.write("<html><body>");
		writer.write("<h2> Live Stock Price Index: </h2>");
		writer.write("<h4> Size: " + priceIndex.getStocks().size() + "</h4>");
		
		for (Stock s: priceIndex.getStocks())
		{
			writer.write("<p> <Strong>(" + s.getNAME_SHORT() + ") " + s.getNAME_FULL() + ":</Strong> <br />" + "&nbsp;&nbsp;&nbsp;&nbsp; Price: " + s.getPrice() + "&nbsp;&nbsp;&nbsp;&nbsp; Volume: "
					+ s.getVolume().toString() + "&nbsp;&nbsp;&nbsp;&nbsp; Last updated " + (System.currentTimeMillis() - s.getLast_update())/1000.0 + " seconds ago by " + s.getLastCrawl() + "</p>");
		}
		writer.write("</html></body>");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);

		JSONObject json = Utils.getJSON(request);

		String name_full = json.getString("name_full");
		String name_short = json.getString("name_short");
		Double price = Double.parseDouble(json.getString("price"));
		Long volume = Long.parseLong(json.getString("volume"));
		
		String id = json.getString("crawl_task_id");
		priceIndex.put(name_full, name_short, price, volume, id);
	}

}