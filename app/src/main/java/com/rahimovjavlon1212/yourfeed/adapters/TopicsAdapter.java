package com.rahimovjavlon1212.yourfeed.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.rahimovjavlon1212.yourfeed.R;
import com.rahimovjavlon1212.yourfeed.database.TopicsDatabase;
import com.rahimovjavlon1212.yourfeed.models.TopicModel;
import com.rahimovjavlon1212.yourfeed.utils.Utils;

import java.util.List;

import static com.rahimovjavlon1212.yourfeed.utils.Utils.DATABASE_NAME;

public class TopicsAdapter extends RecyclerView.Adapter<TopicItemViewHolder> {

    private List<TopicModel> mData;
    private TopicsDatabase topicsDatabase;

    public TopicsAdapter(Context context) {
        topicsDatabase = Room.databaseBuilder(context, TopicsDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();
    }

    public void setData(List<TopicModel> data) {
        this.mData = data;
    }

    @NonNull
    @Override
    public TopicItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TopicItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topic, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TopicItemViewHolder holder, int position) {

        TopicModel topic = mData.get(position);

        holder.name.setText(topic.getSectionName());

        holder.itemView.setOnClickListener(e -> {
            topicsDatabase.getTopicDao().addTopic(topic);
            int mPosition = mData.indexOf(topic);
            mData.remove(topic);
            notifyItemRemoved(mPosition);
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}


class TopicItemViewHolder extends RecyclerView.ViewHolder {

    TextView name;

    TopicItemViewHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.nameTopicItem);
    }
}