package com.example.charliecsfair;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        TextView registerButton = findViewById(R.id.registerButton);
//        Button loginBtn = findViewById(R.id.loginButton);
//        EditText name = findViewById(R.id.etName);
//        EditText pswd = findViewById(R.id.etPassword);
//
//        loginBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String usernameToCheck = name.getText().toString();
//                String passwordToCheck = pswd.getText().toString();
//                tryLogin(usernameToCheck, passwordToCheck, view.getContext(), FeedActivity.class);
//            }
//        });
//
//        registerButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                switchScreens(MainActivity.this, CreateAccountActivity.class);
//            }
//        });
//    }

    DatabaseReference myRef2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("testMessages");
        myRef.setValue("this is a message");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = dataSnapshot.getValue(String.class);
                Log.d("TAG", "Value is: " + value);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { Log.w("tag", "Couldnt get value", error.toException()) }
        });


    }

    public void storeLoginInfo(String username, String documentID) {
        SharedPreferences prefs = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("userId", documentID);
        editor.putString("username", username);

        editor.commit();
    }

    public void switchScreens(Context context, Class c) {
        Intent itn = new Intent(context, c);
        startActivity(itn);
    }

    public void tryLogin(String username, String password, Context context, Class c) {
        if(username.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Username/Password can't be empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            if (task.getResult().size() > 0) { DocumentSnapshot doc = task.getResult().getDocuments().get(0); }
                            else { Toast.makeText(context, "Incorrect username or password", Toast.LENGTH_SHORT).show(); return; }

                            DocumentSnapshot doc = task.getResult().getDocuments().get(0);
                            if(doc.getString("password").equals(password)) {
                                storeLoginInfo(username, doc.getId());
                                switchScreens(context, c);
                            } else { Toast.makeText(context, "Incorrect username or password", Toast.LENGTH_SHORT).show(); }

                        } else { Toast.makeText(context, "Error retrieving login information, please try again later. ", Toast.LENGTH_SHORT).show(); }
                    }
                });
    }
}