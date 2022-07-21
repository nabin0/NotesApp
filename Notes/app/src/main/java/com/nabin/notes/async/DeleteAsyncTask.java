package com.nabin.notes.async;

import android.os.AsyncTask;

import com.nabin.notes.models.Note;
import com.nabin.notes.persistent.NoteDao;

public class DeleteAsyncTask extends AsyncTask<Note, Void, Void> {
    private NoteDao mNoteDao;

    public DeleteAsyncTask(NoteDao noteDao){
        mNoteDao = noteDao;
    }

    @Override
    protected Void doInBackground(Note... notes) {
        mNoteDao.deleteNotes(notes);
        return null;
    }
}
