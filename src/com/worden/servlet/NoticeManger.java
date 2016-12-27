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

public class NoticeManger extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public NoticeManger() {
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
		
		String type = request.getParameter("type") ;
		if(type != null && Integer.parseInt(type)  == 1 ) { //查询消息记录
			response.setContentType("application/json; charset=utf-8");
			PrintWriter out = response.getWriter();
			MysqlUtils mysql  = new MysqlUtils(CommonUtil.dbHost+":"+CommonUtil.dbPort,CommonUtil.dbUser,CommonUtil.dbPassword,CommonUtil.dataBaseName) ;
			out.println(DBGsonUtil.RowList2JsonArry( mysql.Query("select publisher,noticeContent,noticeTitle,noticeGroup,createdAt from notice order by createdAt desc  " ) ) );
			mysql.Close();
			
		} else if(type != null && Integer.parseInt(type)  == 2 ) { //响应发送消息，记录消息内容到数据
			response.setContentType("application/json; charset=utf-8");
			PrintWriter out = response.getWriter();
			String NewNoticeTitle = request.getParameter("NewNoticeTitle") ;
			String NewNoticeContent = request.getParameter("NewNoticeContent") ;
			MysqlUtils mysql  = new MysqlUtils(CommonUtil.dbHost+":"+CommonUtil.dbPort,CommonUtil.dbUser,CommonUtil.dbPassword,CommonUtil.dataBaseName) ;


			//获取id，24位，并保证不重复
			int idLen = 24 ;
			String id = CommonUtil.GetRandomCodes(idLen) ;
			while( mysql.getCount("user", " id = '"+id+"' ") == 1 ) {
				id = CommonUtil.GetRandomCodes(idLen) ;
			}
			
			//String publisher = mysql.Query("select id from user where username = 'fhadmin'")[0].getCellList()[0].getValue().toString();
			int rst = mysql.Insert("notice", "id,publisher,noticeContent,noticeTitle,noticeGroup,createdAt,updatedAt", "'"+id+"','系统管理员','"+NewNoticeContent+"','"+NewNoticeTitle+"','ALL',NOW(),NOW()");
			mysql.Close();
			if( rst == 1 ) {
				out.println(GsonUtil.SimpleJsonString("result", true));
			} else {
				out.println(GsonUtil.SimpleJsonString("result", false));
			}
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
