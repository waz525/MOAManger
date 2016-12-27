package com.worden.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.worden.common.CommonUtil;
import com.worden.common.ExcelUtil;
import com.worden.common.GsonUtil;
import com.worden.db.Cell;
import com.worden.db.MysqlUtils;
import com.worden.db.Row;

public class SignInfoDistill {
	
	/**
	 * 查询个人签到情况，返回json数组
	 * @param StartTime
	 * @param EndTime
	 * @param UserID
	 * @return
	 */
	public String QueryOneSignStat(String StartTime, String EndTime, String UserID) {
		String rst = "" ;
		MysqlUtils mysql  = new MysqlUtils(CommonUtil.dbHost+":"+CommonUtil.dbPort,CommonUtil.dbUser,CommonUtil.dbPassword,CommonUtil.dataBaseName) ;
		Row[] signQueryList = mysql.Query( "sign","id,signin,signout,sign_date,out_time,inAddress,createdAt,outAddress,overtime" , "userId = '"+UserID+"' and createdAt >= '"+StartTime+" 00:00:00' and createdAt <= '"+EndTime+" 23:59:59' ") ;
		if( signQueryList!= null && signQueryList.length > 0 ) {
			
			for( Row signQueryRow:signQueryList) {
				if ( rst.length() > 0 ) rst +="," ;
				Map<String,Object> tmp = new HashMap<String,Object>() ;
				Cell[] signInfo = signQueryRow.getCellList();
				for( Cell cell : signInfo  ) {
					tmp.put(cell.getColumnName(), cell.getValue());
				}

				SignInfo sign = new SignInfo() ;
				sign.setInfo( signInfo[1].getValue().toString(), signInfo[2].getValue().toString(), signInfo[3].getValue().toString(), signInfo[4].getValue().toString(), signInfo[5].getValue().toString(), signInfo[6].getValue().toString());
				if( sign.isSignNormal() ) {
					tmp.put("status", "0");// 0为正常；1为异常；2为提交异常申请
				} else {
					tmp.put("status", "1");
					if( mysql.getCount("approval", "applicantId = '"+UserID+"' and type = 2 and startDate <= '"+signInfo[3].getValue().toString()+"' and endDate >= '"+signInfo[3].getValue().toString()+"' ") > 0 ) {
						tmp.put("status", "2");
					}
				}
				
				rst += GsonUtil.Map2Json(tmp) ;
			}
		}
		
		mysql.Close();
		
		return "["+rst+"]"  ;
	}
	
	

