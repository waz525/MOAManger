package com.worden.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.worden.common.CommonUtil;
import com.worden.data.SignInfoDistill;
import com.worden.data.TravelInfo;
import com.worden.db.DBGsonUtil;
import com.worden.db.MysqlUtils;

public class SignManager extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public SignManager() {
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
	@SuppressWarnings("deprecation")
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		

		String type = request.getParameter("type") ;
		if(type != null && Integer.parseInt(type)  == 1 ) { //导出考勤表
			
			String Year = request.getParameter("Year") ;
			String Month = request.getParameter("Month") ;
			
			String path = request.getRealPath("/");		
			String fileDir = new String(path+"/DownloadFile/");
			
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();	
			SignInfoDistill signInfo = new SignInfoDistill() ;
			out.println(signInfo.ProduceExcelFile(Year, Month, fileDir)) ;
			
		} else if(type != null && Integer.parseInt(type)  == 2 ) { //查询请假信息
			

			String Year = request.getParameter("Year") ;
			String Month = request.getParameter("Month") ;
			String UserID = request.getParameter("UserID") ;
			
			MysqlUtils mysql  = new MysqlUtils(CommonUtil.dbHost+":"+CommonUtil.dbPort,CommonUtil.dbUser,CommonUtil.dbPassword,CommonUtil.dataBaseName) ;

			String startTime = Year+"-"+Month+"-01";
			String endTime = CommonUtil.GetLastDayOfMonth(Year, Month);
			response.setContentType("application/json; charset=utf-8");
			PrintWriter out = response.getWriter();
			if( UserID.equalsIgnoreCase("ALL")) {
				out.println(DBGsonUtil.RowList2JsonArry( mysql.Query("approval" , "*" , "type = 0 and startDate <='"+endTime+"' AND endDate >= '"+startTime+"'  order by applicantId,startDate" ) ) );
			} else {
				out.println(DBGsonUtil.RowList2JsonArry( mysql.Query("approval" , "*" , "applicantId = '"+UserID+"' and type = 0 and startDate <='"+endTime+"' AND endDate >= '"+startTime+"' order by startDate " ) ) ) ;	
			}
			mysql.Close();

		} else if(type != null && Integer.parseInt(type)  == 3) { //查询个人签到信息

			String StartTime = request.getParameter("StartTime") ;
			String EndTime = request.getParameter("EndTime") ;
			String UserID = request.getParameter("UserID") ;

			response.setContentType("application/json; charset=utf-8");
			PrintWriter out = response.getWriter();
			SignInfoDistill signInfo = new SignInfoDistill() ;
			out.println(signInfo.QueryOneSignStat(StartTime, EndTime, UserID)) ;
			
		} else if(type != null && Integer.parseInt(type)  == 4) { //查询出差信息

			String Year = request.getParameter("Year") ;
			String Month = request.getParameter("Month") ;

			MysqlUtils mysql  = new MysqlUtils(CommonUtil.dbHost+":"+CommonUtil.dbPort,CommonUtil.dbUser,CommonUtil.dbPassword,CommonUtil.dataBaseName) ;

			String  endTime = Year+"-"+Month+"-20";
			String  startTime = Year+"-"+Month+"-21";
			startTime = CommonUtil.GetLastMonthDay(endTime);
			response.setContentType("application/json; charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println(DBGsonUtil.RowList2JsonArry( mysql.Query("select a.username,a.realname,b.resident,b.destination,b.startDate,b.endDate,b.reason from user a , travel b where a.id = b.userid and  b.endDate <= '"+endTime+"' AND b.endDate >= '"+startTime+"' order by endDate " ) ) ) ;	
			mysql.Close();
			
			
		} else if(type != null && Integer.parseInt(type)  == 5) { //导出出差登记表

			String Year = request.getParameter("Year") ;
			String Month = request.getParameter("Month") ;
			
			String path = request.getRealPath("/");		
			String fileDir = new String(path+"/DownloadFile/");
			
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();	
			TravelInfo travel = new TravelInfo();
			out.println(travel.ProduceExcelFile(Year, Month, fileDir)) ;

		} else if(type != null && Integer.parseInt(type)  == 6) { //查询特定日期签到信息

			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json; charset=utf-8"); 
			
			PrintWriter out = response.getWriter();
			MysqlUtils mysql  = new MysqlUtils(CommonUtil.dbHost+":"+CommonUtil.dbPort,CommonUtil.dbUser,CommonUtil.dbPassword,CommonUtil.dataBaseName) ;
			
			String DateStr = request.getParameter("DateStr") ;
			String sqlStr="select c.realname , d.userrealname , d.createdAt , d.inAddress from user c , (select b.nextApproval_id, b.resident, b.realname userrealname, a.inAddress, a.createdAt from sign a , user b where a.sign_date = '"+DateStr+"' and a.userId = b.id order by b.nextApproval_id ) d where d.nextApproval_id = c.username order by c.username" ;
			
			out.println(DBGsonUtil.RowList2JsonArry(  mysql.Query(sqlStr) ) ) ;
			
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
