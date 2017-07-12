package com.example.somanshu.cinetrailer;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by somanshu on 9/7/17.
 */

public class MovieAdapter extends ArrayAdapter<Movie> {
    private Context mcontext;
    public MovieAdapter(@NonNull Context context, @NonNull ArrayList<Movie> objects) {
        super(context,0, objects);
        mcontext=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null)
        {
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.movie_list,parent,false);
        }
        final Movie movie=getItem(position);
        //declared final because it's called from inner class
        ImageView imageView=(ImageView) convertView.findViewById(R.id.poster);
        Log.e("Image ",movie.getPoster_path());
        Picasso.with(mcontext).load(movie.getPoster_path()).into(imageView);
        TextView title=(TextView)convertView.findViewById(R.id.title);
        title.setText(movie.getOriginal_title());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it=new Intent(mcontext,MovieDetailsActivity.class);
                it.putExtra("movie",movie);
                mcontext.startActivity(it);
            }
        });
        return convertView ;
    }
}
