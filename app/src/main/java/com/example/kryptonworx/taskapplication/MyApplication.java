package com.example.kryptonworx.taskapplication;

import android.app.Application;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kryptonWorx on 21-Dec-15.
 */
public class MyApplication extends Application {
    public static HashMap<String,ArrayList<PhotoItem>> myList;
    @Override
    public void onCreate() {
        super.onCreate();
        myList=new HashMap<>();
    }
}
