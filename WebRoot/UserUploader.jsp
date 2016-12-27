<%@page import="com.worden.common.CommonUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.worden.common.ExcelUtil" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

String filePath = request.getParameter("filePath");
filePath = java.net.URLDecoder.decode(filePath) ;
ExcelUtil eu = new ExcelUtil() ;
String content = eu.DistillExcelForHtml(filePath) ;

String MCMUrl=CommonUtil.GetPropertiesValue("config.properties", "MCMUrl");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>ShowExcelContent</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="<%=basePath%>js/jquery-1.11.3.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/SHA1.js"></script>
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
  	<div  align="center">
		<%=content %>
	    <p>
	    <p id="msg"></p> <p id="umsg"></p>
	    <input id="backIndex" type="button" name="backIndex" value="返回上一页" >&nbsp;&nbsp;
	    <input id="upload" type="button" name="upload" value="导入用户" >
    
    </div>
  </body>
  
  <script type="text/javascript">
  $(document).ready(function(){
  	//设置第1行第6列的内容
  	$("#tableinfo tr:eq(0) td:eq(6)").text("导入状态") ;
  	//奇偶行不同颜色
  	$("#tableinfo tbody tr:odd").css("background-color", "#CCFFCC");
    $("#tableinfo tbody tr:even").css("background-color", "#CCCCFF");
    $("#tableinfo tr:eq(0)").css("background-color", "#CCCCCC");
    
    
  
  	var token = "" ;
  	$.ajax({
		"url": "<%=MCMUrl%>api/user/login",
		"method": "POST",
		"cache": false,
		"async":false,
		"data":{
		  	"username":"fhadmin" ,
		  	"password":"admin123"
		}
	}).success(function (data, status, header) {
		token=data.id ; 
	})
  	
  	$("#backIndex").click(function(){
  		location.href = "<%=basePath%>UserManage.jsp" ;
  	});
  	
  	
  	
   	$("#upload").click(function(){
  		var NumOk = 0 ;
  		var NumError = 0 ;
		var tStr = "";
		var lineLen = $("#tableinfo tr:gt(0)").length;
		for(lid=0  ; lid <lineLen ; lid++  ) {
				
			tStr = "" ;
			tStr += "\"username\" : \""+$("#tableinfo tr:gt(0):eq("+lid+") td:eq(0)").text()+"\" , ";
			tStr += "\"realname\" : \""+$("#tableinfo tr:gt(0):eq("+lid+") td:eq(1)").text()+"\" , ";
			tStr += "\"password\" : \""+$("#tableinfo tr:gt(0):eq("+lid+") td:eq(2)").text()+"\" , ";
			tStr += "\"nextApproval_id\" : \""+$("#tableinfo tr:gt(0):eq("+lid+") td:eq(3)").text()+"\" , ";
			tStr += "\"mobile\" : \""+$("#tableinfo tr:gt(0):eq("+lid+") td:eq(4)").text()+"\"";
			$("#tableinfo tr:gt(0):eq("+lid+") td:eq(6)").text("正在导入") ;
			
			$.ajax({
				"url": "<%=MCMUrl%>api/user",
				"method": "POST",
				"cache": false,
				"async":false,
				"headers": {
					"authorization":token
				},
				"data":{
					"username":""+$("#tableinfo tr:gt(0):eq("+lid+") td:eq(0)").text(),
					"realname":""+$("#tableinfo tr:gt(0):eq("+lid+") td:eq(1)").text(),
					"password":""+$("#tableinfo tr:gt(0):eq("+lid+") td:eq(2)").text(),
					"nextApproval_id":""+$("#tableinfo tr:gt(0):eq("+lid+") td:eq(3)").text(),
					"mobile":""+$("#tableinfo tr:gt(0):eq("+lid+") td:eq(4)").text(),
					"resident":""+$("#tableinfo tr:gt(0):eq("+lid+") td:eq(5)").text()
				}
			}).success(function (data, status, header) {
			//success body	 
				//$("#umsg").text("OK: "+JSON.stringify(data));
				if(typeof data.createdAt ==="undefined") {
					$("#tableinfo tr:gt(0):eq("+lid+") td:eq(6)").text("失败  ("+data.ERROR+")") ;
					NumError++;
				} else {
					$("#tableinfo tr:gt(0):eq("+lid+") td:eq(6)").text("成功于 "+data.createdAt) ;
					NumOk++;
				}
				
			}).fail(function (header, status, errorThrown) {
			//fail body
				$("#tableinfo tr:gt(0):eq("+lid+") td:eq(6)").text("网络通讯错误") ; 
				NumError++;
			})
			
			
		}
		
  		
  		$("#msg").text("导入成功 "+NumOk+" 条 ，失败 "+NumError+"条 , 共 "+lineLen+" 条 ! " );
  		
  	});
  
  
  
  });
  </script>
  
  
</html>
