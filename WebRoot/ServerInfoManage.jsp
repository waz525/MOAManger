<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
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
    		<td width="20%" align="center"><input type="button" id="QueryInfoBtn" name="QueryInfoBtn" value="信息查询" ></td>
    		<td width="20%" align="center"><input type="button" id="AddInfoBtn" name="AddInfoBtn" value="导入信息" ></td>
    	</tr>
    </table>
    <hr style="height:1px;border:none;border-top:1px solid #555555;width:95%" />
    
    <div id="QueryInfoDiv" align="center">
		销售订单号：<input type="text" id="OrderNumber" name="OrderNumber" />
		销售行号：<input type="text" id="LineNumber" name="LineNumber" />
		<P>
   		<input type="button" id="QueryBtn" name="QueryBtn" value="查询" >
    	<hr style="height:1px;border:none;border-top:1px solid #555555;width:95%" />
    </div>

    <div id="AddInfoDiv" align="center">
		<input type="file" name="fileToUpload" id="fileToUpload" value="" />&nbsp;
		<input id="UpfileBtn" name="UpfileBtn" type="button"value="上传" >
		
    	<hr style="height:1px;border:none;border-top:1px solid #555555;width:95%" />
    </div>
    
    <div id="ResultDiv" align="center">
    </div>
     <p id="msg"></p><P><p id="umsg"></p>
  </body>
  
	<script language=javascript>
		$(document).ready(function(){
			
			$("#QueryInfoDiv").hide();
			$("#AddInfoDiv").hide();
			$("#ResultDiv").empty();
			
			
			
			$("#QueryInfoBtn").click(function(){
				$("#QueryInfoDiv").show();
				$("#AddInfoDiv").hide();
				
				$("#ResultDiv").empty();
			});
			
			$("#AddInfoBtn").click(function(){
				$("#QueryInfoDiv").hide();
				$("#AddInfoDiv").show();
				
				$("#ResultDiv").empty();
				
			});
			
			$("#QueryBtn").click(function(){
				$("#ResultDiv").empty();
				
				if( $("#OrderNumber").val().length == 0 ) {
					alert("错误：请先输入销售订单号！！！")
					return ;
				}
				/*
				if( $("#LineNumber").val().length == 0 ) {
					alert("错误：请先输入销售行号！！！")
					return ;
				}
				*/
								
				$.ajax({
					"url": "<%=basePath%>/servlet/ServerInfoManager?type=1",
					"method": "POST",
					"cache": false,
					"async":false,
					"data":{
						"OrderNumber":""+$("#OrderNumber").val(),
						"LineNumber":""+$("#LineNumber").val()
					}
				}).success(function (data, status, header) {
					//$("#msg").text("OK: "+JSON.stringify(data));
					if ( data.length > 0 ) {
						var table=$("<table border=\"1\"  width=\"90%\" >");
						table.appendTo($("#ResultDiv"));
						var tr=$("<tr style=\"font-weight:bold;\" bgcolor=\"#CCCCCC\"></tr>");
						tr.appendTo(table);
						var td=$("<td align=\"center\" >销售订单号</td><td align=\"center\" >销售行</td><td align=\"center\" >物料号</td><td align=\"center\" >物料描述</td><td align=\"center\" >数量</td><td align=\"center\" >交货日期</td><td align=\"center\" >合同号</td>");
						td.appendTo(tr);
						for( var ind = 0 ; ind <data.length ; ind++ ) {
							tr=$("<tr bgcolor=\"#CCFFCC\"></tr>");
							tr.appendTo(table);
							td=$("<td align=\"center\" >"+data[ind].ordernumber+"</td><td align=\"center\" >"+data[ind].linenumber+"</td><td align=\"center\" >"+data[ind].matternumber+"</td><td align=\"center\" >"+data[ind].matterinfo+"</td><td align=\"center\" >"+data[ind].quantity+"</td><td align=\"center\" >"+data[ind].deliverytime+"</td><td align=\"center\" >"+DecodeString(data[ind].contractnumber)+"</td>");
							td.appendTo(tr);
							
						}
						$("#ResultDiv").append("</table>");
					} else {
						$("#ResultDiv").text("未查询到结果！！！");
					}
				}).fail(function (header, status, errorThrown) {
					$("#ResultDiv").text("网络通讯错误!!!") ; 
				})
				
			
			});
		
			$("#UpfileBtn").click(function(){ 
				$("#ResultDiv").empty();
			
				if( $("#fileToUpload").val().length == 0 ) {
					alert("错误：请先选择文件！！！")
					return ;
				}
				
				$("#ResultDiv").text("正在上传。。。");
				$.ajaxFileUpload({    
					"url":"<%=basePath%>/servlet/FileUploader?type=2", //url自己写   
					"secureuri":false, //这个是啥没啥用  
					"type":"post",  
					"fileElementId":"fileToUpload",//file标签的id    
					"dataType": "json",//返回数据的类型    
					//data:{name:'logan'},//一同上传的数据    
					success: function(data, status){  
						//$("#msg").text("OK: "+JSON.stringify(data));
						$("#ResultDiv").empty();
						$("#ResultDiv").append("<p>成功上传："+data.numInsert+" 条<p>重复数据："+data.numRepeat+" 条<p>错误数据："+data.numError+" 条");
						
					},
					error: function(data, status, e){ 
						$("#ResultDiv").empty();
						$("#ResultDiv").text("返回错误: "+e);
					}
				});
			});
		
		
		});
	
	</script>
</html>
