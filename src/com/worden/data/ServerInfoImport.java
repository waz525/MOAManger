package com.worden.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.worden.common.CommonUtil;
import com.worden.common.ExcelUtil;
import com.worden.db.MysqlUtils;

public class ServerInfoImport extends ExcelUtil {

	private int numInsert = 0 ;
	private int numRepeat = 0 ;
	private int numError = 0 ;
	private int numSheet = 0 ;

	public int getNumInsert() {
		return numInsert;
	}

	public int getNumRepeat() {
		return numRepeat;
	}

	public int getNumError() {
		return numError;
	}
	
	public int getNumAll(){
		return numInsert+numRepeat+numError ;
	}
	

	/**
	 * 处理Excel文件，导入数据到数据库
	 * @param filePath
	 */
	public void DistillExcel(String filePath, MysqlUtils mysql) {

		ServerInfo server = new ServerInfo();
		FileInputStream inStream = null;		
		try {

			inStream = new FileInputStream(new File(filePath));
			Workbook workBook = WorkbookFactory.create(inStream);
			//遍历所有的表
			for( int i = 0 ; i < workBook.getNumberOfSheets() ; i++ ) {
				Sheet sheet = workBook.getSheetAt(i) ;
				numSheet++;
				
				if(sheet.getFirstRowNum() < 0 ) continue;
				Row row = sheet.getRow(sheet.getFirstRowNum() ) ;
				//判断列数
				if( row == null || row.getLastCellNum() < 10 ) continue;
				//判断是不是需要的数据
				if( !getCellValue(row.getCell(1)).equals("销售订单号") ) continue;
				for (int ind = sheet.getFirstRowNum() + 1; ind < sheet.getLastRowNum() + 1; ind++) {
					row = sheet.getRow(ind) ;
					if( row != null ) {
						if( row.getLastCellNum() < 10 ) continue;
						if( getCellValue(row.getCell(1)).length() == 0 ) continue;
						server.SetValue(getCellValue(row.getCell(1)), getCellValue(row.getCell(2)), getCellValue(row.getCell(4)), getCellValue(row.getCell(5)), getCellValue(row.getCell(6)), getCellValue(row.getCell(10)), getCellValue(row.getCell(11)));
						
						int rst = server.InsertDB(mysql);
						if( rst == 0 ) {
							this.numInsert++ ;
						}
						else if( rst == 1 ){
							this.numRepeat++;
						}
						else{
							this.numError++;
							//CommonUtil.PrintInfo(GsonUtil.Object2Json(server));
						}
					}
				}
				CommonUtil.PrintInfo("numInsert:"+numInsert+" ; numRepeat:"+numRepeat+" ; numError:"+numError);
			}
		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


		CommonUtil.PrintInfo("DistillExcel OVER !!! numInsert:"+numInsert+" ; numRepeat:"+numRepeat+" ; numError:"+numError);
	}

	public int getNumSheet() {
		return numSheet;
	}
	
}
