package com.worden.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.worden.common.CommonUtil;
import com.worden.common.GsonUtil;
import com.worden.common.UploadFile;
import com.worden.data.ServerInfoImport;
import com.worden.db.MysqlUtils;


public class FileUploader extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public FileUploader() {
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
		//doPost(request,response);
		PrintWriter out = response.getWriter();
		out.println("Please use post !");
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


		String type = request.getParameter("type") ;
		
		if(type != null && Integer.parseInt(type)  == 1 ) { //上传批量用户信息
			String path_home = request.getRequestURI().substring( 0 , request.getRequestURI().indexOf(request.getServletPath())+1 ) ;
			
			String path = request.getRealPath("/");		
			String Dir = new String(path+"/UploadFile/");
			UploadFile upload = new UploadFile() ;
			upload.setUploadDirectory(Dir) ;
			upload.uploadFile(request) ;
			String filePath=path+"/UploadFile/"+upload.getFilename() ;
			CommonUtil.PrintInfo("FileUploader: upload file 1 -- "+filePath);
			String newUrl=""+path_home+"UserUploader.jsp?filePath="+java.net.URLEncoder.encode(filePath) ;
			response.sendRedirect(newUrl);
		} else if(type != null && Integer.parseInt(type)  == 2 ) { //上传批量硬件信息

			String path = request.getRealPath("/");		
			String Dir = new String(path+"/UploadFile/");
			UploadFile upload = new UploadFile() ;
			upload.setUploadDirectory(Dir) ;
			upload.uploadFile(request) ;
			String filePath=path+"/UploadFile/"+upload.getFilename() ;
			CommonUtil.PrintInfo("FileUploader: upload file 2 -- "+filePath);
			MysqlUtils mysql  = new MysqlUtils(CommonUtil.dbHost+":"+CommonUtil.dbPort,CommonUtil.dbUser,CommonUtil.dbPassword,CommonUtil.dataBaseName) ;
			ServerInfoImport svrInfoImport = new ServerInfoImport();
			svrInfoImport.DistillExcel(filePath,mysql);
			mysql.Close();
			response.setContentType("application/json; charset=utf-8");
			PrintWriter out = response.getWriter();
			out.print( GsonUtil.Object2Json(svrInfoImport) ) ;
			
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
