package com.example.charliecsfair;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
import android.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FeedFragment extends Fragment {

    RecyclerView recyclerView;
    List<String> favList;
    HomeViewAdapter adapter;
    TextView title;

    public FeedFragment() { }

    public static FeedFragment newInstance(String param1, String param2) {
        FeedFragment fragment = new FeedFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);

        title = rootView.findViewById(R.id.favsTV2);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        favList = new ArrayList<String>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new HomeViewAdapter(favList); // adapter = new HomeViewAdapter(getActivity(), favList);
        recyclerView.setAdapter(adapter);


        SharedPreferences prefs = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String userId = prefs.getString("userId", "");


        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .document(userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            List<String> favs = (List<String>) doc.get("favorited");
                            for(String item : favs) { favList.add(item); }
                            adapter.notifyDataSetChanged();

                            if(favList.isEmpty()) {
                                title.setText("Nothing Favorited!");
                            }

                        } else { System.out.println(task.getException().toString()); }
                    }
                });

        return rootView;
    }
}