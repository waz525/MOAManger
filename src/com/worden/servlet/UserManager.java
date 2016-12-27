package com.worden.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.worden.common.CommonUtil;
import com.worden.common.GsonUtil;
import com.worden.db.DBGsonUtil;
import com.worden.db.MysqlUtils;

public class UserManager extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public UserManager() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request,response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8"); 
		

		PrintWriter out = response.getWriter();
		MysqlUtils mysql  = new MysqlUtils(CommonUtil.dbHost+":"+CommonUtil.dbPort,CommonUtil.dbUser,CommonUtil.dbPassword,CommonUtil.dataBaseName) ;
		
		
		/**
		 * 处理类型
		 * 1: 查询 ; 2: 添加 ; 3: 修改   ; 4: 重置密码  ; 5: 删除用户  ; 6: 重启设备ID
		 */
		String type = request.getParameter("type") ;
		
		if(type != null && Integer.parseInt(type)  == 1 ) { //查询用户
			String username = request.getParameter("username") ;
			String realname = request.getParameter("realname") ;
			String mobile = request.getParameter("mobile") ;
			String nextApproval_id = request.getParameter("nextApproval_id") ;
			String resident = request.getParameter("resident") ;
			
			String where = "" ;
			if( username != null && username.length() > 0 ) {
				if( where.length() > 0 ) where += " and " ;
				where+=" username like '%"+username+"%' ";
			}
			if( realname != null && realname.length() > 0 ) {
				if( where.length() > 0 ) where += " and " ;
				where+=" realname like '%"+realname+"%' ";
			}
			if( mobile != null && mobile.length() > 0 ) {
				if( where.length() > 0 ) where += " and " ;
				where+=" mobile like '%"+mobile+"%' ";
			}
			if( nextApproval_id != null && nextApproval_id.length() > 0 ) {
				if( where.length() > 0 ) where += " and " ;
				where+=" nextApproval_id like '%"+nextApproval_id+"%' ";
			}
			if( resident != null && resident.length() > 0 ) {
				if( where.length() > 0 ) where += " and " ;
				where+=" resident like '%"+resident+"%' ";
			}
			
			if( where.length() == 0 ) where = " 1 = 1 " ;
			where += " order by username " ;
			out.println(DBGsonUtil.RowList2JsonArry(mysql.Query("user", "id,username,realname,nextApproval_id,mobile,resident,isout" , where) ) ) ;

			
		} else if (type != null && Integer.parseInt(type)  == 2 ) { //添加用户

			String username = request.getParameter("username") ;
			String realname = request.getParameter("realname") ;
			String mobile = request.getParameter("mobile") ;
			String password = request.getParameter("password") ;
			String resident = request.getParameter("resident") ;
			String nextApproval_id = request.getParameter("nextApproval_id") ;
			String isout = request.getParameter("isout");

			//获取id，24位，并保证不重复
			int idLen = 24 ;
			String id = CommonUtil.GetRandomCodes(idLen) ;
			while( mysql.getCount("user", " id = '"+id+"' ") == 1 ) {
				id = CommonUtil.GetRandomCodes(idLen) ;
			}
			
			int rst = mysql.Insert("user", "id,username,realname,mobile,password,nextApproval_id,resident,isout,createdAt,updatedAt", "'"+id+"','"+username+"','"+realname+"','"+mobile+"','"+password+"','"+nextApproval_id+"','"+resident+"','"+isout+"', NOW(), NOW()") ;
			if( rst == 1 ) {
				out.println( DBGsonUtil.RowList2Json(mysql.Query("user", "id,username,realname,mobile,password,nextApproval_id,resident,isout", " id = '"+id+"' ")) );
			} else {
				out.println( GsonUtil.SimpleJsonString("ERROR","Insert Error "+rst) );
			}
			
		} else if (type != null && Integer.parseInt(type)  == 3 ) { //修改用户

			String username = request.getParameter("username") ;
			String realname = request.getParameter("realname") ;
			String mobile = request.getParameter("mobile") ;
			String nextApproval_id = request.getParameter("nextApproval_id") ;
			String resident = request.getParameter("resident") ;
			String id = request.getParameter("id") ;
			String isout = request.getParameter("isout");
			

			int rst = mysql.Update("user", "username = '"+username+"' ,  realname = '"+realname+"' ,  mobile = '"+mobile+"' ,  nextApproval_id = '"+nextApproval_id+"',  resident = '"+resident+"',  isout = '"+isout+"' , updatedAt = NOW() ",  " id = '"+id+"' ")  ;
			if( rst == 1 ) {
				out.println( DBGsonUtil.RowList2Json(mysql.Query("user", "id,username,realname,mobile,password,nextApproval_id,resident,isout,updatedAt", " id = '"+id+"' ")) );
			} else {
				out.println( GsonUtil.SimpleJsonString("ERROR","Update Data Error "+rst) );
			}
			
		} else if (type != null && Integer.parseInt(type)  == 4 ) { //重置密码
			String id = request.getParameter("id") ;
			String password = request.getParameter("password") ;

			int rst = mysql.Update("user", "password = '"+password+"' , updatedAt = NOW() ",  " id = '"+id+"' ")  ;
			if( rst == 1 ) {
				out.println( DBGsonUtil.RowList2Json(mysql.Query("user", "id,username,realname,mobile,password,nextApproval_id,updatedAt", " id = '"+id+"' ")) );
			} else {
				out.println( GsonUtil.SimpleJsonString("ERROR","ChangePassword Error "+rst) );
			}
			
		} else if (type != null && Integer.parseInt(type)  == 5 ) { //删除用户
			String id = request.getParameter("id") ;

			int rst = mysql.Delete("user", " id = '"+id+"' ") ;
			if( rst == 1 ) {
				out.println( GsonUtil.SimpleJsonString("DELETE",true) );
			} else {
				out.println( GsonUtil.SimpleJsonString("DELETE",false) );
			}
			
		} else if (type != null && Integer.parseInt(type)  == 6 ) { //重置设备ID
			String id = request.getParameter("id") ;
			int rst = mysql.Update("user", "deviceid = NULL , updatedAt = NOW() ",  " id = '"+id+"' ")  ;
			if( rst == 1 ) {
				out.println( GsonUtil.SimpleJsonString("ChangeDeviceid",true) );
			} else {
				out.println( GsonUtil.SimpleJsonString("ChangeDeviceid",false) );
			}
			
		} else {
			out.println( GsonUtil.SimpleJsonString("ERROR","Invalid Type") );
		}
		

	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
