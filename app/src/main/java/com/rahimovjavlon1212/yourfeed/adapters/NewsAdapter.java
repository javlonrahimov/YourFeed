package com.rahimovjavlon1212.yourfeed.adapters;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rahimovjavlon1212.yourfeed.R;
import com.rahimovjavlon1212.yourfeed.models.NewsModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsItemViewHolder> {

    private List<NewsModel> mData;
    public OnMoreClicked onMoreClicked;
    public int startPosition = 0;


    public NewsAdapter() {
        mData = new ArrayList<>();
    }

    public void addData(List<NewsModel> data) {
        startPosition = mData.size() - 1;
        this.mData.addAll(data);
        notifyItemRangeInserted(startPosition, mData.size() - 1);
    }

    public void setData(List<NewsModel> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewsItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0)
            return new NewsItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false));
        else
            return new NewsItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_more, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewsItemViewHolder holder, int position) {
        if (getItemViewType(position) == 1) {
            holder.itemView.setOnClickListener(v -> {
                if (onMoreClicked != null) {
                    onMoreClicked.onClick();
                    holder.progressBar.setVisibility(View.VISIBLE);
                    holder.textMore.setVisibility(View.INVISIBLE);
                }
            });
        } else {
            NewsModel news = mData.get(position);

            Picasso.get()
                    .load(news.getImageUri())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error_placeholder)
                    .into(holder.image);

            holder.title.setText(news.getTitle());
            holder.time.setText(news.getDate().substring(0, 10));
            holder.sectionName.setText(news.getSectionName());

            holder.itemView.setOnClickListener(e -> {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(news.getNewsUrl()));
                holder.itemView.getContext().startActivity(intent);
            });

            holder.shareButton.setOnClickListener(e -> {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, news.getNewsUrl());
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                holder.itemView.getContext().startActivity(Intent.createChooser(shareIntent, holder.itemView.getContext().getResources().getText(R.string.send_to)));
            });

            if (position == 0) {
                ViewGroup.MarginLayoutParams layoutParams =
                        (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
                layoutParams.setMargins(30, 200, 30, 30);
                holder.itemView.requestLayout();
            } else {
                ViewGroup.MarginLayoutParams layoutParams =
                        (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
                layoutParams.setMargins(30, 30, 30, 30);
                holder.itemView.requestLayout();
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mData.size()) {
            return 1;
        } else {
            return 0;
        }
    }

    public interface OnMoreClicked {
        void onClick();
    }
}

class NewsItemViewHolder extends RecyclerView.ViewHolder {

    ImageView image;
    TextView sectionName;
    TextView title;
    TextView time;
    ImageButton shareButton;
    TextView textMore;
    ProgressBar progressBar;

    NewsItemViewHolder(@NonNull View itemView) {
        super(itemView);
        sectionName = itemView.findViewById(R.id.sectionNameNewsItem);
        image = itemView.findViewById(R.id.imageNewsItem);
        title = itemView.findViewById(R.id.titleNewsItem);
        time = itemView.findViewById(R.id.timeNewsItem);
        shareButton = itemView.findViewById(R.id.shareButton);
        textMore = itemView.findViewById(R.id.textMore);
        progressBar = itemView.findViewById(R.id.progressBarMore);
    }
}
