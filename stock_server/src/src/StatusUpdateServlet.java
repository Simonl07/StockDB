package src;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StatusUpdateServlet extends HttpServlet
{

	private Connection connection;
	private TaskStatusController controller;
	private static DecimalFormat df = new DecimalFormat("##.##");

	public StatusUpdateServlet(Connection connection, TaskStatusController controller)
	{
		this.connection = connection;
		this.controller = controller;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String[] status = {"Initialized", "Processing", "Completed"};
		PrintWriter writer = response.getWriter();
		for(CrawlTask c: controller.getTasks()){
			writer.write("<p> <Strong> Crawling Task: " + c.getID() + ":</Strong> <br />" + "&nbsp;&nbsp;&nbsp;&nbsp; Time elapsed: " 
					+ (System.currentTimeMillis() - c.getTIMESTAMP())/1000 + " seconds. <br />&nbsp;&nbsp;&nbsp;&nbsp; Status: "
					+ status[c.getStatus()]  + ". <br />&nbsp;&nbsp;&nbsp;&nbsp; Invalid Stocks: "
					+  c.getInvalidCount() + ". <br />&nbsp;&nbsp;&nbsp;&nbsp; Assigned Size: " 
					+ c.getSIZE() + ". <br />&nbsp;&nbsp;&nbsp;&nbsp; Progress: " 
					+ df.format(((double)c.getCrawled_count()/c.getSIZE())*100) + "%. <br />&nbsp;&nbsp;&nbsp;&nbsp; Success rate: " 
					+ df.format(c.getSuccess_rate()*100) + "%<br />");
			
			for(String s: c.getCRAWLER_MAP().keySet()){
				writer.write("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
				writer.write("<Strong>Crawler " + s + " </Strong> has processed " + c.getCRAWLER_MAP().get(s) + " stocks.  <br />");
			}
			writer.write("</p>");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if(request.getParameter("type") == null){
			return;
		}
		if(request.getParameter("type").equals("invalid")){
			this.controller.reportInvalid(connection, request.getParameter("id"), request.getParameter("stock"));
			System.out.println("Invalid reported: " + request.getParameter("stock"));
		}
		if(request.getParameter("type").equals("update")){
			System.out.println("Updating: " + request.getParameter("id"));
			if(request.getParameter("status").equals("2"))
			{
				archiveCrawlTask(request.getParameter("id"));
			}else{
				this.controller.updateTask(request.getParameter("id"), Integer.parseInt(request.getParameter("status")));
			}
		}
		if(request.getParameter("type").equals("progress")){
			System.out.println("Progress update: " + request.getParameter("task_id") + ": " + request.getParameter("value"));
			controller.crawlerUpdate(request.getParameter("task_id"),request.getParameter("crawler_id"),  Integer.parseInt(request.getParameter("value")));
		}
		if(request.getParameter("type").equals("new_crawler")){
			System.out.println("new crawler(" + request.getParameter("crawler_id") + ") for task " + request.getParameter("task_id") + " participated");
			controller.addCrawler(request.getParameter("task_id"), request.getParameter("crawler_id"));
		}
	}
	
	
	private void archiveCrawlTask(String id){
		PreparedStatement statement = this.controller.archive(connection, id);
		System.out.println("Archiving " + id +  "...");
		System.out.println("\t" + statement);
		try
		{
			statement.execute();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}
