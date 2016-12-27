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

public class ServerInfoManager extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public ServerInfoManager() {
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

		String type = request.getParameter("type") ;
		MysqlUtils mysql  = new MysqlUtils(CommonUtil.dbHost+":"+CommonUtil.dbPort,CommonUtil.dbUser,CommonUtil.dbPassword,CommonUtil.dataBaseName) ;
		
		
		if(type != null && Integer.parseInt(type)  == 1 ) { //查询
			String OrderNumber =  request.getParameter("OrderNumber") ;
			String LineNumber =  request.getParameter("LineNumber") ;
			if( LineNumber != null && LineNumber.length() > 0 ) {
				out.println( DBGsonUtil.RowList2JsonArry( mysql.Query("server", "ordernumber,linenumber,matternumber,matterinfo,quantity,deliverytime,contractnumber",
					"ordernumber like '%"+OrderNumber+"%' and linenumber = '"+LineNumber+"' ") ) ) ;
			} else {
				out.println( DBGsonUtil.RowList2JsonArry( mysql.Query("server", "ordernumber,linenumber,matternumber,matterinfo,quantity,deliverytime,contractnumber",
						"ordernumber like '%"+OrderNumber+"%' ") ) ) ;				
			}
			
		} else {
			out.println( GsonUtil.SimpleJsonString("ERROR","Invalid Type") );
		}
		
		mysql.Close();
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
