package com.rahimovjavlon1212.yourfeed.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.rahimovjavlon1212.yourfeed.models.TopicModel;

import java.util.List;

@Dao
public interface TopicDAO {

    @Insert
    void addTopic(TopicModel topicModel);

    @Delete
    void deleteTopic(TopicModel topicModel);

    @Query("select * from topics")
    List<TopicModel> getTopics();

}
