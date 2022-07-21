package com.nabin.notes.util;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NotesListVerticalSpaceDecorator extends RecyclerView.ItemDecoration {
    private final int verticalSpaceValue;

    public NotesListVerticalSpaceDecorator(int verticalSpaceValue) {
        this.verticalSpaceValue = verticalSpaceValue;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.bottom = verticalSpaceValue;
    }
}
