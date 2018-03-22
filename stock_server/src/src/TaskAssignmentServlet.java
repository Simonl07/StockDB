package src;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.JSONObject;

@SuppressWarnings("serial")
public class TaskAssignmentServlet extends HttpServlet
{
	private SessionFactory factory;
	private TaskStatusController controller;
	public static final String PATH = "/task";
	
	public TaskAssignmentServlet(SessionFactory factory, TaskStatusController controller){
		this.factory = factory;
		this.controller = controller;
	}
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_OK);
		Session hibernateSession = factory.openSession();
		hibernateSession.beginTransaction();
		
		JSONObject responsePackage;
		if(request.getParameter("type") != null && request.getParameter("type").equals("profile"))
		{
			responsePackage = controller.assign(StockInsertionUtils.getSymbolList(hibernateSession), request.getParameter("type"));
		}else{
			responsePackage = controller.assign(hibernateSession, request.getParameter("type"));
		}
		
		
		hibernateSession.getTransaction().commit();
		hibernateSession.close();
		
		response.getWriter().write(responsePackage.toString(4));
	}
}
