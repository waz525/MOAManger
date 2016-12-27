package com.worden.data;

/**
 * 签到状态类
 * @author Worden
 *
 */
public class SignStat {
	/**
	 * 工号
	 */
	private String employeeNumber = "" ;
	/**
	 * 姓名
	 */
	private String realName = "" ;
	/**
	 * 常驻地
	 */
	private String resident = "" ;
	public String getResident() {
		return resident;
	}



	public void setResident(String resident) {
		this.resident = resident;
	}

	/**
	 * 一个月的签到情况
	 */
	private String[] signStat = new String[31] ; 

	public SignStat() {
		for( int i = 0 ; i<signStat.length ; i++) {
			signStat[i] = "休息";
		}
	}
	
	
	
	public String getEmployeeNumber() {
		return employeeNumber;
	}


	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}


	public String getRealName() {
		return realName;
	}
	
	public void setRealName(String realName) {
		this.realName = realName;
	}

	public void setSignStat(int index , String stat ) {
		signStat[index] = stat ;
	}
	
	public String[] getSignStat() {
		return this.signStat ;
	}
	
}
