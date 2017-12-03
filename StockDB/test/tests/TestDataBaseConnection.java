package tests;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import src.DatabaseConnector;

public class TestDataBaseConnection
{
	private DatabaseConnector connector;
	private static Logger log = LogManager.getLogger();
	@Before
	public void setUp() throws Exception
	{
		connector =  new DatabaseConnector("database.properties");
	}

	@Test
	public void testConnection()
	{
		assertTrue(connector.testConnection());
	}
}
