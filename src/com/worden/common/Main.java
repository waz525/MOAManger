package com.worden.common;

public class Main {

	public static void main(String[] args) {
		String oldStr = "明天是2016年9月28日" ;
		String eStr = CommonUtil.EncodeString(oldStr) ;

		String dStr = CommonUtil.DecodeString(eStr) ;
		//CommonUtil.PrintInfo(oldStr);
		CommonUtil.PrintInfo(oldStr, eStr,dStr);
	}

}
