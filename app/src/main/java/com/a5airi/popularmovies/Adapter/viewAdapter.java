package com.a5airi.popularmovies.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.a5airi.popularmovies.Listed_data;
import com.a5airi.popularmovies.R;
import com.a5airi.popularmovies.model.JsonUtils;
import com.a5airi.popularmovies.moviesDB.MoviesContract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by khairy on 3/8/2018.
 */

public class viewAdapter extends RecyclerView.Adapter<viewAdapter.viewHolder> {


    private Cursor cursor;
    private Context context;
    JsonUtils setDetails;
    private Listed_data listed_data;
    private Boolean isFavorite;
    private List<String> MovieId= new ArrayList<String>();


    public viewAdapter(Context context , movie_onclickHandler mHandler , Listed_data data , Boolean Favorite) {
        this.context = context;
        this.listed_data = data;
        movieClickHandler = mHandler;
        this.isFavorite = Favorite;
    }

    final private movie_onclickHandler movieClickHandler ;

    public interface movie_onclickHandler{
        void movie_handler(int position , String MovieId);
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

        if (isFavorite){
            int idIndex = cursor.getColumnIndex(MoviesContract.MoviesDataBase.COLUMN_MOVIE_ID);
            int PhotoIndex = cursor.getColumnIndex(MoviesContract.MoviesDataBase.COLUMN_PHOTO_PATH);
            int TitleIndex = cursor.getColumnIndex(MoviesContract.MoviesDataBase.COLUMN_TITLE);

            cursor.moveToPosition(position);


            MovieId.add(cursor.getString(idIndex));
            String photopath = cursor.getString(PhotoIndex);
            String title = cursor.getString(TitleIndex);

            Picasso.with(context)
                    .load(photopath)
                    .into(holder.movie_img);
            holder.movie_title.setText(title);



        }else {
            setDetails = listed_data.getData_json().get(position);

            Picasso.with(context)
                    .load(setDetails.getCover_image())
                    .into(holder.movie_img);
            holder.movie_title.setText(setDetails.getTitle());
        }
    }



    @Override
    public int getItemCount() {
        if (isFavorite){
            if(cursor == null){
                return 0;
            }
            return cursor.getCount();
        }
        return listed_data.getData_json().size();
    }

    public Cursor swapCursor(Cursor c) {
        if (cursor == c) {
            return null;
        }
        Cursor temp = cursor;
        this.cursor = c;

        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
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
            String id = null;
            if (!MovieId.isEmpty()) {
                 id = MovieId.get(clicked_position);
            }
            movieClickHandler.movie_handler(clicked_position , id);
        }
    }


}
