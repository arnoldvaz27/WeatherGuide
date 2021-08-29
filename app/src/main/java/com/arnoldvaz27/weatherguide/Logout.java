package com.arnoldvaz27.weatherguide;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.arnoldvaz27.weatherguide.R;
import com.arnoldvaz27.weatherguide.databinding.LogoutBinding;

public class Logout extends AppCompatActivity {

    //declaration of fields
    LogoutBinding binding;
    private EditText mobileEdit,fullNameEdit;
    private String mobileNumber,FullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.red));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        binding = DataBindingUtil.setContentView(this, R.layout.logout); //binding xml layout to java file

        //initializations of fields
        mobileEdit = binding.mobile;
        fullNameEdit = binding.fullName;

        //storing data locally
        SharedPreferences DetailsPref = getSharedPreferences("Details", MODE_PRIVATE);
        mobileNumber = DetailsPref.getString("mobile number", null);

        SharedPreferences settingLayoutType = getSharedPreferences("User Profile", MODE_PRIVATE);
        FullName = settingLayoutType.getString("Full Name", null);

        //listeners
        binding.login.setOnClickListener(v -> {
            if(mobileEdit.getText().toString().equals(mobileNumber) && fullNameEdit.getText().toString().equals(FullName)){
                final SharedPreferences DetailPref = getSharedPreferences("Details", MODE_PRIVATE);
                final SharedPreferences.Editor DetailEdit = DetailPref.edit();
                DetailEdit.putString("user registered", "1");
                DetailEdit.putString("user logout", "0");
                DetailEdit.apply();
                startActivity(new Intent(getApplicationContext(),HomeWeather.class));
                finishAffinity();
            }else{
                CustomToast.showToast(getApplicationContext(),"Please enter valid details to login",R.color.red);
            }
        });
    }
}