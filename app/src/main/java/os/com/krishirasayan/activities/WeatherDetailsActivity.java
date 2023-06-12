package os.com.krishirasayan.activities;

import static os.com.krishirasayan.consts.Helper.bindToolbar;
import static os.com.krishirasayan.consts.Helper.w;
import static os.com.krishirasayan.consts.Url.API_WEATHER_FORECAST;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import os.com.krishirasayan.R;
import os.com.krishirasayan.classes.VolleyService;
import os.com.krishirasayan.interfaces.IVolleyResult;

public class WeatherDetailsActivity extends AppCompatActivity {
    Context context = this;
    final int ACTIVITY_TITLE_STRING_ID = R.string.weather;

    TextView tvLocation, tvTempValue, tvTempUnit, tvWeatherBrief, tvRainfall, tvHumidityMax, tvHumidityMin, tvWindSpeed, tvWindDirection, tvWeatherDescription;
    TextView tvForecastToday, tvForecastTodayTempRange, tvForecastTomorrow, tvForecastTomorrowTempRange, tvForecastDayAfterTomorrow, tvForecastDayAfterTomorrowTempRange;

    Toolbar toolbar;
    TextView toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_details);
        bindToolbar(context,R.string.weather);
        setup();
    }

    private void setup() {
        context = this;

        tvLocation = findViewById(R.id.tvLocation);
        tvTempValue = findViewById(R.id.tvTempValue);
        tvTempUnit = findViewById(R.id.tvTempUnit);
        tvWeatherBrief = findViewById(R.id.tvWeatherBrief);
        tvRainfall = findViewById(R.id.tvRainfall);
        tvHumidityMax = findViewById(R.id.tvHumidityMax);
        tvHumidityMin = findViewById(R.id.tvHumidityMin);
        tvWindSpeed = findViewById(R.id.tvWindSpeed);
        tvWindDirection = findViewById(R.id.tvWindDirection);
        tvWeatherDescription = findViewById(R.id.tvWeatherDescription);
        tvForecastToday = findViewById(R.id.tvForecastToday);
        tvForecastTodayTempRange = findViewById(R.id.tvForecastTodayTempRange);
        tvForecastTomorrow = findViewById(R.id.tvForecastTomorrow);
        tvForecastTomorrowTempRange = findViewById(R.id.tvForecastTomorrowTempRange);
        tvForecastDayAfterTomorrow = findViewById(R.id.tvForecastDayAfterTomorrow);
        tvForecastDayAfterTomorrowTempRange = findViewById(R.id.tvForecastDayAfterTomorrowTempRange);

        downloadWeatherReport();
    }

    private void downloadWeatherReport() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("location", "");

        new VolleyService(context).get(API_WEATHER_FORECAST, params, new IVolleyResult() {
            @Override
            public void notifyRequestQueued(String requestName) {
                tvLocation.setText(R.string.please_wait);
            }

            @Override
            public void notifySuccess(String requestName, JSONObject response) {
                setWeatherReport(response);
            }

            @Override
            public void notifySuccess(String requestName, JSONArray response) {

            }

            @Override
            public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                Toast.makeText(context, R.string.login_failed, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setWeatherReport(JSONObject response) {
        try {
            JSONObject report = response.getJSONObject("compiled").getJSONObject("report");
            JSONArray forecast = response.getJSONObject("compiled").getJSONArray("forecast");

            w(report.getString("temp"));
            tvLocation.setText(report.getString("location"));
            tvTempValue.setText(report.getString("temp"));
            tvTempUnit.setText(report.getString("temp_unit"));
            tvWeatherBrief.setText(report.getString("weather"));
            tvWeatherDescription.setText(report.getString("weather_description"));
            tvHumidityMax.setText(report.getString("humidity_max_display"));
            tvHumidityMin.setText(report.getString("humidity_min_display"));
            tvWindSpeed.setText(report.getString("wind_speed"));
            tvWindDirection.setText(report.getString("wind_direction_display"));
            tvRainfall.setText(report.getString("rainfall"));

            tvForecastToday.setText(forecast.getJSONObject(0).getString("display"));
            tvForecastTodayTempRange.setText(forecast.getJSONObject(0).getString("temp_range"));
            tvForecastTomorrow.setText(forecast.getJSONObject(1).getString("display"));
            tvForecastTomorrowTempRange.setText(forecast.getJSONObject(1).getString("temp_range"));
            tvForecastDayAfterTomorrow.setText(forecast.getJSONObject(2).getString("display"));
            tvForecastDayAfterTomorrowTempRange.setText(forecast.getJSONObject(2).getString("temp_range"));
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
