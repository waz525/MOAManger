<%@page import="com.worden.common.CommonUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.io.File" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.ParseException" %>
<%@ page import="com.worden.db.*" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
Calendar cal = Calendar.getInstance();
Date myDate= new Date();
//cal.set(Calendar.YEAR,myDate.getYear()) ;
cal.set(Calendar.MONTH, myDate.getMonth());
cal.set(Calendar.DAY_OF_MONTH, 1);
String firstDay = (new SimpleDateFormat("yyyy-M-d")).format(cal.getTime()) ;
cal.add(Calendar.MONTH, 1);
cal.add(Calendar.DAY_OF_MONTH, -1);
String lastDay = (new SimpleDateFormat("yyyy-M-d")).format(cal.getTime()) ;

String[] oldSignFileList =  CommonUtil.GetFiles(application.getRealPath("/")+"/DownloadFile/", "外勤补助 ");

String[] oldTravelFileList =  CommonUtil.GetFiles(application.getRealPath("/")+"/DownloadFile/", "驻地人员出差表格 ");

MysqlUtils mysql  = new MysqlUtils(CommonUtil.dbHost+":"+CommonUtil.dbPort,CommonUtil.dbUser,CommonUtil.dbPassword,CommonUtil.dataBaseName) ;
//查询用户列表
Row[] users = mysql.Query("user", "id,username,realname" , "username != 'fhadmin' and username not like 'C%' order by CONVERT( realname USING gbk) ") ;

