package com.example.somanshu.cinetrailer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity{

    public MovieAdapter movieAdapter ;
    public ArrayList<Movie> mlist =new ArrayList<Movie>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            calendar.add(Calendar.DATE, -30);
            String date = dateformat.format(calendar.getTime());
            String jsonlink = "http://api.themoviedb.org/3/discover/movie?api_key=e4d10cb7c3b4358ff07ad16d7f546c25&append_to_response=videos&primary_release_date.gte=" + date;
            Log.e("onCreate", "Done");
            QueryUtils call = new QueryUtils(this);
            call.execute(jsonlink);
        }
        catch (Exception e)
        {
            Log.e("onCreate","Error");
        }
    }
    private class QueryUtils extends AsyncTask<String,Void,JSONObject>
    {
        ProgressDialog progressDialog;
        Context mContext;
        public QueryUtils(Context context) {
            this.mContext = context;
        }
        @Override
        protected void onPreExecute() {
            progressDialog=new ProgressDialog(mContext);
            progressDialog.setMessage("Fetching Movie Data");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }
        @Override
        protected JSONObject doInBackground(String... strings) {
            String root=null;
            JSONObject jsonObject=null;
            try
            {
                Log.e("url",strings[0]);
                root=fetch(new URL(strings[0]));
                if(root==null)
                {
                    return null;
                }
                Log.e("doinbackground","Json string extracted");
            }
            catch (Exception e)
            {
                Log.e("fetch ","error doin" +
                        "background");
                return null;
            }
            try
            {
                jsonObject=new JSONObject(root);
            }
            catch (JSONException e)
            {
                return null;
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject root) {
            if(root==null) {
                Toast.makeText(mContext,"No internet Connection",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                return;
            }
            Log.e("onPostExecute ","root obtained");
            try
            {

                JSONArray movielist=root.getJSONArray("results");
                for(int i=0;i<movielist.length();i++)
                {
                    Log.e("loop ",""+i);
                    mlist.add(new Movie(movielist.getJSONObject(i)));
                }
                progressDialog.dismiss();
                Log.e("onPostExecute","progressdialog Dismissed");
                Log.e("List size ",""+movielist.length());
                movieAdapter=new MovieAdapter(mContext,mlist);
                ListView listView=(ListView)findViewById(R.id.listview);
                listView.setAdapter(movieAdapter);
                Log.e("list","created");
            }
            catch (Exception e)
            {
                progressDialog.dismiss();
            }
        }

        protected String fetch(URL url)
        {
            HttpURLConnection connection=null;
            InputStream inputStream=null;
            StringBuffer fetched=new StringBuffer();
            String json=null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                Log.e("String ",url.toString());
                connection.connect();
                Log.e("fetch module ","Connected");
                inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while((line=reader.readLine())!=null)
                {
                    fetched.append(line);
                }
                json=fetched.toString();
            }
            catch (Exception exp)
            {
                return null;
            }
            finally
            {
                if(connection!=null)
                {
                    connection.disconnect();
                }
                if(inputStream!=null)
                {
                    try {
                        inputStream.close();
                    }
                    catch (IOException e)
                    {
                        return null;
                    }
                }
                Log.e("fetch"," connection closed");
            }
            return json;
        }
    }
}
