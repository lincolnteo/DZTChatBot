import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeatherForecast {

    public static <AsyncHttpClient, OkHttpClient, Response> void main(String[] args) throws IOException, InterruptedException {


        String latitude = "51.896893";
        String longitude = "-8.486316";
        weatherForecast(latitude, longitude);

    }
    public static <AsyncHttpClient, OkHttpClient, Response> void weatherForecast(String latitude,String longitude) throws IOException, InterruptedException {
        // Construct API URL with latitude and longitude parameters
        String url = "https://ai-weather-by-meteosource.p.rapidapi.com/daily?lat="+latitude+"&lon="+longitude+"&language=en&units=auto";

        // Create an HTTP request with required headers
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("x-rapidapi-key", "3ec6205bf9msh83861deaecbd4b8p1ad8b7jsnedd27a4f8084")
                .header("x-rapidapi-host", "ai-weather-by-meteosource.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        // Send the request and capture the response
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        //System.out.println(response.body());

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
        ArrayList<String> summarys = new ArrayList<>();

        // Extract all matches
        while (matcher3.find()) {
            summarys.add(matcher3.group(1));  // Group 1 captures the value inside quotes
        }

        // Displaying the extracted data for the first 4 days
        // today and 3 days after
        for(int i = 0;i < 4;i++){
            System.out.println("Date:" + days.get(i)+" Weather condition:"+weathers.get(i)+"\nWeather Description:"+summarys.get(i));
        }
    }

    }
