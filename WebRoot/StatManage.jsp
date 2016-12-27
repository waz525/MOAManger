<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
Date myDate= new Date();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'ServerInfoManage.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="<%=basePath%>js/jquery-1.11.3.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/ajaxfileupload.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/deci.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/jbase64.js"></script>

  </head>
  
  <body>
    <table border="0" cellspacing="0" width="60%" align="center" >
    	<tr height="20px">
    		<td width="20%" align="center"><input type="button" id="SignStatBtn" name="SignStatBtn" value="签到统计" ></td>
    		<td width="20%" align="center"><input type="button" id="TravelStatBtn" name="TravelStatBtn" value="出差统计" ></td>
    	</tr>
    </table>
    
    <P>
    
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
 		<input type="button" id="SignStat" name="SignStat" value="签到统计" >
    </div>
    
    
	<div id="TravelStatDiv"  align="center">
  		<select id="TravelStat_year">
  			
  		<%for( int i = 2016 ; i < 2025 ; i++ ) { %>
  			<option value="<%=i%>" <% if( i==myDate.getYear()+1900) { %>selected<%} %>  ><%=i %></option>
  		<%}%>
  		</select>年&nbsp;&nbsp;
 		<input type="button" id="TravelStat" name="TravelStat" value="年度出差统计" >
    </div>
    
    <P>
    <div id="ResultDiv" align="center">
    </div>
     <p id="msg"></p><P><p id="umsg"></p>
  </body>
  
	<script language=javascript>
		$(document).ready(function(){
			
			$("#ResultDiv").empty();
			$("#SignStatDiv").hide();
			$("#TravelStatDiv").hide();
			
			
			
			$("#SignStatBtn").click(function(){
				
				$("#ResultDiv").empty();
				
				$("#SignStatDiv").show();
				$("#TravelStatDiv").hide();
				
			});
			
			$("#TravelStatBtn").click(function(){
				
				$("#ResultDiv").empty();
				
				$("#SignStatDiv").hide();
				$("#TravelStatDiv").show();
				
			});
			
			
			
			$("#SignStat").click(function(){
				
				$("#ResultDiv").empty();
				var DateStr = $("#SignStat_year").val() + "-" + $("#SignStat_month").val() + "-" + $("#SignStat_day").val() ;
				
				$.ajax({
		  			"url": "<%=basePath%>/StatManager?type=1",
		  			"method": "POST",
					"cache": false,
					"async":false,
		  			"data": {
						"DateStr": DateStr
					}
		  		}).success(function (data, status, header) {
		  			//$("#msg").text("OK: "+JSON.stringify(data));
		  			if ( data.length > 0 ) {
						var table=$("<table border=\"1\"  width=\"40%\" >");
						table.appendTo($("#ResultDiv"));
						var tr=$("<tr bgcolor=\"#CCCCCC\"></tr>");
						tr.appendTo(table);
						var td=$("<td colspan=\"4\" align=\"center\" >"+DateStr+" 签到小时统计</td>");
						td.appendTo(tr);
						var tr=$("<tr style=\"font-weight:bold;\" bgcolor=\"#CCCCCC\"></tr>");
						tr.appendTo(table);
						var td=$("<td align=\"center\" >小时</td><td align=\"center\" >签到人数</td>");
						td.appendTo(tr);
						for( var ind = 0 ; ind <data.length ; ind++ ) {
							tr=$("<tr bgcolor=\"#CCFFCC\"></tr>");
							tr.appendTo(table);
							td=$("<td align=\"center\" >"+data[ind].hour+"</td><td align=\"center\" >"+data[ind].count+"</td>");
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
			
			
			
			$("#TravelStat").click(function(){
				
				$("#ResultDiv").empty();
				
				$.ajax({
		  			"url": "<%=basePath%>/StatManager?type=2",
		  			"method": "POST",
					"cache": false,
					"async":false,
		  			"data": {
						"DateStr":  $("#TravelStat_year").val()
					}
		  		}).success(function (data, status, header) {
		  			//$("#msg").text("OK: "+JSON.stringify(data));
		  			if ( data.length > 0 ) {
						var table=$("<table border=\"1\"  width=\"60%\" >");
						table.appendTo($("#ResultDiv"));
						var tr=$("<tr style=\"font-weight:bold;\" bgcolor=\"#CCCCCC\"></tr>");
						tr.appendTo(table);
						var td=$("<td align=\"center\" >工号</td><td align=\"center\" >姓名</td><td align=\"center\" >出差次数</td><td align=\"center\" >出差天数</td>");
						td.appendTo(tr);
						for( var ind = 0 ; ind <data.length ; ind++ ) {
							tr=$("<tr bgcolor=\"#CCFFCC\"></tr>");
							tr.appendTo(table);
							td=$("<td align=\"center\" >"+data[ind].username+"</td><td align=\"center\" >"+data[ind].realname+"</td><td align=\"center\" >"+data[ind].count+"</td><td align=\"center\" >"+data[ind].alldays+"</td>");
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
			
			
		});
	</script>
</html>
