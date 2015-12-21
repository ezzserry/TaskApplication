package com.example.kryptonworx.taskapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

/**
 * Created by kryptonWorx on 21-Dec-15.
 */
public class PhotoDetails extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private DetailsViewAdapter adapter;
    ProgressDialog loading = null;
    private ArrayList<PhotoItem> photoItemList;

    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photodetails);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            String ownerID = (String) bundle.get("owner_id");
            String secret = "1b8cb171e3815662";
            url= " https://api.flickr.com/services/rest/?method=flickr.people.getPhotos&api_key=f215a67afdb72fe1c0b48384925b047d&user_id=" + ownerID + "&format=json&nojsoncallback=1&api_sig=%s";
            String app_sign = MD5(secret + "api_keyf215a67afdb72fe1c0b48384925b047dformatjsonmethodflickr.people.getPhotosnojsoncallback1user_id"+ownerID);
            url = String.format(url, app_sign);
        }
        initViews();

        getOwner_photos();
    }

    private void getOwner_photos() {
        loading.show();
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

                                // adapter
                                adapter = new DetailsViewAdapter(PhotoDetails.this, photoItemList);
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

    private void initViews() {
        loading = new ProgressDialog(this);
        loading.setCancelable(true);
        loading.setMessage("Please Wait");
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_details);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
