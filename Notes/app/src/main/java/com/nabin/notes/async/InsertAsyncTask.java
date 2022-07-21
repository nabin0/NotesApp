package com.nabin.notes.async;

import android.os.AsyncTask;

import com.nabin.notes.models.Note;
import com.nabin.notes.persistent.NoteDao;

public class InsertAsyncTask extends AsyncTask<Note, Void, Void> {
    private NoteDao mNoteDao;

    public InsertAsyncTask(NoteDao noteDao){
        mNoteDao = noteDao;
    }

    @Override
    protected Void doInBackground(Note... notes) {
        mNoteDao.insertNotes(notes);
        return null;
    }
}
