package com.hypernymbiz.logistics;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hypernymbiz.logistics.api.ApiInterface;
import com.hypernymbiz.logistics.model.User;
import com.hypernymbiz.logistics.model.WebAPIResponse;
import com.hypernymbiz.logistics.utils.LoginUtils;
import com.onesignal.OneSignal;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Metis on 21-Mar-18.
 */

public class LoginActivity extends AppCompatActivity {
    Button btn_sign;
    private EditText edit_username, edit_password;
    private TextInputLayout inputLayout_username, inputLayout_password;
    private ProgressBar progressBar;
    String email, getUserAssociatedEntity;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn_sign = (Button) findViewById(R.id.sign_in);
        btn_sign = (Button) findViewById(R.id.sign_in);
        inputLayout_username = (TextInputLayout) findViewById(R.id.input_layout_username);
        inputLayout_password = (TextInputLayout) findViewById(R.id.input_layout_password);
        edit_username = (EditText) findViewById(R.id.edittext_username);
        edit_password = (EditText) findViewById(R.id.edittext_password);
        progressBar = (ProgressBar) findViewById(R.id.loader);
        edit_username.addTextChangedListener(new MyTextWatcher(edit_username));
        edit_password.addTextChangedListener(new MyTextWatcher(edit_password));
        pref = getApplicationContext().getSharedPreferences("TAG", MODE_PRIVATE);


        progressBar.setVisibility(View.GONE);
        if (LoginUtils.isUserLogin(getApplicationContext())) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
//            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            btn_sign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submit();
                }
            });
        }


    }

    private void submit() {
        if (!validateName())
            return;
        if (!validatePassword())
            return;

        progressBar.setVisibility(View.VISIBLE);

        String username = edit_username.getText().toString(); //    "driver@kotal.com"
        String password = edit_password.getText().toString();//    "hypernym123"

        HashMap<String, Object> body = new HashMap<>();
        body.put("email", username);
        body.put("password", password);
        // body.put("push_key", ScheduleUtils.getUserOneSignalId(this));
        getUserAssociatedEntity = LoginUtils.getUserAssociatedEntity(this);

        ApiInterface.retrofit.loginUser(body).enqueue(new Callback<WebAPIResponse<User>>() {
            @Override
            public void onResponse(Call<WebAPIResponse<User>> call, Response<WebAPIResponse<User>> response) {
//                Log.d("TAAAG",""+response.body().status);
//                Log.d("TAAAG",""+response.body().response);
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    try {
                        if (response.body().status) {
                            OneSignal.sendTag("email", response.body().response.getEmail());
//                         Toast.makeText(LoginActivity.this, response.body().response.getToken(), Toast.LENGTH_SHORT).show();
                            LoginUtils.saveUserToken(LoginActivity.this, response.body().response.getToken(), Integer.toString(response.body().response.getAssociatedEntity()));
                            LoginUtils.userLoggedIn(LoginActivity.this);
                            Log.d("TAAAG", "" + LoginUtils.getUserToken(getApplicationContext()));
                            getUserAssociatedEntity = response.body().response.getAssociatedEntity().toString();
                            email = response.body().response.getEmail();
                            editor = pref.edit();
                            editor.putString("Email", email);
                            editor.commit();

                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Wrong email & password", Snackbar.LENGTH_LONG);
                            View view = snackbar.getView();
                            TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                            view.setBackgroundColor(ContextCompat.getColor(getApplication(), R.color.colorPrimary));
                            tv.setTextColor(ContextCompat.getColor(getApplication(), R.color.colorDialogToolbarText));
                            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            snackbar.show();
                        }


                    } catch (Exception ex) {
                        progressBar.setVisibility(View.GONE);
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Establish Network Connection!", Snackbar.LENGTH_LONG);
                        View view = snackbar.getView();
                        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                        view.setBackgroundColor(ContextCompat.getColor(getApplication(), R.color.colorPrimary));
                        tv.setTextColor(ContextCompat.getColor(getApplication(), R.color.colorDialogToolbarText));
                        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        snackbar.show();
                    }
                }


            }

            @Override
            public void onFailure(Call<WebAPIResponse<User>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Establish Network Connection!", Snackbar.LENGTH_LONG);
                View view = snackbar.getView();
                TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                view.setBackgroundColor(ContextCompat.getColor(getApplication(), R.color.colorPrimary));
                tv.setTextColor(ContextCompat.getColor(getApplication(), R.color.colorDialogToolbarText));
                tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                snackbar.show();
            }
        });

    }

    private boolean validateName() {
        if (edit_username.getText().toString().trim().isEmpty()) {
            inputLayout_username.setError(getString(R.string.err_msg_name));
            requestFocus(edit_username);
            return false;
        } else {
            inputLayout_username.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (edit_password.getText().toString().trim().isEmpty()) {
            inputLayout_password.setError(getString(R.string.err_msg_password));
            requestFocus(inputLayout_password);
            return false;
        } else {
            inputLayout_password.setErrorEnabled(false);
        }
        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    private class MyTextWatcher implements TextWatcher {
        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            switch (view.getId()) {
                case R.id.edittext_username:
                    validateName();
                    break;
                case R.id.edittext_password:
                    validatePassword();
                    break;
            }

        }
    }

}



