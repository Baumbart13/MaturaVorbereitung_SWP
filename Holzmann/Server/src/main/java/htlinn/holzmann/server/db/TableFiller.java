package htlinn.holzmann.server.db;

import htlinn.holzmann.server.models.Article;
import htlinn.holzmann.server.models.Supplier;
import htlinn.holzmann.server.models.ShopEntity;

import java.util.LinkedList;
import java.util.List;

public interface TableFiller {
    public ShopEntity createRandomEntity();

    public default List<ShopEntity> createRandomEntities() {
        return createRandomEntities(10);
    }

    public default List<ShopEntity> createRandomEntities(int count) {
        var l = new LinkedList<ShopEntity>();
        for (int i = 0; i < count; i++) {
            l.add(createRandomEntity());
        }
        return l;
    }

    public static class SupplierFiller implements TableFiller {
        private String createRandomName(){
            final String[] NAMES = new String[]{"Siemens", "Bosch", "Klein & Partner", "LAMEA", "Hewlett Packard", "Coca Cola"};
            return NAMES[(int) (Math.random() * NAMES.length)];
        }

        private String createRandomEmail(String supplierName){
            final String[] COUNTRY_DOMAIN = new String[]{"de", "at", "ch", "com", "org"};

            var randCountryDomain = COUNTRY_DOMAIN[(int) (Math.random() * COUNTRY_DOMAIN.length)];
            return "info@" + supplierName.toLowerCase().replace(" ", "") + randCountryDomain;
        }

        private String createRandomPlace(){
            final String[] PLACES = new String[]{"Wien", "Graz", "Salzburg", "Linz", "Innsbruck", "Klagenfurt", "Berlin", "Hamburg"};
            return PLACES[(int) (Math.random() * PLACES.length)];
        }

        private String createRandomPostalCode(){
            return createRandomNumber(1010, 99999) + "";
        }

        private String createRandomCountry(String email){
            return switch (email.substring(email.lastIndexOf(".") + 1)) {
                case "de" -> "Deutschland";
                case "at" -> "Österreich";
                case "ch" -> "Schweiz";
                case "com" -> "USA";
                case "org" -> "Japan";
                default -> "International :upside_down:";
            };
        }

        private String createRandomStreet(){
            final String[] STREETS = new String[]{"Peter-Habeler-Straße", "Maria-Theresien-Straße", "Anichstraße", "brandbergstraße"};
            return STREETS[(int) (Math.random() * STREETS.length)];
        }

        private int createRandomNumber(int min, int max) {
            return min + (int) (Math.random() * (max - min));
        }

        @Override
        public ShopEntity createRandomEntity() {
            var name = createRandomName();
            var email = createRandomEmail(name);
            var place = createRandomPlace();
            var postalCode = createRandomPostalCode();
            var country = createRandomCountry(email);
            var street = createRandomStreet();

            var s = new Supplier(0,
                    name,
                    email,
                    place,
                    postalCode,
                    street,
                    createRandomNumber(1, 100) + "",
                    createRandomCountry(email));
            return s;
        }
    }

    public static class ArticleFiller implements TableFiller {
        private String createRandomTitle(){
            final String[] NAMES = new String[]{""};

            return NAMES[(int) (Math.random() * NAMES.length)];
        }

        private String createRandomDescription(){
            var c = new char[(int) (Math.random() * 10000)];

            for(int i = 0; i < c.length; i++){
                c[i] = (char) (Math.random() * 255);
            }

            return String.copyValueOf(c);
        }

        private double createRandomPrice(){
            var rand = Math.random() * 1500.0d;
            rand = (int)(1500*100.0d);
            return rand * 0.01d;
        }

        @Override
        public ShopEntity createRandomEntity() {
            //public Article(int artikelId, String bezeichnung, String beschreibung, double verkaufspreisNetto, int lieferantId)
            var title = createRandomTitle();
            var description = createRandomDescription();
            var price = createRandomPrice();
            var a = new Article(0,
                    title,
                    description,
                    price,
                    0);
            return a;
        }

    }
}
