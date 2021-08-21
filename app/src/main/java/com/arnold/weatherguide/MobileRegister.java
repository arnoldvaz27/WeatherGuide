package com.arnold.weatherguide;

import static com.arnold.weatherguide.CustomToast.showToast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.arnold.weatherguide.databinding.MobileRegisterBinding;

public class MobileRegister extends AppCompatActivity {

    //declaration of fields
    private MobileRegisterBinding binding;
    private EditText mobileEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.red));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        binding = DataBindingUtil.setContentView(this, R.layout.mobile_register);  //binding xml layout to java file

        //initialization of fields
        mobileEdt = binding.mobile;
        mobileEdt.setSelection(mobileEdt.getText().length());
        mobileEdt.requestFocus();

        //listeners
        binding.continueButton.setOnClickListener(v -> {
            int numberLength = mobileEdt.getText().toString().trim().length();
            if (numberLength != 10) {
                showToast(getApplicationContext(), "Please enter valid 10 digit number", R.color.red);
            } else if (!TextUtils.isDigitsOnly(mobileEdt.getText().toString())) {
                showToast(getApplicationContext(),"Please enter numbers only",R.color.red);
            } else {
                binding.continueButton.setEnabled(false);
                String mobileNumber = mobileEdt.getText().toString();
                new Handler().postDelayed(() -> {
                    final SharedPreferences DetailPref = getSharedPreferences("Details", MODE_PRIVATE);
                    final SharedPreferences.Editor DetailEdit = DetailPref.edit();
                    DetailEdit.putString("mobile number", mobileNumber);
                    DetailEdit.apply();
                    Intent intent = new Intent(getApplicationContext(), Registration.class);
                    startActivity(intent);
                    finishAffinity();
                }, 0);
            }
        });
    }

}