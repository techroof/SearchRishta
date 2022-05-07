package com.techroof.searchrishta;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ShortlistedDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Shortlisted shortlisted);

    @Update
    void update(Shortlisted shortlisted);

    @Delete
    void delete(Shortlisted shortlisted);

    @Query("DELETE FROM shortlisted_table WHERE userid = :userid")
    abstract void deleteByUserId(String userid);


    @Query("DELETE  FROM shortlisted_table")
    //void deleteallnotes();
    void AllNotes();

    @Query("SELECT * FROM shortlisted_table")
    LiveData<List<Shortlisted>> getAllShortlisted();

}
