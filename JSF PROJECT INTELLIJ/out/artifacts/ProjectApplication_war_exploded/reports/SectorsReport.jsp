<!DOCTYPE html>
<html dir="ltr">
<head>
<title>Sectors Report</title>
<link rel='stylesheet' type='text/css' href='./css/main.css'>
<link rel='stylesheet' type='text/css' href='./css/style.css'>
<%@page import="com.synisys.training.report.SectorReportItem"%>

</head>
<body class='body-style'>
	<div class='main-container'>
		<jsp:useBean id="sectorReport" type="com.synisys.training.report.SectorReport" scope="request"/>
				
		<h1 class='section-title1 fontSize18 '><%=sectorReport.getReportItem().getName() %></h1>
		<p class='section-title fontSize18 clear'><%=sectorReport.getReportItem().getDescription() %></p>
		<div class="clear"></div>
		<div class='content'>
			<table class='table-style fontSize11 table-style2'>
				<tr>
					<th class='table-header th-style1'>Sector</th>
					<th class='table-header th-style'>Total Amount (USD)</th>
				</tr>
				<%
    			for (SectorReportItem sector : sectorReport.getSectorItems()){
    			%>
				<tr>
					<td class='td-style1'><%=sector.getSectorName()%></td>
					<td class='td-style alignRight'><%=sector.getAmount()%></td>
				</tr>
				<%		
    			}
    			%>
			</table>
		</div>
	</div>
</body>
</html>