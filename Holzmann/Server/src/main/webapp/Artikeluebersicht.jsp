<%@ page import="java.sql.Connection" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.LinkedList" %>
<%@ page import="htlinn.holzmann.server.Globals" %>
<%@ page import="htlinn.holzmann.server.db.DbManager" %>
<%@ page import="htlinn.holzmann.server.models.Article" %>
<%@ page import="htlinn.holzmann.server.models.Supplier" %>
<%@ page import="htlinn.holzmann.server.models.ShopEntity" %>
<%@ page import="java.lang.Exception" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
</head>
<%!
    private String createErrorString(String msg){
        return "<h3 style='color:red'>" + msg + "</h3>";
    }
%>
<script type="text/javascript">
    function showArtikel(supplierName){
        const x = new XMLHttpRequest();
        x.onload = function(){
            alert(this.responseText);
            var arr = JSON.parse(this.responseText);
            var html = convertArticlesToHtml(arr);
            document.getElementById("article_table").innerHTML = html;
        }
        x.open("GET", "/GetArticlesServlet?sId=" + supplierName, true);
        x.setRequestHeader("Content-Type", "x-www-form-urlencoded");
        x.send();
    }
    function convertArticlesToHtml(arr){
        var html = "<table>";
        html +=
            "<thead>" +
                "<tr>" +
                    "<th>ID</th>" +
                    "<th>Bezeichnung</th>" +
                    "<th>Beschreibung</th>" +
                    "<th>Preis</th>" +
                "</tr>" +
            "</thead>";
        for(var i = 0; i < arr.length; i++){
            html += "<tr>";
            html += "<td>" + arr[i].artikelId + "</td>";
            html += "<td>" + arr[i].bezeichnung + "</td>";
            html += "<td>" + arr[i].beschreibung + "</td>";
            html += "<td>" + arr[i].verkaufspreisNetto + "</td>";
            html += "</tr>";
        }
        html += "</table>";
        return html;
    }
</script>
<body>
<p>Lieferant auswählen: </p>
<%
    boolean wasException = false;
    try {
        Connection con = Globals.dbm().getConnection();
        if (con == null) {
            out.append("<h3 style='color:red'>" + "No connection to database" + "</h3><br>");
        }
        List<String> lNames = Globals.dbm().holeLieferantenNamen(con);
        out.append("<select name='LieferantenSelect' id='LieferantenSelect'>");
        if(lNames.size() == 0){
            out.append("<option>Keine Lieferanten vorhanden</option>");
        }
        else {
            for (i = 0; i < lNames.size(); ++i) {
                out.append("<option>" + lNames.get(i) + "</option>");
            }
        }
        Globals.dbm().releaseConnection(con);
        out.append("</select>");
        out.append("")
    } catch (Exception e) {
        wasException = true;
        out.append("<br><h3 style='color:red'>" + "There was an error: " + e.getMessage() + "</h3><br>");
    }
    if(!wasException){
        out.append("<br><h3 style='color:green'>" + "There was no error" + "</h3><br>");
    }
%>
<p>Artikel auswählen: </p>
<!-- Table with articles from chosen supplier -->

<div id="article_table">

</div>

<br><br>
<p>Some text</p>

<form id="randomArticleFormId" name="randomArticleForm" method="post">
    <input type="submit" value="Random Article" name="randomArticleButton">
</form><br>


</body>

</html>