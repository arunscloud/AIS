package com.serv;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;

import com.dao.DBConnection;

/**
 * Servlet implementation class PdfGen
 */
@WebServlet("/PdfGen")
public class PdfGen extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PdfGen() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
		DBConnection db=new DBConnection();
		ArrayList cropPro=db.cropMatch(request.getParameter("Crop"));
		
		System.out.println(request.getParameter("Crop")+" "+request.getParameter("date"));
		
	        
		try {

	        ByteArrayOutputStream output = new ByteArrayOutputStream();
	        output = createPDF(request.getParameter("Crop"),request.getParameter("date"),request.getParameter("acreage"),cropPro);

	        response.addHeader("Content-Type", "application/force-download"); 
	        response.addHeader("Content-Disposition", "attachment; filename=\"CropProtocol.pdf\"");
	        response.getOutputStream().write(output.toByteArray());

	    } catch (Exception ex) {            
	        ex.printStackTrace();
	    }   
		
	}

	public ByteArrayOutputStream createPDF(String crop, String date, String acreage, ArrayList cropPro) throws IOException, COSVisitorException {

	    PDDocument document;
	    PDPage page;
	    PDFont font;
	    PDPageContentStream contentStream;
	    PDJpeg front;
	    PDJpeg back;

	    InputStream inputFront;
	    InputStream inputBack;
	    ByteArrayOutputStream output = new ByteArrayOutputStream(); 

	    // Creating Document
	    document = new PDDocument();

	 // Creating Pages
	    
	    /*ArrayList<String> string = new ArrayList<String>();

	    BufferedReader br = new BufferedReader(
	            new InputStreamReader(new FileInputStream("C:\\Users\\Sainath\\Desktop\\prot.txt")));
	    try {
	        String line;
	        while ((line = br.readLine()) != null) {
	        	string.add(line);
	        }
	    } finally {
	        br.close();
	    }*/
	       
	        
	        Iterator itr=cropPro.iterator();
	        int count=0;
	        
	        page = new PDPage();

  	        // Adding page to document
  	        document.addPage(page); 
              
  	        // Adding FONT to document
  	        font = PDType1Font.HELVETICA;           


  	        // Next we start a new content stream which will "hold" the to be created content.
  	        contentStream = new PDPageContentStream(document, page);
  	        
  	      contentStream.beginText();
	        contentStream.setFont(font, 10);
	        contentStream.moveTextPositionByAmount(12, 770);
	        contentStream.drawString(crop);
	        contentStream.endText();
	        
	        contentStream.beginText();
	        contentStream.setFont(font, 10);
	        contentStream.moveTextPositionByAmount(9, 750);
	        contentStream.drawString("For Date: "+date);
	        contentStream.endText();
	        
	        contentStream.beginText();
	        contentStream.setFont(font, 10);
	        contentStream.moveTextPositionByAmount(9, 740);
	        contentStream.drawString("You farm Acreage is "+acreage+" acres");
	        contentStream.endText();
  	        
	        while(itr.hasNext())
	        {
	              count++;
	              String protStr=(String) itr.next();
	             // if(count%77==0)
	              {                

	      	        // Let's define the content stream
			        
			        contentStream.beginText();
			        contentStream.setFont(font, 7);
			        contentStream.moveTextPositionByAmount(10, 770-(count+4)*10);
			        contentStream.drawString(count+". "+protStr.trim());
			        contentStream.endText();
			        
			        
			        contentStream.close();
		
	              }
	        }
	        
	        System.out.println(count);
	    // Finally Let's save the PDF
	    document.save(output);
	    document.close();

	    return output;
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
