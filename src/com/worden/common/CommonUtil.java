package com.worden.common;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.UUID;


public class CommonUtil {
	/**
	 * 数据库服务器IP，保存于WEB-INF/config/dbconfig.properties
	 */
	public static String  dbHost = GetPropertiesValue("dbconfig.properties", "Host");

	/**
	 * 数据库服务器端口，保存于WEB-INF/config/dbconfig.properties
	 */
	public static String  dbPort = GetPropertiesValue("dbconfig.properties", "Port");

	/**
	 * 数据库用户名，保存于WEB-INF/config/dbconfig.properties
	 */
	public static String  dbUser = GetPropertiesValue("dbconfig.properties", "User");

	/**
	 * 数据库用户密码，保存于WEB-INF/config/dbconfig.properties
	 */
	public static String  dbPassword = GetPropertiesValue("dbconfig.properties", "Password");

	/**
	 * 数据库名，保存于WEB-INF/config/dbconfig.properties
	 */
	public static String  dataBaseName = GetPropertiesValue("dbconfig.properties", "DBName");

	
	
	/**
	 * 读取配置文件设置
	 * @param propFile
	 * @param key
	 * @return
	 */
	public static String GetPropertiesValue(String propFile , String key) {

		Properties pro = new Properties();
		try{
			FileInputStream in = new FileInputStream(CommonUtil.class.getResource("/").getPath()+"../config/"+propFile);
			pro.load(in); 
		}catch(FileNotFoundException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pro.getProperty(key); 
	}
	

	/**
	 * iso8859-1转化UTF-8
	 * @param old
	 * @return
	 */
	public static String ISO2UTF( String old )
	{
		String newer = null ;
		try {
			newer = new String( old.getBytes("iso8859-1"),"utf-8") ;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return newer ;
	}

	/**
	 * UTF-8转化iso8859-1
	 * @param old
	 * @return
	 */
	public static String UTF2ISO( String old )
	{
		String newer = null ;
		try {
			newer = new String( old.getBytes("utf-8"),"iso8859-1") ;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return newer ;
	}

	/**
	 * iso8859-1 转化 gb2312
	 * @param old
	 * @return
	 */
	public static String ISO2GB( String old ) {
		String newer = null ;
		try {
			newer = new String( old.getBytes("iso8859-1"),"gb2312") ;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return newer ;
	}
	
	/**
	 * gb2312 转化 iso8859-1
	 * @param old
	 * @return
	 */
	public static String GB2ISO( String old ) {
		String newer = null ;
		try {
			newer = new String( old.getBytes("gb2312"),"iso8859-1") ;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return newer ;
	}
	

	/**
	 * gb2312 转化 UTF-8
	 * @param old
	 * @return
	 */
	public static String GB2UTF( String old ) {
		String newer = null ;
		try {
			newer = new String( old.getBytes("gb2312"),"utf-8") ;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return newer ;
	}

	/**
	 * UTF-8 转化 gb2312 
	 * @param old
	 * @return
	 */
	public static String UTF2GB( String old ) {
		String newer = null ;
		try {
			newer = new String( old.getBytes("utf-8"),"gb2312") ;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return newer ;
	}

	
	/**
	 * 打印到标准输出
	 * @param info
	 * @param args
	 */
	public static void PrintInfo( String... infos) {
		//System.out.print("==> "+info+" <== ");
        for (String s : infos) {  
        	System.out.print("==> "+s+" <== ");
        }
        System.out.println();
	}
	
	/**
	 * 将字符串由 oldEncode 转化 newEncode
	 * @param old
	 * @param oldEncode
	 * @param newEncode
	 * @return
	 */
	public static String ChangeCharacterEncoding(String old , String oldEncode , String newEncode) {

		String newer = null ;
		try {
			newer = new String( old.getBytes(oldEncode),newEncode) ;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return newer ;
	}

	/**
	 * 查询文件
	 * @param folder
	 * @param keyWord
	 * @return
	 */
	public static File[] searchFile(File folder, final String keyWord) {
		File[] subFolders = folder.listFiles(new FileFilter() {
			
			 public boolean accept(File pathname) {
				 if (pathname.isDirectory()|| (pathname.isFile() && pathname.getName().toLowerCase().contains(keyWord.toLowerCase())))
					 return true;
				 return false;
			 }
			
		});
		
		List<File> result = new ArrayList<File>();
		for (int i = 0; i < subFolders.length; i++) {
			if (subFolders[i].isFile()) {
				result.add(subFolders[i]);
			}else {
				File[] foldResult = searchFile(subFolders[i], keyWord);
				for (int j = 0; j < foldResult.length; j++) {
					result.add(foldResult[j]);
				}
			}
		}
		
		File files[] = new File[result.size()];
		result.toArray(files);
		return files;
	}
	
	/**
	 * 获取目录所有文件名
	 * @param dir
	 * @param keyword
	 * @return
	 */
	public static String[] GetFiles(String dir , String keyword ) {

		File folder = new File(dir);
		
		if (!folder.exists()) {
			return null ;
		}
		ArrayList<String> rst = new ArrayList<String>();
		File[] result = searchFile(folder, keyword);
		for (int i = 0; i < result.length; i++) {
			rst.add( result[i].getName()) ;
		}
		
		return rst.toArray(new String[0]) ;
	}
	

	
	/**
	 * 取一定长度的随机字符串,最大为32
	 * @param len
	 * @return
	 */
	public static String GetRandomCodes(int len) {
        String s = null;
        try {
            s = new String(UUID.randomUUID().toString().replaceAll("-", "").substring(0, (len>32?32:len)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }
	
	/**
	 * 获取这个月最后一天日期
	 * @param year
	 * @param month
	 * @return AS: 216-09-30
	 */
	@SuppressWarnings("deprecation")
	public static String GetLastDayOfMonth(String year, String month) {
		String startTime = year+"-"+month+"-01";
		String endTime = year+"-"+month+"-30";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		Date myDate;
		try {
			myDate = sdf.parse(startTime);
			cal.set(Calendar.YEAR,myDate.getYear()+1900) ;
			cal.set(Calendar.MONTH, myDate.getMonth());
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.add(Calendar.MONTH, 1);
			cal.add(Calendar.DAY_OF_MONTH, -1);
			endTime =sdf.format(cal.getTime()) ;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return endTime ;
	}
	
	/**
	 * 取上一月同天
	 * @param monthDay
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String GetLastMonthDay(String monthDay) {
		String rst = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");Calendar cal = Calendar.getInstance();
		Date myDate;
		try {
			myDate = sdf.parse(monthDay);
			cal.set(Calendar.YEAR,myDate.getYear()+1900) ;
			cal.set(Calendar.MONTH, myDate.getMonth());
			cal.set(Calendar.DATE, myDate.getDate());
			cal.add(Calendar.MONTH, -1);
			rst =sdf.format(cal.getTime()) ;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return rst ;
	}
	
	
	/**
	 * 以字符替换方式加密数据
	 * @param oldStr
	 * @return
	 */
	public static String ChangeString(String oldStr) {
		String rst = "" ;
		String aStr = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789=+/" ;
		String bStr = "OrsLPFGwQCHzASR67efghYUd5VWKy+/90=pqM48X2ZaNbcT3D1IJvijklmnotuBEx" ;
		
		for( int i = 0 ; i<oldStr.length() ; i++) {
			int ind = aStr.indexOf(oldStr.charAt(i));
			if( ind > -1 ) {
				rst += bStr.charAt(ind) ;
			} else {
				rst += oldStr.charAt(i) ;
			}
		}
		
		return rst ;
	}
	
	public static String ChangeString1(String oldStr) {
		String rst = "" ;
		String aStr = "OrsLPFGwQCHzASR67efghYUd5VWKy+/90=pqM48X2ZaNbcT3D1IJvijklmnotuBEx" ;
		String bStr = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789=+/" ;

		for( int i = 0 ; i<oldStr.length() ; i++) {
			int ind = aStr.indexOf(oldStr.charAt(i));
			if( ind > -1 ) {
				rst += bStr.charAt(ind) ;
			} else {
				rst += oldStr.charAt(i) ;
			}
			
		}
		
		return rst ;
	}
	/**
	 * 加密字符串；Base64+字符变换2次
	 * @param oldStr
	 * @return
	 */
	public static String EncodeString(String oldStr){
		String rst = "" ;
		try {
			rst = ChangeString( ChangeString(encodeBase64(oldStr.getBytes("utf-8"))) );
		} catch (Exception e) {
			e.printStackTrace();
		}   
		return rst;
	}

	/**
	 * 解密字符串；字符变换+Base64
	 * @param oldStr
	 * @return
	 */
	public static String DecodeString(String oldStr) {
		String rst = "" ;
		try {
			CommonUtil.PrintInfo(ChangeString1(ChangeString1(oldStr)));
	        rst = new String((decodeBase64(ChangeString1(ChangeString1(oldStr))))) ;
		} catch (Exception e) {
			e.printStackTrace();
		}   
		return rst;
	}
	
	/***
	 * encode by Base64
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String encodeBase64(byte[]input) {
		
		try {
			Class clazz = Class.forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");
			Method mainMethod= clazz.getMethod("encode", byte[].class);
			mainMethod.setAccessible(true);
			Object retObj=mainMethod.invoke(null, new Object[]{input});
			return (String)retObj;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null ;
	}
	/***
	 * decode by Base64
	 */
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static byte[] decodeBase64(String input) {
		try {
			Class clazz=Class.forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");
			Method mainMethod;
			mainMethod = clazz.getMethod("decode", String.class);
			mainMethod.setAccessible(true);
			 Object retObj=mainMethod.invoke(null, input);
			 return (byte[])retObj;
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null ;
	}

}
