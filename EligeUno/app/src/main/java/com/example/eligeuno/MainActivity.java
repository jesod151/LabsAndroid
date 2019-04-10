package com.example.eligeuno;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.ExecutionException;



public class MainActivity extends AppCompatActivity {

    Elements images, juegos;
    ImageView imageView;
    Random rand;
    int a, b, c, d, winner, selected;
    Button ba, bb, bc, bd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imagen);

        getDocument downloadTask = new getDocument();
        Document result = null;
        try {
            result = downloadTask.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        ba = findViewById(R.id.btnA);
        bb = findViewById(R.id.btnB);
        bc = findViewById(R.id.btnC);
        bd = findViewById(R.id.btnD);

        ba.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected = 0;
                validateWin();
            }
        });

        bb.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected = 1;
                validateWin();
            }
        });

        bc.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected = 2;
                validateWin();
            }
        });

        bd.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected = 3;
                validateWin();
            }
        });

        juegos = result.select(".container-body h3");
        images = result.select(".media-wrapper.image img");
        images.remove(0);

        rand = new Random();

        resetGame();
    }

    public void validateWin(){

        if(selected == winner){
            Toast.makeText(this, "Has acertado!", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this, "Has fallado", Toast.LENGTH_LONG).show();
        }

        resetGame();

    }

    public void resetGame(){
        getWinner();
        setRands();
        setNames();
        new ImageDownloader(imageView).execute(images.get(getWinnerImage()).attr("src").toString());
    }

    public int getWinnerImage(){

        switch(winner){
            case 0:
                return a;
            case 1:
                return b;
            case 2:
                return c;
            case 3:
                return d;
        }
        return 0;
    }

    public void setNames(){

        ba.setText(juegos.get(a).toString().substring(juegos.get(a).toString().indexOf(". ") + 2, juegos.get(a).toString().indexOf("</")));
        bb.setText(juegos.get(b).toString().substring(juegos.get(b).toString().indexOf(". ") + 2, juegos.get(b).toString().indexOf("</")));
        bc.setText(juegos.get(c).toString().substring(juegos.get(c).toString().indexOf(". ") + 2, juegos.get(c).toString().indexOf("</")));
        bd.setText(juegos.get(d).toString().substring(juegos.get(d).toString().indexOf(". ") + 2, juegos.get(d).toString().indexOf("</")));

    }

    public void setRands(){

        int limit = juegos.size();
        a = rand.nextInt(limit);

        do{
            b = rand.nextInt(limit);
        }while(b == a);

        do{
            c = rand.nextInt(limit);
        }while(c == a || c == b);

        do{
            d = rand.nextInt(limit);
        }while(d == a || d == b || d == c);

    }

    public void getWinner(){

        winner = rand.nextInt(4);

    }

    public String getName(int index){

        return juegos.get(index).toString().substring(juegos.get(index).toString().indexOf(". ") + 2, juegos.get(index).toString().indexOf("</"));
    }

    public String getImageUrl(int index){

        return images.get(index).attr("src").toString();

    }

    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        ImageView imageView;

        public ImageDownloader(ImageView img){
            this.imageView = img;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.connect();
                InputStream inputStream = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap){
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);
        }
    }


    public class getDocument extends AsyncTask<Void, Void, Document> {

        @Override
        protected Document doInBackground(Void... params) {
            Document doc = null;
            try {
                doc = Jsoup.connect("https://www.hobbyconsolas.com/reportajes/100-juegos-miticos-nes-49310").get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return doc;
        }
    }
}



