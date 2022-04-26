package htlinn.holzmann.server.models;

public class Supplier extends ShopEntity {
	public static String TABLE_NAME(){return "Lieferanten";}

	private int lieferantId;
	private String name;
	private String email;
	private String ort;
	private String plz;
	private String strasse;
	private String hnr;
	private String land;

	public Supplier() {
		this(0, "", "", "", "", "", "", "");
	}

	public Supplier(int lieferantId, String name, String email, String ort, String plz, String strasse, String hnr, String land) {
		setLieferantId(lieferantId);
		setName(name);
		setEmail(email);
		setOrt(ort);
		setPlz(plz);
		setStrasse(strasse);
		setHnr(hnr);
		setLand(land);
	}

	public int getLieferantId() {
		return lieferantId;
	}

	public void setLieferantId(int lieferantId) {
		this.lieferantId = lieferantId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOrt() {
		return ort;
	}

	public void setOrt(String ort) {
		this.ort = ort;
	}

	public String getPlz() {
		return plz;
	}

	public void setPlz(String plz) {
		this.plz = plz;
	}

	public String getStrasse() {
		return strasse;
	}

	public void setStrasse(String strasse) {
		this.strasse = strasse;
	}

	public String getHnr() {
		return hnr;
	}

	public void setHnr(String hnr) {
		this.hnr = hnr;
	}

	public String getLand() {
		return land;
	}

	public void setLand(String land) {
		this.land = land;
	}
}
