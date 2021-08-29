package com.arnoldvaz27.weatherguide;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.arnoldvaz27.weatherguide.R;
import com.arnoldvaz27.weatherguide.databinding.UserProfileBinding;

public class UserProfile extends AppCompatActivity {

    UserProfileBinding binding;
    String mobileNumber,FullName,Gender,DOB,Age,Address1,Address2,Pincode,District,State;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.red));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        binding = DataBindingUtil.setContentView(this, R.layout.user_profile);

        SharedPreferences DetailsPref = getSharedPreferences("Details", MODE_PRIVATE);
        mobileNumber = DetailsPref.getString("mobile number", null);

        SharedPreferences settingLayoutType = getSharedPreferences("User Profile", MODE_PRIVATE);
        FullName = settingLayoutType.getString("Full Name", null);
        Gender = settingLayoutType.getString("Gender", null);
        DOB = settingLayoutType.getString("Dob", null);
        Age = settingLayoutType.getString("Age", null);
        Address1 = settingLayoutType.getString("Address_one", null);
        Address2 = settingLayoutType.getString("Address_two", null);
        Pincode = settingLayoutType.getString("Pincode", null);
        District = settingLayoutType.getString("District", null);
        State = settingLayoutType.getString("State", null);

        binding.mobile.setText(mobileNumber);
        binding.fullName.setText(FullName);
        binding.gender.setText(Gender);
        binding.dob.setText(DOB);
        binding.age.setText(Age);
        binding.address1.setText(Address1);
        binding.address2.setText(Address2);
        binding.pinCode.setText(Pincode);
        binding.district.setText(District);
        binding.state.setText(State);

        if(Address2.equals("")){
            binding.address2Layout.setVisibility(View.GONE);
        }

        binding.back.setOnClickListener(v -> finish());
        binding.edit.setOnClickListener(v -> {
            final SharedPreferences UserProfile = getSharedPreferences("User Profile", MODE_PRIVATE);
            final SharedPreferences.Editor UserProfileEdit = UserProfile.edit();
            UserProfileEdit.putString("Full Name", null);
            UserProfileEdit.putString("Gender", null);
            UserProfileEdit.putString("Dob", null);
            UserProfileEdit.putString("Age", null);
            UserProfileEdit.putString("Address_one", null);
            UserProfileEdit.putString("Address_two", null);
            UserProfileEdit.putString("Pincode", null);
            UserProfileEdit.putString("District", null);
            UserProfileEdit.putString("State", null);
            UserProfileEdit.apply();
            final SharedPreferences DetailPref = getSharedPreferences("Details", MODE_PRIVATE);
            final SharedPreferences.Editor DetailEdit = DetailPref.edit();
            DetailEdit.putString("user registered", "0");
            DetailEdit.putString("user logout", "0");
            DetailEdit.apply();
            startActivity(new Intent(getApplicationContext(),Registration.class));
            finishAffinity();
        });
    }


}