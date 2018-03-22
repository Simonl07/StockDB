package src;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.json.JSONObject;

@SuppressWarnings("serial")
public class ProfileUpdate extends HttpServlet
{
	public static final String PATH = "/profile";
	private SessionFactory factory;

	public ProfileUpdate(SessionFactory factory)
	{
		this.factory = factory;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		
		JSONObject json = Utils.getJSON(request);
		
		
		String symbol = json.getString("symbol");
		String name_full = json.getString("name_full");
		String address = json.getString("address");
		String city = json.getString("city");
		String state = json.getString("state");
		String country = json.getString("country");
		String zipcode = json.getString("zipcode");
		String industry = json.getString("industry");
		String sector = json.getString("sector");
		String website = json.getString("website");
		String description = json.getString("description");
		int employee = json.getInt("employee");
		int stock_id = json.getInt("stock_id");
		
		Session session = factory.openSession();
		session.beginTransaction();
		
		Stock stock;
		@SuppressWarnings("unchecked")
		Query<Stock> query = session.createQuery("from Stock where SYMBOL = :symbol");
		query.setParameter("symbol", symbol);
		List<Stock> lst = query.list();
		if(lst.size() == 1){
			stock = lst.get(0);
		}else{
			stock = new Stock(name_full, symbol);
		}
		stock.setAddress1(address);
		stock.setCity(city);
		stock.setState(state);
		stock.setCountry(country);
		stock.setZip(zipcode);
		stock.setIndustry(industry);
		stock.setSector(sector);
		stock.setWebsite(website);
		stock.setDescription(description);
		stock.setEmployee(employee);
		
		
		session.save(stock);
		session.getTransaction().commit();
		session.close();
		
	}
	
}
