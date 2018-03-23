package src;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class TestServer
{

	public static void main(String[] args) throws FileNotFoundException, SQLException, IOException
	{
		
		File f = new File("src/hibernate.cfg2.xml");
		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		SessionFactory recordFactory = new Configuration().configure(f).buildSessionFactory();
		
		final int PORT;
		
		if (args[0].equals("-l")){
			PORT = 8080;
		}else{
			PORT = 80;
		}
		Server server = new Server(PORT);

		ServletHandler handler = new ServletHandler();
		
		Session hibernateSession = sessionFactory.openSession();
		hibernateSession.beginTransaction();
		
		TaskStatusController controller = new TaskStatusController(hibernateSession);
		
		hibernateSession.getTransaction().commit();
		hibernateSession.close();
		handler.addServletWithMapping(new ServletHolder(new PostServlet(recordFactory)), PostServlet.PATH);
		handler.addServletWithMapping(new ServletHolder(new PriceUpdate(sessionFactory)), PriceUpdate.PATH);
		handler.addServletWithMapping(new ServletHolder(new TaskAssignmentServlet(sessionFactory, controller)), TaskAssignmentServlet.PATH);
		handler.addServletWithMapping(new ServletHolder(new StatusUpdateServlet(sessionFactory, controller)), "/update");
		//handler.addServletWithMapping(new ServletHolder(new APIKeyDistributionServlet(connection)), APIKeyDistributionServlet.PATH);
		handler.addServletWithMapping(new ServletHolder(new ProfileUpdate(sessionFactory)), ProfileUpdate.PATH);
		handler.addServletWithMapping(new ServletHolder(new ProfileAPI(sessionFactory)), ProfileAPI.PATH);
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
