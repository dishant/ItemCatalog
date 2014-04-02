package com.pricechutney.pricecatalog.flipkart;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DataUpload
{
	public static void uploadData(Connection conn, String filename) throws SQLException
	{
		Statement statement = conn.createStatement();
		
		String statementText = "LOAD DATA LOCAL INFILE '"+filename+"' REPLACE INTO TABLE catalog (id,name,url) ";
		
		statement.execute(statementText);
	}
}
