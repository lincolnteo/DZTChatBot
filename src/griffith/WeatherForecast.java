import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeatherForecast {

    public static <AsyncHttpClient, OkHttpClient, Response> void weatherForecast(String latitude,String longitude) throws IOException, InterruptedException {
        // Construct API URL with latitude and longitude parameters
        String url = "https://ai-weather-by-meteosource.p.rapidapi.com/daily?lat="+latitude+"&lon="+longitude+"&language=en&units=auto";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("x-rapidapi-key", "3ec6205bf9msh83861deaecbd4b8p1ad8b7jsnedd27a4f8084")
                .header("x-rapidapi-host", "ai-weather-by-meteosource.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
//        System.out.println(response.body());

        // Store response body for processing
        String body = response.body();

        // Regular expression to find all "day":"<value>"
        Pattern pattern = Pattern.compile("\"day\":\"(.*?)\"");
        Matcher matcher = pattern.matcher(body);

        // List to store extracted values
        ArrayList<String> days = new ArrayList<>();

        // Extract all matches
        while (matcher.find()) {
            days.add(matcher.group(1));  // Group 1 captures the value inside quotes
        }

        // Regular expression to find all "weather":"<value>"
        Pattern pattern2 = Pattern.compile("\"weather\":\"(.*?)\"");
        Matcher matcher2 = pattern2.matcher(body);

        // List to store extracted values
        ArrayList<String> weathers = new ArrayList<>();

        // Extract all matches
        while (matcher2.find()) {
            weathers.add(matcher2.group(1));  // Group 1 captures the value inside quotes
        }

        // Regular expression to find all "summary":"<value>"
        Pattern pattern3 = Pattern.compile("\"summary\":\"(.*?)\"");
        Matcher matcher3 = pattern3.matcher(body);

        // List to store extracted values
        ArrayList<String> summaries = new ArrayList<>();

        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(body);
            JSONObject daily = (JSONObject) jsonObject.get("daily");
            JSONArray data = (JSONArray) daily.get("data");

            // Extract all matches
            while (matcher3.find()) {
                summaries.add(matcher3.group(1));  // Group 1 captures the value inside quotes
            }

            // Displaying the extracted data for the first 4 days
            // today and 3 days after
            for (int i = 1; i < 4; i++) {
                JSONObject dayData = (JSONObject) data.get(i);
                String day = (String) dayData.get("day");
                String weather = (String) dayData.get("weather");
                String summary = (String) dayData.get("summary");
                double feelsLike = (double) dayData.get("feels_like");

                // Suggest appropriate clothing based on the feels_like temperature
                String clothingSuggestion;
                if (feelsLike < 5) {
                    clothingSuggestion = "Wear a heavy coat and warm clothing.";
                } else if (feelsLike < 10) {
                    clothingSuggestion = "Wear a jacket and warm clothing.";
                } else if (feelsLike < 15) {
                    clothingSuggestion = "Wear a light jacket or sweater.";
                } else {
                    clothingSuggestion = "Wear light clothing.";
                }

                // Display the extracted data

                Color.println("\nDate: " + day,Color.Type.WHITE);

                Color.println("Weather condition: " + weather,Color.Type.PURPLE);

                Color.println("Weather Description: " + summary,Color.Type.YELLOW);

                Color.println("Clothing Suggestion: " + clothingSuggestion ,Color.Type.GREEN);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
