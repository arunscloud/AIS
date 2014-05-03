package com.dao;

import java.sql.*;
import java.util.ArrayList;

 
public class DBConnection {
 
	public ArrayList cropMatch (String crop) 
	{
        
		ArrayList<String> cropProtocol=new ArrayList<String>();
		
		System.out.println("-------- Oracle JDBC Connection Testing ------");
 
		try {
 
			Class.forName("oracle.jdbc.driver.OracleDriver");
 
		} catch (ClassNotFoundException e) {
 
			System.out.println("Where is your Oracle JDBC Driver?");
			e.printStackTrace();
			//return;
 
		}
 
		System.out.println("Oracle JDBC Driver Registered!");
 
		Connection connection = null;
		ResultSet rs = null;
		String str=crop;
 
		try {
 
			connection = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/XE","ais","ais");
			CallableStatement cstmt = connection.prepareCall("{CALL PROTOCOL_RETRIEVE_CROP_test3(?,?)}");
			cstmt.setString(1, str);
			cstmt.registerOutParameter(2,oracle.jdbc.OracleTypes.CURSOR);
			cstmt.execute();
			rs = (ResultSet)cstmt.getObject(2);
			
			while(rs.next())
			{
				cropProtocol.add(rs.getString(1));
				System.out.print(rs.getString(1));
				
			}

 
		} catch (SQLException e) {
 
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			//return;
 
		}
 
		   
     return cropProtocol;     

	}
 
}
