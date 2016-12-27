package com.worden.common;


import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelUtil {
	
	/**
	 * 解析excel文件，取出所有数据，并生成table的html代码
	 * @param filePath
	 * @return
	 */
	public String DistillExcelForHtml(String filePath) {

		String Content = "" ;
		int maxLen = 0 ;
		Content += "<table border=\"1\" id=\"tableinfo\" name=\"tableinfo\" >" ;
		FileInputStream inStream = null;
		try {

			inStream = new FileInputStream(new File(filePath));
			Workbook workBook = WorkbookFactory.create(inStream);
			Sheet sheet = workBook.getSheetAt(0) ;
			Row row = null ;
			for (int ind = 0; ind < sheet.getLastRowNum() + 1; ind++) {
				row = sheet.getRow(ind) ;
				if( row != null ) {
					
					Cell cell = row.getCell(0);
					if( getCellValue(cell).length() == 0 ) continue;
					
					Content += "<tr>" ;
					if( row.getLastCellNum() > maxLen ) maxLen = row.getLastCellNum() ;
					for (int j = 0 ; j < row.getLastCellNum() ; j++) {
						cell = row.getCell(j);
						Content += "<td>"+getCellValue(cell)+"</td>" ;
					}
					if( row.getLastCellNum() < maxLen ) {
						for( int i = row.getLastCellNum() ; i<maxLen ; i++) {
							Content += "<td></td>" ;
						}
					}
					Content += "<td></td>" ;
					Content += "</tr>" ;
				}
			}
		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Content += "</table>";
		
		
		//System.out.println("\nExcelUtil: Content -->  "+Content+ "\n===> END Content <==");
		return Content ;
		
	}

	/**
	 * 解析excel文件，取出所有数据，并生成table的html代码
	 * @param filePath
	 * @param columnList 形如："0:1:3"
	 * @return
	 */
	public String DistillExcelForHtml(String filePath, String columnList) {

		String Content = "" ;

		String[] colListStr = columnList.split(",");
		int[] colList = new int[colListStr.length] ;
		for( int j = 0 ; j < colListStr.length ; j++) {
			colList[j] = Integer.parseInt(colListStr[j]) ;
			CommonUtil.PrintInfo(""+colList[j]);
		}
		Content += "<table border=\"1\" id=\"tableinfo\" name=\"tableinfo\" >" ;
		FileInputStream inStream = null;
		try {

			inStream = new FileInputStream(new File(filePath));
			Workbook workBook = WorkbookFactory.create(inStream);
			Sheet sheet = workBook.getSheetAt(0) ;
			Row row = null ;
			for (int ind = 0; ind < sheet.getLastRowNum() + 1; ind++) {
				row = sheet.getRow(ind) ;
				if( row != null ) {
					
					Cell cell = row.getCell(0);
					if( getCellValue(cell).length() == 0 ) continue;
					
					Content += "<tr>" ;
					for( int j = 0 ; j < colList.length ; j++) {
						
						if(colList[j] < row.getLastCellNum() ) {
							cell = row.getCell(colList[j]);
							Content += "<td>"+getCellValue(cell)+"</td>" ;
						} else {
							Content += "<td></td>" ;
						}
					}
					
					Content += "</tr>" ;
				}
			}
		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Content += "</table>";
		
		
		//System.out.println("\nExcelUtil: Content -->  "+Content+ "\n===> END Content <==");
		return Content ;
		
	}

	/**
	 * 获取单元格数据
	 * @param cell
	 * @return
	 */
	protected String getCellValue(Cell cell) {
	        String cellValue = "";
	        if (cell != null) {
	            switch (cell.getCellType()) {
	                case Cell.CELL_TYPE_NUMERIC:
	                    if (DateUtil.isCellDateFormatted(cell)) {
	                        //DataFormatter formatter = new DataFormatter();
	                        //cellValue = formatter.formatCellValue(cell);
	                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	                        cellValue= sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue())).toString();
	                    } else {
	                    	DecimalFormat df = new DecimalFormat("0");
	                    	cellValue = df.format(cell.getNumericCellValue());
	                    }
	                    break;
	                case Cell.CELL_TYPE_STRING:
	                    cellValue = cell.getStringCellValue();
	                    break;
	                case Cell.CELL_TYPE_BOOLEAN:
	                    cellValue = String.valueOf(cell.getBooleanCellValue());
	                    break;
	                case Cell.CELL_TYPE_FORMULA:
	                    cellValue = String.valueOf(cell.getCellFormula());
	                    break;
	                case Cell.CELL_TYPE_BLANK:
	                    cellValue = "";
	                    break;
	                case Cell.CELL_TYPE_ERROR:
	                    cellValue = "";
	                    break;
	                default:
	                    cellValue = cell.toString().trim();
	                    break;
	            }
	        }
	        return cellValue.trim();
	    }

	
	public void CopyFile(String oldFile , String newFile ) {
		int length=2097152;
		try {
			FileInputStream in=new FileInputStream(oldFile);
			FileOutputStream out=new FileOutputStream(newFile);
			byte[] buffer=new byte[length];
			while(true){
				int ins=in.read(buffer);
				if(ins==-1){
					in.close();
					out.flush();
					out.close();
				} else {
					out.write(buffer,0,ins);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public Workbook LoadExcelFile(String filePath) {

		FileInputStream in = null;  
		Workbook wb = null;
		
		try {
				in = new FileInputStream(filePath);
				wb = WorkbookFactory.create(in);
				in.close(); 
		} catch (IOException e) {
			e.printStackTrace();
		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}
		
		return wb ;
	}
	
	public void SaveExcelFile(Workbook wb  , String filePath) {

		FileOutputStream of = null; 
		try {
			of = new FileOutputStream(filePath);
			wb.write(of); 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
