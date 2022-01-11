package com.example.android20;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetImage().execute("https://www.upmedia.mg/upload/article/20160815174424757360.jpg");
            }
        });

    }

    private class GetImage extends AsyncTask<String, Integer, Bitmap>{

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("載入圖片中...");
            pd.setCancelable(false);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pd.show();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            double progress = 0;
            Bitmap bitmap;
            for(String urlStr: params)
            {
                try {
                    URL url = new URL(urlStr);
                    bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                    publishProgress((int)(progress+=100/(double)params.length));
                    if(progress>=100){
                        Thread.sleep(500);
                        return bitmap;
                    }

                }catch (Exception e){
                    Log.e("TAG", "wtf");
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            pd.setProgress(values[0]);
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            pd.dismiss();
            imageView.setImageBitmap(bitmap);
        }

    }
}
