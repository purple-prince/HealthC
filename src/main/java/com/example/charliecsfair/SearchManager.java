package com.example.charliecsfair;
import java.util.ArrayList;

public class SearchManager {
    String[] allStrains = {
            "Blue Dream", "Sour Diesel", "GSC", "Green Crack",
            "OG Kush", "Granddaddy Purple", "GG4", "Jack Herer",
            "White Widow", "Sherbert", "Pineapple Express", "Durban Poison",
            "Bubba Kush", "Northern Lights", "Trainwreck", "AK-47",
            "Strawberry Cough", "Headband", "Lemon Haze",
            "Chemdawg", "Blueberry", "Super Lemon Haze", "Super Silver Haze",
            "Gelato", "Purple Kush", "Wedding Cake", "Grape Ape",
            "Alaskan Thunder Fuck", "Cherry Pie", "Blackberry Kush", "Master Kush",
            "Maui Wowie", "Amnesia Haze", "Mazar X Blueberry OG", "Cheese",
            "Purple Punch", "Tahoe OG", "Golden Goat", "Death Star",
            "Harlequin", "LA Confidential", "Chocolope", "Platinum Cookies",
            "Bruce Banner", "God's Gift", "Tangie", "Purple Urkle",
            "Mango Kush", "Lemon Kush", "Banana Kush", "Afghan Kush",
            "White Rhino", "Bubble Gum", "Hindu Kush", "Agent Orange",
            "Fire OG", "Purple Haze", "Berry White", "G13",
            "Ghost Train Haze", "Candyland", "Cinderella 99", "Alien OG",
            "LSD", "Blueberry Kush", "Dosidos", "Animal Cookies",
            "Ice Cream Cake", "Lemon Skunk", "9 lb Hammer", "Dutch Treat"
    };

    public ArrayList<String> getRelevantSearches(String searchText) {

        ArrayList<String> results = new ArrayList<String>();

        for(int i = 0; i < allStrains.length; i++) {
            if(allStrains[i].toLowerCase().contains(searchText.toLowerCase())) {
                results.add(allStrains[i]);
            }

            if(results.size() > 5) {
                return results;
            }
        }

        return results;
    }
}
