package htlinn.holzmann.server.db;

import htlinn.holzmann.server.models.Article;
import htlinn.holzmann.server.models.Supplier;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.LinkedList;
import java.util.List;

public class DbManager {
    private static final Logger logger = LoggerFactory.getLogger(DbManager.class);
    private static final String DB_NAME = "holzi_matura_db";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "DuArschloch4";
    private static final String DB_HOSTNAME = "localhost:3306";

    public DbManager() {
        // check if jdbc driver is available
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            logger.atError().log("ClassNotFoundException: jdbc driver not found");
        }
    }

    public Connection getConnection() {
        Connection con = null;
        try {
        var conString = String.format("jdbc:mysql://%s/%s?useUnicode=true&characterEncoding=utf8&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC",
                DB_HOSTNAME, DB_NAME);
        con = DriverManager.getConnection(
                conString,
                DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            //throw new Exception(e);
            logger.atError().log("SQLException: Unable to connect to database");
            e.printStackTrace();
            return con;
        } finally {
            if (con == null) {
                logger.atDebug().log("No connection was created");
            } else {
                logger.atDebug().log("Connection was created");
            }
        }
        return con;
    }

    public void releaseConnection(Connection con) {
        if (con != null) {
            try {
                if (con.isClosed()) {
                    logger.atInfo().log("No connection to release");
                    return;
                }
                con.close();
            } catch (SQLException e) {
                logger.atError().log("SQLException: Cannot release connection");
            }
            return;
        }
        logger.atInfo().log("No connection to release");
    }

    public void saveArticle(@NotNull Connection con, @NotNull Article art) {
        var sql = String.format("INSERT INTO %s" +
                        "(Artikelid," +
                        "Bezeichnung," +
                        "Beschreibung," +
                        "VerkaufspreisNetto," +
                        "LieferantId)" +
                        "VALUES (?, ?, ?, ?, ?);",
                Article.TABLE_NAME());
        PreparedStatement stmnt = null;
        try {
            stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, art.getArtikelId());
            stmnt.setString(2, art.getBezeichnung());
            stmnt.setString(3, art.getBeschreibung());
            stmnt.setDouble(4, art.getVerkaufspreisNetto());
            stmnt.setInt(5, art.getLieferantId());
            var colCount = stmnt.executeUpdate();
            if (colCount != 0) {
                logger.atDebug().log("Article has been saved");
            }
        } catch (SQLException e) {
            logger.atError().log("SQLException: Cannot save article");
        } finally {
            if (stmnt != null) {
                try {
                    stmnt.close();
                } catch (SQLException e) {
                    logger.atError().log("SQLException: Unable to close ResultSet properly");
                }
            }
        }
    }

    public void updateArticle(@NotNull Connection con, @NotNull Article art) {
        var sql = String.format("INSERT INTO %s" +
                        "(ArtikelId," +
                        "Bezeichnung," +
                        "Beschreibung," +
                        "VerkaufspreisNetto," +
                        "LieferantId)" +
                        "VALUES (?, ?, ?, ?, ?)" +
                        "ON DUPLICATE KEY UPDATE" +
                        "Bezeichnung=?" +
                        "Beschreibung=?" +
                        "VerkaufspreisNetto=?" +
                        "LieferantId=?;",
                Article.TABLE_NAME());
        PreparedStatement stmnt = null;
        try {
            stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, art.getArtikelId());
            stmnt.setString(2, art.getBezeichnung());
            stmnt.setString(3, art.getBeschreibung());
            stmnt.setDouble(4, art.getVerkaufspreisNetto());
            stmnt.setInt(5, art.getLieferantId());
            // ON DUPLICATE KEY UPDATE
            stmnt.setString(6, art.getBezeichnung());
            stmnt.setString(7, art.getBeschreibung());
            stmnt.setDouble(8, art.getVerkaufspreisNetto());
            stmnt.setInt(9, art.getLieferantId());

            stmnt.executeUpdate();
        } catch (SQLException e) {
            logger.atError().log("SQLException: Cannot update article");
        } finally {
            if (stmnt != null) {
                try {
                    stmnt.close();
                } catch (SQLException e) {
                    logger.atError().log("SQLException: Unable to close ResultSet properly");
                }
            }
        }
    }

    public Supplier holeLieferant(Connection con, String name) {
        var sql = String.format(
                "SELECT LieferantId, Email, Ort, PLZ, Strasse, Hnr, Land" +
                        "FROM %s WHERE Name=?;", Supplier.TABLE_NAME());
        var lieferant = new Supplier();
        PreparedStatement stmnt = null;
        ResultSet rs = null;
        try {
            stmnt = con.prepareStatement(sql);
            stmnt.setString(1, name);
            rs = stmnt.executeQuery();
            if (!rs.first()) {
                throw new SQLException(String.format("SQLException: No valid entry for %s in \"Lieferanten\" was found", name));
            }
            var id = rs.getInt("LieferantId");
            var email = rs.getString("Email");
            var ort = rs.getString("Ort");
            var plz = rs.getString("PLZ");
            var strasse = rs.getString("Strasse");
            var hnr = rs.getString("Hnr");
            var land = rs.getString("Land");
            lieferant = new Supplier(id, name, email, ort, plz, strasse, hnr, land);
            rs.close();
        } catch (SQLException e) {
            logger.atError().addArgument(lieferant.getName()).log("SQLException: Unable to fetch Lieferant {}");
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.atError().log("SQLException: Unable to close ResultSet properly");
                }
            }
            if (stmnt != null) {
                try {
                    stmnt.close();
                } catch (SQLException e) {
                    logger.atError().log("SQLException: Unable to close ResultSet properly");
                }
            }
        }
        return lieferant;
    }

    public List<String> holeLieferantenNamen(Connection con) {
        var names = new LinkedList<String>();
        PreparedStatement stmnt = null;
        ResultSet rs = null;
        try {
            stmnt = con.prepareStatement(String.format("SELECT Name FROM %s;", Supplier.TABLE_NAME()));
            rs = stmnt.executeQuery();
            while (rs.next()) {
                names.add(rs.getString("Name"));
            }
            rs.close();
        } catch (SQLException e) {
            logger.atError().log("SQLException: Unable to fetch names of Lieferanten");
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.atError().log("SQLException: Unable to close ResultSet properly");
                }
            }
            if (stmnt != null) {
                try {
                    stmnt.close();
                } catch (SQLException e) {
                    logger.atError().log("SQLException: Unable to close ResultSet properly");
                }
            }
        }
        return names;
    }

    public List<Article> holeArtikelVonLieferant(Connection con, Supplier l) {

        var sql = "SELECT a.ArtikelId," +
                "a.Bezeichnung," +
                "a.Beschreibung," +
                "a.VerkaufspreisNetto," +
                "a.LieferantId FROM Artikel a WHERE a.LieferantId = ?";

        var arts = new LinkedList<Article>();
        PreparedStatement stmnt = null;
        ResultSet rs = null;
        try {
            stmnt = con.prepareStatement(sql);
            rs = stmnt.executeQuery();

            while (rs.next()) {
                arts.add(new Article(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getDouble(4),
                        rs.getInt(5)
                ));
            }
            rs.close();
        } catch (SQLException e) {
            logger.atError().addArgument(l.getName()).log("SQLException: Unable to fetch articles of Lieferant {}");
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.atError().log("SQLException: Unable to close ResultSet properly");
                }
            }
            if (stmnt != null) {
                try {
                    stmnt.close();
                } catch (SQLException e) {
                    logger.atError().log("SQLException: Unable to close ResultSet properly");
                }
            }
        }

        return arts;
    }

    public void saveSupplier(Connection con, Supplier l) {
        var supps = this.holeLieferantenNamen(con);
        if (supps.contains(l.getName())) {
            logger.atInfo().addArgument(l.getName()).log("Supplier {} already exists");
            return;
        }
        try {
            var stmnt = con.prepareStatement(
                    "INSERT INTO Lieferanten (LieferantId, Name, Email, Ort, PLZ, Strasse, Hnr, Land) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            stmnt.setInt(1, l.getLieferantId());
            stmnt.setString(2, l.getName());
            stmnt.setString(3, l.getEmail());
            stmnt.setString(4, l.getOrt());
            stmnt.setString(5, l.getPlz());
            stmnt.setString(6, l.getStrasse());
            stmnt.setString(7, l.getHnr());
            stmnt.setString(8, l.getLand());
            var colCount = stmnt.executeUpdate();
            if(colCount != 0){
                logger.atDebug().log("Supplier has been saved");
            }
        }catch(SQLException e) {
            logger.atError().addArgument(l.getName()).log("SQLException: Unable to save Lieferant {}");
        }
    }
}
