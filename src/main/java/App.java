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
    public static final String COUNTRY = "US";
    public static final String ZIPCODE = "98007";
    public static String URL = "http://www.islamicfinder.us/index.php/api/prayer_times/" + "?country=" + COUNTRY + "&zipcode=" + ZIPCODE;
    public static final String[] PRAYER_NAMES = {"Fajr", "Sunrise", "Dhuhr", "Asr", "Maghreb", "Isha"};
    public static final String LOADING_MESSAGE = "Loading...";
    public static final String APP_NAME = "مفتاح الجنة الصلاة";

    private JFrame frame;
    public JLabel[] jalebis = new JLabel[PRAYER_NAMES.length];

    public App() {
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
        tablePanel.setPreferredSize(new Dimension(100, 100));

        // Create JLabels to hold the salah names/times
        for (int i = 0; i < PRAYER_NAMES.length; i++) {
            tablePanel.add(new JLabel(PRAYER_NAMES[i], SwingConstants.CENTER));
            jalebis[i] = new JLabel(LOADING_MESSAGE, SwingConstants.CENTER);
            tablePanel.add(jalebis[i]);
        }

        // Create header
        MiftaahHeader headerLabel = new MiftaahHeader();

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
}
