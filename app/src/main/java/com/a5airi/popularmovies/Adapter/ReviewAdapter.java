package com.a5airi.popularmovies.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.a5airi.popularmovies.R;
import com.a5airi.popularmovies.model.ReviewResult;
import com.a5airi.popularmovies.model.TrailerResult;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder>{


private Context context;
private List<ReviewResult> reviewResultList;


public ReviewAdapter(Context context, List<ReviewResult> reviewResultList) {
        this.context = context;
        this.reviewResultList = reviewResultList;
        }


    @Override
    public ReviewAdapter.ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int reviewLayout = R.layout.reviewlist_view;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(reviewLayout , parent , false);
        ReviewAdapter.ReviewHolder reviewHolder = new ReviewAdapter.ReviewHolder(view);
        return reviewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ReviewHolder holder, int position) {
        ReviewResult reviewResult = reviewResultList.get(position);
        String author = reviewResult.getAuthor();
        String content = reviewResult.getContent();
        holder.author.setText(author);
        holder.content.setText(content);
    }

    @Override
    public int getItemCount() {
        return reviewResultList.size();
    }


public class ReviewHolder extends RecyclerView.ViewHolder {

    TextView author;
    TextView content;

    public ReviewHolder(View itemView) {
        super(itemView);
        author = (TextView) itemView.findViewById(R.id.review_author);
        content = (TextView) itemView.findViewById(R.id.review_content);
    }

    }
}

