package com.a5airi.popularmovies.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.a5airi.popularmovies.Listed_data;
import com.a5airi.popularmovies.R;
import com.a5airi.popularmovies.model.JsonUtils;
import com.squareup.picasso.Picasso;


/**
 * Created by khairy on 3/8/2018.
 */

public class viewAdapter extends RecyclerView.Adapter<viewAdapter.viewHolder> {



    private Context context;
    JsonUtils setDetails;
    private Listed_data listed_data;




    public viewAdapter(Context context , movie_onclickHandler mHandler , Listed_data data) {
        this.context = context;
        this.listed_data = data;
        movieClickHandler = mHandler;
    }

    final private movie_onclickHandler movieClickHandler ;

    public interface movie_onclickHandler{
        void movie_handler(int position);
    }



    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layour_main_view = R.layout.main_view;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layour_main_view , parent , false);
        viewHolder holder = new viewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {


        setDetails = listed_data.getData_json().get(position);

        Picasso.with(context)
                .load(setDetails.getCover_image())
                .into(holder.movie_img);
        holder.movie_title.setText(setDetails.getTitle());

    }



    @Override
    public int getItemCount() {
        return listed_data.getData_json().size();
    }

    public  class  viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView movie_img ;
        TextView movie_title;

        public viewHolder(View itemView) {
            super(itemView);

            movie_img = (ImageView) itemView.findViewById(R.id.movie_imageView);
            movie_title = (TextView) itemView.findViewById(R.id.moview_textView);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int clicked_position = getAdapterPosition();
            movieClickHandler.movie_handler(clicked_position);
        }
    }


}
