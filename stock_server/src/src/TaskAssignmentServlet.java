package src;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

public class TaskAssignmentServlet extends HttpServlet
{
	private Connection connection;
	private TaskStatusController controller;
	
	public TaskAssignmentServlet(Connection connection, TaskStatusController controller){
		this.connection = connection;
		this.controller = controller;
	}
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_OK);

		JSONObject responsePackage = controller.assign(StockInsertionUtils.getStockList(connection), request.getParameter("type"));
		
		response.getWriter().write(responsePackage.toString(4));
	}
}
