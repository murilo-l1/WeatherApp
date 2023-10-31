// chamado para as nossas APIs, que contém as informações em tempo real do clima de uma dada localização,
// backend;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.IOException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class WeatherAppApi {

    public static JSONObject getWeatherData(String locationName){
        //usando a geoLocation API, pegamos as coordenadas (latitude e longitude) dado um local inserido na função.
        JSONArray locationData = getLocationData(locationName);

        //extraindo latitude e longitude da JSONArray retornada por getLocationData();
        JSONObject location = (JSONObject) locationData.get(0);
        double latitude = (double) location.get("latitude");
        double longitude = (double) location.get("longitude");

        //construindo a url que chama a API usando a latitude e longitude que extraimos
        String urlString = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude + "&longitude=" + longitude + "2&hourly=temperature_2m,relativehumidity_2m,precipitation_probability,weathercode,windspeed_10m&timezone=America%2FSao_Paulo";

        try{
            //chama a API e estabelece conexao
            HttpURLConnection connection = fetchApiResponse(urlString);

            //checando status de conexao - 200 == OK
            if(connection.getResponseCode() != 200){
                System.out.println("Erro: não foi possível estabelecer conexão com a API de clima");
                return null;
            }

            //construindo o meu resultJson provindo da API de clima
            StringBuilder resultJson = new StringBuilder();
            Scanner scanner = new Scanner(connection.getInputStream());
            while(scanner.hasNext()){
                //ler e armazenar no resultJson
                resultJson.append(scanner.nextLine());
            }
            scanner.close();
            connection.disconnect();

            //transformando a 'string' num tipo de dado possivel de ser armazenado pela JSONArray
            JSONParser parser = new JSONParser();
            JSONObject resultJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));

            //receber os dados sobre o tempo dado um horario, esses são encontrados entre os dados na aba 'hourly'
            JSONObject hourly = (JSONObject) resultJsonObj.get("hourly");

            //queremos os dados no tempo em que está a ser feita a busca, para isso precisamos pegar os tempos e armazena - los em uma JSONArray
            JSONArray time = (JSONArray) hourly.get("time");

            //posicao (index) do tempo atual do sistema na timeList da API
            int indexOfTime = findIndexOfCurrentTime(time);

            //pegando a temperatura na hora atual
            JSONArray tempetureData = (JSONArray) hourly.get("temperature_2m");
            double tempeture = (double) tempetureData.get(indexOfTime);

            //pegando o codigo de clima e convertendo - o
            JSONArray weatherCode = (JSONArray) hourly.get("weathercode");
            // convertendo o weatherCode para uma coisa mais fácil de compreender
            String weatherCondition = convertWeatherCode((long) weatherCode.get(indexOfTime));

            // pegando a humidade
            JSONArray relativeHumidity = (JSONArray) hourly.get("relativehumidity_2m");
            long humidity = (long) relativeHumidity.get(indexOfTime);

            //pegando a velocidade do vento
            JSONArray windSpeedData = (JSONArray) hourly.get("windspeed_10m");
            double windSpeed = (double) windSpeedData.get(indexOfTime);

            //pegando a probabilidade de chuva
            JSONArray rainprobData = (JSONArray) hourly.get("precipitation_probability");
            long rain_probability = (long) rainprobData.get(indexOfTime);

            //criando agora o meu JSONOBject para ser facilmente acessado pelo front (GUI)
            JSONObject weatherData = new JSONObject();
            weatherData.put("tempeture", tempeture);
            weatherData.put("weatherCondition", weatherCondition);
            weatherData.put("humidity", humidity);
            weatherData.put("windSpeed", windSpeed);
            weatherData.put("precipitation_probability", rain_probability);

            return weatherData;

        }catch (Exception e){
            e.printStackTrace();
        }

        // se algum dos processos de busca falhar eu nao retorno o JSONObject, mas sim, NULL
        return null;
    }

    //retorna coordenadas geograficas, dada uma localizacao qualquer
    private static JSONArray getLocationData(String locationName){

        // se a localizacao digitada pelo usuario conter espacos em branco, mude isso para +, para que assim o link seja lido corretamente;
        locationName = locationName.replaceAll(" ","+");

        //esse link contem a coordenada geografica de qualquer local digitado, entao fazemos uma sutil alteração no link para que ele receba a locationName;
        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" + locationName + "&count=10&language=en&format=json";

        try{
            //chama a API e pega a resposta dada, se possivel
            HttpURLConnection connection = fetchApiResponse(urlString);

            //quando a gente chama uma conexao da internet, essa pode ser classificado em diferentes status,
            //sendo 200 a reposta que a gente deseja para dizer que uma boa conexao foi estabelecida;
            if(connection.getResponseCode() != 200){
                System.out.println("Erro: não foi possível estabelecer conexão com a API de geolocalização");
                return null;
            }

            //se foi possivel realizar conexão, armazenaremos os resultados da API aqui;
            StringBuilder resultJson = new StringBuilder();

            //ler o arquivo Json gerado, pós conexao corretamente estabelecida;
            Scanner scanner = new Scanner(connection.getInputStream());

            //enquanto houver coisa pra ser lida 'hasNext', a gente vai armazenando tudo no resultJson
            while(scanner.hasNext()) {
                    resultJson.append(scanner.nextLine());
            }

            //fecha o scanner
            scanner.close();

            //desabilita a conexão, pois agora os dados estão inseridos em resultJson
            connection.disconnect();

            //transformando a JsonString em JsonObject, pois JSONArray recebe JSONObjects
            JSONParser parser = new JSONParser();
            JSONObject resultJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));

            //pegando todos os dados gerados pela API da localização desejada, esses são encontrados na aba 'results'
            JSONArray locationData = (JSONArray) resultJsonObj.get("results");

            return locationData;

        }catch (Exception e){
            e.printStackTrace();
        }

        //nao foi possivel encontrar a localizacao
        return null;
    }

    // pega o link ja feito com a localizacao desejada e tenta estabelecer uma conexao de fato.
    private static HttpURLConnection fetchApiResponse(String urlString){
        try{
            //tenta estabelecer uma conexao com o servidor
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            //a gente pode pedir para estabelecer conexões de diferentes modos quando se trata de um chamado a API, aqui usarei GET
            connection.setRequestMethod("GET");

            //conectando a nossa API
            connection.connect();

            return connection;

        }catch (IOException e){
            e.printStackTrace();
        }

        //se nao estabeleceu a conexao, retorna null;
        return null;
    }

    //com o horario atual retornado por getCurrentTime(), eu vou iterar sobre a list devolvida pela API para encontrar o seu index na JSONArray
    private static int findIndexOfCurrentTime(JSONArray timeList){
        String currentTime = getCurrentTime();

        //iterando na lista de tempos para encontrar qual bate com o nosso horario atual
        for(int i = 0; i < timeList.size(); i++){
            String time = (String) timeList.get(i);
            if(time.equalsIgnoreCase(currentTime) ){
                return i;
            }
        }
        return 0;
    }

    //metodo que define data e hora do sistema e o formata para modo de leitura da API
    public static String getCurrentTime(){
        //pegar a data e hora atual quando a busca esta sendo realizada, usando uma funcao que pega esses valores do sistema
        LocalDateTime currentDateTime = LocalDateTime.now();

        //passando dado para ANO-MES-DIAT00:00 - ex: (2023-10-24T04:44), pois é assim que a API fornece e lê os dados;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");

        String formattedDateTime = currentDateTime.format(formatter);

        return formattedDateTime;
    }

    //conversao de weathercode para algo mais legivel, categorizando os codigos de clima
    private static String convertWeatherCode(long weatherCode){
        String weatherCondition = "";

        if(weatherCode == 0L){
            weatherCondition = "Clear";
        } else if (weatherCode <= 3L && weatherCode > 0L) {
            weatherCondition = "Cloudy";
        } else if ((weatherCode >= 51L && weatherCode <= 67L) || (weatherCode >= 80L && weatherCode <= 99L)) {
            weatherCondition = "Rain";
        } else if (weatherCode>= 71L && weatherCode <= 77L) {
            weatherCondition = "Snow";
        }

        return weatherCondition;
    }

}