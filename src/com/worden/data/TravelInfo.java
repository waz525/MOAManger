package com.worden.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.worden.common.CommonUtil;
import com.worden.common.ExcelUtil;
import com.worden.db.Cell;
import com.worden.db.MysqlUtils;
import com.worden.db.Row;

public class TravelInfo {
	private String startDate ;
	private String endDate ;
	private String destination ;
	private int minDay = -1 ;
	private int maxDay = -1 ;

	public int getMinDay() {
		return minDay;
	}


	public int getMaxDay() {
		return maxDay;
	}


	@SuppressWarnings("deprecation")
	public void calcDays(String startDay , String endDay) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date sDay = sdf.parse(startDay);
			Date eDay = sdf.parse(endDay) ;
			Date sTime = sdf.parse(startDate) ;
			Date eTime = eDay;
			
			if( !endDate.equalsIgnoreCase("待定") ) {
				eTime = sdf.parse(endDate);
			}
			
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
	
	

	public String getStartDate() {
		return startDate;
	}


	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}


	public String getEndDate() {
		return endDate;
	}


	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}


	public String getDestination() {
		return destination;
	}


	public void setDestination(String destination) {
		this.destination = destination;
	}


	public String ProduceExcelFile(String year  , String month , String fileDir) {

		String  endTime = year+"-"+month+"-20";
		String  startTime = year+"-"+month+"-21";
		startTime = CommonUtil.GetLastMonthDay(endTime);
		
		MysqlUtils mysql  = new MysqlUtils(CommonUtil.dbHost+":"+CommonUtil.dbPort,CommonUtil.dbUser,CommonUtil.dbPassword,CommonUtil.dataBaseName) ;
		
		Row[] queryResult = mysql.Query("select a.username,a.realname,b.resident,b.destination,b.startDate,b.endDate,b.reason from user a , travel b where a.id = b.userid and  b.endDate <= '"+endTime+"' AND b.endDate > '"+startTime+"' order by endDate " ) ;	
		mysql.Close();
		
		///////////////////////////////////////////////////////////////////////////////////
		//将签到信息写入excel文件
		String newFile = "驻地人员出差表格 - "+year+month+".xlsx" ;
		String fullFileName = fileDir + newFile ;
		ExcelUtil eu = new ExcelUtil() ;
		Workbook workBook = eu.LoadExcelFile( fileDir+"驻地人员出差表格.xlsx") ;
		Sheet sheet = workBook.getSheetAt(0) ;
		//设置单元格样式
		CellStyle cellStyle = workBook.createCellStyle(); 
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框    
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框    
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框    
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框   
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中    
		
		int rowIndex = 1 ;
		org.apache.poi.ss.usermodel.Row row;
		org.apache.poi.ss.usermodel.Cell cell ;
		for( Row r : queryResult) {
			Cell[] cellList = r.getCellList();
			row = sheet.getRow(rowIndex) ;
			//建立相应的row和cell
			if( row == null ) row = sheet.createRow(rowIndex) ;
			for( int j = 0 ; j <= 7 ; j++ ) {
				if( row.getCell(j) == null )  {
					cell = row.createCell(j) ; 
					cell.setCellStyle(cellStyle);
				}
			}
			
			row.getCell(0).setCellValue(cellList[0].getValue().toString());
			row.getCell(1).setCellValue(cellList[1].getValue().toString());
			row.getCell(2).setCellValue("技术支持部");
			row.getCell(3).setCellValue(cellList[2].getValue().toString());
			row.getCell(4).setCellValue(cellList[3].getValue().toString());
			row.getCell(5).setCellValue(cellList[4].getValue().toString());
			row.getCell(6).setCellValue(cellList[5].getValue().toString());
			row.getCell(7).setCellValue(cellList[6].getValue().toString());
			
			rowIndex++;
			
		}
		
		eu.SaveExcelFile(workBook, fullFileName);
		///////////////////////////////////////////////////////////////////////////////////
		
		//返回新文件名
		return newFile;
		
	}
}
