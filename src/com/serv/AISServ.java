package com.serv;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.dao.DBConn;
import com.dao.DBConn4;
import com.dao.DBConn5;
import com.dao.DBconnectionForMY;

/**
 * Servlet implementation class AISServ
 */
@WebServlet("/AISServ")
public class AISServ extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AISServ() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub\
        
		String region=request.getParameter("region");
		String soilType=request.getParameter("soilType");
		//String soiltype=request.getParameter("soiltype");
		int soilph=Integer.parseInt(request.getParameter("soilPH"));
		int temperature=Integer.parseInt(request.getParameter("temp"));
		
		DBConn db1=new DBConn();
		DBConn4 db2=new DBConn4();
		DBConn5 db3=new DBConn5();
		
		ArrayList crops1=db1.getCropsPHTEMP("Soil PH",Integer.parseInt(request.getParameter("soilPH")));
		ArrayList crops2=db1.getCropsPHTEMP("Temperature", Integer.parseInt(request.getParameter("temp")));
		ArrayList crops3=db2.getCropsSoilType(request.getParameter("soilType"));
		ArrayList crops4=db3.getCropsRegion(request.getParameter("region"));
		
		ArrayList total=new ArrayList();
		String[] totalC=new String[30];
		
		Iterator itr;
		
		itr=crops1.iterator();
		while(itr.hasNext())
		{
			total.add(itr.next());
		}
		
		itr=crops2.iterator();
		while(itr.hasNext())
		{
			total.add(itr.next());
		}
		itr=crops3.iterator();
		while(itr.hasNext())
		{
			total.add(itr.next());
		}
		itr=crops4.iterator();
		while(itr.hasNext())
		{
			total.add(itr.next());
		}
		
		itr=total.iterator();
		int count =0;
		while(itr.hasNext())
		{
			totalC[count]=(String) itr.next();
			count++;
		}
		
		String matchedCrop="empty";
		
		List asList = Arrays.asList(totalC);
		Set<String> mySet = new HashSet<String>(asList);
		for(String s: mySet){

		 System.out.println(s + " " +Collections.frequency(asList,s));
		 if(Collections.frequency(asList,s)==4)
		 {
			 matchedCrop=s; 
		 }

		}
		
		JSONArray list = new JSONArray();
		  list.add(matchedCrop);
		  
		  DBconnectionForMY forMY=new DBconnectionForMY();
		 String my= forMY.getMarketYield(matchedCrop);

		JSONObject obj=new JSONObject();
		obj.put("crops",list);
		obj.put("MY", my);
	
		
		  StringWriter out = new StringWriter();
		  obj.writeJSONString(out);
		  String jsonText = out.toString();
		  System.out.print(jsonText);
		  
		
	    response.setContentType("application/json");  // Set content type of the response so that jQuery knows what it can expect.
	    //response.setCharacterEncoding("UTF-8"); // You want world domination, huh?
	    response.getWriter().write(jsonText);       // Write response body.
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
