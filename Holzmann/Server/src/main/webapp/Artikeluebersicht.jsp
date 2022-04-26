<%@ page import="htlinn.holzmann.server.db.DbManager" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.LinkedList" %>
<%@ page import="java.io.IOException" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
</head>
<%
    DbManager dbm = new DbManager();
%>
<%!
    private String createErrorString(String msg){
        return "<h3 style='color:red'>" + msg + "</h3>";
    }
%>
<body>
<p>Lieferant auswählen: </p>
<%
    try {
        Connection con = dbm.getConnection();
        if (con == null) {
            out.append(createErrorString("No connection to database"));
        }
        List<String> lNames = new LinkedList<>();//dbm.holeLieferantenNamen(con);
        out.append("<select name='LieferantenSelect' id='LieferantenSelect>");
        for (int i = 0; i < 25; ++i) {
            out.append("<option>hallo" + i + "</option>");
        }
        dbm.releaseConnection(con);
        out.append("</select>");
    } catch (Exception e) {
        out.append(createErrorString("No connection to database"));
    }
%>
</body>

</html>