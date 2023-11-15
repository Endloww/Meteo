package com.example.meteo;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.widget.EditText;
import android.widget.TextView;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class CityTextViewTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testCityUpdateOnSearch() {
        // Obtenez le scenario de l'activité en cours
        ActivityScenario<MainActivity> activityScenario = activityScenarioRule.getScenario();

        // Réalisez vos actions de test ici
        activityScenario.onActivity(activity -> {
            final String newCity = "Paris";
            EditText editText = activity.findViewById(R.id.editTextCity);
            activity.runOnUiThread(() -> editText.setText(newCity));

            activity.findViewById(R.id.btnSearch).performClick();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            TextView textView = activity.findViewById(R.id.textViewCity);
            assertEquals(newCity, textView.getText().toString());
        });
    }
}
