package com.nabin.notes.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nabin.notes.R;
import com.nabin.notes.interfaces.NoteClickListener;
import com.nabin.notes.models.Note;
import com.nabin.notes.util.Utility;

import java.util.ArrayList;

public class NoteRecyclerAdapter extends RecyclerView.Adapter<NoteRecyclerAdapter.ViewHolder> {
    private final NoteClickListener mNoteClickListener;
    private final ArrayList<Note> mNotes;

    public NoteRecyclerAdapter(ArrayList<Note> notes, NoteClickListener noteClickListener) {
        this.mNotes = notes;
        mNoteClickListener = noteClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_layout_list_item, parent, false);
        return new ViewHolder(view, mNoteClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            String dateTime = mNotes.get(position).getDate();
            String month = dateTime.substring(0, 2);
            month = Utility.getTextMonth(month);
            String year = dateTime.substring(3);
            dateTime = month + ' ' + year;

            holder.textTitle.setText(mNotes.get(position).getTitle());
            holder.textTimeStamp.setText(dateTime);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle, textTimeStamp;
        NoteClickListener noteClickListener;

        public ViewHolder(@NonNull View itemView, NoteClickListener noteClickListener) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.layout_title);
            textTimeStamp = itemView.findViewById(R.id.layout_timestamp);
            this.noteClickListener = noteClickListener;

            itemView.setOnClickListener(v -> {
                if (noteClickListener != null) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        noteClickListener.onItemClick(position);
                    }
                }
            });

        }

    }
}
