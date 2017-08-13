package com.hidayatasep.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getRecipeData();
    }

    public void getRecipeData(){
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Timber.e(e.toString());
                        Toast.makeText(MainActivity.this, R.string.error_label, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                Timber.d("result = " + result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(result.isEmpty()){
                            Toast.makeText(MainActivity.this, R.string.error_label, Toast.LENGTH_SHORT).show();
                        }else{

                        }
                    }
                });
            }
        });
    }
}
