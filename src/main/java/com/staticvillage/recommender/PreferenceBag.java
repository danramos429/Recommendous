package com.staticvillage.recommender;

import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray;
import org.apache.mahout.cf.taste.model.PreferenceArray;

import java.util.*;

/**
 * Created by joelparrish on 12/29/14.
 */
public class PreferenceBag {
    private int max;
    private HashMap<String, Integer> map;

    public PreferenceBag(){
        max = 1;
        map = new HashMap<String, Integer>();
    }

    public void add(String items){
        if(map.containsKey(items)){
            int v = map.get(items);
            v++;

            if(max < v)
                max = v;

            map.put(items, v);
        }else
            map.put(items, 1);
    }

    public int getCount(String items){
        return map.get(items);
    }

    public PreferenceArray getPreferences(long userId){
        PreferenceArray prefs = new GenericUserPreferenceArray(map.size());
        prefs.setUserID(0, userId);

        int index = 0;
        for(Map.Entry<String, Integer> entry : map.entrySet()){
            prefs.setItemID(index, Long.valueOf(entry.getKey()));
            prefs.setValue(index, 1);

            index++;
        }

        return prefs;
    }

    public PreferenceArray getWeightedPreferences(long userId){
        PreferenceArray prefs = new GenericUserPreferenceArray(map.size());
        prefs.setUserID(0, userId);

        int index = 0;
        for(Map.Entry<String, Integer> entry : map.entrySet()){
            prefs.setItemID(index, Long.valueOf(entry.getKey()));
            prefs.setValue(index, entry.getValue() / max);

            index++;
        }

        return prefs;
    }
}
