import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.json.JSONObject;

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
		
		
		private Map<String, String> getHeadersInfo(HttpServletRequest request) {

	        Map<String, String> map = new HashMap<String, String>();

	        Enumeration headerNames = request.getHeaderNames();
	        while (headerNames.hasMoreElements()) {
	            String key = (String) headerNames.nextElement();
	            String value = request.getHeader(key);
	            map.put(key, value);
	        }
	        

	        return map;
	    }
		
		private Logger log = LogManager.getLogger();
		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
		{
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html");
			response.setStatus(HttpServletResponse.SC_OK);
			getHeadersInfo(request);
			
			
			//log.info("MessageServlet ID " + this.hashCode() + " handling POST request.");
			
			
			BufferedReader reader = request.getReader();
			
			String line = "";
			String content = "";
			while((line = reader.readLine()) != null)
			{
				content += line;
			}
			
			
			JSONObject json = new JSONObject(content);
			
			for(String key: json.keySet())
			{
				System.out.println(key + ": " + json.get(key));
			}
			System.out.println();
		}

	}

}

