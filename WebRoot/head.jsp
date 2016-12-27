<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%

String sRemoteAddr = request.getRemoteAddr();
if ( sRemoteAddr.indexOf("10.1.") != 0 && sRemoteAddr.indexOf("0:0:0:0:0:0:0:1") != 0 ) {
	out.write(""+sRemoteAddr);
	return  ;
}

String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>HEAD</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="<%=basePath%>css/head.css" />

  </head>
  
  <body>
    <table border="0" cellspacing="0" width="60%" align="center" >
    	<tr height="20px">
    		<td width="20%" align="center" class="td_r" ><a href="<%=basePath%>UserManage.jsp" target="main">用户管理</a></td>
    		<td width="20%" align="center" class="td_r" ><a href="<%=basePath%>SignManage.jsp" target="main">考勤信息</a></td>
    		<td width="20%" align="center" class="td_r" ><a href="<%=basePath%>NoticeManage.jsp" target="main">通知信息</a></td>
    		<td width="20%" align="center" class="td_r" ><a href="<%=basePath%>ServerInfoManage.jsp" target="main">硬件信息</a></td>
    		<td width="20%" align="center" ><a href="<%=basePath%>StatManage.jsp" target="main">统计信息</a></td>
			
    	</tr>
    </table>
  </body>
</html>
