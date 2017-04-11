package com.minminaya.rxjava_retrofit_demo.cache;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.minminaya.rxjava_retrofit_demo.App;
import com.minminaya.rxjava_retrofit_demo.model.Item;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.List;

/**
 * Created by Niwa on 2017/4/11.
 */

public class Database {

    private static String DATA_FILE_NAME = "data.db";
    private static Database INSTANCE;
    File dataFile = new File(App.getINSTANCE().getFilesDir(), DATA_FILE_NAME);
    Gson gson = new Gson();

    public Database() {
    }


    public static Database getINSTANCE() {
        if(INSTANCE == null){
            INSTANCE = new Database();
        }
        return INSTANCE;
    }


    public List<Item> readItems(){
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Reader reader = new FileReader(dataFile);
            return gson.fromJson(reader, new TypeToken<List<Item>>(){}.getType());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void writeItems(List<Item> items){
        String json = gson.toJson(items);

        try {
        if(!dataFile.exists()){
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

            Writer writer = new FileWriter(dataFile);
            writer.write(json);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void delete(){
        dataFile.delete();
    }



}
