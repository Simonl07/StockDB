package src;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class TestServer
{

	public static void main(String[] args) throws FileNotFoundException, SQLException, IOException
	{
		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		
		final int PORT;
		
		if (args[0].equals("-l")){
			PORT = 8080;
		}else{
			PORT = 80;
		}
		Server server = new Server(PORT);

		ServletHandler handler = new ServletHandler();
		TaskStatusController controller = new TaskStatusController();
		StockIndex index = new StockIndex();
		//handler.addServletWithMapping(new ServletHolder(new PostServlet(connection)), "/summary");
		handler.addServletWithMapping(new ServletHolder(new PriceUpdate()), "/");
		handler.addServletWithMapping(new ServletHolder(new TaskAssignmentServlet(sessionFactory, controller)), TaskAssignmentServlet.PATH);
		//handler.addServletWithMapping(new ServletHolder(new StatusUpdateServlet(connection, controller)), "/update");
		//handler.addServletWithMapping(new ServletHolder(new APIKeyDistributionServlet(connection)), APIKeyDistributionServlet.PATH);
		handler.addServletWithMapping(new ServletHolder(new ProfileUpdate(sessionFactory, index)), ProfileUpdate.PATH);
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
