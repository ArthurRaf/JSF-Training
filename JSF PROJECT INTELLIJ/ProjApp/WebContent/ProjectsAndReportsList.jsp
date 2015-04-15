<!DOCTYPE html>
<html dir="ltr">
<head>
    <title>Project Data Entry Form</title>
    <link rel='stylesheet' type='text/css' href='./css/main.css'>
    <link rel='stylesheet' type='text/css' href='./css/style.css'>
    <script type="text/javascript" src="./js/utils.js"></script>
    <%@page import="com.synisys.training.report.ReportItem"%>
    <%@page import="java.util.List"%>
    <%@page import="java.util.ArrayList"%>

</head>
<body class='body-style'>
<div class='main-container'>
    <form class="form-style1">
        <div class='content'>
            <h2 class='field-area fontSize11'>
                <span class='field-title'>Projects List</span>
            </h2>
            <table class='table-style fontSize11 table-style2'>
                <tr>
                    <th class='table-header th-style1'></th>
                    <th class='table-header th-style'><span>Projects</span></th>
                </tr>
                <tr>
                    <td class='td-style'><a href='./Blank.xhtml' class='ico-del1'
                                            title="Delete"
                                            onclick="return sis.utils.confirm('Delete anyway?');"></a></td>
                    <td class='td-style'><a class='link-style'
                                            href='./ProjectAddEditForm.xhtml'>Project 1</a></td>
                </tr>
                <tr>
                    <td class='td-style'><a href='#' class='ico-del1'
                                            title="Delete"></a></td>
                    <td class='td-style'><a class='link-style' href='#'>Project
                        2</a></td>
                </tr>
                <tr>
                    <td class='td-style'><a href='#' class='ico-del1'
                                            title="Delete"></a></td>
                    <td class='td-style'><a class='link-style' href='#'>Project
                        3</a></td>
                </tr>
                <tr>
                    <td class='td-style'><a href='#' class='ico-del1'
                                            title="Delete"></a></td>
                    <td class='td-style'><a class='link-style' href='#'>Project
                        4</a></td>
                </tr>
                <tr>
                    <td class='td-style'><a href='#' class='ico-del1'
                                            title="Delete"></a></td>
                    <td class='td-style'><a class='link-style' href='#'>Project
                        5</a></td>
                </tr>
                <tr>
                    <td class='td-style'><a href='#' class='ico-del1'
                                            title="Delete"></a></td>
                    <td class='td-style'><a class='link-style' href='#'>Project
                        6</a></td>
                </tr>
            </table>
            <%--<button class='button-style2' formaction="./ProjectAddEditForm.xhtml">Add</button>--%>
            <%--<h:commandButton styleClass="button-style" value="Add"  action="projectAddEditForm"
                             actionListener="#{projectForm.addNewProject}" immediate="true">
            </h:commandButton>--%>
        </div>
    </form>

    <%--<form class="form-style1" id='reportsForm' action="loadReport" method="get">
        <input type='hidden' value='' name='currentReportId' id='currentReportId'/>
        <div class='content'>
            <h2 class='field-area fontSize11'>
                <span class='field-title'>Reports List</span>
            </h2>
            <table class='table-style fontSize11 table-style2'>
                <tr>
                    <th class='table-header th-style'><span>Report Name</span></th>
                    <th class='table-header th-style'><span>Description</span></th>
                </tr>
                <%
                    List<ReportItem> reports = (ArrayList<ReportItem>) request.getServletContext().getAttribute("reportsList");
                    for (ReportItem reportItem : reports) {
                %>
                <tr>
                    <td class='td-style'>
                        <a class='link-style' href='#'
                           onclick="document.getElementById('currentReportId').value=<%=reportItem.getReportId()%>; document.getElementById('reportsForm').submit();">
                            <%=reportItem.getName()%>
                        </a>
                    </td>
                    <td class='td-style'>
							<span> 
								<%=reportItem.getDescription()%>								
							</span>
                    </td>
                </tr>
                <%
                    }
                %>
            </table>
        </div>
    </form>--%>
</div>
</body>
</html>