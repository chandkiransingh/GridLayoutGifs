package com.ck.gridlayout;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ck.gridlayout.MyAdapters.ImageAdapter;
import com.ck.gridlayout.MyClasses.AppDatabase;
import com.ck.gridlayout.MyClasses.ImagesData;
import com.ck.gridlayout.MyClasses.ImagesDataOffline;
import com.codemonkeylabs.fpslibrary.FrameDataCallback;
import com.codemonkeylabs.fpslibrary.TinyDancer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    ProgressDialog loading = null;
    private int start = 0;
    private int end = 25;
    private int offset = 0;
    GridView gridview;
    public ArrayList<ImagesData> imglist = new ArrayList<ImagesData>();
    ImageAdapter imgAdapter;
    boolean connected = false;
    ImagesData imgData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TinyDancer.create()
                .show(MainActivity.this);

        //alternatively
        TinyDancer.create()
                .redFlagPercentage(.1f)
                .startingXPosition(200)
                .startingYPosition(600)
                .show(MainActivity.this);
        TinyDancer.create()
                .addFrameDataCallback(new FrameDataCallback() {
                    @Override
                    public void doFrame(long previousFrameNS, long currentFrameNS, int droppedFrames) {
                        //collect your stats here
                    }
                })
                .show(MainActivity.this);

        loading = new ProgressDialog(MainActivity.this);
        loading.setMessage("Please wait.....");
        gridview = (GridView) findViewById(R.id.gridview);
//        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View v, int position, long id){
                Intent i = new Intent(getApplicationContext(), SingleViewActivity.class);
                i.putExtra("id", position);
                i.putExtra("url", imgAdapter.getItem(position).toString());
                startActivity(i);
            }
        });

        gridview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                Log.d(TAG, "onScrollStateChanged: "+gridview.getChildAt(i).getBottom()+" ................."+ (gridview.getHeight() + gridview.getScrollY()));
                if (!gridview.canScrollVertically(1)) {
                    // bottom of scroll view
                    Log.d(TAG, "onScrollStateChanged: bottom");
                    if(connected){
                        loadNextDataFromApi();
                    }
                }
                if (!gridview.canScrollVertically(-1)) {
                    // top of scroll view
                    Log.d(TAG, "onScrollStateChanged: Top");
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
//                Toast.makeText(MainActivity.this, "Scrolling", Toast.LENGTH_SHORT).show();
            }
        });


        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
            getGifs(true);
        }
        else {
            connected = false;
            Toast.makeText(this, "No internet connection loading previous images", Toast.LENGTH_SHORT).show();
            getLocalImages();
        }
    }


    public void getGifs(final boolean showloader) {

        loading.setMessage("Fetching Gifs ...");
        if(showloader) {
            loading.show();
        }

        String uri = String.format("https://api.giphy.com/v1/stickers/trending?api_key=ihGPS35stL0VhASRNPAc6feokYuiTsCV&limit=25&rating=g&offset=%1$s", start);
        Log.d(TAG, "getGifs: url got here is "+uri+" and offset here is "+offset);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: response is "+response);
                        JSONObject jsonobject=null;
                        try {
                            jsonobject = new JSONObject(response);
//                            String data = jsonobject.getString("data");
//                            Log.d(TAG, "onResponse: data"+data);
//                            Log.d(TAG, "onResponse: data as json array: "+jsonobject.getJSONArray("data"));
//                            Log.d(TAG, "onResponse: pagination as json array: "+jsonobject.getJSONObject("pagination"));

                            JSONArray dataArray = jsonobject.getJSONArray("data");
                            JSONObject imageObject = null, allImages = null,imagePropery=null;
                            String urlGif = "",previewUrlGif = "";
                            int size = dataArray.length();

                            if(size>0){
                                for(int i=0;i<size;i++){
                                    imageObject = (JSONObject)dataArray.get(i);
                                    Log.d(TAG, "onResponse: imageobject is "+imageObject);
                                    allImages = imageObject.getJSONObject("images");
                                    imagePropery = allImages.getJSONObject("original");
                                    urlGif = (String) imagePropery.get("url");
                                    Log.d(TAG, "onResponse: original gif url is "+urlGif);
                                    imagePropery = allImages.getJSONObject("preview_webp");
                                    previewUrlGif = (String) imagePropery.get("url");
                                    Log.d(TAG, "onResponse: preview gif url is "+previewUrlGif);
                                    imgData = new ImagesData(urlGif,previewUrlGif);
                                    imglist.add(imgData);
                                    saveNewImage(urlGif,previewUrlGif);
                                    loading.dismiss();
                                }
                                Log.d(TAG, "onResponse: imgData.getPreviewImage() "+imgData.getPreviewImage());
                                imgAdapter = new ImageAdapter(MainActivity.this,imglist);
                                gridview.setAdapter(imgAdapter);
                                if(start>0){
                                    gridview.smoothScrollToPosition(start+25);
                                }
                            }
                            else{
                                Toast.makeText(MainActivity.this, "Sorry no more images found", Toast.LENGTH_SHORT).show();
                                loading.dismiss();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, "onResponse: Crashed json");
                            loading.dismiss();
                            Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Sorry no more images found", Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        ApplicationController.getInstance().addToRequestQueue(stringRequest);

    }

    public void loadNextDataFromApi() {
        System.out.println("page is "+offset);
        start = (offset*25)+1;
        end = end + 25;
        System.out.println("Start is "+start);
        System.out.println("End is "+end);
        offset = offset +1;
        getGifs(true);
    }

    public void saveNewImage(String urlGif, String previewUrlGif){
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());

        ImagesDataOffline imagesDataOffline = new ImagesDataOffline();
        imagesDataOffline.OriginalImageLocal = urlGif;
        imagesDataOffline.previewImageLocal = previewUrlGif;
        db.imgDataDao().insertImage(imagesDataOffline);
    }

    public void getLocalImages(){
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());

        List<ImagesDataOffline> images = db.imgDataDao().getAllImages();
        for(int j=0;j<images.size();j++){
            Log.d(TAG, "getLocalImages: "+images.get(j));
            ImagesDataOffline newImgobj = images.get(j);
            imgData = new ImagesData(newImgobj.OriginalImageLocal.toString(),newImgobj.previewImageLocal.toString());
            imglist.add(imgData);
        }

        imgAdapter = new ImageAdapter(MainActivity.this,imglist);
        Log.d(TAG, "getLocalImages: images get here "+images);
        gridview.setAdapter(imgAdapter);

    }

}