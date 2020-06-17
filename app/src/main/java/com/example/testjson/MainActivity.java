package com.example.testjson;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {
    private String TAG =  MainActivity.class.getSimpleName();
    ArrayList<HashMap<String,String>> contactList;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);

        new GetContacts().execute();
    }

    private class GetContacts extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            Toast.makeText(getApplicationContext(),"JSON Data is Downloading", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            String url = "https://api.androidhive.info/contacts/";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response From URL"+ jsonStr);
            if (jsonStr != null){
                try {
                    //proses ambil data
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray contacts = jsonObj.getJSONArray("contacts");

                    //proses ambil kolom
                    for(int i = 0; i < contacts.length(); i++){
                        JSONObject c = contacts.getJSONObject(i);
                        String id = c.getString("id");
                        String name = c.getString("name");

                        HashMap<String, String> contact =  new HashMap<>();
                        contact.put("id",id);
                        contact.put("name",name);

                        contactList.add(contact);
                    }

                }catch (JSONException e){
                    Log.e(TAG, "JSON parsing error "+ e.getMessage());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(MainActivity.this, contactList, R.layout.list_item, new String[]{"id", "name"},
                    new int[] {R.id.identity, R.id.name});
            lv.setAdapter(adapter);
        }
    }
}