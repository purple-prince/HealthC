package com.example.charliecsfair;

import android.app.FragmentTransaction;
import android.os.Bundle;

import android.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

//import androidx.fragment.app.FragmentManager;
import android.app.FragmentManager;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    SearchManager searchManager;
    View view;
    SearchView searchBar;
    TextView result1, result2, result3, result4, result5, result6;//, result7, result8;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchManager = new SearchManager();
    }

    public void onSearchResultClick(String itemName) {

        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(view.getContext().INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

        Bundle bundle = new Bundle();
        bundle.putString("itemName", itemName);
        ItemDetailFragment itemDetailFragment = new ItemDetailFragment();
        itemDetailFragment.setArguments(bundle);
        getActivity().getFragmentManager().beginTransaction().replace(R.id.flFragment, itemDetailFragment).commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_search, container, false);
        searchBar = (SearchView) view.findViewById(R.id.searchBar);
        result1 = (TextView) view.findViewById(R.id.sr1);
        result2 = (TextView) view.findViewById(R.id.sr2);
        result3 = (TextView) view.findViewById(R.id.sr3);
        result4 = (TextView) view.findViewById(R.id.sr4);
        result5 = (TextView) view.findViewById(R.id.sr5);
        result6 = (TextView) view.findViewById(R.id.sr6);

        result1.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { onSearchResultClick(result1.getText().toString()); } });
        result2.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { onSearchResultClick(result2.getText().toString()); } });
        result3.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { onSearchResultClick(result3.getText().toString()); } });
        result4.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { onSearchResultClick(result4.getText().toString()); } });
        result5.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { onSearchResultClick(result5.getText().toString()); } });
        result6.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { onSearchResultClick(result6.getText().toString()); } });


        result1.setEnabled(false);
        result2.setEnabled(false);
        result3.setEnabled(false);
        result4.setEnabled(false);
        result5.setEnabled(false);
        result6.setEnabled(false);

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                result1.setText("");
                result2.setText("");
                result3.setText("");
                result4.setText("");
                result5.setText("");
                result6.setText("");

                ArrayList<String> results = searchManager.getRelevantSearches(s.toLowerCase());

                if(results.size() > 0) {
                    result1.setText(results.get(0));
                    result1.setEnabled(true);
                    if(results.size() > 1) {
                        result2.setText(results.get(1));
                        result2.setEnabled(true);
                        if(results.size() > 2) {
                            result3.setText(results.get(2));
                            result3.setEnabled(true);
                            if(results.size() > 3) {
                                result4.setText(results.get(3));
                                result4.setEnabled(true);
                                if(results.size() > 4) {
                                    result5.setText(results.get(4));
                                    result5.setEnabled(true);
                                    if(results.size() > 5) {
                                        result6.setText(results.get(5));
                                        result6.setEnabled(true);
                                    }
                                }
                            }
                        }
                    }
                } else { result1.setText("Nothing Found :("); }
                if(results.size() < 6) { result6.setEnabled(false); }
                if(results.size() < 5) { result5.setEnabled(false); }
                if(results.size() < 4) { result4.setEnabled(false); }
                if(results.size() < 3) { result3.setEnabled(false); }
                if(results.size() < 2) { result2.setEnabled(false); }
                if(results.size() < 1) { result1.setEnabled(false); }

                return false;
            }
        });
        return view;
    }

    public SearchFragment() { }

}