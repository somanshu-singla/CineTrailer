package com.example.somanshu.cinetrailer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
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

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class MainActivity extends AppCompatActivity implements IntentCaller{

    public MovieAdapter movieAdapter ;
    public ArrayList<Movie> mlist =new ArrayList<Movie>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        QueryUtils call = new QueryUtils(this);
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        calendar.add(Calendar.DATE,-31);
        String date = dateformat.format(calendar.getTime());
        String jsonlink="http://api.themoviedb.org/3/discover/movie?api_key=e4d10cb7c3b4358ff07ad16d7f546c25&append_to_response=videos&primary_release_date.gte="+date;
        Log.e("onCreate","Done");
        call.execute(jsonlink);
    }
    public MovieAdapter getadap()
    {
        return new MovieAdapter(this,mlist,this);
    }
    public void callIntent(Movie movie)
    {
        Intent it=new Intent(MainActivity.this,MovieDetailsActivity.class);
        it.putExtra("movie",movie);
        startActivity(it);
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
            JSONObject root=null;
            try
            {
                Log.e("url",strings[0]);
                root=new JSONObject(fetch(new URL(strings[0])));
                //for internet - uses permission
            }
            catch (Exception e)
            {
                Toast.makeText(mContext,"Network Error",Toast.LENGTH_SHORT);
                return root;
            }
            return root;
        }

        @Override
        protected void onPostExecute(JSONObject root) {
            if(root==null) {
                progressDialog.dismiss();
                Toast.makeText(mContext, "Network Error", Toast.LENGTH_SHORT);
                return;
            }
            Log.e("root ","obtained");
            try
            {

                JSONArray movielist=root.getJSONArray("results");
                Log.e("List huahua","lol");
                for(int i=0;i<movielist.length();i++)
                {
                    Log.e("loop ",""+i);
                    mlist.add(new Movie(movielist.getJSONObject(i)));
                }
                progressDialog.dismiss();
                Log.e("progressdialog","Dismissed");
                Log.e("List size ",""+movielist.length());
                movieAdapter=getadap();
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
                Log.e("Connection ","Done Line 95");
                connection.setRequestMethod("GET");
                Log.e("String ",url.toString());
                Log.e("Connection ","Done Line 96");
                connection.connect();
                Log.e("Connection ","Done Line 98");
                inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while((line=reader.readLine())!=null)
                {
                    fetched.append(line);
                }
                json=fetched.toString();
                Log.e("Line ","103");
            }
            catch (IOException exp)
            {
                Log.e("Line ","error");
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
                        return "";
                    }
                }
                Log.e("Everything ","closed");
            }
            return json;
        }
    }
}
