import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class App implements ActionListener {
    private JFrame frame;
    public static final String COUNTRY = "US";
    public static final String ZIPCODE = "98007";
    public static String URL = "http://www.islamicfinder.us/index.php/api/prayer_times/" + "?country=" + COUNTRY + "&zipcode=" + ZIPCODE;
    public static final String[] PRAYER_NAMES = {"Fajr", "Sunrise", "Dhuhr", "Asr", "Maghreb", "Isha"};
    public static JLabel[] jalebis = new JLabel[PRAYER_NAMES.length];
    public static final String LOADING_MESSAGE = "Loading...";
    public static final String APP_NAME = "مفتاح الجنة الصلاة";
    public App() {
        /*
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
         */
        setUI();
        getTimings(true);
    }

    private void setUI() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,500);
        frame.setLayout(new BorderLayout());

        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new GridLayout(6, 2));
        //tablePanel.setBackground(Color.blue);
        tablePanel.setPreferredSize(new Dimension(100, 100));

        // Create JLabels to hold the salah names/times
        for (int i = 0; i < PRAYER_NAMES.length; i++) {
            tablePanel.add(new JLabel(PRAYER_NAMES[i], SwingConstants.CENTER));
            jalebis[i] = new JLabel(LOADING_MESSAGE, SwingConstants.CENTER);
            tablePanel.add(jalebis[i]);
        }

        // Create header
        JLabel headerLabel = new JLabel(APP_NAME, SwingConstants.CENTER);
        headerLabel.setFont(new Font(headerLabel.getFont().getName(), Font.BOLD, 30));
        headerLabel.setBackground(Color.lightGray);
        headerLabel.setOpaque(true);
        headerLabel.setPreferredSize(new Dimension(100, 100));

        //jbutton to refresh
        JButton refresh = new JButton("Refresh Timings");
        refresh.addActionListener(this);
        refresh.setPreferredSize(new Dimension(50, 50));



        frame.add(tablePanel, BorderLayout.CENTER);
        frame.add(headerLabel, BorderLayout.NORTH);
        frame.add(refresh, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getTimings(false);
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

    //TODO - remove firstTime
    // Get timing info and return in correct format
    private void getTimings(boolean firstTime) {
        System.out.println("getting timings");
        for (int i = 0; i < PRAYER_NAMES.length; i++) {
            System.out.println("modifying text of label: " + jalebis[i].getText());
            jalebis[i].setText(LOADING_MESSAGE);
            System.out.println("result: " + jalebis[i].getText());
        }

        Response obj = parseJson(callTimings());
        if (obj != null) {
            String salahs[] = {
                obj.results.Fajr.replaceAll("%",""),
                obj.results.Duha.replaceAll("%",""),
                obj.results.Dhuhr.replaceAll("%",""),
                obj.results.Asr.replaceAll("%",""),
                obj.results.Maghrib.replaceAll("%",""),
                obj.results.Isha.replaceAll("%","")
            };
            for (int i = 0; i < PRAYER_NAMES.length; i++) {
                jalebis[i].setText(salahs[i]);
            }
        } else {
            System.out.println("failure");
        }
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
