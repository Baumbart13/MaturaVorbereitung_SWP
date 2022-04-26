<%@ page import="htlinn.holzmann.server.db.DbManager" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.LinkedList" %>
<%@ page import="java.io.IOException" %>
<%@ page import="htlinn.holzmann.server.models.Article" %>
<%@ page import="htlinn.holzmann.server.db.TableFiller" %>
<%@ page import="htlinn.holzmann.server.models.Supplier" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
</head>
<%!
    DbManager dbm = new DbManager();
    TableFiller articleFiller = new TableFiller.ArticleFiller();
    TableFiller supplierFiller = new TableFiller.SupplierFiller();
    private String createErrorString(String msg){
        return "<h3 style='color:red'>" + msg + "</h3>";
    }
    private void generateArticle(){
        Connection con = dbm.getConnection();
        Article a = (Article)articleFiller.createRandomEntity();
        dbm.saveArticle(con, a);
        dbm.releaseConnection(con);
    }
    private void generateSupplier(){
        Connection con = dbm.getConnection();
        Supplier s = (Supplier)supplierFiller.createRandomEntity();
        dbm.saveSupplier(con, s);
        dbm.releaseConnection(con);
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
<br><br>

<form id="randomArticleFormId" name="randomArticleForm" method="post">
    <input type="submit" value="Random Article" name="randomArticleButton">
</form>
<input type="button" onclick="generateArticle()" value="Artikel generieren">
<input type="button" onclick="generateSupplier()" value="Lieferant generieren">


</body>

</html>