package com.rahimovjavlon1212.yourfeed.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.rahimovjavlon1212.yourfeed.models.TopicModel;

import java.util.List;

@Dao
public interface TopicDAO {

    @Insert
    long addTopic(TopicModel topicModel);

    @Update
    void updateTopic(TopicModel topicModel);

    @Delete
    void deleteTopic(TopicModel topicModel);

    @Query("select * from topics")
    List<TopicModel> getTopics();

    @Query("select * from topics where id==:topicId")
    public List<TopicModel> getTopic(long topicId);

}
