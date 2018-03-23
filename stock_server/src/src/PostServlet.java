package src;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.JSONObject;

public class PostServlet extends HttpServlet
{
	/**
	 * 
	 */

	private SessionFactory factory;
	public static final String PATH = "/record";
	
	public PostServlet(SessionFactory factory){
		this.factory = factory;
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);

		// log.info("MessageServlet ID " + this.hashCode() + " handling GET
		// request.");
	}

	private Map<String, String> getHeadersInfo(HttpServletRequest request)
	{

		Map<String, String> map = new HashMap<String, String>();

		Enumeration headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements())
		{
			String key = (String) headerNames.nextElement();
			String value = request.getHeader(key);
			map.put(key, value);
		}

		return map;
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		getHeadersInfo(request);

		JSONObject json = Utils.getJSON(request);

		Record record = new Record();
		record.setName_long(json.getString("name_long"));
		record.setName_short(json.getString("name_short"));
		record.setSymbol(json.getString("symbol"));
		record.setPrice_close(json.getDouble("price_close"));
		record.setPrice_open(json.getDouble("price_open"));
		record.setRange_day_high(json.getDouble("range_day_high"));
		record.setRange_day_low(json.getDouble("range_day_low"));
		record.setRange_52w_high(json.getDouble("range_52w_high"));
		record.setRange_52w_low(json.getDouble("range_52w_low"));
		record.setVolume(json.getDouble("volume"));
		record.setVolume_avg(json.getDouble("volume_avg"));
		record.setMarket_cap(json.getDouble("market_cap"));
		record.setBeta(json.getDouble("beta"));
		record.setPe_ratio(json.getDouble("pe_ratio"));
		record.setEps(json.getDouble("eps"));
		record.setDividend(json.getDouble("dividend"));
		record.setTarget_est_1Y(json.getDouble("target_est_1Y"));
		record.setDividend_yield(json.getDouble("dividend_yield"));
		record.setEx_dividend_date(LocalDate.parse(json.getString("ex_dividend_date")));
		record.setEarnings_date_begin(LocalDate.parse(json.getString("earnings_date_begin")));
		record.setEarnings_date_end(LocalDate.parse(json.getString("earnings_date_end")));
		
		record.setInstock_timestamp(LocalDateTime.now());
		Session hibernateSession = factory.openSession();
		hibernateSession.beginTransaction();

		hibernateSession.save(record);
		
		hibernateSession.getTransaction().commit();
		hibernateSession.close();
	}

}