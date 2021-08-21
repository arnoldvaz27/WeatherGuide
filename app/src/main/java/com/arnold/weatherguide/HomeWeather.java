package com.arnold.weatherguide;

import static com.arnold.weatherguide.CustomToast.showToast;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.arnold.simplify.Primitives;
import com.arnold.weatherguide.databinding.HomeWeatherBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint({"UseCompatLoadingForColorStateLists","UseCompatLoadingForDrawables","DefaultLocale","SetTextI18n",})
public class HomeWeather extends AppCompatActivity {

    //declaration of fields
    private HomeWeatherBinding binding;
    private JSONObject find;
    private String pincode, name, url;
    private int category;
    private ImageView backgroundForm;
    private TextView city, PincodeTxt,location;
    private EditText cityName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.black));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        binding = DataBindingUtil.setContentView(this, R.layout.home_weather); // binding the xml layout to the java file


        //initialization of fields
        backgroundForm = binding.backgroundForm;
        city = binding.city;
        PincodeTxt = binding.pincode;
        cityName = binding.cityName;
        location = binding.location;

        //local storage for storing data
        SharedPreferences settingLayoutType = getSharedPreferences("User Profile", MODE_PRIVATE);
        pincode = settingLayoutType.getString("Pincode", null);
        location.setText(pincode);
        searchPin();

        //listeners
        binding.menu.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(getApplicationContext(), binding.menu);
            //Inflating the Popup using xml file
            popup.getMenuInflater().inflate(R.menu.menu_file, popup.getMenu());

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.profile) {
                    startActivity(new Intent(getApplicationContext(), UserProfile.class));
                }
                if (item.getItemId() == R.id.logout) {
                    final SharedPreferences DetailPref = getSharedPreferences("Details", MODE_PRIVATE);
                    final SharedPreferences.Editor DetailEdit = DetailPref.edit();
                    DetailEdit.putString("user registered", "1");
                    DetailEdit.putString("user logout", "1");
                    DetailEdit.apply();
                    startActivity(new Intent(getApplicationContext(), Logout.class));
                    finishAffinity();
                }
                return true;
            });

            popup.show();
        });

        city.setOnClickListener(v -> {
            PincodeTxt.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.round_style));
            PincodeTxt.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.white));
            city.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.round_style));
            city.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.teal_700));
            binding.searchCity.setVisibility(View.VISIBLE);
            cityName.setSelection(cityName.getText().length());
            cityName.requestFocus();
        });
        PincodeTxt.setOnClickListener(v -> {
            city.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.round_style));
            city.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.white));
            PincodeTxt.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.round_style));
            PincodeTxt.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.teal_700));
            binding.searchCity.setVisibility(View.GONE);
            cityName.setText("");
            location.setText(pincode);
            searchPin();
        });
        binding.search.setOnClickListener(v -> {
            name = cityName.getText().toString();
            if (name.equals("")) {
                showToast(getApplicationContext(), "Please enter the city name", R.color.red);
            } else {
                location.setText(name);
                searchCity();
            }
        });
        binding.reload.setOnClickListener(v -> {
            if (category == 0) {
                url = "https://api.weatherbit.io/v2.0/current?&postal_code=" + pincode + "&country=IN&key=3774962fbde44fb2bd6cc69cbe4fa2ee"; //url of the weather bit api
                Search();
            } else if (category == 1) {
                url = "https://api.weatherbit.io/v2.0/current?&city=" + name + "&country=IN&key=3774962fbde44fb2bd6cc69cbe4fa2ee";
                Search();
            }
        });
    }

    //method that contains url for searching city
    private void searchCity() {
        url = "https://api.weatherbit.io/v2.0/current?&city=" + name + "&country=IN&key=3774962fbde44fb2bd6cc69cbe4fa2ee";
        category = 1;
        Search();
    }

    //method that contains url for searching pincode
    private void searchPin() {
        url = "https://api.weatherbit.io/v2.0/current?&postal_code=" + pincode + "&country=IN&key=3774962fbde44fb2bd6cc69cbe4fa2ee"; //url of the api and the region = asia
        category = 0;
        Search();
    }

    private void Search() {

        //Rest api converted into json object and json array to get the data and display it in the ui fields
        binding.progressCircular.setVisibility(View.VISIBLE);
        binding.reload.setVisibility(View.GONE);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                JSONArray details = response.getJSONArray("data");
                find = details.getJSONObject(0);
                char tmp = 0x00B0;
                String temp = find.getString("temp");
                String cloud = find.getString("clouds");
                String windSpeed = find.getString("wind_spd");
                String windDirection = find.getString("wind_cdir_full");
                String pressure = find.getString("pres");
                String longitude = find.getString("lon");
                String latitude = find.getString("lat");
                String aqi = find.getString("aqi");
                String humid = find.getString("rh");
                String vis = find.getString("vis");
                String seaLevel = find.getString("slp");
                String uv = find.getString("uv");
                String dew = find.getString("dewpt");
                String sunset = find.getString("sunset");
                String snow = find.getString("snow");
                String heat = find.getString("app_temp");
                JSONObject movies = find.getJSONObject("weather");
                String desc = movies.getString("description");
                binding.description.setText(desc);
                float f = Primitives.CentigradeToFahrenheit(Float.parseFloat(temp));
                float temperature = Float.parseFloat(temp);
                float windFloat = Float.parseFloat(windSpeed);
                String winds = String.format("%.5f", windFloat);
                String fahrenheit = String.format("%.1f", f);
                String celcius = String.format("%.1f", temperature);
                binding.Centigrade.setText(celcius + tmp + "C");
                binding.Fahrenheit.setText(fahrenheit + tmp + "F");
                binding.windSpeed.setText(winds + " m/s");
                binding.windDirection.setText(windDirection);
                binding.pressure.setText(pressure + " mb");
                binding.longitude.setText(longitude + " tmp");
                binding.longitude.setText(longitude + tmp);
                binding.latitude.setText(latitude + tmp);
                binding.latitude.setText(latitude + tmp);
                binding.airQuality.setText(aqi);
                binding.humidity.setText(humid + " %");
                binding.clouds.setText(cloud + " %");
                binding.visibility.setText(vis + " Km");
                binding.seaLevel.setText(seaLevel + " mb");
                binding.dew.setText(dew + tmp + "C");
                binding.rays.setText(uv);
                binding.snow.setText(snow + " mm/hr");
                binding.sunset.setText(sunset);
                binding.heat.setText(heat + tmp + "C");
                if (desc.startsWith("Thunderstorm")) {
                    backgroundForm.setImageDrawable(getApplicationContext().getDrawable(R.drawable.thunderstorm));
                } else if (desc.contains("Drizzle")) {
                    backgroundForm.setImageDrawable(getApplicationContext().getDrawable(R.drawable.drizzle));
                } else if (desc.endsWith("Rain") || desc.endsWith("rain")) {
                    backgroundForm.setImageDrawable(getApplicationContext().getDrawable(R.drawable.rain));
                } else if (desc.endsWith("Clouds") || desc.endsWith("clouds")) {
                    backgroundForm.setImageDrawable(getApplicationContext().getDrawable(R.drawable.clouds));
                } else if (desc.contains("Clear sky")) {
                    backgroundForm.setImageDrawable(getApplicationContext().getDrawable(R.drawable.sunny));
                } else if (desc.contains("Flurries") || desc.contains("Sleet")) {
                    backgroundForm.setImageDrawable(getApplicationContext().getDrawable(R.drawable.snow));
                } else if (desc.contains("Mist") || desc.contains("Smoke") || desc.contains("Haze")) {
                    backgroundForm.setImageDrawable(getApplicationContext().getDrawable(R.drawable.msh));
                } else if (desc.contains("Sand") || desc.contains("Fog")) {
                    backgroundForm.setImageDrawable(getApplicationContext().getDrawable(R.drawable.fog));
                } else {
                    backgroundForm.setImageDrawable(getApplicationContext().getDrawable(R.drawable.splashscreen));
                }
                binding.progressCircular.setVisibility(View.GONE);
                binding.reload.setVisibility(View.VISIBLE);
            } catch (JSONException e) {
                showToast(getApplicationContext(), "Something went wrong, Please try again and check the city name", R.color.red);
                binding.progressCircular.setVisibility(View.GONE);
                binding.reload.setVisibility(View.VISIBLE);
                location.setText("Error");
            }
        }, error -> {
            showToast(getApplicationContext(), "Something went wrong, Please try again and check the city name", R.color.red);
            binding.progressCircular.setVisibility(View.GONE);
            binding.reload.setVisibility(View.VISIBLE);
            location.setText("Error");
        });

        MySingleton.getInstance(HomeWeather.this).addToRequestQueue(request); //class to handle requests(this will help to reduce the load)
    }

}