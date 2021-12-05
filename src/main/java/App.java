import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class App {
    private JFrame frame;
    private JScrollPane scrollPane;
    private JTable timingTable;
    private JPanel panelMain;
    public static final String country = "US";
    public static final String zipcode = "98007";
    public static final String URL = "http://www.islamicfinder.us/index.php/api/prayer_times/" + "?country=" + country + "&zipcode=" + zipcode;
    public App() {
        frame = new JFrame("Miftaah Al Jannah");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String[][] salahs = getTimings();
        String columns[] = {"Salah", "Athaan"};
        timingTable = new JTable(salahs, columns);
        JLabel header = new JLabel("Salaah Timings");
        scrollPane = new JScrollPane(timingTable);
        panelMain = new JPanel();
        panelMain.add(header);
        panelMain.add(scrollPane);
        frame.add(panelMain);
        frame.setSize(450, 400);
        frame.setVisible(true);
    }


    private static class Response {
        public Results results;
        public Settings settings;
        public boolean success;
    }
    private static class Results {
        public String Fajr;
        public String Duha;
        public String Dhuhr;
        public String Asr;
        public String Maghrib;
        public String Isha;
    }

    private static class Settings {
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

    private static class Location {
        public String city;
        public String state;
        public String country;
    }

    private static class FajrRule {
        public int type;
        public int value;
    }

    private static class MaghribRule {
        public int type;
        public int value;
    }

    private static class IshaRule {
        public int type;
        public int value;
    }


    // Get timing info and return in correct format
    private String[][] getTimings() {
        Response obj = parseJson(callTimings());
        if (obj != null) {
            String salahs[][] = {
                    {"Fajr", obj.results.Fajr.replaceAll("%","")},
                    {"Sunrise", obj.results.Duha.replaceAll("%","")},
                    {"Dhuhr", obj.results.Dhuhr.replaceAll("%","")},
                    {"Asr", obj.results.Asr.replaceAll("%","")},
                    {"Maghreb", obj.results.Maghrib.replaceAll("%","")},
                    {"Isha", obj.results.Isha.replaceAll("%","")}
            };
            return salahs;
        }
        System.out.println("failure");
        return null;
    }

    private Response parseJson(String content) {
        ObjectMapper mapper = new ObjectMapper();
        Response obj = null;
        try {
            obj = mapper.readValue(content, Response.class);
        } catch (Exception e) {
            System.out.println(e);
        }
        return obj;
    }

    // make api call to get timing info
    private String callTimings() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(
                        URI.create(URL))
                .header("accept","application/json")
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            System.out.println("ERROR CALLING API: " + e);
        }
        return null;
    }

    public static void main(String[] args) {
        new App();
    }

//pushing onto github
}
