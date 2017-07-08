import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

public class TestServer
{

	public static void main(String[] args)
	{
		Server server = new Server(80);
		
		ServletHandler handler = new ServletHandler();
		handler.addServletWithMapping(PostServlet.class, "/");
		
		server.setHandler(handler);
		
		try
		{
			server.start();
			server.join();
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static class PostServlet extends HttpServlet
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
		{
			response.setContentType("text/html");
			response.setStatus(HttpServletResponse.SC_OK);
			
			//log.info("MessageServlet ID " + this.hashCode() + " handling GET request.");
		}
		
		
		private Logger log = LogManager.getLogger();
		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
		{
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html");
			response.setStatus(HttpServletResponse.SC_OK);
			
			
			
			//log.info("MessageServlet ID " + this.hashCode() + " handling POST request.");
			
			
			BufferedReader reader = request.getReader();
			
			String line = "";
			while((line = reader.readLine()) != null)
			{
				System.out.println(line);
			}
			
		}

	}

}

