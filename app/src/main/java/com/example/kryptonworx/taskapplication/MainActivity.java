package com.example.kryptonworx.taskapplication;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private ArrayList<PhotoItem> photoItemList;
    private RecyclerView mRecyclerView;
    private MyRecyclerViewAdapter adapter;
    ProgressDialog loading = null;
    EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        loading = new ProgressDialog(this);
        loading.setCancelable(true);
        loading.setMessage("Please Wait");
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        etSearch = (EditText) findViewById(R.id.search);
        etSearch.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    String query = etSearch.getText().toString();
                    if(MyApplication.myList.containsKey(query)){
                        adapter = new MyRecyclerViewAdapter(MainActivity.this, MyApplication.myList.get(query));
                        mRecyclerView.setAdapter(adapter);
                    }
                    else
                    search(query);
                    return true;
                }
                return false;
            }
        });
    }

    private void search(final String query) {
        loading.show();

        String url = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=f215a67afdb72fe1c0b48384925b047d&text=%s&format=json&nojsoncallback=1&api_sig=%s";
        String secret = "1b8cb171e3815662";

        String app_sign = MD5(secret + "api_keyf215a67afdb72fe1c0b48384925b047dformatjsonmethodflickr.photos.searchnojsoncallback1text" + query);
        url = String.format(url, query, app_sign);

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String stat = response.getString("stat");
                            if (stat.equals("ok")) {
                                photoItemList = new ArrayList<>();
                                loading.dismiss();
                                Toast.makeText(getApplicationContext(), stat, Toast.LENGTH_LONG).show();

                                JSONArray jsonArray = response.getJSONObject("photos").getJSONArray("photo");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = (JSONObject) jsonArray.get(i);
                                    PhotoItem photoItem = new PhotoItem();
                                    photoItem.setTitle(object.getString("title"));
                                    photoItem.setFarm(object.getString("farm"));
                                    photoItem.setId(object.getString("id"));
                                    photoItem.setOwner(object.getString("owner"));
                                    photoItem.setSecret(object.getString("secret"));
                                    photoItem.setServer(object.getString("server"));
                                    photoItemList.add(photoItem);
                                }

                                MyApplication.myList.put(query,photoItemList);
                                // adapter
                                adapter = new MyRecyclerViewAdapter(MainActivity.this, photoItemList);
                                mRecyclerView.setAdapter(adapter);
                            } else {
                                loading.dismiss();
                                Toast.makeText(getApplicationContext(), stat, Toast.LENGTH_LONG).show();


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Try again please", Toast.LENGTH_LONG).show();
                            loading.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Try again please", Toast.LENGTH_LONG).show();
                        loading.dismiss();
                    }
                }
        ) {

        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(postRequest);

    }


    public String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }
}