	/**
	 * 按月生成考勤表，包含考勤和请假
	 * @param year
	 * @param month
	 * @param fileDir 文件生成路径
	 * @return 新文件名
	 */
	public String ProduceExcelFile(String year  , String month , String fileDir) {

		String startTime = year+"-"+month+"-01";
		String endTime = CommonUtil.GetLastDayOfMonth(year, month);
		
		
		MysqlUtils mysql  = new MysqlUtils(CommonUtil.dbHost+":"+CommonUtil.dbPort,CommonUtil.dbUser,CommonUtil.dbPassword,CommonUtil.dataBaseName) ;
		
		ArrayList<SignStat> signList = new ArrayList<SignStat>() ;
		//获取所有用户信息，不包含测试用户
		Row[] userQueryList = mysql.Query("user", "id,username,realname,resident" , "isout = 1 order by nextApproval_id,username") ;
		for( Row row:userQueryList) { 
			//单个用户信息，一个cell一个信息
			Cell[] userInfo = row.getCellList();
			String uid = userInfo[0].getValue().toString() ;
			SignStat signOneMan = new SignStat() ;
			signOneMan.setEmployeeNumber(userInfo[1].getValue().toString() );
			signOneMan.setRealName(userInfo[2].getValue().toString());
			signOneMan.setResident(userInfo[3].getValue().toString());
			
			
			
			//查询用户签到信息
			Row[] signQueryList = mysql.Query( "sign","id,signin,signout,sign_date,out_time,inAddress,createdAt" , "userId = '"+uid+"' and createdAt >= '"+startTime+" 00:00:00' and createdAt <= '"+endTime+" 23:59:59' ") ;
			if( signQueryList!= null && signQueryList.length > 0 ) {
				///////////////////////////////////////////////////////////////////////////////////
				//解析每一条签到信息
				for( Row signQueryRow:signQueryList) { 
					SignInfo sign = new SignInfo() ;
					Cell[] signInfo = signQueryRow.getCellList();
					sign.setInfo( signInfo[1].getValue().toString(), signInfo[2].getValue().toString(), signInfo[3].getValue().toString(), signInfo[4].getValue().toString(), signInfo[5].getValue().toString(), signInfo[6].getValue().toString());
					signOneMan.setSignStat(sign.getSignDay()-1, sign.getSignStatus());
				}
				
			}	
				///////////////////////////////////////////////////////////////////////////////////
				//处理请假申请
				Row[] approvalQueryList = mysql.Query("approval" , "startDate, endDate " , "applicantId = '"+uid+"' and type = 0 and startDate <='"+endTime+"' AND endDate >= '"+startTime+"' " ) ;
				//解析每一条签到信息
				if( approvalQueryList != null ) {
					for( Row approvalQueryRow:approvalQueryList) { 
						Cell[] approvalInfo  = approvalQueryRow.getCellList() ;
						ApprovalInfo appInfo = new ApprovalInfo() ;
						appInfo.setStartTime(approvalInfo[0].getValue().toString());
						appInfo.setEndTime(approvalInfo[1].getValue().toString());
						
						appInfo.calcDays(startTime, endTime);
						if( appInfo.getMinDay() > 0 && appInfo.getMaxDay() > 0) {
							for( int i = appInfo.getMinDay()  ; i<=appInfo.getMaxDay() ; i++ ) {
								signOneMan.setSignStat(i-1, "请假");
							}
						}
						
					}
				}
				
				///////////////////////////////////////////////////////////////////////////////////
				//处理签到异常申请
				Row[] apprQueryList = mysql.Query("approval" , "startDate, endDate " , "applicantId = '"+uid+"' and type = 2 and startDate <'"+endTime+"' AND endDate > '"+startTime+"' " ) ;
				//解析每一条签到信息
				if( apprQueryList != null ) {
					for( Row approvalQueryRow:apprQueryList) { 
						Cell[] approvalInfo  = approvalQueryRow.getCellList() ;
						ApprovalInfo appInfo = new ApprovalInfo() ;
						appInfo.setStartTime(approvalInfo[0].getValue().toString());
						appInfo.setEndTime(approvalInfo[1].getValue().toString());
						
						appInfo.calcDays(startTime, endTime);
						if( appInfo.getMinDay() > 0 && appInfo.getMaxDay() > 0) {
							for( int i = appInfo.getMinDay()  ; i<=appInfo.getMaxDay() ; i++ ) {
								//signOneMan.setSignStat(i-1, "异常处理");
								signOneMan.setSignStat(i-1, signOneMan.getResident());
							}
						}
					}
					
				}
				
				///////////////////////////////////////////////////////////////////////////////////
				//处理出差报务
				Row[] travelQueryList = mysql.Query("travel" , "startDate, endDate, destination " , "userid = '"+uid+"' and startDate <= '"+endTime+"' AND ( endDate = '待定' or endDate >= '"+startTime+"' ) " ) ;
				//解析每一条签到信息
				if( travelQueryList != null ) {
					for( Row  travelQueryRow:travelQueryList) { 
						Cell[]  travelInfoCell  = travelQueryRow.getCellList() ;
						TravelInfo travelInfo = new TravelInfo() ;
						travelInfo.setStartDate(travelInfoCell[0].getValue().toString());
						travelInfo.setEndDate(travelInfoCell[1].getValue().toString());
						travelInfo.setDestination(travelInfoCell[2].getValue().toString());
						
						travelInfo.calcDays(startTime, endTime);
						if( travelInfo.getMinDay() > 0 && travelInfo.getMaxDay() > 0) {
							for( int i = travelInfo.getMinDay()  ; i<=travelInfo.getMaxDay() ; i++ ) {
								signOneMan.setSignStat(i-1, travelInfo.getDestination());
							}
						}
					}
					
				}
				///////////////////////////////////////////////////////////////////////////////////
				
				//将签到信息加入信息列表
				signList.add(signOneMan);
			
			
			
		}
		//关闭数据库连接
		mysql.Close();
		
		///////////////////////////////////////////////////////////////////////////////////
		//将签到信息写入excel文件
		String newFile = "外勤补助  ("+startTime+"至"+endTime+").xlsx" ;
		String fullFileName = fileDir + newFile ;
		int numberOfDays = Integer.parseInt(endTime.split("-")[2]); 
		ExcelUtil eu = new ExcelUtil() ;
		
		Workbook workBook = eu.LoadExcelFile( fileDir+"外勤补助"+numberOfDays+".xlsx") ;

		Sheet sheet = workBook.getSheetAt(0) ;
		int rowIndex = 3 ;
		org.apache.poi.ss.usermodel.Row row;
		for( SignStat ss :signList) {
			row = sheet.getRow(rowIndex) ;
			row.getCell(0).setCellValue("技术支持部");
			row.getCell(1).setCellValue(ss.getRealName());
			row.getCell(2).setCellValue(ss.getEmployeeNumber());
			row.getCell(3).setCellValue("技术支持");
			row.getCell(5).setCellValue(ss.getResident());
			rowIndex++ ;
		}
		//空一行再写入合计等信息
		rowIndex++ ;
		row = sheet.getRow(rowIndex++) ;
		row.getCell(0).setCellValue("合计");
		row = sheet.getRow(rowIndex++) ;
		row.getCell(4).setCellValue("部门经理");
		row.getCell(8).setCellValue("签批时间");
		
		sheet = workBook.getSheetAt(1) ;
		rowIndex = 4 ;
		for( SignStat ss :signList) {
			row = sheet.getRow(rowIndex) ;
			row.getCell(0).setCellValue(ss.getRealName());
			String[] str = ss.getSignStat() ;
			for( int cellIndex = 1 ; cellIndex<numberOfDays+1 ; cellIndex++) {
				row.getCell(cellIndex).setCellValue(str[cellIndex-1]) ;
			}
			rowIndex++ ;
		}
		
		
		eu.SaveExcelFile(workBook, fullFileName);
		///////////////////////////////////////////////////////////////////////////////////
		
		//返回新文件名
		return newFile;
	}
}
