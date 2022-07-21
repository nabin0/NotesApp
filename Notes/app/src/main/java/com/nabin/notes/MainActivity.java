package com.nabin.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.nabin.notes.activities.NoteActivity;
import com.nabin.notes.adapters.NoteRecyclerAdapter;
import com.nabin.notes.interfaces.NoteClickListener;
import com.nabin.notes.models.Note;
import com.nabin.notes.persistent.NoteRepository;
import com.nabin.notes.util.NotesListVerticalSpaceDecorator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NoteClickListener
        , View.OnClickListener {

    // Ui components
    private RecyclerView mRecyclerView;


    // Vars
    private final ArrayList<Note> mNotes = new ArrayList<>();
    private NoteRecyclerAdapter mNoteAdapter;
    private NoteRepository mNoteRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.recyclerView);

        //Set Activity ActionBar
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        setTitle("Notes");

        findViewById(R.id.fab).setOnClickListener(this);

        mNoteRepository = new NoteRepository(this);

        initRecyclerView();
        retrieveNotes();
        // addDummyNote();


    }

    private void retrieveNotes() {
        mNoteRepository.retrieveNotesTask().observe(this, new Observer<List<Note>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(List<Note> notes) {
                if (mNotes.size() > 0) { // If notes are already in mNotes clear the list
                    mNotes.clear();
                }
                if (notes != null) {
                    mNotes.addAll(notes);
                    mNoteAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void addDummyNote() {
        for (int i = 0; i < 1000; i++) {
            mNotes.add(new Note("title: " + i, "content  " + i, "Dec 2023"));
        }
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        //Set RecyclerView Item decorator (Vertical space between items)
        NotesListVerticalSpaceDecorator decorator = new NotesListVerticalSpaceDecorator(10);
        mRecyclerView.addItemDecoration(decorator);

        //ItemTouchHelper for swipe operation
        new ItemTouchHelper(simpleCallbackItemTouchHelper).attachToRecyclerView(mRecyclerView);

        mNoteAdapter = new NoteRecyclerAdapter(mNotes, this);
        mRecyclerView.setAdapter(mNoteAdapter);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, NoteActivity.class);
        intent.putExtra("selected_note", mNotes.get(position));
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, NoteActivity.class);
        startActivity(intent);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void deleteNote(Note note) {
        mNotes.remove(note);
        mNoteRepository.deleteNote(note);
        mNoteAdapter.notifyDataSetChanged();
    }

    private final ItemTouchHelper.SimpleCallback simpleCallbackItemTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            deleteNote(mNotes.get(viewHolder.getAdapterPosition()));
        }
    };

}
