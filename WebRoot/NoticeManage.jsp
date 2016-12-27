<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>NoticeManage</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="<%=basePath%>js/jquery-1.11.3.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/SHA1.js"></script> 

  </head>
  
  <body>
    <table border="0" cellspacing="0" width="60%" align="center" >
    	<tr height="20px">
    		<td width="20%" align="center"><input type="button" id="NoticeQueryBtn" name="NoticeQueryBtn" value="历史消息查询" ></td>
    		<td width="20%" align="center"><input type="button" id="NoticePushBtn" name="NoticePushBtn" value="消息发送" ></td>
    	</tr>
    </table>
    <p>
        
    <div id="NoticePushDiv" align="center">
   		<div title="点击输入主题" ><input type="text" id="NewNoticeTitle" name="NewNoticeTitle" value="" ></div><p>
   		<div title="点击输入内容" ><textarea id="NewNoticeContent"  name="NewNoticeContent" rows="10" cols="40" ></textarea></div><p>
   		<input type="button" id="SendMsgBtn" name="SendMsgBtn" value="发送" >
    </div>
    
    <div id="ResultDiv" align="center">
    </div>
     <p id="msg"></p><P> <p id="umsg"></p>
  </body>
  
  <script language=javascript>
  	$(document).ready(function(){
  		
			$("#NoticeQueryDiv").hide();
			$("#NoticePushDiv").hide();
		
		
			$("#NoticeQueryBtn").click(function(){
				$("#ResultDiv").empty();
				$("#NoticePushDiv").hide();
				
				$.ajax({
		  			"url": "<%=basePath%>/servlet/NoticeManger?type=1",
		  			"method": "POST",
					"cache": false,
					"async":false,
		  		}).success(function (data, status, header) {
		  			$("#ResultDiv").empty();
		  			//$("#msg").text("OK: "+JSON.stringify(data));
		  			if ( data.length > 0 ) {
		  				
		  				var table=$("<table border=\"1\"  width=\"90%\" >");
						table.appendTo($("#ResultDiv"));
						var tr=$("<tr style=\"font-weight:bold;\" bgcolor=\"#CCCCCC\"></tr>");
						tr.appendTo(table);
						var td=$("<td align=\"center\" >发送时间</td><td align=\"center\" >发送者</td><td align=\"center\" >通知主题</td><td align=\"center\" >通知内容</td><td align=\"center\" >接收群组</td>");
						td.appendTo(tr);
						for( var ind = 0 ; ind <data.length ; ind++ ) {
						
							tr=$("<tr bgcolor=\"#CCFFCC\"></tr>");
							tr.appendTo(table);
							td=$("<td align=\"center\" >"+data[ind].createdAt+"</td><td align=\"center\" >"+data[ind].publisher+"</td><td align=\"center\" >"+data[ind].noticeTitle+"</td><td align=\"center\" >"+data[ind].noticeContent+"</td><td align=\"center\" >"+data[ind].noticeGroup+"</td>");
							td.appendTo(tr);
						}
						$("#ResultDiv").append("</table>");
		  			}else {
						$("#ResultDiv").text("未查询到结果！！！");
					}
		  		}).fail(function (header, status, errorThrown) {
			 		$("#ResultDiv").empty();
		  			$("#ResultDiv").append(header.responseText);
		  		});
		  		
			});
		
			$("#NoticePushBtn").click(function(){
				$("#ResultDiv").empty();
				
				$("#NoticeQueryDiv").hide();
				$("#NoticePushDiv").show();
			});
		
			$("#SendMsgBtn").click(function(){
			
				if( $("#NewNoticeTitle").val().length == 0 ) {
					alert("错误：主题必填！！！")
					return ;
				}
				
				if( $("#NewNoticeContent").val().length == 0 ) {
					alert("错误：内容必填！！！")
					return ;
				}
				
				$("#ResultDiv").empty();
				$("#ResultDiv").text("正在发送，请稍后。。。");
				
				
				$.ajax({
		  			"url": "<%=basePath%>/servlet/NoticeManger?type=2",
		  			"method": "POST",
					"cache": false,
					"async":false,
					"data":{
						"NewNoticeTitle":$("#NewNoticeTitle").val(),
						"NewNoticeContent":$("#NewNoticeContent").val()
					}
		  		}).success(function (data, status, header) {
		  			
		  			if( data.result ) {
		  				$("#msg").text("写入成功！！！发送。。。") ; 
		  				var now = Date.now();
		  				var appId = "A6914993168466";
						var appKey = SHA1("A6914993168466" + "UZ" + "688E0460-3078-FA93-B9DB-FE9FDA618D63" + "UZ" + now) + "." + now;
						
						$.ajax({
							"url": "https://p.apicloud.com/api/push/message",
							"method": "POST",
							"cache": false,
							"async":false,
							"headers": {
								"X-APICloud-AppId": ""+appId,
								"X-APICloud-AppKey": ""+appKey
							},
							"dataType" : "json",
							"data":{
								"values" : {
									"title" : "新通知",
									"content" : "你有新的通知，请打开技术支持移动OA查看！(此为测试，谢谢)",
									"type" : 1, //– 消息类型，1:消息 2:通知
									"platform" : 0 //0:全部平台，1：ios, 2：android
								}
							}
						}).success(function (data, status, header) {
						//success body
							$("#ResultDiv").text("通知发送成功！！！") ; 
						}).fail(function (header, status, errorThrown) {
						//fail body
							$("#umsg").text("OK: "+JSON.stringify(header));
							$("#ResultDiv").text("ChangeUser -- 网络通讯错误") ; 
						});
						
						
						//$("#ResultDiv").text("通知发送成功！！！") ; 
		  			}
		  				  		
		  		}).fail(function (header, status, errorThrown) {
			 		$("#ResultDiv").empty();
		  			$("#ResultDiv").append(header.responseText);
		  		});
		  		
		  		
			});
		
  	});
  </script>
  
</html>
