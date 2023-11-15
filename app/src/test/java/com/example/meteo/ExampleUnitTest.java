package com.example.meteo;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
import android.widget.Button;
import android.widget.Toast;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;


public class MainActivityTest {

    private MainActivity activity;

    @Test
    public void testGetTemperatureFromButton() {
        Button button = new Button(activity);
        button.setText("25.5°");

        double temperature = activity.getTemperatureFromButton(button);

        assertEquals(25.5, temperature, 0.01);
    }

    @Test
    public void testFormatDate() {
        int timestamp = 1636790400; //

        String formattedDate = activity.formatDate(timestamp);
    }

    @Test
    public void testCheckAnswerCorrect() {
        activity.forecastData = new ForecastData();

        double actualTemperature = activity.forecastData.getForecasts().get(0).getMain().getTemperature();

        Button btnAnswer1 = new Button(activity);
        btnAnswer1.setText(String.format("%.1f°", actualTemperature));

        activity.checkAnswer(actualTemperature);

        assertEquals(MainActivity.this, "Correct !", Toast.LENGTH_SHORT).show();
    }

    @Test
    public void testCheckAnswerIncorrect() {
        activity.forecastData = new ForecastData();

        double actualTemperature = activity.forecastData.getForecasts().get(0).getMain().getTemperature();

        Button btnAnswer1 = new Button(activity);
        btnAnswer1.setText(String.format("%.1f°", actualTemperature + 5.0));

        activity.checkAnswer(actualTemperature);

        assertEquals(Toast.makeText(MainActivity.this, "Faux !", Toast.LENGTH_SHORT).show());
    }

}