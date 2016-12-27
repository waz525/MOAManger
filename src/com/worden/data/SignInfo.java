package com.worden.data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 签到信息类
 * @author Worden
 *
 */
public class SignInfo {
	private int in ;
	private int out ; 
	private String sign_date ; 
	private String out_time ;
	private String inAddress ;
	private String createdAt ;
	
	public String getInfo() {
		String rst = "in: "+in+"; out: "+out+"; sign_date: "+sign_date+"; out_time: "+out_time+"; inAddress: "+inAddress+"; createdAt: "+createdAt+" !!! ";
		return rst ;
	}
	/**
	 * 设置信息
	 * @param id
	 * @param in
	 * @param out
	 * @param sign_date
	 * @param out_time
	 * @param inAddress
	 * @param createdAt
	 */
	public void setInfo( String in , String out , String sign_date, String out_time, String inAddress, String createdAt) {
		this.in = Integer.parseInt(in) ;
		this.out = Integer.parseInt(out);
		this.sign_date = sign_date ;
		this.out_time = out_time;
		this.inAddress = inAddress;
		this.createdAt = createdAt;
	}
	
	/**
	 * 获取签到的日号，取不到为-1
	 * @return
	 */
	public int getSignDay() {
		if( sign_date.indexOf("-")>-1) {
			String[] str =  sign_date.split("-");
			return Integer.parseInt(str[2]);
		}
		return -1;
	}
	
	/**
	 * 从inAddress里取城市名
	 * @return
	 */
	public String getSignCity() {

		if( inAddress.indexOf("市")>-1) {
			String[] str =  inAddress.split("市");
			return str[0] ;
		}
		return "未取到城市名" ;
	}

	/**
	 * 获取工作状态
	 * <p>有进有出，且时间差超过8小时为正常，其余为异常。
	 * <p>正常显示所在市名
	 * @return
	 */
	public String getSignStatus() {
		
		if( isSignNormal() ) {
			return getSignCity();
		} else {
			return "异常" ;
		}
	}
	/**
	 * 判断是否签到正常
	 * <p>有进有出，且时间差超过8小时为正常，其余为异常。
	 * @return
	 */
	public boolean isSignNormal() {

		if( out == 0 ) {
			return false;
		}
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Date intime;
		Date outtime;
		float diff = -1 ;
		try {
			intime = df.parse(this.createdAt);
			outtime = df.parse( this.out_time);
			diff = (outtime.getTime() - intime.getTime())/ (1000 * 60 * 60);
				
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if( diff >= 8 ) {
			return true;
		} else {
			return false;
		}
	}
}
