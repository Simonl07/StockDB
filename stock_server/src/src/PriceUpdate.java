package src;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.JSONObject;

public class PriceUpdate extends HttpServlet
{
	private PriceUpdator updator;
	private SessionFactory factory;
	public static final String PATH = "/";
	private static DecimalFormat df = new DecimalFormat("##.##");

	public PriceUpdate(SessionFactory factory)
	{
		this.factory = factory;
		updator = new PriceUpdator();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);

		Session hibernateSession = factory.openSession();
		hibernateSession.beginTransaction();
		
		List<Stock> stocks = PriceUpdator.getStocks(hibernateSession);
		
		hibernateSession.getTransaction().commit();
		hibernateSession.close();
		
		
		PrintWriter writer = response.getWriter();

		writer.write("<html><body>");
		writer.write("<h1> Stock Price Index: </h1>");
		writer.write("<h3> Current update rate: " + df.format(updator.getUpdateRate()) + " stocks/s </h3>");
		writer.write("<h3> Current average latency: " + df.format(updator.getAverageLatency()) + " seconds </h3>");
		writer.write("<strong> Size: " + stocks.size() + "</strong>");
		
		for (Stock s: stocks)
		{
			writer.write("<p> <Strong>(" + s.getSYMBOL() + ") " + s.getNAME_FULL() + ":</Strong> <br />" + "&nbsp;&nbsp;&nbsp;&nbsp; Price: " + s.getLatest_price() + "&nbsp;&nbsp;&nbsp;&nbsp; Volume: "
					+ s.getLatest_volume().toString() + "&nbsp;&nbsp;&nbsp;&nbsp; Last updated " + Duration.between(LocalDateTime.now(), s.getLast_update()).getSeconds() + " seconds ago by " + s.getLastCrawl() + "</p>");
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
		int stock_id = json.getInt("stock_id");
		Double price = Double.parseDouble(json.getString("price"));
		Long volume = Long.parseLong(json.getString("volume"));
		
		int crawl_id = json.getInt("crawl_task_id");
		
		Session hibernateSession = factory.openSession();
		hibernateSession.beginTransaction();
		
		updator.updatePrice(stock_id, price, volume, crawl_id, hibernateSession);
		
		hibernateSession.getTransaction().commit();
		hibernateSession.close();
		
	}

}