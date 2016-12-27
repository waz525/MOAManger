package com.worden.db;

import java.util.HashMap;
import java.util.Map;

import com.worden.common.GsonUtil;
/**
 * 转换数据库查询结果到json
 * @author Worden
 *
 */
public class DBGsonUtil extends GsonUtil {

	/**
	 * 将数据库查询结果转换成json
	 * @param rowList
	 * @return
	 */
	
	public static String RowList2Json(Row[] rowList ) {
		String rst = "";
		
		if( rowList != null && rowList.length > 0 ) {
			
			for( Row row :rowList) {
				if ( rst.length() > 0 ) rst +="," ;
				Cell[] cellList = row.getCellList() ;
				Map<String,Object> tmp = new HashMap<String,Object>() ;
				for( Cell cell : cellList  ) {
					
					tmp.put(cell.getColumnName(), cell.getValue());
				}
				rst += GsonUtil.Map2Json(tmp) ;
			}
			
			if(rowList.length > 1 ) {
				rst = "["+rst+"]" ;
			}
			
		}
		
		if( rst.length() == 0 ) rst =  "[]" ;
		
		return rst ;
	}

	
	/**
	 * 将数据库查询结果转换成json
	 * @param rowList
	 * @return
	 */
	public static String RowList2JsonArry(Row[] rowList ) {
		String rst = RowList2Json(rowList) ;
		if( rst.indexOf('[') != 0) rst =  "["+rst+"]" ;
		return rst ;
		
	}
}
