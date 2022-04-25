package htlinn.holzmann.db;

import htlinn.holzmann.models.Artikel;
import htlinn.holzmann.models.Lieferant;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class DbManager {
    private static DbManager instance;
    private static final Logger logger = LoggerFactory.getLogger(DbManager.class);

    public static DbManager getInstance() {
        if (instance == null) {
            instance = new DbManager();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return this.getConnection("localhost", "3306", "Holzmann_Matura", "root", "root");
    }

    public Connection getConnection(String hostname, String port, String database, String username, String password) throws SQLException {
        var con = DriverManager.getConnection(
                String.format("jdbc:mysql://%s:%s/%s", hostname, port, database),
                username,
                password);
        return con;
    }

    public void releaseConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                logger.atError().log("SQLException: Cannot release connection");
            }
            return;
        }
        logger.atInfo().log("No connection to release");
    }

    public void speichereArtikel(@NotNull Connection con, @NotNull Artikel art) {
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
                Artikel.TABLE_NAME());
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

    public Lieferant holeLieferant(Connection con, String name) {
        /**
         *
         LieferantId int primary key auto_increment,
         Name varchar(128) not null unique,
         Email varchar(255) not null,
         Ort varchar(128) not null,
         PLZ varchar(128) not null,
         Strasze varchar(128) not null,
         Hnr varchar(128) not null,
         Land varchar(128) not null
         */

        var sql = String.format(
                "SELECT LieferantId, Email, Ort, PLZ, Strasse, Hnr, Land" +
                        "FROM %s WHERE Name=?;", Lieferant.TABLE_NAME());
        var lieferant = new Lieferant();
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
            lieferant = new Lieferant(id, name, email, ort, plz, strasse, hnr, land);
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
            stmnt = con.prepareStatement(String.format("SELECT Name FROM %s;", Lieferant.TABLE_NAME()));
            rs = stmnt.executeQuery();
            while (rs.first()) {
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

    public List<Artikel> holeArtikelVonLieferant(Connection con, Lieferant l) {

        var sql = "SELECT a.ArtikelId," +
                "a.Bezeichnung," +
                "a.Beschreibung," +
                "a.VerkaufspreisNetto," +
                "a.LieferantId FROM Artikel a WHERE a.LieferantId = ?";

        var arts = new LinkedList<Artikel>();
        PreparedStatement stmnt = null;
        ResultSet rs = null;
        try {
            stmnt = con.prepareStatement(sql);
            rs = stmnt.executeQuery();

            while (rs.next()) {
                arts.add(new Artikel(
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
}
