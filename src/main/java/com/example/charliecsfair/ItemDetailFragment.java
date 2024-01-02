package com.example.charliecsfair;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.os.AsyncTask;
import android.os.Bundle;

import android.app.Fragment;

import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

public class ItemDetailFragment extends Fragment {

    /*
    name, imageurl, type, thclevel, mostcommonterpene, description,
    relaxed, happy, sleepy, euphoric, uplifted, dry_mouth,
    dry_eyes, dizzy, paranoid, anxious, stress, pain, depression
    ,anxiety, insomnia, hungry, talkative, headache, ptsd, creative, energetic
    fatigue, focused, giggly, lack_of_appetite, nausea, headaches, bipolar_disorder, tingly, cramps, aroused, inflammation
     */

    View view;
    TextView type, thcLevel, title, description, symptom1, symptom2, symptom3, circle1TV, circle2TV, circle3TV;
    ImageView backBtn, starIcon;
    String itemName = "";
    DocumentSnapshot itemDoc;
    ProgressBar circle1, circle2, circle3;
    boolean isFavorite = false;
    int CurrentProgress = 0;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        setViews();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("items")
                .document(itemName.toLowerCase())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            itemDoc = task.getResult();
                            updateUI();
                        } else {
                            System.out.println(task.getException().toString());
                        }
                    }
                });

        checkIfIsFavorite();

        starIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFavoriteButtonClicked();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getFragmentManager().beginTransaction().replace(R.id.flFragment, new SearchFragment()).commit();
            }
        });


    }

    public void checkIfIsFavorite() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .document("e58ed763-928c-4155-bee9-fdbaaadc15f3")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            System.out.println("SUCCESS");
                            DocumentSnapshot doc = task.getResult();
                            List<String> ls = (List<String>) doc.get("favorited");
                            isFavorite = ls.contains(itemName);

                            if(isFavorite) { starIcon.setImageResource(R.drawable.star_fill); }
                            else { starIcon.setImageResource(R.drawable.star_stroke); }
                        }
                    }
                });
    }

    public void onFavoriteButtonClicked() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document("e58ed763-928c-4155-bee9-fdbaaadc15f3");



        if(isFavorite) {
            starIcon.setImageResource(R.drawable.star_stroke);
            docRef.update("favorited", FieldValue.arrayRemove(itemName));
            isFavorite = false;

            //update favorite status in firestore

        } else {
            starIcon.setImageResource(R.drawable.star_fill);
            docRef.update("favorited", FieldValue.arrayUnion(itemName));
            isFavorite = true;
        }
    }

























    public void setViews() {
        circle1 = view.findViewById(R.id.circle1);
        circle2 = view.findViewById(R.id.circle2);
        circle3 = view.findViewById(R.id.circle3);
        circle1TV = view.findViewById(R.id.circle1_tv);
        circle2TV = view.findViewById(R.id.circle2_tv);
        circle3TV = view.findViewById(R.id.circle3_tv);
        title = view.findViewById(R.id.itemTitle);
        type = view.findViewById(R.id.type);
        thcLevel = view.findViewById(R.id.thcLevel);
        backBtn = view.findViewById(R.id.backButton);
        description = view.findViewById(R.id.description);
        symptom1 = view.findViewById(R.id.symptom1);
        symptom2 = view.findViewById(R.id.symptom2);
        symptom3 = view.findViewById(R.id.symptom3);
        starIcon = view.findViewById(R.id.star_icon);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.getArguments().clear();
    }

    public static ItemDetailFragment newInstance() {
        ItemDetailFragment fragment = new ItemDetailFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    public ItemDetailFragment() { }

    public String getItemName() {
        return this.itemName;
    }

    public void updateUI() {

        String strType = itemDoc.getString("type");
        type.setText(strType.substring(0, 1).toUpperCase() + strType.substring(1).toLowerCase());
        thcLevel.setText(itemDoc.getDouble("thcLevel").intValue() + "% THC");
        description.setText(itemDoc.getString("description"));
        circle1.setRotation(270);
        circle2.setRotation(270);
        circle3.setRotation(270);
        title.setText(itemName);

        ArrayList<String> symptoms = (ArrayList<String>) itemDoc.get("helpsWith");

        symptom1.setText(symptoms.get(0));
        symptom2.setText(symptoms.get(1));
        symptom3.setText(symptoms.get(2));

        setTopStats();
    }

    public void setTopStats() {

        int filledCircles = 0;

        int happyAmount = 0; //57
        int creativeAmount = 0; //35
        int talkativeAmount = 0; //0
        int sleepyAmount = 0; //0
        int hungryAmount = 0; //0
        int energeticAmount = 0; //0
        int euphoricAmount = 0; //53
        int gigglyAmount = 0; //0
        int relaxedAmount = 0; //42

        if(itemDoc.contains("happy")) { happyAmount = itemDoc.getDouble("happy").intValue();}
        if(itemDoc.contains("creative")) { creativeAmount = itemDoc.getDouble("creative").intValue();}
        if(itemDoc.contains("talkative")) { talkativeAmount = itemDoc.getDouble("talkative").intValue();}
        if(itemDoc.contains("sleepy")) { sleepyAmount = itemDoc.getDouble("sleepy").intValue();}
        if(itemDoc.contains("hungry")) { hungryAmount = itemDoc.getDouble("hungry").intValue();}
        if(itemDoc.contains("energetic")) { energeticAmount = itemDoc.getDouble("energetic").intValue();}
        if(itemDoc.contains("euphoric")) { euphoricAmount = itemDoc.getDouble("euphoric").intValue();}
        if(itemDoc.contains("giggly")) { gigglyAmount = itemDoc.getDouble("giggly").intValue();}
        if(itemDoc.contains("relaxed")) { relaxedAmount = itemDoc.getDouble("relaxed").intValue();}

        //happy, relaxed, relaxed
        ArrayList<String> all = new ArrayList<String>();
        //happy, creative, talkative

        // initial sort of first 3 vals
        if(happyAmount > creativeAmount) {
            if(happyAmount > talkativeAmount) {
                all.add("happy");
                if(talkativeAmount > creativeAmount) {
                    all.add("talkative");
                    all.add("creative");
                } else {
                    all.add("creative");
                    all.add("talkative");
                }
            } else {
                all.add("talkative");
                all.add("happy");
                all.add("creative");
            }
        } else {
            if(creativeAmount > talkativeAmount) {
                all.add("creative");
                if(happyAmount > talkativeAmount) {
                    all.add("happy");
                    all.add("talkative");
                } else {
                    all.add("talkative");
                    all.add("happy");
                }
            } else {
                all.add("talkative");
                all.add("creative");
                all.add("happy");
            }
        }

        for(int i = 2; i >= 0; i--) {
            if(all.get(i).equals("happy")) {  if(sleepyAmount > happyAmount) { all.set(i, "sleepy"); break; } }
            else if(all.get(i).equals("creative")) { if(sleepyAmount > creativeAmount) { all.set(i, "sleepy"); break; }  }
            else { if(sleepyAmount > talkativeAmount) { all.set(i, "sleepy"); break; } }
        }

        for(int i = 2; i >= 0; i--) {
            if(all.get(i).equals("happy")) {  if(hungryAmount > happyAmount) { all.set(i, "sleepy"); break; } }
            else if(all.get(i).equals("creative")) { if(hungryAmount > creativeAmount) { all.set(i, "hungry"); break; }  }
            else if(all.get(i).equals("sleepy")) { if(hungryAmount > sleepyAmount) { all.set(i, "hungry"); break; } }
            else { if(hungryAmount > talkativeAmount) { all.set(i, "hungry"); break; } }
        }

        for(int i = 2; i >= 0; i--) {
            if(all.get(i).equals("happy")) {  if(energeticAmount > happyAmount) { all.set(i, "energetic"); break; } }
            else if(all.get(i).equals("creative")) { if(energeticAmount > creativeAmount) { all.set(i, "energetic"); break; }  }
            else if(all.get(i).equals("sleepy")) { if(energeticAmount > sleepyAmount) { all.set(i, "energetic"); break; } }
            else if(all.get(i).equals("hungry")) { if(energeticAmount > hungryAmount) { all.set(i, "energetic"); break; } }
            else { if(energeticAmount > talkativeAmount) { all.set(i, "energetic"); break; } }
        }

        //happy, creative, talkative
        //happy, euphoric, euphoric

        for(int i = 2; i >= 0; i--) {
            if(all.get(i).equals("happy")) {  if(euphoricAmount > happyAmount) { all.set(i, "euphoric"); break; } }
            else if(all.get(i).equals("creative")) { if(euphoricAmount > creativeAmount) { all.set(i, "euphoric"); break; }  }
            else if(all.get(i).equals("sleepy")) { if(euphoricAmount > sleepyAmount) { all.set(i, "euphoric"); break; } }
            else if(all.get(i).equals("hungry")) { if(euphoricAmount > hungryAmount) { all.set(i, "euphoric"); break; } }
            else if(all.get(i).equals("energetic")) { if(euphoricAmount > euphoricAmount) { all.set(i, "euphoric"); break; } }
            else { if(euphoricAmount > talkativeAmount) { all.set(i, "euphoric"); break; } }
        }
        for(int i = 2; i >= 0; i--) {
            if(all.get(i).equals("happy")) {  if(gigglyAmount > happyAmount) { all.set(i, "giggly"); break; } }
            else if(all.get(i).equals("creative")) { if(gigglyAmount > creativeAmount) { all.set(i, "giggly"); break; }  }
            else if(all.get(i).equals("sleepy")) { if(gigglyAmount > sleepyAmount) { all.set(i, "giggly"); break; } }
            else if(all.get(i).equals("hungry")) { if(gigglyAmount > hungryAmount) { all.set(i, "giggly"); break; } }
            else if(all.get(i).equals("energetic")) { if(gigglyAmount > energeticAmount) { all.set(i, "giggly"); break; } }
            else if(all.get(i).equals("euphoric")) { if(gigglyAmount > euphoricAmount) { all.set(i, "giggly"); break; } }
            else { if(gigglyAmount > talkativeAmount) { all.set(i, "giggly"); break; } }
        }
        for(int i = 2; i >= 0; i--) {
            if(all.get(i).equals("happy")) {  if(relaxedAmount > happyAmount) { all.set(i, "relaxed"); break; } }
            else if(all.get(i).equals("creative")) { if(relaxedAmount > creativeAmount) { all.set(i, "relaxed"); break; }  }
            else if(all.get(i).equals("sleepy")) { if(relaxedAmount > sleepyAmount) { all.set(i, "relaxed"); break; } }
            else if(all.get(i).equals("hungry")) { if(relaxedAmount > hungryAmount) { all.set(i, "relaxed"); break; } }
            else if(all.get(i).equals("energetic")) { if(relaxedAmount > energeticAmount) { all.set(i, "relaxed"); break; } }
            else if(all.get(i).equals("euphoric")) { if(relaxedAmount > euphoricAmount) { all.set(i, "relaxed"); break; } }
            else if(all.get(i).equals("giggly")) { if(relaxedAmount > creativeAmount) { all.set(i, "relaxed"); break; }  }
            else { if(relaxedAmount > talkativeAmount) { all.set(i, "relaxed"); break; } }
        }








        switch(all.get(0)) {
            case "happy":
                circle1.setProgress(happyAmount);
                circle1TV.setText("Happy");
                break;
            case "creative":
                circle1.setProgress(creativeAmount);
                circle1TV.setText("Creative");
                break;
            case "talkative":
                circle1.setProgress(talkativeAmount);
                circle1TV.setText("Talkative");
                break;
            case "sleepy":
                circle1.setProgress(sleepyAmount);
                circle1TV.setText("Sleepy");
                break;
            case "hungry":
                circle1.setProgress(hungryAmount);
                circle1TV.setText("Hungry");
                break;
            case "energetic":
                circle1.setProgress(energeticAmount);
                circle1TV.setText("Energetic");
                break;
            case "euphoric":
                circle1.setProgress(euphoricAmount);
                circle1TV.setText("Euphoric");
                break;
            case "giggly":
                circle1.setProgress(gigglyAmount);
                circle1TV.setText("Giggly");
                break;
            case "relaxed":
                circle1.setProgress(relaxedAmount);
                circle1TV.setText("Relaxed");
                break;
        }

        switch(all.get(1)) {
            case "happy":
                circle2.setProgress(happyAmount);
                circle2TV.setText("Happy");
                break;
            case "creative":
                circle2.setProgress(creativeAmount);
                circle2TV.setText("Creative");
                break;
            case "talkative":
                circle2.setProgress(talkativeAmount);
                circle2TV.setText("Talkative");
                break;
            case "sleepy":
                circle2.setProgress(sleepyAmount);
                circle2TV.setText("Sleepy");
                break;
            case "hungry":
                circle2.setProgress(hungryAmount);
                circle2TV.setText("Hungry");
                break;
            case "energetic":
                circle2.setProgress(energeticAmount);
                circle2TV.setText("Energetic");
                break;
            case "euphoric":
                circle2.setProgress(euphoricAmount);
                circle2TV.setText("Euphoric");
                break;
            case "giggly":
                circle2.setProgress(gigglyAmount);
                circle2TV.setText("Giggly");
                break;
            case "relaxed":
                circle2.setProgress(relaxedAmount);
                circle2TV.setText("Relaxed");
                break;
        }

        switch(all.get(2)) {
            case "happy":
                circle3.setProgress(happyAmount);
                circle3TV.setText("Happy");
                break;
            case "creative":
                circle3.setProgress(creativeAmount);
                circle3TV.setText("Creative");
                break;
            case "talkative":
                circle3.setProgress(talkativeAmount);
                circle3TV.setText("Talkative");
                break;
            case "sleepy":
                circle3.setProgress(sleepyAmount);
                circle3TV.setText("Sleepy");
                break;
            case "hungry":
                circle3.setProgress(hungryAmount);
                circle3TV.setText("Hungry");
                break;
            case "energetic":
                circle3.setProgress(energeticAmount);
                circle3TV.setText("Energetic");
                break;
            case "euphoric":
                circle3.setProgress(euphoricAmount);
                circle3TV.setText("Euphoric");
                break;
            case "giggly":
                circle3.setProgress(gigglyAmount);
                circle3TV.setText("Giggly");
                break;
            case "relaxed":
                circle3.setProgress(relaxedAmount);
                circle3TV.setText("Relaxed");
                break;
        }
    }

    public void setCircleInfo(int circleNum, int amount) {
        switch(circleNum) {
            case 1:
                circle1.setProgress(amount);
                circle1TV.setText(amount + "%");
            case 2:
                circle2.setProgress(amount);
                circle2TV.setText(amount + "%");
            case 3:
                circle3.setProgress(amount);
                circle3TV.setText(amount + "%");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_item_detail, container, false);
        Bundle bundle = this.getArguments();
        this.itemName = bundle.getString("itemName");
        return view;
    }

}