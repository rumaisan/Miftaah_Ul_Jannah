// Defines the structure of the JSON response from the Islamic Finder API.
public class Response {

    public Results results;
    public Settings settings;
    public boolean success;

    public static class Results {
        public String Fajr;
        public String Duha;
        public String Dhuhr;
        public String Asr;
        public String Maghrib;
        public String Isha;
    }

    public static class Settings {
        public String name;
        public Location location;
        public double latitude;
        public double longitude;
        public String timezone;
        public int method;
        public int juristic;
        public int high_latitude;
        public FajrRule fajir_rule;
        public MaghribRule maghrib_rule;
        public IshaRule isha_rule;
        public int time_format;

    }

    public static class Location {
        public String city;
        public String state;
        public String country;
    }

    public static class FajrRule {
        public int type;
        public int value;
    }

    public static class MaghribRule {
        public int type;
        public int value;
    }

    public static class IshaRule {
        public int type;
        public int value;
    }
}
