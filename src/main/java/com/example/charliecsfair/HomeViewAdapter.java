package com.example.charliecsfair;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.google.firebase.firestore.core.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class HomeViewAdapter extends RecyclerView.Adapter<HomeViewAdapter.HomeViewHolder> {
    private List<String> data;
    public HomeViewAdapter(List<String> data) {
        this.data = data;
    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.test_thang, parent, false);
        return new HomeViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(HomeViewHolder holder, int position) {

        if(data.isEmpty()) {
            holder.title.setText("Nothing Favorited!");
        }

        String item = data.get(position);
        holder.textView.setText(item);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("items").document(item.toLowerCase())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    int thcAmt = doc.getDouble("thcLevel").intValue();
                    String type = doc.getString("type");
                    type = type.substring(0, 1).toUpperCase() + type.substring(1);

                    holder.attrsTv.setText(type + " • " + thcAmt + "% THC");
                } else {
                    System.out.println(task.getException().toString());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public static class HomeViewHolder extends RecyclerView.ViewHolder {

        public TextView textView, attrsTv, title;

        public HomeViewHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.textView55);
            attrsTv = itemView.findViewById(R.id.tt_attrTv);
            title = itemView.findViewById(R.id.favsTV2);

        }
    }

}
