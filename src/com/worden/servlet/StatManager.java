package com.worden.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.worden.common.CommonUtil;
import com.worden.db.DBGsonUtil;
import com.worden.db.MysqlUtils;

/**
 * Servlet implementation class StatManager
 */
@WebServlet("/StatManager")
public class StatManager extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StatManager() {
        super();
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8"); 
		

		PrintWriter out = response.getWriter();
		MysqlUtils mysql  = new MysqlUtils(CommonUtil.dbHost+":"+CommonUtil.dbPort,CommonUtil.dbUser,CommonUtil.dbPassword,CommonUtil.dataBaseName) ;
		
		/**
		 * 统计类型
		 * 1: 签到; 2: 出差
		 */
		String type = request.getParameter("type") ;
		
		if(type != null && Integer.parseInt(type)  == 1 ) { //签到
			
			String DateStr = request.getParameter("DateStr") ;
			String sqlStr="select a.hour,count(*) count from ( select DATE_FORMAT(createdAt, '%h') hour from sign where sign_date = '"+DateStr+"'  order by hour) a group by a.hour ";
			
			out.println(DBGsonUtil.RowList2JsonArry(  mysql.Query(sqlStr) ) ) ;
			
		} else if(type != null && Integer.parseInt(type)  == 2 ) { //出差统计
			
			String DateStr = request.getParameter("DateStr") ;
			String sql = "select a.username, a.realname , b.count , b.alldays from user a , (select userid , count(*) count ,sum( TO_DAYS(endDate) - TO_DAYS(startDate) + 1 ) alldays  from travel where endDate like '%"+DateStr+"%' group by userid ) b  where a.id = b.userid order by b.alldays desc " ;
			
			out.println(DBGsonUtil.RowList2JsonArry(mysql.Query(sql) ) ) ;

		}
		
		
	}

}
