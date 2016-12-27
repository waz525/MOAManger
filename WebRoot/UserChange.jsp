<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

String strid = request.getParameter("id");
String strusername = request.getParameter("username");
String strrealname = request.getParameter("realname");
String strmobile = request.getParameter("mobile");
String strnextApproval_id = request.getParameter("nextApproval_id");
String strresident = request.getParameter("resident");
String strisout = request.getParameter("isout");

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  	
    <base href="<%=basePath%>">
    
    <title><%=strrealname %>的用户信息</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<script type="text/javascript" src="<%=basePath%>js/jquery-1.11.3.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/SHA1.js"></script>
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
    <table align="center">
    	<tr>
    		<h2 align="center">修改用户信息</h2>
    	</tr>
    	<tr height="30px">	
			<td align="center" >&nbsp;&nbsp;工号：<input type="text" id="username" name="username" value="<%=strusername%>" /></td>
    	</tr>
    	<tr height="30px">	
			<td align="center">&nbsp;&nbsp;姓名：<input type="text" id="realname" name="realname" value="<%=strrealname%>" /></td>
    	</tr>
    	<tr height="30px">	
			<td align="center">&nbsp;手机号：<input type="text" id="mobile" name="mobile" value="<%=strmobile%>" /></td>
    	</tr>
    	<tr height="30px">	
			<td align="center">上级工号：<input type="text" id="nextApproval_id" name="nextApproval_id" value="<%=strnextApproval_id%>" /></td>
    	</tr>
    	<tr height="30px">	
			<td align="center">&nbsp;常驻地：<input type="text" id="resident" name="resident" value="<%=strresident%>" /></td>
    	</tr>
    	<tr height="30px">	
			<td align="center">
	    		常驻标识：
				<select id="isout">
					<option value="1">是</option>
					<option value="0" <%if( strisout.equalsIgnoreCase("0"))  { %>selected<%} %>>否</option>
				</select>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			</td>
    	</tr>
    	<tr height="30px">
    		<td align="center">
    			<P>
	    		<input type="button" id="Reset" name="Reset" value="重设" >
	    		<input type="button" id="ChangeUser" name="ChangeUser" value="确认修改" >
	    		<input type="button" id="delUser" name="delUser" value="删除此用户" >
	    	</td>
	    </tr>
	    <tr height="30px">
	    	<td align="center">
	    		<input type="button" id="ChangeDeviceid" name="ChangeDeviceid" value="设备ID重置" >
	    		<input type="button" id="ChangePassword" name="ChangePassword" value="登录密码重置" >
    		</td>
    	</tr>
    </table>
	<p>
	<div id="resultdiv" align="center">		
	</div>
  </body>
  
  
	<script language=javascript>
		$(document).ready(function(){
			

		 	$("#Reset").click(function(){
				$("#username").val("<%=strusername%>");
				$("#realname").val("<%=strrealname%>");
				$("#mobile").val("<%=strmobile%>");
				$("#nextApproval_id").val("<%=strnextApproval_id%>");
				$("#resident").val("<%=strresident%>");
			});
		  	
		  	
			$("#ChangeUser").click(function(){
			
				$("#resultdiv").empty();
				
				
				//判断各文本框的值				
				if( $("#username").val().length == 0 ) {
					alert("错误：工号必填！！！")
					return ;
				} 
				
				if( $("#realname").val().length == 0 ) {
					alert("错误：姓名必填！！！")
					return ;
				} 
				
				if( $("#mobile").val().length == 0 ) {
					alert("错误：手机号必填！！！")
					return ;
				}
				
				if( $("#nextApproval_id").val().length == 0 ) {
					alert("错误：上级工号必填！！！")
					return ;
				}
				
				if( $("#resident").val().length == 0 ) {
					alert("错误：上级工号必填！！！")
					return ;
				}
				
				
				if(confirm("确定修改此用户信息?") ) {
				
						$.ajax({
							"url": "<%=basePath%>/servlet/UserManager?type=3",
							"method": "POST",
							"cache": false,
							"async":false,
							"data":{
								"id":"<%=strid%>",
								"username":""+$("#username").val(),
								"realname":""+$("#realname").val(),
								"nextApproval_id":""+$("#nextApproval_id").val(),
								"mobile":""+$("#mobile").val(),
								"isout":""+$("#isout").val(),
								"resident":""+$("#resident").val()
							}
						}).success(function (data, status, header) {
						//success body	 
							if(typeof data.updatedAt ==="undefined") {
								$("#resultdiv").text("修改用户信息失败  ("+data.error.message+")") ;
							} else {
								$("#resultdiv").text("修改用户信息成功于 "+data.updatedAt) ;
							}
							
						}).fail(function (header, status, errorThrown) {
						//fail body
							$("#resultdiv").text("ChangeUser -- 网络通讯错误") ; 
						});
				}
				
				
		  	});
		  	
		  	
			$("#ChangePassword").click(function(){
				
				$("#resultdiv").empty();
				
				if(confirm("确定需要重置此用户密码?") ) {
				
						$.ajax({
							"url": "<%=basePath%>/servlet/UserManager?type=4",
							"method": "POST",
							"cache": false,
							"async":false,
							"data":{
								"id":"<%=strid%>",
								"password":"admin123"
							}
						}).success(function (data, status, header) {
						//success body	 
							if(typeof data.updatedAt ==="undefined") {
								$("#resultdiv").text("重置密码失败  ("+data.ERROR+")") ;
							} else {
								$("#resultdiv").text("重置密码成功，默认密码为 admin123") ;
							}
							
						}).fail(function (header, status, errorThrown) {
						//fail body
							$("#resultdiv").text("ChangePassword -- 网络通讯错误") ; 
						});
				}
		  	});
		  	
		  	
		  	
			$("#delUser").click(function(){
			
				$("#resultdiv").empty();
				
				if(confirm("确定需要删除此用户?") ) {
					
					
						$.ajax({
							"url": "<%=basePath%>/servlet/UserManager?type=5",
							"method": "POST",
							"cache": false,
							"async":false,
							"data": {"id":"<%=strid%>"}
						}).success(function (data, status, header) {
						//success body
							
							if(data.DELETE) {
								$("#resultdiv").text("删除成功!") ;
							} else {
								$("#resultdiv").text("删除用户失败 !!!") ;
							}
							
						}).fail(function (header, status, errorThrown) {
						//fail body
							$("#resultdiv").text("ChangePassword -- 网络通讯错误") ; 
						});
					}
				
				});
		  	
			$("#ChangeDeviceid").click(function(){
			
				$("#resultdiv").empty();
				
				if(confirm("确定需要重置设备?") ) {
					
					
						$.ajax({
							"url": "<%=basePath%>/servlet/UserManager?type=6",
							"method": "POST",
							"cache": false,
							"async":false,
							"data": {"id":"<%=strid%>"}
						}).success(function (data, status, header) {
						//success body
							
							
							if(data.ChangeDeviceid) {
								$("#resultdiv").text("设备ID重置成功!") ;
							} else {
								$("#resultdiv").text("设备ID重置失败 !!!") ;
							}
							
						}).fail(function (header, status, errorThrown) {
						//fail body
							$("#resultdiv").text("ChangePassword -- 网络通讯错误") ; 
						});
					}
				
				});
		  	
		  	
  		});
  	</script>
  	
</html>
