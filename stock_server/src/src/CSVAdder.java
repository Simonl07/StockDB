package src;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class CSVAdder
{

	public static void main(String[] args) throws SQLException, IOException
	{
		Scanner scan = new Scanner(new File("../List.csv"));
		String full = scan.nextLine();
		
		String[] arr = full.split(",");
		
		SessionFactory factory = new Configuration().configure().buildSessionFactory();
		
		Session hibernateSession = factory.openSession();
		hibernateSession.beginTransaction();
		int cnt = 0;
		for(String s: arr){
			System.out.print(cnt + ":\n");
			cnt++;
			hibernateSession.save(new Symbol(s));
		}
		
		hibernateSession.getTransaction().commit();
		hibernateSession.close();
		
		
		
		
		
	}

}
