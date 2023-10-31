import org.json.simple.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WeatherAppGui extends JFrame {

    private JSONObject weatherData;

    public WeatherAppGui(){

        //adicionar titulo
        super("Weather Check");

        //fechar ao apertar o x
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //tamanho da janela em pixeis
        setSize(450,650);

        //abrir a janela ao centro da tela, ou seja, em relacao a nenhuma outra janela existente
        setLocationRelativeTo(null);

        //eu é quem colocarei as coisas na tela da gui, logo eu nao quero que o sistema faça alteracoes no layout
        setLayout(null);

        //impossibilitado de mudar as proporcoes
        setResizable(false);

        // Get the content pane and change its background color
        Container contentPane = getContentPane();
        Color myColor = new Color(0xC1C5CE);
        contentPane.setBackground(myColor);

        addGuiComponents();
    }

    private void addGuiComponents(){

        //campo de busca
        JTextField searchTextField = new JTextField();
        searchTextField.setBounds(15,15,351,45);
        searchTextField.setBorder(BorderFactory.createLineBorder(Color.GRAY,2));
        searchTextField.setFont(new Font("Verdana", Font.PLAIN,24));
        add(searchTextField);

        //imagens do clima
        JLabel weatherConditionImage = new JLabel(loadImage("src/assets/cloudy.png"));
        weatherConditionImage.setBounds(0,125,450,217);
        add(weatherConditionImage);

        //temperatura atual (10C, -2C)
        JLabel tempetureText = new JLabel("10");
        tempetureText.setBounds(0,350,450,54);
        tempetureText.setFont(new Font("Verdana",Font.BOLD, 48));
        tempetureText.setHorizontalAlignment(SwingConstants.CENTER); //alinhando o texto de temperatura ao centro da tela
        add(tempetureText);

        //descricao da condicao da imagem (nublado, ensolarado)
        JLabel  weatherConditionDesc = new JLabel("Cloudy");
        weatherConditionDesc.setBounds(0,405,450,36);
        weatherConditionDesc.setFont(new Font("Verdana", Font.PLAIN,32));
        weatherConditionDesc.setHorizontalAlignment(SwingConstants.CENTER);
        add(weatherConditionDesc);

        //imagem de humidade
        JLabel humidityImage = new JLabel(loadImage("src/assets/humidity.png"));
        humidityImage.setBounds(7,490,74,66);
        add(humidityImage);

        //texto de humidade
        JLabel humidityText = new JLabel("<html><b>Humidity </b> 100%</html>"); //voce tambem pode instanciar o texto como sendo de tipo HTML, uma maneira diferente de posiciona -lo;
        humidityText.setBounds(70,500,70,40);
        humidityText.setFont(new Font("Verdana", Font.PLAIN,14));
        add(humidityText);

        //imagem de velocidade do vento
        JLabel windSpeedImage = new JLabel(loadImage("src/assets/windspeed.png"));
        windSpeedImage.setBounds(285,490,74,66);
        add(windSpeedImage);

        //texto de velocidade do vento
        JLabel windSpeedText = new JLabel("<html><b>Wind </b> 15km/h</html>");
        windSpeedText.setBounds(350,490,85,55);
        windSpeedText.setFont(new Font("Verdana", Font.PLAIN, 14));
        add(windSpeedText);

        //texto de probabilidade de precipitacao
        JLabel precipitationText = new JLabel("<html><b>Rain\nProbability </b> 0%</html>");
        precipitationText.setBounds(200,490,85,55);
        precipitationText.setFont(new Font("Verdana", Font.PLAIN,14));
        add(precipitationText);

        //imagem de probabilidade de precipitacao
        JLabel precipitaionImage = new JLabel(loadImage("src/assets/precipitation.png"));
        precipitaionImage.setBounds(140,490,74,66);
        add(precipitaionImage);

        //botao de busca
        JButton searchButton = new JButton(loadImage("src/assets/search.png"));
        searchButton.setBounds(375, 13,47,45);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //pegando a localizacao digitada pelo usuario
                String userInput = searchTextField.getText();

                //certificando que o 'input' é válido (é um texto mesmo)
                if(userInput.replaceAll("\\s","").length() <= 0){
                    return;
                }

                //pegando o clima do local digitado
                weatherData = WeatherAppApi.getWeatherData(userInput);

                //atualizando a GUI com os dados de getWeatherData()

                //atualizar imagem do tempo
                String weatherCondition = (String) weatherData.get("weatherCondition");
                switch(weatherCondition){
                    case "Clear":
                        weatherConditionImage.setIcon(loadImage("src/assets/clear.png"));
                        break;
                    case "Cloudy":
                        weatherConditionImage.setIcon(loadImage("src/assets/cloudy.png"));
                        break;
                    case "Rain":
                        weatherConditionImage.setIcon(loadImage("src/assets/rain.png"));
                        break;
                    case "Snow":
                        weatherConditionImage.setIcon(loadImage("src/assets/snow.png"));
                        break;
                }

                //atualizar texto de temperatura
                double tempeture = (double) weatherData.get("tempeture");
                tempetureText.setText(tempeture + " °C");

                //atualizar texto de condicao do tempo
                weatherConditionDesc.setText(weatherCondition);

                //atualizar texto de humidade
                long humidity = (long) weatherData.get("humidity");
                humidityText.setText("<html><b>Humidity</b> " +  humidity + "%</html>");

                //atualizar texto de velocidade do vento
                double windSpeed = (double) weatherData.get("windSpeed");
                windSpeedText.setText("<html><b>Wind</b> " +  windSpeed + "km/h</html>");

                //atualizar texto de precipitacao
                long precipitation = (long) weatherData.get("precipitation_probability");
                precipitationText.setText("<html><b>Rain\nProbability </b>" + precipitation + "%</html>");

            }
        });
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));//alterando o cursor ao passar pelo botao de busca (virar maozinha)

        add(searchButton);
    }

    //metodo que permite carregar imagens(assets) adicionados de forma simplificada
    private ImageIcon loadImage(String imagePath){
        try{
            //carregar uma imagem a partir de um dado path(caminho explorer)
            BufferedImage image = ImageIO.read(new File(imagePath));
            return new ImageIcon(image);

        }catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("Não foi possível encontrar a imagem solicitada");
        return null;
    }


}