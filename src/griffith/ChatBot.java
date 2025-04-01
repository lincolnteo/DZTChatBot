
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ChatBot {
	public static void main(String[] args) {

		// Derrick Longkai Zhang 3133272
		// my task is to input a city name from user, then get the location's: latitude and longitude with Location API
		// using try() and catch() to handle necessary exceptions.
		try {

			// Open scanner to let user input a city
			Scanner scanner = new Scanner(System.in);

			// initiate the city variable
			String city = "";
			// using do while loop to let user quit if input: No
			do {
				// asking user to input a city name or input "No" to exit
				System.out.println("-----------------------------------------------");
				System.out.print("Please enter a city (or enter \"No\" to exit): ");

				// getting the inputted data
				city = scanner.next();
				// if inputted data == 'No' then print out a message and quit the loop
				if (city.equalsIgnoreCase("No")) {
					System.out.println("You have quited");
					break;
				}

				// Get location data
				// create JsonObject to get the Location
				JSONObject cityLocationData = getLocationData(city);

				// invalid location means we should not continue further
				if (cityLocationData == null) {
					System.out.println("Location \"" + city + "\" was not found, please ensure the spelling is correct and re-enter your selection.");
					continue;
				}

				// get the latitude of the city
				double latitude = (double) cityLocationData.get("latitude");
				// get the longitude of the city
				double longitude = (double) cityLocationData.get("longitude");

				// print out the city's name and location:
				System.out.println("City: " + city);
				System.out.println("Latitude: " + latitude);
				System.out.println("Longitude: " + longitude);
				// implementation of displayWeatherData() method
				displayWeatherData(latitude, longitude);

				// Derrick Longkai Zhang 3133272
				// add weather forecast
				// convert double latitude and longitude to String
				String latitude1 = Double.toString(latitude);
				String longitude1 = Double.toString(longitude);
				// test weatherForecast class
				// create an instance of WeatherForecast class
				WeatherForecast cityOneForecast = new WeatherForecast();
				// call the weatherForecast() method in the class to display today, and 3 days after's weather forecast
				System.out.println("Weather Forecast:");
				cityOneForecast.weatherForecast(latitude1,longitude1);

				// while condition: city is not equal to "No"
			} while (!city.equalsIgnoreCase("No"));
			// catch potential Exception
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Derrick Longkai Zhang 3133272
	// method getLocationData()
	// type of the method is JSONObject with one argument: String city
	public static JSONObject getLocationData(String city) {
		// regex expression to trim the city format
		city = city.replaceAll(" ", "+");
		// getting the json city url and store it as a string
		String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" +
				city + "&count=1&language=en&format=json";

		try {
			// step1. Fetch the API response based on API Link
			HttpURLConnection apiConnection = fetchApiResponse(urlString);

			// check for response status
			// 200 - means that the connection was a success
			if (apiConnection.getResponseCode() != 200) {
				System.out.println("Error: Could not connect to geography API.");
				return null;
			}

			// step2. Read the response and convert store String type
			String jsonResponse = readApiResponse(apiConnection);

			// step3. Parse the string into a JSON Object
			JSONParser parser = new JSONParser();
			JSONObject resultsJsonObj = (JSONObject) parser.parse(jsonResponse);

			// step4. Retrieve Location Data
			JSONArray locationData = (JSONArray) resultsJsonObj.get("results");

			// ensure results have been found before returning them
			if (locationData == null) {
				System.out.println("Error: No results were found for the city query \"" + city + "\"");
				return null;
			}

			return (JSONObject) locationData.get(0);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Derrick Longkai Zhang 3133272
	//fetchApiResponse() method with String argument: the jason city url
	private static HttpURLConnection fetchApiResponse(String urlString) {
		try {
			// attempt to connect to the API
			URL url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			// set request method to get data from the API url
			connection.setRequestMethod("GET");
			// return the connection condition
			return connection;
		} catch (IOException e) {
			e.printStackTrace();
		}

		// connection fail
		return null;
	}

	// Derrick Longkai Zhang 3133272
	// method readApiResponse with a HttpURLConnection argument apiConnection
	private static String readApiResponse(HttpURLConnection apiConn) {
		try {
			// Create a StringBuilder to store the resulting JSON data
			StringBuilder result = new StringBuilder();

			// Use a Scanner to read the InputStream from the HttpURLConnection
			Scanner scanner = new Scanner(apiConn.getInputStream());

			// Loop through each line in the response and append it to the StringBuilder
			while (scanner.hasNext()) {
				// Append the current line to the StringBuilder
				result.append(scanner.nextLine());
			}
			// close scanner
			scanner.close();

			// Return the JSON data as a String
			return result.toString();

		} catch (IOException e) {
			e.printStackTrace();
		}
		// Return null if there was an issue reading the response
		return null;
	}

	// Lincoln Teo Ming Kern 3122215
	// method displayWeatherData() with two arguments: double latitude, double longitude
	private static void displayWeatherData(double latitude, double longitude) {
		try {
			//Get the API response from the OpenWeatherMap API
			// with two arguments: latitude and longitude to input the corresponding coordinates into the API link
			String url = "https://api.openweathermap.org/data/2.5/forecast?lat=" + latitude + "&lon=" + longitude + "&units=metric&appid=ecfb062d63ee7dbdea0650c7d7f89eb4";
			HttpURLConnection apiConnection = fetchApiResponse(url);

			// check for response status code
			// 200 - means that the request was successful
			if (apiConnection.getResponseCode() != 200) {
				System.out.println("Error: Could not connect to API Weather");
				return;
			}

			// Read the response and convert to String type
			String jsonResponse = readApiResponse(apiConnection);

			// Parse the string into a JSON Object
			JSONParser parser = new JSONParser();
			JSONObject jsonObject = (JSONObject) parser.parse(jsonResponse);
			JSONArray list = (JSONArray) jsonObject.get("list");

			// Store the data into variables
			if (list != null && !list.isEmpty()) {
				//retrieves the first element from the JSON array list
				JSONObject firstEntry = (JSONObject) list.get(0);
				// extracts the main JSON object from the firstEntry JSON object
				JSONObject main = (JSONObject) firstEntry.get("main");

				// get current temp from the Json Object named main
				double temperature = (double) main.get("temp");
				System.out.println("Current Temperature (C): " + temperature);

				// get minimum temp from the Json Object named main
				double tempMin = (double) main.get("temp_min");
				System.out.println("Minimum Temperature (C): " + tempMin);

				// get max temp from the Json Object named main
				double tempMax = (double) main.get("temp_max");
				System.out.println("Maximum Temperature (C): " + tempMax);

				// get humidity from the Json Object named main
				long humidity = (long) main.get("humidity");
				System.out.println("Humidity: " + humidity);

				//code extracts the wind JSON object from the firstEntry:
				JSONObject wind = (JSONObject) firstEntry.get("wind");
				// get wind speed from the Json Object named wind
				double windSpeed = (double) wind.get("speed");
				System.out.println("Wind Speed (Km/H): " + windSpeed);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}