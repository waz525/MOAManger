package com.worden.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

public class UploadFile {

	private static String newline = "\n" ;	
	private String uploadDirectory = "." ;
	private String ContentType = "" ;
	private String CharacterEncoding = "" ;
	private String strFilename = "" ;
	
	private String getFileName( String s )
	{
		int i = s.lastIndexOf("\\");
		if( i<0 || i>=s.length()-1 )
		{
			i = s.lastIndexOf("/") ;
			if( i<0 || i>=s.length()-1 )
				return s ;
		}
		return s.substring(i+1) ;
	}
	
	public String getFilename()
	{
		return strFilename ;
	}
	
	public void setUploadDirectory( String s )
	{
		uploadDirectory = s ;
	}
	
	public void setContentType( String s )
	{
		ContentType = s ;
		int j ;
		if( ( j = ContentType.indexOf("boundary=") ) != -1 )
		{
			ContentType = ContentType.substring( j+9 ) ; 
			ContentType = "--"+ContentType ;
		}
	}
	
	public void setCharacterEncoding( String s )
	{
		CharacterEncoding = s ;
	}
	
	public void uploadFile( HttpServletRequest req ) throws ServletException,IOException 
	{
		setCharacterEncoding( req.getCharacterEncoding() ) ;
		setContentType( req.getContentType() ) ;
		uploadFile( req.getInputStream() ) ;
	}
	
	public void uploadFile( ServletInputStream servletinputstream ) throws ServletException,IOException
	{
		String s5 = null ;
		String filename = null ;
		byte Linebyte[] = new byte[4096] ;
		byte outLinebyte[] = new byte[4096] ;
		int ai[] = new int[1];
		int ail[] = new int[1] ;
		String line ;
		while( ( line=readLine(Linebyte , ai , servletinputstream , CharacterEncoding) ) != null)
		{
			int i = line.indexOf("filename=") ;
			if( i >= 0 )
			{
				line = line.substring(i+10) ;
				if( (i=line.indexOf("\""))>0 )
					line = line.substring(0,i) ;
				break ;
			}
		}
		filename = line ;
		if( filename != null && !filename.equals("\"") )
		{
			filename = getFileName( filename ) ;
			strFilename = filename ;
			String sContentType = readLine( Linebyte , ai , servletinputstream , CharacterEncoding ) ;
			if( sContentType.indexOf("Content-Type") >= 0)
			{
				readLine(Linebyte , ai , servletinputstream , CharacterEncoding);
			}
			File file = new File(uploadDirectory , filename);
			FileOutputStream FileOutputStream1 = new FileOutputStream(file) ;
			while((sContentType=readLine(Linebyte , ai , servletinputstream , CharacterEncoding)) != null)
			{
				if(sContentType.indexOf(ContentType) == 0 && Linebyte[0] == 45)
					break ;
				if(s5 != null)
				{
					FileOutputStream1.write(outLinebyte , 0 , ail[0]);
					FileOutputStream1.flush() ;	
				}
				s5 = readLine(outLinebyte , ail , servletinputstream , CharacterEncoding);
				if(s5==null || s5.indexOf(ContentType)==0 && outLinebyte[0]==45)
					break ;
				FileOutputStream1.write(Linebyte , 0 , ai[0]);
				FileOutputStream1.flush();				
			}
			byte byte0 ;
			if(newline.length()==1)
				byte0 = 2 ;
			else
				byte0 = 1 ;
			if(s5!=null && outLinebyte[0]!=45 && ail[0]>newline.length()*byte0)
				FileOutputStream1.write(outLinebyte , 0 , ail[0]-newline.length()*byte0);
			if(sContentType!=null && Linebyte[0] != 45 && ai[0]>newline.length()*byte0)
				FileOutputStream1.write(Linebyte , 0 , ai[0]-newline.length()*byte0);
			FileOutputStream1.close() ;
		}
	}
	
	private String readLine(byte Linebyte[] , int ai[] , ServletInputStream servletinputstream , String CharacterEncoding)
	{
		try{
			ai[0] = servletinputstream.readLine(Linebyte , 0 , Linebyte.length);
			if(ai[0]==-1)
				return null ;
		}
		catch(IOException _ex){
			return null ;
		}
		try{
			if(CharacterEncoding==null){
				return new String(Linebyte , 0 , ai[0]);
			}
			else{
				return new String(Linebyte , 0 , ai[0] , CharacterEncoding);
			}
		}
		catch(Exception _ex){
			return null ;
		}
	}	
}
