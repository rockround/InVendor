package com.invendordev.invendor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Login_or_Register extends AppCompatActivity {

    private Button btnNavLogin;
    private Button btnNavRegister;
    Context context = this;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_or_register);

        btnNavLogin = (Button) findViewById(R.id.btnNavLogin);
        btnNavRegister = (Button) findViewById(R.id.btnNavRegister);

        btnNavRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Clicked Register button", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnNavLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Clicked Login button", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}
