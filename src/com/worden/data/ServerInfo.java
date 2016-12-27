package com.worden.data;

import com.worden.common.CommonUtil;
import com.worden.db.MysqlUtils;
/**
 * 设备硬件信息
 * @author Worden
 * @version 1.0
 */
public class ServerInfo {
	private String id = "";
	private String ordernumber;
	private String linenumber;
	private String matternumber;
	private String matterinfo;
	private String quantity;
	private String deliverytime;
	private String contractnumber;

	public void SetValue(String ordernumber, String linenumber, String matternumber, String matterinfo, String quantity, String deliverytime, String contractnumber ) {
		this.ordernumber = ordernumber ;
		this.linenumber = linenumber ;
		this.matternumber = matternumber ;
		this.matterinfo = matterinfo ;
		this.quantity = quantity ;
		this.deliverytime = deliverytime ;
		this.contractnumber = contractnumber ;
		this.id = ordernumber+linenumber+matternumber ;
		
	}
	
	public String GetID() {		
		return id ;
	}
	
	public String GetContractNumber() {
		return CommonUtil.EncodeString(this.contractnumber) ;
	}
	
	/**
	 * 将Server数据插入数据库
	 * @param mysql
	 * @return 0：正常插入；1：重复；2：插入失败
	 */
	public int InsertDB(MysqlUtils mysql) {
		if( mysql.getCount("server", "id = '"+GetID()+"' ") > 0 ) {
			return 1 ; 
		}
		//必须要有到达日期
		if( this.deliverytime.length() < 4) return 2 ;
		
		int f = mysql.Insert("server", "id, ordernumber, linenumber, matternumber, matterinfo, quantity, deliverytime, contractnumber, createdAt, updatedAt",
				"'"+id+"', '"+ordernumber+"', '"+linenumber+"', '"+matternumber+"', '"+matterinfo+"', '"+quantity+"', '"+deliverytime+"', '"+GetContractNumber()+"', NOW(), NOW()");
		
		if( f != 1 ) {
			 return 2 ;
		}
		
		return 0 ;
	}

}
