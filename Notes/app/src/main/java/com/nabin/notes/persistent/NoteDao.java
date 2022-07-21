package com.nabin.notes.persistent;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.nabin.notes.models.Note;
import java.util.List;

@Dao
public interface NoteDao {
    @Insert
    long[] insertNotes(Note... notes);

    @Query("SELECT * FROM notes")
    LiveData<List<Note>> getNotes();

    @Query("SELECT * FROM notes WHERE title = :title")
    List<Note> getNotesWithCustomQuery(String title);

    @Update
    int updateNotes(Note... notes);

    @Delete
    int deleteNotes(Note... notes);
}
