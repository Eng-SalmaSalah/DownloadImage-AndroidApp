package com.salma.downloadimage;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    Button downloadBtn;
    EditText linkToImage;
    ImageView downloadedImage;
    URL imageURL;
    AsyncDownload asyncDownload;
    Bitmap flagImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        downloadBtn = (Button) findViewById(R.id.btnDownload);
        linkToImage = (EditText) findViewById(R.id.linkToDownload);
        downloadedImage = (ImageView) findViewById(R.id.imgDownloaded);
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    asyncDownload = new AsyncDownload();
                    imageURL = new URL(linkToImage.getText().toString());
                    asyncDownload.execute(imageURL);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    private class AsyncDownload extends AsyncTask<URL, Void, Bitmap> {


        @Override
        protected Bitmap doInBackground(URL... urls) {
            Bitmap image = null;
            image = download(urls[0]);
            return image;
        }

        private Bitmap download(URL url) {
            HttpsURLConnection httpsURLConnection;
            InputStream inputStream = null;
            try {
                httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.connect();
                inputStream = httpsURLConnection.getInputStream();
                flagImage = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return flagImage;

            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                downloadedImage.setImageBitmap(bitmap);
                Toast.makeText(MainActivity.this, "Image Downloaded Successfully", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(MainActivity.this, "Cannot Download Image", Toast.LENGTH_SHORT).show();

            }
        }
    }
}

