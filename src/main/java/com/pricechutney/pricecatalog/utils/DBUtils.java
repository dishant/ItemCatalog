package com.pricechutney.pricecatalog.utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class DBUtils
{
	
	public static Connection getFlipkartDBConnection() throws SQLException, IOException
	{
		Resource resource = new ClassPathResource("/flipkart.conf");
		Properties props = PropertiesLoaderUtils.loadProperties(resource);
		String host = props.getProperty("host");
		String username = props.getProperty("username");
		String password = props.getProperty("password");
		return DriverManager.getConnection(host,username,password);
	}
	
}
