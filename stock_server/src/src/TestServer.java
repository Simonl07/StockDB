package src;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.json.JSONObject;

public class TestServer
{

	private static Connection connection = null;

	public static void main(String[] args) throws FileNotFoundException, SQLException, IOException
	{
		connection = new DatabaseConnector().getConnection();
		
		final int PORT;
		
		if (args[0].equals("-l")){
			PORT = 8080;
		}else{
			PORT = 80;
		}
		Server server = new Server(PORT);

		ServletHandler handler = new ServletHandler();
		TaskStatusController controller = new TaskStatusController();
		handler.addServletWithMapping(new ServletHolder(new PostServlet(connection)), "/");
		handler.addServletWithMapping(new ServletHolder(new PriceUpdate(connection)), "/live");
		handler.addServletWithMapping(new ServletHolder(new TaskAssignmentServlet(connection, controller)), "/list");
		handler.addServletWithMapping(new ServletHolder(new StatusUpdateServlet(connection, controller)), "/update");
		handler.addServletWithMapping(new ServletHolder(new APIKeyDistributionServlet(connection)), APIKeyDistributionServlet.PATH);
		handler.addServletWithMapping(TrafficLightServlet.class, "/go");
			
		
		server.setHandler(handler);

		try
		{
			System.out.println("start");
			server.start();
			server.join();
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
