package src;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.JSONArray;
import org.json.JSONObject;

public class ProfileAPI extends HttpServlet{
	private SessionFactory factory;
	public static final String PATH = "/api/profile";
	
	public ProfileAPI(SessionFactory factory){
		this.factory = factory;
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		long start = System.currentTimeMillis();
		String query = "from Stock WHERE ";
		if(request.getParameter("symbol") != null){
			query += "symbol='" + request.getParameter("symbol") + "'";
			query += " and ";
		}
		if(request.getParameter("sector") != null){
			query += "sector='" + request.getParameter("sector") + "'";
			query += " and ";
		}
		if(request.getParameter("industry") != null){
			query += "industry='" + request.getParameter("industry") + "'";
			query += " and ";
		}
		if(request.getParameter("state") != null){
			query += "state='" + request.getParameter("state") + "'";
			query += " and ";
		}
		if(request.getParameter("city") != null){
			query += "city='" + request.getParameter("city") + "'";
			query += " and ";
		}
		if(request.getParameter("zip") != null){
			query += "zip='" + request.getParameter("zip") + "'";
			query += " and ";
		}
		if(request.getParameter("priceGT") != null){
			query += "latest_price>" + request.getParameter("priceGT");
			query += " and ";
		}
		if(request.getParameter("priceLT") != null){
			query += "latest_price<" + request.getParameter("priceLT");
			query += " and ";
		}
		if(request.getParameter("employeeGT") != null){
			query += "employee>" + request.getParameter("employeeGT");
			query += " and ";
		}
		if(request.getParameter("employeeLT") != null){
			query += "employee<" + request.getParameter("employeeLT");
			query += " and ";
		}
		if(request.getParameter("keyword") != null){
			query += "description like '% " + request.getParameter("keyword") + " %'";
			query += " and ";
		}
		
		
		query = query.substring(0, query.length() - 5);
		System.out.println(query);
		
		Session session = factory.openSession();
		session.beginTransaction();
		
		@SuppressWarnings("unchecked")
		List<Stock> stocks = session.createQuery(query).list();
		
		long end = System.currentTimeMillis();
		
		JSONObject json = new JSONObject();
		json.accumulate("request_type", "profile");
		json.accumulate("time_took",(end - start) / 1000.0);
		json.accumulate("size", stocks.size());
		
		JSONArray results = new JSONArray();
		for(Stock s: stocks){
			results.put(s.toJSON());
		}
		
		session.getTransaction().commit();
		session.close();
		
		json.accumulate("results", results);
		
		response.getWriter().write(json.toString(4));

	}
	
	
	
}
