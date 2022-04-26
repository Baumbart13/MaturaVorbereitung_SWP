package htlinn.holzmann.server.models;

public class Article extends ShopEntity {
	public static String TABLE_NAME(){
		return "Artikel";
	}
	private int artikelId;
	private String bezeichnung;
	private String beschreibung;
	private double verkaufspreisNetto;
	private int lieferantId;

	public Article() {
		this(0, "", "", 0.0, 0);
	}

	public Article(int artikelId, String bezeichnung, String beschreibung, double verkaufspreisNetto, int lieferantId) {
		setArtikelId(artikelId);
		setBezeichnung(bezeichnung);
		setBeschreibung(beschreibung);
		setVerkaufspreisNetto(verkaufspreisNetto);
		setLieferantId(lieferantId);
	}

	public int getArtikelId() {
		return artikelId;
	}

	public void setArtikelId(int artikelId) {
		this.artikelId = artikelId;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}

	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

	public double getVerkaufspreisNetto() {
		return verkaufspreisNetto;
	}

	public void setVerkaufspreisNetto(double verkaufspreisNetto) {
		this.verkaufspreisNetto = verkaufspreisNetto;
	}

	public int getLieferantId() {
		return lieferantId;
	}

	public void setLieferantId(int lieferantId) {
		this.lieferantId = lieferantId;
	}
}
