package com.sp.consolidatedlibraryapplication2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText userIdInput, passwordInput;
    private Button loginButton;
    private DatabaseHelper dbHelper;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userIdInput = findViewById(R.id.user_id);
        passwordInput = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);

        dbHelper = new DatabaseHelper();
        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        loginButton.setOnClickListener(v -> attemptLogin());
    }

    private void attemptLogin() {
        String userId = userIdInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (userId.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        dbHelper.loginUser(userId, password, new DatabaseHelper.OnUserLoginListener() {
            @Override
            public void onSuccess() {
                saveUserId(userId);
                Intent intent = new Intent(LoginActivity.this, HomePage.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(LoginActivity.this, "Login Failed: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserId(String userId) {
        UserSession.getInstance().setUserId(userId);
    }
}
