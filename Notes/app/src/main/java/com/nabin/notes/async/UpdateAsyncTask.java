package com.nabin.notes.async;

import android.os.AsyncTask;

import com.nabin.notes.models.Note;
import com.nabin.notes.persistent.NoteDao;

public class UpdateAsyncTask extends AsyncTask<Note, Void, Void> {
    private NoteDao mNoteDao;

    public UpdateAsyncTask(NoteDao noteDao){
        mNoteDao = noteDao;
    }

    @Override
    protected Void doInBackground(Note... notes) {
        mNoteDao.updateNotes(notes);
        return null;
    }
}
