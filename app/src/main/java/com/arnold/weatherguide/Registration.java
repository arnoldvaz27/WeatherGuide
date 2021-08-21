package com.arnold.weatherguide;

import static com.arnold.weatherguide.CustomToast.showToast;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.arnold.weatherguide.databinding.RegistrationBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class Registration extends AppCompatActivity {

    //declaration of fields
    private RegistrationBinding binding;
    private JSONObject find;
    private String status,date,month,year;
    int mYear, mDay, mMonth;
    private EditText fullName, address1, address2, pincode;
    private TextView gender, dob, district, state,ageTxt;
    private ImageView calendar;
    private Button check;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.red));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        binding = DataBindingUtil.setContentView(this, R.layout.registration);

        //initialization of fields
        fullName = binding.fullName;
        gender = binding.gender;
        TextView mobileNumber = binding.mobile;
        address1 = binding.address1;
        address2 = binding.address2;
        dob = binding.dob;
        ageTxt = binding.age;
        pincode = binding.pinCode;
        district = binding.district;
        state = binding.state;
        calendar = binding.calendar;
        check = binding.check;
        progressBar = binding.progressCircular;

        //storing data locally
        SharedPreferences DetailsPref = getSharedPreferences("Details", MODE_PRIVATE);
        mobileNumber.setText(DetailsPref.getString("mobile number", null));
        year = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
        date = new SimpleDateFormat("dd", Locale.getDefault()).format(new Date());
        month = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date());

        //focusing on the edit text as soon the activity is opened
        fullName.setSelection(fullName.getText().length());
        fullName.requestFocus();

        //listeners
        binding.infoName.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(getApplicationContext(), binding.infoName);
            popup.getMenuInflater().inflate(R.menu.information, popup.getMenu());
            popup.getMenu().findItem(R.id.address1).setVisible(false);
            popup.getMenu().findItem(R.id.address2).setVisible(false);
            popup.show();
        });
        binding.infoAddress1.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(getApplicationContext(), binding.infoAddress1);
            popup.getMenuInflater().inflate(R.menu.information, popup.getMenu());
            popup.getMenu().findItem(R.id.fullName).setVisible(false);
            popup.getMenu().findItem(R.id.address2).setVisible(false);
            popup.show();
        });
        binding.infoAddress2.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(getApplicationContext(), binding.infoAddress2);
            popup.getMenuInflater().inflate(R.menu.information, popup.getMenu());
            popup.getMenu().findItem(R.id.address1).setVisible(false);
            popup.getMenu().findItem(R.id.fullName).setVisible(false);
            popup.show();
        });
        binding.genderCategory.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(getApplicationContext(), binding.genderCategory);
            popup.getMenuInflater().inflate(R.menu.gender, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.male) {
                    gender.setText(R.string.male);
                }
                if (item.getItemId() == R.id.female) {
                    gender.setText(R.string.female);
                }
                return true;
            });

            popup.show();
        });
        calendar.setOnClickListener(v -> DatePicker(calendar));
        pincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("UseCompatLoadingForColorStateLists")
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    check.setEnabled(false);
                    check.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.hint));

                } else {
                    check.setEnabled(true);
                    check.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.yellow));
                }
            }
        });
        check.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            check.setEnabled(false);
            Fetching();
        });
        binding.save.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(fullName.getText().toString()) || TextUtils.isEmpty(gender.getText().toString()) || TextUtils.isEmpty(dob.getText().toString()) || TextUtils.isEmpty(ageTxt.getText().toString()) || TextUtils.isEmpty(address1.getText().toString()) || TextUtils.isEmpty(pincode.getText().toString())
                    || TextUtils.isEmpty(district.getText().toString()) || TextUtils.isEmpty(state.getText().toString())) {
                showToast(getApplicationContext(), "Please enter all the details in order to save details", R.color.red);
                progressBar.setVisibility(View.INVISIBLE);
            } else if (fullName.getText().toString().length() > 50) {
                showToast(getApplicationContext(), "Please enter valid number of character", R.color.red);
                progressBar.setVisibility(View.INVISIBLE);
            } else if (address1.getText().toString().length() < 3 || address1.getText().toString().length() > 50) {
                showToast(getApplicationContext(), "Please enter valid number of character", R.color.red);
                progressBar.setVisibility(View.INVISIBLE);
            } else {
                final SharedPreferences DetailPref = getSharedPreferences("Details", MODE_PRIVATE);
                final SharedPreferences.Editor DetailEdit = DetailPref.edit();
                DetailEdit.putString("user registered", "1");
                DetailEdit.putString("user logout", "0");
                DetailEdit.apply();
                final SharedPreferences UserProfile = getSharedPreferences("User Profile", MODE_PRIVATE);
                final SharedPreferences.Editor UserProfileEdit = UserProfile.edit();
                UserProfileEdit.putString("Full Name", fullName.getText().toString());
                UserProfileEdit.putString("Gender", gender.getText().toString());
                UserProfileEdit.putString("Dob", dob.getText().toString());
                UserProfileEdit.putString("Age", ageTxt.getText().toString());
                UserProfileEdit.putString("Address_one", address1.getText().toString());
                UserProfileEdit.putString("Address_two", address2.getText().toString());
                UserProfileEdit.putString("Pincode", pincode.getText().toString());
                UserProfileEdit.putString("District", district.getText().toString());
                UserProfileEdit.putString("State", state.getText().toString());
                UserProfileEdit.apply();
                startActivity(new Intent(getApplicationContext(), HomeWeather.class));
                finishAffinity();
                progressBar.setVisibility(View.INVISIBLE);
            }

        });

    }

    //method to fetch the data
    private void Fetching() {
        String url = "https://api.postalpincode.in/pincode/" + pincode.getText().toString();

        @SuppressLint("SetTextI18n") JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            try {
                find = response.getJSONObject(0);
                status = find.getString("Status");
                if (status.equals("Error")) {
                    showToast(getApplicationContext(), "Please provide valid 6 digit pincode", R.color.red);
                    state.setText("State : ");
                    district.setText("District : ");
                    check.setEnabled(true);
                } else {
                    JSONArray movies = find.getJSONArray("PostOffice");
                    JSONObject details = movies.getJSONObject(0);
                    String District = details.getString("District");
                    String State = details.getString("State");
                    state.setText("State : " + State);
                    district.setText("District : " + District);
                }
                progressBar.setVisibility(View.INVISIBLE);
            } catch (JSONException e) {
                showToast(getApplicationContext(), "Something went wrong, please try again", R.color.red);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }, error -> {
            progressBar.setVisibility(View.INVISIBLE);
            showToast(getApplicationContext(), "Something went wrong, please try again", R.color.red);
        });
        //in case of any error this toast will be executed
        check.setEnabled(true);
        MySingleton.getInstance(Registration.this).addToRequestQueue(request);
    }

    public void DatePicker(View v) {

        if (v == calendar) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            @SuppressLint("SetTextI18n") DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        dob.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        String ages =  getAge(year,(monthOfYear + 1),dayOfMonth);
                        ageTxt.setText(ages);
                    }, mYear, mMonth, mDay);

            datePickerDialog.show();
        }

    }

    private String getAge(int gYear,int gMonth,int gDay) {
        int yr = Integer.parseInt(year) - gYear;
        int mn = Integer.parseInt(month) - gMonth;
        int dt = Integer.parseInt(date) - gDay;

        return yr +" years "+mn+" months "+dt+" days";
    }
}