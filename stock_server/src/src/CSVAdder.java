package src;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class CSVAdder
{

	public static void main(String[] args) throws SQLException, IOException
	{
		Connection c = new DatabaseConnector().getConnection();
		Scanner scan = new Scanner(new File("..\\Crawler\\List.csv"));
		String full = scan.nextLine();
		
		String[] arr = full.split(",");
		
		
		
		for(String s: arr){
			String sql = "INSERT INTO id_name VALUES(DEFAULT, ?)";
			PreparedStatement statement = c.prepareStatement(sql);
			statement.setString(1, s);
			System.out.println(statement);
			statement.execute();
		}
	}

}
