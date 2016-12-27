<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String MCMServer = "http://10.10.10.10:8080/mcm/";

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  	
    <base href="<%=basePath%>">
    
    <title>UserManage</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<script type="text/javascript" src="<%=basePath%>js/jquery-1.11.3.js"></script>
  </head>
  
  <body>
    <table border="0" cellspacing="0" width="60%" align="center" >
    	<tr height="20px">
    		<td width="20%" align="center"><input type="button" id="QueryUserBtn" name="QueryUserBtn" value="用户查询" ></td>
    		<td width="20%" align="center"><input type="button" id="AddUserBtn" name="AddUserBtn" value="添加用户" ></td>
    		<td width="20%" align="center"><input type="button" id="UploadUserBtn" name="UploadUserBtn" value="批量上传" ></td>
    	</tr>
    </table>
    <p>
	<div id="infodiv">
		<div id="queryinfodiv"  align="center" >
			&nbsp;&nbsp;工号：<input type="text" id="username" name="username" />
			&nbsp;姓名：<input type="text" id="realname" name="realname" />
			手机号：<input type="text" id="mobile" name="mobile" />
			<p>
			上级工号：<input type="text" id="nextApproval_id" name="nextApproval_id" />
			常驻地：<input type="text" id="resident" name="resident" />
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<p>
			<input type="button" id="QueryUser" name="QueryUser" value="查询" >
		</div>
		<div id="addinfodiv"  align="center" >
			&nbsp;&nbsp;工号：<input type="text" id="add_username" name="add_username" />
			&nbsp;姓名：<input type="text" id="add_realname" name="add_realname" />
			手机号：<input type="text" id="add_mobile" name="add_mobile" />
			<p>
			上级工号：<input type="text" id="add_nextApproval_id" name="add_nextApproval_id" />
			常驻地：<input type="text" id="add_resident" name="add_resident" value="江苏省南京市" />
			常驻标识：
			<select id="isout">
				<option value="1" >是</option>
				<option value="0" >否</option>
			</select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<p>
			<input type="button" id="AddUser" name="AddUser" value="增加用户" >
				
		</div>
	  	<div id="uploadinfodiv">
		 	<form id="uploadform" method="post" enctype="multipart/form-data" method="post" >
				<table width="60%" border="0" align="center">
					<tr>
						<td align="center" >批量上传：&nbsp;<input type="file" name="filepath" id="filepath" value="" /><a href="OtherFiles/user.xlsx"><input type="button" id="TempleFile" name="TempleFile" value="下载模板" ></a></td>
					</tr>
					<tr align="center">
						<td align="center" >
							<input id="btn1" type=reset value="重设" >&nbsp;&nbsp;&nbsp;&nbsp;
							<input id="btn2" type="button" name="Submit" value="上传文件" onclick="OnSubmit();">						
						</td>
					</tr>
				</table>
			</form>
		</div>
	</div>
	<p>
	<div id="ResultDiv" align="center">		
	</div>
	 <p id="msg"></p><P> <p id="umsg"></p>
	<script language=javascript>
		$(document).ready(function(){
			
			$("#queryinfodiv").hide();
			$("#addinfodiv").hide();
			$("#uploadinfodiv").hide();
			
			
			$("#QueryUserBtn").click(function(){
				$("#username").val("");
				$("#realname").val("");
				$("#mobile").val("");
				
				$("#ResultDiv").empty();
				
				$("#queryinfodiv").show();
				$("#addinfodiv").hide();
				$("#uploadinfodiv").hide();
			});
		
			$("#AddUserBtn").click(function(){
				$("#add_username").val("");
				$("#add_realname").val("");
				$("#add_mobile").val("");
				$("#add_nextApproval_id").val("");
			
				$("#ResultDiv").empty();
				
				$("#queryinfodiv").hide();
				$("#addinfodiv").show();
				$("#uploadinfodiv").hide();
			});
		
			$("#UploadUserBtn").click(function(){
				$("#ResultDiv").empty();
				
				$("#queryinfodiv").hide();
				$("#addinfodiv").hide();
				$("#uploadinfodiv").show();
			});
			
			
			$("#QueryUser").click(function(){
		  		var dataJson = {
			    			"realname":  "" , 
			    			"username": "" ,
			    			"mobile": "" ,
			    			"nextApproval_id": "",
			    			"resident": "" 
			    			
				}
				
				
				if( $("#username").val().length == 0 ) {
					delete dataJson.username ;
				} else {
					dataJson.username = $("#username").val().toUpperCase() ;
				}
				
				if( $("#realname").val().length == 0 ) {
					delete dataJson.realname ;
				} else {
					dataJson.realname = $("#realname").val() ;
				}
				
				if( $("#mobile").val().length == 0 ) {
					delete dataJson.mobile ;
				} else {
					dataJson.mobile = $("#mobile").val() ;
				}
				
				if( $("#nextApproval_id").val().length == 0 ) {
					delete dataJson.nextApproval_id ;
				} else {
					dataJson.nextApproval_id = $("#nextApproval_id").val() ;
				}
				
				if( $("#resident").val().length == 0 ) {
					delete dataJson.resident ;
				} else {
					dataJson.resident = $("#resident").val() ;
				}
				
				$("#ResultDiv").empty();
				
				$.ajax({
					"url": "<%=basePath%>/servlet/UserManager?type=1",
					"method": "POST",
					"cache": false,
					"async":false,
					"data":dataJson
				}).success(function (data, status, header) {
				//success body
				
					if ( data.length > 0 ) {
						var table=$("<table border=\"1\"  width=\"60%\" >");
						table.appendTo($("#ResultDiv"));
						var tr=$("<tr style=\"font-weight:bold;\" bgcolor=\"#CCCCCC\"></tr>");
						tr.appendTo(table);
						var td=$("<td align=\"center\" >工号</td><td align=\"center\" >姓名</td><td align=\"center\" >上级工号</td><td align=\"center\" >手机号</td><td align=\"center\" >常驻地</td><td align=\"center\" >是否常驻</td>");
						td.appendTo(tr);
						for( var ind = 0 ; ind <data.length ; ind++ ) {
							tr=$("<tr bgcolor=\"#CCFFCC\"></tr>");
							tr.appendTo(table);
							//td=$("<td align=\"center\" ><a href=\"UserChange.jsp?uid="+data[ind].id+"\">"+data[ind].id+"</a></td><td align=\"center\" >"+data[ind].username+"</td><td align=\"center\" >"+data[ind].realname+"</td><td align=\"center\" >"+data[ind].nextApproval_id+"</td><td align=\"center\" >"+data[ind].mobile+"</td>");
							td=$("<td align=\"center\" ><a href=\"UserChange.jsp?id="+data[ind].id+"&username="+data[ind].username+"&realname="+data[ind].realname+"&resident="+data[ind].resident+"&isout="+data[ind].isout+"&nextApproval_id="+data[ind].nextApproval_id+"&mobile="+data[ind].mobile+"\" title=\"点击修改用户信息\" target=\"_blank\" >"+data[ind].username+"</td><td align=\"center\" >"+data[ind].realname+"</td><td align=\"center\" >"+data[ind].nextApproval_id+"</td><td align=\"center\" >"+data[ind].mobile+"</td><td align=\"center\" >"+data[ind].resident+"</td><td align=\"center\" >"+(data[ind].isout==1 ? "是": "<font color=\"red\">否</font>" )+"</td>");
							td.appendTo(tr);
							
						}
						tr=$("<tr bgcolor=\"#CCCCFF\"></tr>");
						tr.appendTo(table);
						td=$("<td colspan=\"6\" align=\"center\" >共查询出"+data.length+"条结果</td>");
						td.appendTo(tr);
						//trend.appendTo(table);
						$("#ResultDiv").append("</table>");
					} else {
						$("#ResultDiv").text("未查询到结果！！！");
					}
				}).fail(function (header, status, errorThrown) {
				//fail body
					
					$("#ResultDiv").append(header.responseText) ;
				});				
					
			});
			
			
			
		
			$("#AddUser").click(function(){
				$("#ResultDiv").empty();
				
				//判断各文本框的值				
				if( $("#add_username").val().length == 0 ) {
					alert("错误：工号必填！！！")
					return ;
				} 
				
				if( $("#add_realname").val().length == 0 ) {
					alert("错误：姓名必填！！！")
					return ;
				} 
				
				if( $("#add_mobile").val().length == 0 ) {
					alert("错误：手机号必填！！！")
					return ;
				}
				
				if( $("#add_nextApproval_id").val().length == 0 ) {
					alert("错误：上级工号必填！！！")
					return ;
				}
				
				if( $("#add_resident").val().length == 0 ) {
					alert("错误：常驻地必填！！！")
					return ;
				}
				
								
				$.ajax({
					"url": "<%=basePath%>/servlet/UserManager?type=2",
					"method": "POST",
					"cache": false,
					"async":false,
					"data":{
						"username":""+$("#add_username").val(),
						"realname":""+$("#add_realname").val(),
						"password":"admin123",
						"nextApproval_id":""+$("#add_nextApproval_id").val(),
						"resident":""+$("#add_resident").val(),
						"mobile":""+$("#add_mobile").val(),
						"isout":""+$("#isout").val()
					}
				}).success(function (data, status, header) {
				//success body	 
					//$("#msg").text("OK: "+JSON.stringify(data));
					//$("#umsg").text("uOK: "+typeof data.ERROR );
					if(typeof data.ERROR === "string") {
						$("#ResultDiv").text("失败  ("+data.ERROR+")") ;
					} else {
						var table=$("<table border=\"1\"  width=\"60%\" >");
						table.appendTo($("#ResultDiv"));
						var tr=$("<tr style=\"font-weight:bold;\" bgcolor=\"#CCCCCC\"></tr>");
						tr.appendTo(table);
						var td=$("<td align=\"center\" >工号</td><td align=\"center\" >姓名</td><td align=\"center\" >上级工号</td><td align=\"center\" >手机号</td><td align=\"center\" >常驻地</td><td align=\"center\" >是否常驻</td>");
						td.appendTo(tr);
						tr=$("<tr bgcolor=\"#CCCCFF\"></tr>");
						tr.appendTo(table);
						td=$("<td align=\"center\" ><a href=\"UserChange.jsp?id="+data.id+"&username="+data.username+"&realname="+data.realname+"&resident="+data.resident+"&isout="+data[ind].isout+"&nextApproval_id="+data.nextApproval_id+"&mobile="+data.mobile+"\" title=\"点击修改用户信息\" target=\"_blank\" >"+data.username+"</td><td align=\"center\" >"+data.realname+"</td><td align=\"center\" >"+data.nextApproval_id+"</td><td align=\"center\" >"+data.mobile+"</td><td align=\"center\" >"+data.resident+"</td><td align=\"center\" >"+(data.isout==1 ? "是": "<font color=\"red\">否</font>" )+"</td>");
						td.appendTo(tr);
						$("#ResultDiv").append("</table>");
					}
					
				}).fail(function (header, status, errorThrown) {
				//fail body
					
					$("#ResultDiv").text("网络通讯错误!!!") ; 
				})
				
			});
		
		});
	
	
	
	
	
		function OnSubmit()
		{
			uploadform.action="<%=basePath%>/servlet/FileUploader?type=1";
			uploadform.submit();
		}	
	</script>
  </body>
</html>