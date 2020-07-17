package com.example.notekeeper;

import android.os.Bundle;

import androidx.lifecycle.ViewModel;

public class NoteActivityViewModel extends ViewModel {
    public static final String ORIGINAL_NOTE_COURSE_ID = "com.example.notekeeper.ORIGINAL_NOTE_COURSE_ID";
    public static final String ORIGINAL_NOTE_TITLE = "com.example.notekeeper.ORIGINAL_NOTE_TITLE";
    public static final String ORIGINAL_NOTE_TEXT = "com.example.notekeeper.ORIGINAL_NOTE_TEXT";


    public String mOriginalNOteCourseID;
    public String mOriginalNoteTitle;
    public String mOriginalNoteText;
    public boolean mISNewlyCreated = true;


    //EVEN IF ACTIVITY AND VIEW MODEL ARE DESTROYED
    public void saveState(Bundle outState) {
        outState.putString(ORIGINAL_NOTE_COURSE_ID, mOriginalNOteCourseID);
        outState.putString(ORIGINAL_NOTE_TEXT,mOriginalNoteText);
        outState.putString(ORIGINAL_NOTE_TITLE, mOriginalNoteTitle);

    }

    public void restoreState(Bundle inState){
        mOriginalNoteTitle = inState.getString(ORIGINAL_NOTE_TITLE);
        mOriginalNoteText = inState.getString(ORIGINAL_NOTE_TEXT);
        mOriginalNOteCourseID = inState.getString(ORIGINAL_NOTE_COURSE_ID);
    }
}
