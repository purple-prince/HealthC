package com.example.charliecsfair;

import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private HomeViewAdapter adapter;

    public HomeFragment() { }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        String[] tempData = {"a","e","i","o","u"};

        List<String> tempList = Arrays.asList(tempData);
        adapter = new HomeViewAdapter(tempList);
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void getFavorites() {
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

                            adapter = new HomeViewAdapter(favs);
                            recyclerView.setAdapter(adapter);
                        }
                    }
                });
    }
}
