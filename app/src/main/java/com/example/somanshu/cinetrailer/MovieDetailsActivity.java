package com.example.somanshu.cinetrailer;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.R.attr.path;

public class MovieDetailsActivity extends AppCompatActivity {

    TextView trailer;
    Movie m;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        final Movie movie=getIntent().getExtras().getParcelable("movie");
        m=movie;
        setTitle(movie.getOriginal_title());
        ImageView imageView=(ImageView)findViewById(R.id.movie_poster);
        Log.e("Called ","adap");
        Log.e("Image ",movie.getPoster_path());
        Picasso.with(this).load(movie.getPoster_path()).into(imageView);
        TextView title=(TextView)findViewById(R.id.originaltitle);
        trailer=(TextView)findViewById(R.id.trailer);
        title.setText(movie.getOriginal_title());
        TextView date=(TextView)findViewById(R.id.date);
        date.setText("Release date : "+movie.getRelease_date());
        TextView description=(TextView)findViewById(R.id.description);
        description.setText(movie.getOverview());
        String url=movie.getTrailer_path();
        QueryUtils util=new QueryUtils();
        util.execute(url);
    }
    public void setlistener(final String path)
    {
        if(path==null||path=="")
        {
            trailer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(MovieDetailsActivity.this,"No trailer available",Toast.LENGTH_SHORT);
                }
            });
        }
        else
        {
            trailer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    m.setTrailer_path("https://www.youtube.com/watch?v="+path);
                    Log.e("Final set thing ",m.getTrailer_path());
                    Intent it=new Intent(MovieDetailsActivity.this,YoutubeWeb.class);
                    it.putExtra("movie",m);
                    startActivity(it);
                }
            });
        }
    }
    private class QueryUtils extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String path=null;
            try
            {
                URL url=new URL(strings[0]);
                path=getpath(url);
            }
            catch (Exception e)
            {

            }
            return path;
        }


        @Override
        protected void onPostExecute(String s) {
            String path=null;
            if(s==null||s=="")
            {
                return ;
            }
            try
            {

                JSONObject root=new JSONObject(s);
                JSONArray arr=root.getJSONArray("results");
                root=arr.getJSONObject(0);
                path=root.getString("key");
            }
            catch (Exception e)
            {

            }
            setlistener(path);
        }
        protected String getpath(URL url)
        {
            HttpURLConnection connection=null;
            BufferedReader reader=null;
            InputStream inputStream=null;
            String path=null;
            try
            {
                connection=(HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                inputStream=connection.getInputStream();
                reader=new BufferedReader(new InputStreamReader(inputStream));
                String line;
                StringBuffer buffer=new StringBuffer();
                while((line=reader.readLine())!=null)
                {
                    buffer.append(line);
                }
                path=buffer.toString();
            }
            catch (Exception e)
            {

            }
            finally {
                try
                {

                    if(connection!=null)connection.disconnect();
                    if(inputStream!=null)inputStream.close();
                }
                catch (IOException e)
                {

                }
            }
            return path;
        }
    }
}
