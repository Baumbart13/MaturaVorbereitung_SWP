package htlinn.holzmann.server.servlets;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import htlinn.holzmann.server.Globals;
import htlinn.holzmann.server.models.Article;
import htlinn.holzmann.server.models.ShopEntity;
import htlinn.holzmann.server.models.Supplier;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "saveRandomSupplierServlet", value = "/saveRandomSupplierServlet")
public class SaveRandomSupplierServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Save Random Supplier Servlet";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        var supps = Globals.getSupplierFiller().createRandomEntities();
        var con = Globals.dbm().getConnection();
        var lNames = Globals.dbm().holeLieferantenNamen(con);

        for(ShopEntity s : supps){
            if(s.getClassname().equalsIgnoreCase("Supplier")) {
                if(lNames.contains(((Supplier)s).getName())){
                    continue;
                }
                Globals.dbm().saveSupplier(con, (Supplier)s);
            } else if (s.getClassname().equalsIgnoreCase("Article")) {
                Globals.dbm().saveArticle(con, (Article)s);
            }
        }
    }

    public void destroy() {
    }
}