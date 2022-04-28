package htlinn.holzmann.server;

import htlinn.holzmann.server.db.DbManager;
import htlinn.holzmann.server.db.TableFiller;

public class Globals {
    private Globals(){}
    private static DbManager dbManager;
    public static DbManager getDbManager(){
        if(dbManager == null){
            dbManager = new DbManager();
        }
        return dbManager;
    }
    public static DbManager dbm(){
        return getDbManager();
    }

    private static TableFiller.SupplierFiller supplierFiller;
    public static TableFiller.SupplierFiller getSupplierFiller(){
        if(supplierFiller == null){
            supplierFiller = new TableFiller.SupplierFiller();
        }
        return supplierFiller;
    }

    private static TableFiller.ArticleFiller articleFiller;
    public static TableFiller.ArticleFiller getArticleFiller(){
        if(articleFiller == null){
            articleFiller = new TableFiller.ArticleFiller();
        }
        return articleFiller;
    }
}
