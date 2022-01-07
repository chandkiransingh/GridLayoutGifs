package com.ck.gridlayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import static android.content.ContentValues.TAG;

public class SingleViewActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_view);

        Intent i = getIntent();
        int position = i.getExtras().getInt("id");
        String url = i.getStringExtra("url");
        Log.d(TAG, "onCreate: position is "+position);
        ImageView imageView = (ImageView) findViewById(R.id.SingleView);
        Glide.with(SingleViewActivity.this).load(url).into(imageView);
    }
}