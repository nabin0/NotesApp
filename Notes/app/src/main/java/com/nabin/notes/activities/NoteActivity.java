package com.nabin.notes.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nabin.notes.LinedEditText;
import com.nabin.notes.R;
import com.nabin.notes.models.Note;
import com.nabin.notes.persistent.NoteRepository;
import com.nabin.notes.util.Utility;

public class NoteActivity extends AppCompatActivity implements View.OnTouchListener,
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener,
        View.OnClickListener,
        TextWatcher {

    // Ui component
    private LinedEditText mLinedEditText;
    private EditText mEditTitle;
    private TextView mTextTitle;
    private RelativeLayout mBackArrowContainer, mCheckContainer;
    private ImageButton mBackArrow, mCheckBtn;


    // vars
    private boolean isNewNote;
    private Note mInitialNote;
    private Note mFinalNote;
    private GestureDetector mGestureDetector;
    private static final int EDIT_MODE_ENABLED = 1;
    private static final int EDIT_MODE_DISABLED = 0;
    private int mMode;
    private NoteRepository mNoteRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        mLinedEditText = findViewById(R.id.edit_note);
        mEditTitle = findViewById(R.id.edit_title_text);
        mTextTitle = findViewById(R.id.text_title_view);
        mCheckContainer = findViewById(R.id.check_container);
        mCheckBtn = findViewById(R.id.image_check);
        mBackArrow = findViewById(R.id.image_arrow_back);
        mBackArrowContainer = findViewById(R.id.arrow_back_container);

        mNoteRepository = new NoteRepository(this);


        if (getIncomingIntent()) {
            // If it is a new Note (Show in Edit mode)
            setInitialNoteProperties();
            enableEditMode();
        } else {
            // if it is not a new Note (Show  in View Mode)
            setNoteProperties();
            disableContentInteraction();
        }

        setListeners();
    }

    private void disableContentInteraction() {
        mLinedEditText.setKeyListener(null);
        mLinedEditText.setFocusable(false);
        mLinedEditText.setFocusableInTouchMode(false);
        mLinedEditText.setCursorVisible(false);
        mLinedEditText.clearFocus();
    }

    private void enableContentInteraction() {
        mLinedEditText.setKeyListener(new EditText(this).getKeyListener());
        mLinedEditText.setFocusable(true);
        mLinedEditText.setFocusableInTouchMode(true);
        mLinedEditText.setCursorVisible(true);
        mLinedEditText.requestFocus();
    }

    private void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private boolean getIncomingIntent() {
        if (getIntent().hasExtra("selected_note")) {
            mInitialNote = getIntent().getParcelableExtra("selected_note");

            mFinalNote = new Note();
            mFinalNote.setTitle(mInitialNote.getTitle());
            mFinalNote.setContent(mInitialNote.getTitle());
            mFinalNote.setId(mInitialNote.getId());
            mFinalNote.setDate(mInitialNote.getDate());

            isNewNote = false;
            return false;
        }
        isNewNote = true;
        return true;
    }

    private void saveChanges() {
        if (isNewNote) {
            saveNewNote();
        } else {
            // Update
            saveUpdates();
        }
    }

    private void saveUpdates(){
        mNoteRepository.updateNote(mFinalNote);
    }

    private void saveNewNote() {
        mNoteRepository.insertNoteTask(mFinalNote);
    }

    private void enableEditMode() {
        mBackArrowContainer.setVisibility(View.GONE);
        mCheckContainer.setVisibility(View.VISIBLE);

        mTextTitle.setVisibility(View.GONE);
        mEditTitle.setVisibility(View.VISIBLE);

        mMode = EDIT_MODE_ENABLED;
        enableContentInteraction();
    }

    private void disableEditMode() {
        mCheckContainer.setVisibility(View.GONE);
        mBackArrowContainer.setVisibility(View.VISIBLE);

        mEditTitle.setVisibility(View.GONE);
        mTextTitle.setVisibility(View.VISIBLE);

        mMode = EDIT_MODE_DISABLED;
        disableContentInteraction();

        String temp = mLinedEditText.getText().toString();
        temp = temp.replace("\n", "");
        temp = temp.replace(" ", "");
        if (temp.length() > 0) {
            mFinalNote.setTitle(mEditTitle.getText().toString());
            mFinalNote.setContent(mLinedEditText.getText().toString());
            mFinalNote.setDate(Utility.getTimeStamp());

            if (!mInitialNote.getTitle().equals(mFinalNote.getTitle()) || !mInitialNote.getContent().equals(mFinalNote.getContent())) {
                saveChanges();
            }
        }

        mTextTitle.setText(mFinalNote.getTitle());
    }

    private void setListeners() {
        mGestureDetector = new GestureDetector(this, this);
        mLinedEditText.setOnTouchListener(this);
        mCheckBtn.setOnClickListener(this);
        mTextTitle.setOnClickListener(this);
        mBackArrow.setOnClickListener(this);

    }

    private void setNoteProperties() {
        mTextTitle.setText(mInitialNote.getTitle());
        mEditTitle.setText(mInitialNote.getTitle());
        mLinedEditText.setText(mInitialNote.getContent());
    }

    private void setInitialNoteProperties() {
        mTextTitle.setText("Note Title");
        mEditTitle.setText("Note Title");

        mInitialNote = new Note();
        mFinalNote = new Note();
        mInitialNote.setTitle("Note Title");
        mInitialNote.setContent("");
        mFinalNote.setTitle("Note Title");
        mFinalNote.setContent("Note Title");
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        enableEditMode();
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.image_check) {
            disableEditMode();
            hideSoftKeyboard();
        } else if (id == R.id.text_title_view) {
            enableEditMode();
            mEditTitle.requestFocus();
            mEditTitle.setSelection(mEditTitle.length());
        } else if (id == R.id.image_arrow_back) {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (mMode == EDIT_MODE_ENABLED) {
            onClick(mCheckBtn);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("mode", mMode);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mMode = savedInstanceState.getInt("mode");
        if (mMode == EDIT_MODE_ENABLED) {
            enableEditMode();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}


