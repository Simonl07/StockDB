package src;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Tester
{

	public static void main(String[] args)
	{
		
		
		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		
		Server s = new Server();
		
	}

}
