<%@ page import="htlinn.holzmann.server.db.DbManager" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
</head>

<body>
<p>Lieferant auswählen: </p> <select name="LieferantenSelect" id="LieferantenSelect">
    <%
        DbManager db = new DbManager();
        Connection con = db.getConnection();
        List<String> lNames = db.holeLieferantenNamen(con);
        for(String n : lNames) {
            out.append("<p>hallo</p></br>");
        }
    %>
</body>

</html>