mysql.Close();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  	
    <base href="<%=basePath%>">
    
    <title>SignManage</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0"> 
	<script type="text/javascript" src="<%=basePath%>js/jquery-1.11.3.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/SHA1.js"></script>  
	<script type="text/javascript" src="<%=basePath%>js/calendar.js"></script>  
	
  </head>
  
  <body>
    <table border="0" cellspacing="0" width="60%" align="center" >
    	<tr height="20px">
    		<td width="20%" align="center"><input type="button" id="SignStatBtn" name="SignStatBtn" value="按日查询签到" ></td>
    		<td width="20%" align="center"><input type="button" id="SignStatisticsBtn" name="SignStatisticsBtn" value="签到查询" ></td>
    		<td width="20%" align="center"><input type="button" id="OffWorkBtn" name="OffWorkBtn" value="休假申请查询" ></td>
    		<td width="20%" align="center"><input type="button" id="SignFileBtn" name="SignFileBtn" value="考勤表" ></td>
    		<td width="20%" align="center"><input type="button" id="TravelBtn" name="TravelBtn" value="出差登记" ></td>
    	</tr>
    </table>
    <p>
    
	<div id="SignStatDiv"  align="center">
  		<select id="SignStat_year">
  			
  		<%for( int i = 2016 ; i < 2025 ; i++ ) { %>
  			<option value="<%=i%>" <% if( i==myDate.getYear()+1900) { %>selected<%} %>  ><%=i %></option>
  		<%}%>
  		</select>年
  		<select id="SignStat_month">
  			
  		<%for( int i = 1 ; i < 13 ; i++ ) { String j = "" ; if( i < 10) j = "0"+i ; else j = ""+i ; %>
  			<option value="<%=j%>" <% if( i==myDate.getMonth()+1) { %>selected<%} %>  ><%=j %></option>
  		<%}%>
 		</select>月
  		<select id="SignStat_day">
  			
  		<%for( int i = 1 ; i < 31 ; i++ ) { String j = "" ; if( i < 10) j = "0"+i ; else j = ""+i ; %>
  			<option value="<%=j%>" <% if( i==myDate.getDate()) { %>selected<%} %>  ><%=j %></option>
  		<%}%>
 		</select>日&nbsp;&nbsp;
 		<input type="button" id="SignStat" name="SignStat" value="查询" >
    </div>
    
	<div id="SignStaticsDiv" align="center">
		起始时间：<input type="text" id="StartTimeInput" onClick="return Calendar('StartTimeInput');" class="text_time" value="<%=firstDay %>" style="width:80px;border:1px solid;"/>
		&nbsp;&nbsp;
		结束时间：<input type="text" id="EndTimeInput"  onClick="return Calendar('EndTimeInput');" class="text_time" value="<%=lastDay %>" style="width:80px;border:1px solid;"/>
		<P>
		<% if( users.length > 0 )  {%>
			选择人员：
			<select id="SignStat_User">
			<% for( Row user:users) { 
			Cell[] cellList = user.getCellList();
			String id = cellList[0].getValue().toString() ;
			String userinfo =  cellList[1].getValue().toString() +" - "+ cellList[2].getValue().toString() ;
			%>
			<option value="<%=id%>"  ><%=userinfo%></option>
			<% } %>
			</select>
			<input type="button" id="SignQuery" name="SignQuery" value="查询" >
		<% } else {%>
			未查询到用户信息，请联系管理员！！！
		<% } %>
	</div>
    <div id="OffWorkDiv"  align="center">
  		<select id="OffWork_year">
  			
  		<%for( int i = 2016 ; i < 2025 ; i++ ) { %>
  			<option value="<%=i%>" <% if( i==myDate.getYear()+1900) { %>selected<%} %>  ><%=i %></option>
  		<%}%>
  		</select>年
  		<select id="OffWork_month">
  			
  		<%for( int i = 1 ; i < 13 ; i++ ) { String j = "" ; if( i < 10) j = "0"+i ; else j = ""+i ; %>
  			<option value="<%=j%>" <% if( i==myDate.getMonth()) { %>selected<%} %>  ><%=j %></option>
  		<%}%>
  		</select>月&nbsp;人员:
  		<select id="OffWork_User">
  			<option value="ALL"  >全部</option>
  			<% if( users.length > 0 )  { for( Row user:users) { 
			Cell[] cellList = user.getCellList();
			String id = cellList[0].getValue().toString() ;
			String userinfo =  cellList[1].getValue().toString() +" - "+ cellList[2].getValue().toString() ;
			%>
			<option value="<%=id%>"  ><%=userinfo%></option>
			<% } }%>
  		</select>&nbsp;
  		<input type="button" id="OffWork" name="OffWork" value="查询休假申请" >
  		
    	
    </div>
  	<div id="SignFileDiv" align="center">
  		<select id="SignFileDiv_year">
  			
  		<%for( int i = 2016 ; i < 2025 ; i++ ) { %>
  			<option value="<%=i%>" <% if( i==myDate.getYear()+1900) { %>selected<%} %>  ><%=i %></option>
  		<%}%>
  		</select>年&nbsp;
  		<select id="SignFileDiv_month">
  			
  		<%for( int i = 1 ; i < 13 ; i++ ) { String j = "" ; if( i < 10) j = "0"+i ; else j = ""+i ; %>
  			<option value="<%=j%>" <% if( i==myDate.getMonth()) { %>selected<%} %>  ><%=j %></option>
  		<%}%>
  		</select>月&nbsp;
		<input type="button" id="DownloadSignExcel" name="DownloadSignExcel" value="生成考勤表" >
		<input type="button" id="OldSignFileBtn" name="OldSignFileBtn" value="历史考勤表" >
  	</div>
  	
  	<div id="TravelDiv" align="center">
  		<select id="TravelDiv_year">
  			
  		<%for( int i = 2016 ; i < 2025 ; i++ ) { %>
  			<option value="<%=i%>" <% if( i==myDate.getYear()+1900) { %>selected<%} %>  ><%=i %></option>
  		<%}%>
  		</select>年&nbsp;
  		<select id="TravelDiv_month">
  			
  		<%for( int i = 1 ; i < 13 ; i++ ) { String j = "" ; if( i < 10) j = "0"+i ; else j = ""+i ; %>
  			<option value="<%=j%>" <% if( i==myDate.getMonth()) { %>selected<%} %>  ><%=j %></option>
  		<%}%>
  		</select>月&nbsp;
		<input type="button" id="QueryTravelBtn" name="QueryTravelBtn" value="查询出差记录" >
		<input type="button" id="ProduceTravelBtn" name="ProduceTravelBtn" value="导出出差表" >
		<input type="button" id="OldTravelBtn" name="OldTravelBtn" value="历史 出差表" >
  	</div>
  	<p>
    <div id="OldSignFileDiv" align="center">
    <% if (oldSignFileList!=null )  { for( int i =oldSignFileList.length-1 ; i>=0 ; i-- ) { String f = oldSignFileList[i]; %>
    	<li><a href="<%=basePath%>/DownloadFile/<%=f%>" ><%=f%></a><p>
    		
    <% } } else {%>
    	没有找到历史文件
    <% } %>
    </div>
    <div id="OldTravelFileDiv" align="center">
    <% if (oldTravelFileList!=null )  { for( int i =oldTravelFileList.length-1 ; i>=0 ; i-- ) { String f = oldTravelFileList[i]; %>
    	<li><a href="<%=basePath%>/DownloadFile/<%=f%>" ><%=f%></a><p>
    		
    <% } } else {%>
    	没有找到历史文件
    <% } %>
    </div>
    <div id="ResultDiv" align="center">
    </div>
	<div align="center" >
    	<p id="msg"></p><P><p id="umsg"></p>
    </div>
  </body>
  
	
	<script language=javascript>
		$(document).ready(function(){
				
			
			$("#SignFileDiv").hide();
			$("#SignStatDiv").hide();
			$("#SignStaticsDiv").hide();
			$("#OldSignFileDiv").hide();
			$("#OldTravelFileDiv").hide();
			$("#OffWorkDiv").hide();
			$("#TravelDiv").hide();
			
			
			$("#SignStatBtn").click(function(){
				
				$("#ResultDiv").empty();
				
				$("#OffWorkDiv").hide();
				$("#SignStatDiv").show();
				$("#SignFileDiv").hide();
				$("#OldSignFileDiv").hide();
				$("#OldTravelFileDiv").hide();
				$("#TravelDiv").hide();
				$("#SignStaticsDiv").hide();
				
			});
			$("#SignStatisticsBtn").click(function(){
				$("#UsernameInput").val("");
				$("#RealnameInput").val("");
				$("#ResultDiv").empty();
				
				$("#OffWorkDiv").hide();
				$("#SignStatDiv").hide();
				$("#SignFileDiv").hide();
				$("#OldSignFileDiv").hide();
				$("#OldTravelFileDiv").hide();
				$("#TravelDiv").hide();
				$("#SignStaticsDiv").show();
			});
			
			$("#SignFileBtn").click(function(){
			 	$("#ResultDiv").empty();
				
				$("#OffWorkDiv").hide();
				$("#SignStatDiv").hide();
				$("#OldSignFileDiv").hide();
				$("#OldTravelFileDiv").hide();
				$("#SignStaticsDiv").hide();
				$("#TravelDiv").hide();
				$("#SignFileDiv").show();
			});
			 
			 
			
			$("#OffWorkBtn").click(function(){
			 	$("#ResultDiv").empty();
				
				$("#SignFileDiv").hide();
				$("#SignStatDiv").hide();
				$("#OldSignFileDiv").hide();
				$("#OldTravelFileDiv").hide();
				$("#SignStaticsDiv").hide();
				$("#TravelDiv").hide();
				$("#OffWorkDiv").show();
			});
			 
			$("#TravelBtn").click(function(){
			 	$("#ResultDiv").empty();
				
				$("#OffWorkDiv").hide();
				$("#SignStatDiv").hide();
				$("#SignFileDiv").hide();
				$("#OldSignFileDiv").hide();
				$("#OldTravelFileDiv").hide();
				$("#SignStaticsDiv").hide();
				$("#TravelDiv").show();
			});
			 
			 
			$("#OldSignFileBtn").click(function(){
			 	$("#ResultDiv").empty();
				$("#OldSignFileDiv").show();
			});
			 
			$("#OldTravelBtn").click(function(){
			 	$("#ResultDiv").empty();
				$("#OldTravelFileDiv").show();
			});
			
			
			
			//按日查询签到
			$("#SignStat").click(function(){
				
				$("#ResultDiv").empty();
				var DateStr = $("#SignStat_year").val() + "-" + $("#SignStat_month").val() + "-" + $("#SignStat_day").val() ;
				
				$.ajax({
		  			"url": "<%=basePath%>/servlet/SignManager?type=6",
		  			"method": "POST",
					"cache": false,
					"async":false,
		  			"data": {
						"DateStr": DateStr
					}
		  		}).success(function (data, status, header) {
		  			//$("#msg").text("OK: "+JSON.stringify(data));
		  			if ( data.length > 0 ) {
						var table=$("<table border=\"1\"  width=\"80%\" >");
						table.appendTo($("#ResultDiv"));
						var tr=$("<tr style=\"font-weight:bold;\" bgcolor=\"#CCCCCC\"></tr>");
						tr.appendTo(table);
						var td=$("<td align=\"center\" >上级姓名</td><td align=\"center\" >姓名</td><td align=\"center\" >签到时间</td><td align=\"center\" >签到地址</td>");
						td.appendTo(tr);
						for( var ind = 0 ; ind <data.length ; ind++ ) {
							tr=$("<tr bgcolor=\"#CCFFCC\"></tr>");
							tr.appendTo(table);
							td=$("<td align=\"center\" >"+data[ind].realname+"</td><td align=\"center\" >"+data[ind].userrealname+"</td><td align=\"center\" >"+data[ind].createdAt+"</td><td align=\"left\" >"+data[ind].inAddress+"</td>");
							td.appendTo(tr);
							
						}
						tr=$("<tr bgcolor=\"#CCCCFF\"></tr>");
						tr.appendTo(table);
						td=$("<td colspan=\"4\" align=\"center\" >共查询出"+data.length+"条结果</td>");
						td.appendTo(tr);
						//trend.appendTo(table);
						$("#ResultDiv").append("</table>");
		  			} else {
						$("#ResultDiv").text("未查询到结果！！！");
					}
		  		});
				
			});
			
			
			 
			$("#SignQuery").click(function(){
				$("#ResultDiv").empty();
		  		$.ajax({
		  			"url": "<%=basePath%>/servlet/SignManager?type=3",
		  			"method": "POST",
					"cache": false,
					"async":false,
		  			"data": {
						"StartTime": $("#StartTimeInput").val(),
						"EndTime": $("#EndTimeInput").val(),
						"UserID": $("#SignStat_User").val()
					}
		  		}).success(function (data, status, header) {
		  			$("#ResultDiv").empty();
		  			//$("#msg").text("OK: "+JSON.stringify(data));
		  			if ( data.length > 0 ) {
		  				
		  				var table=$("<table border=\"1\"  width=\"90%\" >");
						table.appendTo($("#ResultDiv"));
						var tr=$("<tr style=\"font-weight:bold;\" bgcolor=\"#CCCCCC\"></tr>");
						tr.appendTo(table);
						var td=$("<td align=\"center\" >签到时间</td><td align=\"center\" >签退时间</td><td align=\"center\" >签到地址</td><td align=\"center\" >签退地址</td><td align=\"center\" >加班状态</td><td align=\"center\" ><div title=\"状态包括：正常、异常、异常申请\">签到状态</div></td>");
						td.appendTo(tr);
						for( var ind = 0 ; ind <data.length ; ind++ ) {
						
							var status = "正常";
							if( data[ind].status==0 ) {
								tr=$("<tr bgcolor=\"#CCFFCC\"></tr>");
							}
							else {
								tr=$("<tr bgcolor=\"#FFCCCC\"></tr>");
								
								if( data[ind].status==1 ) {
									status = "异常";
								} else {
									status = "异常申请";
								}
							}
							tr.appendTo(table);
							td=$("<td align=\"center\" >"+data[ind].createdAt+"</td><td align=\"center\" >"+(data[ind].out_time == null ? "NULL" : data[ind].out_time)+"</td><td align=\"center\" >"+data[ind].inAddress+"</td><td align=\"center\" >"+(data[ind].outAddress == null ? "NULL" : data[ind].outAddress)+"</td><td align=\"center\" >"+data[ind].overtime+"</td><td align=\"center\" >"+status+"</td>");
							td.appendTo(tr);
						}
						tr=$("<tr bgcolor=\"#CCCCFF\"></tr>");
						tr.appendTo(table);
						td=$("<td colspan=\"6\" align=\"center\" >共查询出"+data.length+"条结果</td>");
						td.appendTo(tr);
						$("#ResultDiv").append("</table>");
		  			}else {
						$("#ResultDiv").text("未查询到结果！！！");
					}
		  		}).fail(function (header, status, errorThrown) {
			 		$("#ResultDiv").empty();
		  			$("#ResultDiv").append(header.responseText);
		  		});
		  		
			});
			 
			$("#OffWork").click(function(){
			 	$("#ResultDiv").empty();
			 	
		  		$.ajax({
		  			"url": "<%=basePath%>/servlet/SignManager?type=2",
		  			"method": "POST",
					"cache": false,
					"async":false,
		  			"data": {
						"Year": $("#OffWork_year").val(),
						"Month": $("#OffWork_month").val(),
						"UserID": $("#OffWork_User").val()
					}
		  		}).success(function (data, status, header) {
		  			$("#ResultDiv").empty();
		  			//$("#msg").text("OK: "+JSON.stringify(data));
		  			if ( data.length > 0 ) {
		  				var table=$("<table border=\"1\"  width=\"60%\" >");
						table.appendTo($("#ResultDiv"));
						var tr=$("<tr style=\"font-weight:bold;\" bgcolor=\"#CCCCCC\"></tr>");
						tr.appendTo(table);
						var td=$("<td align=\"center\" >姓名</td><td align=\"center\" >开始日期</td><td align=\"center\" >结束日期</td><td align=\"center\" >休假理由</td><td align=\"center\" >状态</td>");
						td.appendTo(tr);
						for( var ind = 0 ; ind <data.length ; ind++ ) {
							
							if( data[ind].status==0 ) {
								tr=$("<tr bgcolor=\"#FFCCCC\"></tr>");
							} else {
								tr=$("<tr bgcolor=\"#CCFFCC\"></tr>");
							}
							tr.appendTo(table);
							td=$("<td align=\"center\" >"+data[ind].applicantName+"</td><td align=\"center\" >"+data[ind].startDate+"</td><td align=\"center\" >"+data[ind].endDate+"</td><td align=\"center\" >"+data[ind].reason+"</td><td align=\"center\" >"+(data[ind].status==0?"待审批":"审批通过")+"</td>");
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
			 
			$("#DownloadSignExcel").click(function(){
			
				$("#OldSignFileDiv").hide();
			 	$("#ResultDiv").empty();
			 	
		  		$.ajax({
		  			"url": "<%=basePath%>/servlet/SignManager?type=1",
		  			"method": "POST",
		  			"data": {
						"Year": $("#SignFileDiv_year").val(),
						"Month": $("#SignFileDiv_month").val()
					}
		  		}).success(function (data, status, header) {
		  			$("#ResultDiv").empty();
		  			$("#ResultDiv").text("正在下载文件"+data);
		  			//window.open("<%=basePath%>/DownloadFile/"+data);
		  			window.location.href = "<%=basePath%>/DownloadFile/"+data;
		  			$("#ResultDiv").text("《"+data+"》下载完成 !!!");
		  		
		  		}).fail(function (header, status, errorThrown) {
			 		$("#ResultDiv").empty();
		  			$("#ResultDiv").append(header.responseText);
		  		});
		  		
		  		$("#ResultDiv").text("正在后台计算，请等待。。。");
	  		
			});
			
			$("#QueryTravelBtn").click(function(){
			 	$("#ResultDiv").empty();
			 	
		  		$.ajax({
		  			"url": "<%=basePath%>/servlet/SignManager?type=4",
		  			"method": "POST",
					"cache": false,
					"async":false,
		  			"data": {
						"Year": $("#TravelDiv_year").val(),
						"Month": $("#TravelDiv_month").val()
					}
		  		}).success(function (data, status, header) {
		  			$("#ResultDiv").empty();
		  			//$("#msg").text("OK: "+JSON.stringify(data));
		  			if ( data.length > 0 ) {
		  				var table=$("<table border=\"1\"  width=\"60%\" >");
						table.appendTo($("#ResultDiv"));
						var tr=$("<tr style=\"font-weight:bold;\" bgcolor=\"#CCCCCC\"></tr>");
						tr.appendTo(table);
						var td=$("<td align=\"center\" >工号</td><td align=\"center\" >姓名</td><td align=\"center\" >常驻地</td><td align=\"center\" >出差地</td><td align=\"center\" >开始日期</td><td align=\"center\" >结束日期</td><td align=\"center\" >出差事由</td>");
						td.appendTo(tr);
						for( var ind = 0 ; ind <data.length ; ind++ ) {
							
							if( data[ind].status==0 ) {
								tr=$("<tr bgcolor=\"#FFCCCC\"></tr>");
							} else {
								tr=$("<tr bgcolor=\"#CCFFCC\"></tr>");
							}
							tr.appendTo(table);
							td=$("<td align=\"center\" >"+data[ind].username+"</td><td align=\"center\" >"+data[ind].realname+"</td><td align=\"center\" >"+data[ind].resident+"</td><td align=\"center\" >"+data[ind].destination+"</td><td align=\"center\" >"+data[ind].startDate+"</td><td align=\"center\" >"+data[ind].endDate+"</td><td align=\"center\" >"+data[ind].reason+"</td>");
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
			
			
			$("#ProduceTravelBtn").click(function(){
			
				$("#OldTravelFileDiv").hide();
			 	$("#ResultDiv").empty();
			 	
		  		$.ajax({
		  			"url": "<%=basePath%>/servlet/SignManager?type=5",
		  			"method": "POST",
		  			"data": {
						"Year": $("#TravelDiv_year").val(),
						"Month": $("#TravelDiv_month").val()
					}
		  		}).success(function (data, status, header) {
		  			$("#ResultDiv").empty();
		  			$("#ResultDiv").text("正在下载文件"+data);
		  			//window.open("<%=basePath%>/DownloadFile/"+data);
		  			window.location.href = "<%=basePath%>/DownloadFile/"+data;
		  			$("#ResultDiv").text("《"+data+"》下载完成 !!!");
		  		
		  		}).fail(function (header, status, errorThrown) {
			 		$("#ResultDiv").empty();
		  			$("#ResultDiv").append(header.responseText);
		  		});
		  		
		  		$("#ResultDiv").text("正在后台计算，请等待。。。");
	  		
			});
			
			  	
	  	});
	</script>
</html>


