package com.example.charliecsfair;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        EditText username = findViewById(R.id.createUsername);
        EditText pswd = findViewById(R.id.createPassword);
        Button btn = findViewById(R.id.button);



        btn.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View view) {

                String usernameTxt = username.getText().toString();
                String passwordTxt = pswd.getText().toString();

                createAccount(CreateAccountActivity.this, usernameTxt, passwordTxt);
            }
        });
    }

    public void createAccount(Context context, String username, String password) {
        if(username.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Username/Password can't be empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("username", username);
        userInfo.put("password", password);
        userInfo.put("favorited", new ArrayList<>());

        db.collection("users")
                .add(userInfo)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(context, "Account created! You can now log in.", Toast.LENGTH_SHORT).show();
                        switchScreens(context, MainActivity.class);
                    }
                });
    }

    public void switchScreens(Context context, Class c) {
        Intent itn = new Intent(context, c);
        startActivity(itn);
    }
}