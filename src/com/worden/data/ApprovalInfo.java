package com.worden.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 请假类
 * @author Worden
 *
 */
public class ApprovalInfo {
	public String startTime = "" ;
	public String endTime = "" ;
	public int minDay = -1 ;
	public int maxDay = -1 ;
	
	public int getMinDay() {
		return minDay;
	}


	public int getMaxDay() {
		return maxDay;
	}


	/**
	 * 计算日期区间，再取minDay和maxDay
	 * @param startDay
	 * @param endDay
	 */
	@SuppressWarnings("deprecation")
	public void calcDays(String startDay , String endDay) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date sDay = sdf.parse(startDay);
			Date eDay = sdf.parse(endDay) ;
			Date sTime = sdf.parse(startTime) ;
			Date eTime = sdf.parse(endTime) ;

			if( sDay.getTime() > sTime.getTime() ) {
				this.minDay = sDay.getDate();
			} else {
				this.minDay = sTime.getDate() ;
			}
			
			if( eDay.getTime() > eTime.getTime()) {
				this.maxDay = eTime.getDate() ;
			} else {
				this.maxDay = eDay.getDate() ;
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}


	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

}