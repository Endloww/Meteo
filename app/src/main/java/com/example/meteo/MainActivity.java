package com.example.meteo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.meteo.databinding.ActivityMainBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.graphics.Color;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    ForecastData forecastData;
    private String currentCity = "Annecy";
    void checkAnswer(double selectedTemperature) {
        double actualTemperature = forecastData.getForecasts().get(0).getMain().getTemperature();
        actualTemperature = Math.round(actualTemperature * 10.0) / 10.0; // Arrondir à un chiffre après la virgule

        Button btnAnswer1 = findViewById(R.id.btnAnswer1);
        Button btnAnswer2 = findViewById(R.id.btnAnswer2);
        Button btnAnswer3 = findViewById(R.id.btnAnswer3);

        int colorCorrect = ContextCompat.getColor(this, R.color.colorCorrectAnswer);
        int colorIncorrect = ContextCompat.getColor(this, R.color.colorIncorrectAnswer);

        if (selectedTemperature == actualTemperature) {
            // Réponse correcte
            btnAnswer1.setBackgroundColor(selectedTemperature == getTemperatureFromButton(btnAnswer1) ? colorCorrect : colorIncorrect);
            btnAnswer2.setBackgroundColor(selectedTemperature == getTemperatureFromButton(btnAnswer2) ? colorCorrect : colorIncorrect);
            btnAnswer3.setBackgroundColor(selectedTemperature == getTemperatureFromButton(btnAnswer3) ? colorCorrect : colorIncorrect);

            Toast.makeText(MainActivity.this, "Correct !", Toast.LENGTH_SHORT).show();
        } else {
            // Réponse incorrecte
            btnAnswer1.setBackgroundColor(getTemperatureFromButton(btnAnswer1) == actualTemperature ? colorCorrect : colorIncorrect);
            btnAnswer2.setBackgroundColor(getTemperatureFromButton(btnAnswer2) == actualTemperature ? colorCorrect : colorIncorrect);
            btnAnswer3.setBackgroundColor(getTemperatureFromButton(btnAnswer3) == actualTemperature ? colorCorrect : colorIncorrect);

            Toast.makeText(MainActivity.this, "Faux !", Toast.LENGTH_SHORT).show();
        }
    }

    double getTemperatureFromButton(Button button) {
        String buttonText = button.getText().toString();

        try {
            double temperature = Double.parseDouble(buttonText.replace("°", "").trim());
            return temperature;
        } catch (NumberFormatException e) {
            return -1.0;
        }
    }
    void resetButtonColors() {
        Button btnAnswer1 = findViewById(R.id.btnAnswer1);
        Button btnAnswer2 = findViewById(R.id.btnAnswer2);
        Button btnAnswer3 = findViewById(R.id.btnAnswer3);

        int colorDefault = ContextCompat.getColor(this, R.color.colorDefault);

        btnAnswer1.setBackgroundColor(colorDefault);
        btnAnswer2.setBackgroundColor(colorDefault);
        btnAnswer3.setBackgroundColor(colorDefault);
    }

    private void searchTemperatureForCity(String cityName) {
        resetButtonColors(); // Réinitialiser les couleurs des boutons
        OpenWeatherServices service = RetrofitClientInstance.getRetrofitInstance().create(OpenWeatherServices.class);
        Call<ForecastData> call = service.getForecastForCity(cityName, "e075a0b59517e88cc46940bb262add13","metric"); // Remplacez "your_api_key" par votre clé API OpenWeather

        call.enqueue(new Callback<ForecastData>() {
            @Override
            public void onResponse(Call<ForecastData> call, Response<ForecastData> response) {
                if (response.isSuccessful()) {
                    forecastData = response.body();
                    if (forecastData != null && forecastData.getForecasts() != null && forecastData.getForecasts().size() > 0) {
                        currentCity = forecastData.getCity().getName(); // Mettre à jour la ville courante
                        binding.textViewCity.setText(currentCity); // Mettre à jour le texte de la ville
                        binding.textViewTemp.setText(String.valueOf(forecastData.getForecasts().get(0).getMain().getTemperature()) + "°");
                        binding.textViewQuestion.setText("Quelle est la température à ");
                        updateAnswerButtons();
                    } else {
                        Toast.makeText(MainActivity.this, "Aucune donnée météo disponible pour cette ville.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Erreur lors de la récupération des données météo.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ForecastData> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Une erreur est survenue lors de la récupération des données météo.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void updateAnswerButtons() {
        double actualTemperature = forecastData.getForecasts().get(0).getMain().getTemperature();
        double[] possibleAnswers = shuffleAnswers(actualTemperature);
        Button btnAnswer1 = findViewById(R.id.btnAnswer1);
        Button btnAnswer2 = findViewById(R.id.btnAnswer2);
        Button btnAnswer3 = findViewById(R.id.btnAnswer3);

        // Formater les réponses pour afficher un seul chiffre après la virgule
        String answer1Text = String.format("%.1f°", possibleAnswers[0]);
        String answer2Text = String.format("%.1f°", possibleAnswers[1]);
        String answer3Text = String.format("%.1f°", possibleAnswers[2]);

        btnAnswer1.setText(answer1Text);
        btnAnswer2.setText(answer2Text);
        btnAnswer3.setText(answer3Text);
    }


    private double[] shuffleAnswers(double actualTemperature) {
        double[] answers = new double[3];
        int correctPosition = (int) (Math.random() * 3);
        answers[correctPosition] = actualTemperature;

        for (int i = 0; i < 3; i++) {
            if (i != correctPosition) {
                double incorrectTemperature;
                do {
                    incorrectTemperature = actualTemperature + (Math.random() * 10 - 5);
                } while (incorrectTemperature == actualTemperature);

                answers[i] = incorrectTemperature;
            }
        }

        return answers;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Button btnSearch = findViewById(R.id.btnSearch);
        final EditText editTextCity = findViewById(R.id.editTextCity);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userInputCity = editTextCity.getText().toString();
                if (!userInputCity.isEmpty()) {
                    // Mettez à jour la ville actuelle avec la nouvelle ville saisie
                    currentCity = userInputCity;
                    searchTemperatureForCity(userInputCity);
                } else {
                    Toast.makeText(MainActivity.this, "Veuillez entrer une ville", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnAnswer1 = findViewById(R.id.btnAnswer1);
        Button btnAnswer2 = findViewById(R.id.btnAnswer2);
        Button btnAnswer3 = findViewById(R.id.btnAnswer3);

        btnAnswer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double temperature = getTemperatureFromButton(btnAnswer1);
                checkAnswer(temperature);
            }
        });

        btnAnswer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double temperature = getTemperatureFromButton(btnAnswer2);
                checkAnswer(temperature);
            }
        });

        btnAnswer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double temperature = getTemperatureFromButton(btnAnswer3);
                checkAnswer(temperature);
            }
        });

        Button btnShowForecast = findViewById(R.id.btnShowForecast);
        btnShowForecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Appeler la méthode pour afficher les prévisions des 5 prochains jours
                showFiveDayForecast(currentCity);
            }
        });

        OpenWeatherServices service = RetrofitClientInstance.getRetrofitInstance().create(OpenWeatherServices.class);
        Call<ForecastData> call = service.getForcast();
        call.enqueue(new Callback<ForecastData>() {
            @Override
            public void onResponse(Call<ForecastData> call, Response<ForecastData> response) {
                forecastData = response.body();
                binding.textViewCity.setText(forecastData.getCity().getName());
                binding.textViewTemp.setText(String.valueOf(forecastData.getForecasts().get(0).getMain().getTemperature()) + "°");
                binding.textViewQuestion.setText("Quelle est la température à ");
                updateAnswerButtons();
            }

            @Override
            public void onFailure(Call<ForecastData> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Une erreur est survenue !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Ajoutez la méthode pour afficher les prévisions des 5 prochains jours
    private void showFiveDayForecast(String cityName) {
        OpenWeatherServices service = RetrofitClientInstance.getRetrofitInstance().create(OpenWeatherServices.class);
        Call<ForecastData> call = service.getFiveDayForecast(cityName, "e075a0b59517e88cc46940bb262add13", "metric");

        call.enqueue(new Callback<ForecastData>() {
            @Override
            public void onResponse(Call<ForecastData> call, Response<ForecastData> response) {
                if (response.isSuccessful()) {
                    ForecastData forecastList = response.body();
                    if (forecastList != null && forecastList.getForecasts() != null && forecastList.getForecasts().size() >= 5) {
                        // Afficher les températures minimales et maximales des 5 prochains jours
                        StringBuilder forecastText = new StringBuilder("Prévisions pour les 5 prochains jours :\n");
                        for (int i = 0; i < 5; i++) {
                            Forecast forecast = forecastList.getForecasts().get(i);
                            double minTemp = forecast.getMain().getTempMin();
                            double maxTemp = forecast.getMain().getTempMax();
                            String date = formatDate(forecast.getDatetime());
                            forecastText.append(date).append(": ").append("Min ").append(minTemp).append("°C, Max ").append(maxTemp).append("°C\n");
                        }

                        // Afficher le texte dans votre vue appropriée (TextView ou Toast, par exemple)
                        Toast.makeText(MainActivity.this, forecastText.toString(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Aucune donnée de prévision disponible.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Erreur lors de la récupération des prévisions.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ForecastData> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Une erreur est survenue lors de la récupération des prévisions.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Ajoutez la méthode pour formater la date (à adapter selon le format de date reçu)
    String formatDate(int timestamp) {
        // À adapter selon le format de date reçu
        // Pour l'exemple, renvoie une chaîne vide
        return "";
    }
}


