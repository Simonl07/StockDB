package src;

import java.io.IOException;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

public class TrafficLightServlet extends HttpServlet
{
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		Calendar curr = Calendar.getInstance();
		JSONObject json = new JSONObject();
		if(curr.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && curr.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY){
			if(curr.get(Calendar.HOUR_OF_DAY) >= 14 && curr.get(Calendar.HOUR_OF_DAY) < 21){
				json.put("go", 1);
			}else{
				json.put("go", 0);
			}
		}else{
			json.put("go", "0");
		}
		response.getWriter().write(json.toString(4));
	}
}
