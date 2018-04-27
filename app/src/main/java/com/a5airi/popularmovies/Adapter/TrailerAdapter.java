package com.a5airi.popularmovies.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.a5airi.popularmovies.R;
import com.a5airi.popularmovies.model.TrailerResult;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerHolder>{


    private Context context;
    private List<TrailerResult> trailerJsonList;
    final private Trailer_onclickHandler trailerOnclickHandler;


    public TrailerAdapter(Context context, List<TrailerResult> trailerJson, Trailer_onclickHandler trailerOnclickHandler) {
        this.context = context;
        this.trailerJsonList = trailerJson;
        this.trailerOnclickHandler = trailerOnclickHandler;
    }

    public interface Trailer_onclickHandler{
        void trailer_handler(String key);
    }

    @Override
    public TrailerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int trailerLayout = R.layout.trailerlist_view;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(trailerLayout , parent , false);
        TrailerHolder trailerHolder = new TrailerHolder(view);
        return trailerHolder;
    }

    @Override
    public void onBindViewHolder(TrailerHolder holder, int position) {
        TrailerResult trailerResultt = trailerJsonList.get(position);
        String key = trailerResultt.getKey();
        String Url = "http://img.youtube.com/vi/" + key + "/0.jpg";
        Picasso.with(context)
                .load(Url)
                .into(holder.trailerImage);
    }

    @Override
    public int getItemCount() {
        return trailerJsonList.size();
    }


    public class TrailerHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        ImageView trailerImage;

        public TrailerHolder(View itemView) {
            super(itemView);
            trailerImage = (ImageView) itemView.findViewById(R.id.trailerimage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clicked_position = getAdapterPosition();
            TrailerResult trailerResult = trailerJsonList.get(clicked_position);
            String key = trailerResult.getKey();
            trailerOnclickHandler.trailer_handler(key);

        }
    }
}
