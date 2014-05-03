package com.dao;


import java.sql.*;
import java.util.ArrayList;
 
public class DBConn4 {
 
	public ArrayList getCropsSoilType(String soilType) {
 
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
		String str1="Soil Type";
		String str2=soilType;
 
		try {
 
			connection = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/XE","ais","ais");
			CallableStatement cstmt = connection.prepareCall("{CALL GetCrop_Soil_Type_Test (?,?,?)}");
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
