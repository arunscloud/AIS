package com.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DBconnectionForMY {
	
	public String getMarketYield(String crop) {
		 
		System.out.println("-------- Oracle JDBC Connection Testing ------");
 
		try {
 
			Class.forName("oracle.jdbc.driver.OracleDriver");
 
		} catch (ClassNotFoundException e) {
 
			System.out.println("Where is your Oracle JDBC Driver?");
			e.printStackTrace();
			
		}
 
		String My="";
		
		System.out.println("Oracle JDBC Driver Registered!");
 
		Connection connection = null;
		ResultSet rs = null;
		String str=crop;
			
 
		try {
 
			connection = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/XE","ais","ais");
			CallableStatement cstmt = connection.prepareCall("{CALL GET_CROP_MARKET_PRICE (?,?)}");
			cstmt.setString(1, str);
			cstmt.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
			cstmt.execute();
			rs = (ResultSet)cstmt.getObject(2);
			
			while(rs.next())
			{My=rs.getString(1);}

 
		} catch (SQLException e) {
 
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
 
		}
 
		
		return My;
		   
      

	}

	

}
