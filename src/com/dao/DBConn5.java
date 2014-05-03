package com.dao;



import java.sql.*;
import java.util.ArrayList;
 
public class DBConn5 {
 
	public ArrayList getCropsRegion(String region) {
 
		System.out.println("-------- Oracle JDBC Connection Testing ------");
 
		try {
 
			Class.forName("oracle.jdbc.driver.OracleDriver");
 
		} catch (ClassNotFoundException e) {
 
			System.out.println("Where is your Oracle JDBC Driver?");
			e.printStackTrace();
			
		}
 
		ArrayList crops=new ArrayList();
		
		System.out.println("Oracle JDBC Driver Registered!");
 
		Connection connection = null;
		ResultSet rs = null;
		String str1="Region";
		String str2=region;
 
		try {
 
			connection = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/XE","ais","ais");
			CallableStatement cstmt = connection.prepareCall("{CALL GetCrop_Region_Test (?,?,?)}");
			cstmt.setString(1, str1);
			cstmt.setString(2,str2);
			cstmt.registerOutParameter(3, oracle.jdbc.OracleTypes.CURSOR);
			cstmt.execute();
			rs = (ResultSet)cstmt.getObject(3);
			
			while(rs.next())
			{crops.add(rs.getString(1));}

 
		} catch (SQLException e) {
 
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
 
		}
 
		
		return crops;
		   
      

	}
 
}
