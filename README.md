<h1>WeatherCheck App </h1>
<h2>Introduction</h2>
<p>
    The Weather App is a simple Java-based application that provides users with real-time weather information given a location. It fetches weather data from an external API and displays it in a graphical user interface (GUI). Users can enter a location, and the app retrieves and 
    presents weather details, including temperature, weather condition, humidity, and wind speed.
</p>

</p>

<p align="center">
    <img src="https://github.com/murilo-l1/WeatherApp/blob/main/WeatherAppGUI/WeatherCheckScreenshot.png" align="center">
</p>

<h2>Technologies Used</h2>
<p>
    The Weather App utilizes the following technologies and libraries:
</p>
<ul>
  <li>Java 18</li>
  <li><a href="https://code.google.com/archive/p/json-simple/downloads">JSON Simple</a> - Used to parse and read through JSON data</li>
  <li><a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.net/java/net/HttpURLConnection.html">HTTPURLConnection</a>: Java's built-in library for making HTTP requests to fetch data from external APIs.</li>
</ul>

<h2>Class Summaries</h2>

<h3>1.0. AppLauncher </h3>
<p>
    <strong>Description:</strong> The AppLauncher class serves as the initializer for the GUI using the 'invokelater()' to initialize the thread and display the window generated in 'WeatherAppGui'.
</p>

<h3>2.0. WeatherAppGui </h3>
<p>
    <strong>Description:</strong> The WeatherAppGui class represents the graphical user interface (GUI) of the Weather App. It is responsible for displaying weather information for the given location.
</p>
<p>
    <strong>Summary:</strong> This class handles the layout and display of GUI components, including the text field, labels, search button, icons and the image of the weather. It also implements the user interface for entering a location and updating the weather information based on user input. 'Frontend'
</p>

<h3>3.0. WeatherAppApi </h3>
<p>
    <strong>Description:</strong> The WeatherAppApi class handles the API calls used, using the first call to retrieve geocordinates from the inputted location and the second to get content that is going to be displayed on screen using the geocordinates retrived from
  the first call.
</p>
<p>
    <strong>Summary:</strong> This class encapsulates the core functionality of the Weather App. Including methods that can call and retrieve information from the API's, fetch a connection to declare the requests to create a JSONObject that can be easily accessed in the GUI class. 'Backend'
</p>
