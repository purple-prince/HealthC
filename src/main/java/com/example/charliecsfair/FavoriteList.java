package com.example.charliecsfair;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FavoriteList extends AppCompatActivity {

    RecyclerView recyclerView;
    List<String> favList;
    HomeViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);

        recyclerView = findViewById(R.id.recycler_view);
        favList = new ArrayList<String>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HomeViewAdapter(favList); // adapter = new HomeViewAdapter(this, favList);
        recyclerView.setAdapter(adapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .document("e58ed763-928c-4155-bee9-fdbaaadc15f3")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            List<String> favs = (List<String>) doc.get("favorited");

                            for(String item : favs) { favList.add(item); }

                            adapter.notifyDataSetChanged();

                        } else { System.out.println(task.getException().toString()); }
                    }
                });
    }
}