package com.example.notekeeper;

import android.content.Intent;
import android.os.Bundle;

import com.example.notekeeper.DataClasses.CourseInfo;
import com.example.notekeeper.DataClasses.DataManager;
import com.example.notekeeper.DataClasses.NoteInfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

public class NoteActivity extends AppCompatActivity {

    public static final String NOTE_POSITION  = "com.example.notekeeper.NOTE_POSITION";
    public static final int POSITION_NOT_SET = -1;
    private NoteInfo mNote;
    private Boolean mIsNewNote;
    private Spinner mSpinnerCourses;
    private EditText mTextNoteText;
    private EditText mTextNoteTitle;
    private int mNotePosition;
    private boolean mIsCancelling;
    private NoteActivityViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewModelProvider viewModelProvider = new ViewModelProvider(getViewModelStore(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()));
        mViewModel = viewModelProvider.get(NoteActivityViewModel.class);

        if (mViewModel.mISNewlyCreated && savedInstanceState!=null){
            mViewModel.restoreState(savedInstanceState);
        }
        mViewModel.mISNewlyCreated = false;


        mSpinnerCourses = findViewById(R.id.spinner_courses);
        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        ArrayAdapter<CourseInfo> adapterCourses = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,courses);
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCourses.setAdapter(adapterCourses);

        readDisplayStateValues();
        saveOriginalNoteValues();

        mTextNoteText = (EditText) findViewById(R.id.textNoteText);
        mTextNoteTitle =  (EditText) findViewById(R.id.textNoteTitle );
        if (!mIsNewNote){
            displayNotes(mSpinnerCourses, mTextNoteText, mTextNoteTitle);

        }


    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState!=null){
            mViewModel.saveState(outState);
        }
    }

    private void saveOriginalNoteValues() {
        if (mIsNewNote)
            return;
        mViewModel.mOriginalNOteCourseID = mNote.getCourse().getCourseId();
        mViewModel.mOriginalNoteText = mNote.getText();
        mViewModel.mOriginalNoteTitle = mNote.getTitle();


    }

    private void displayNotes(Spinner spinnerCourses, EditText textNoteText, EditText textNoteTitle) {
        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        int courseIndex = courses.indexOf(mNote.getCourse());
        spinnerCourses.setSelection(courseIndex);
        textNoteText.setText(mNote.getText());
        textNoteTitle.setText(mNote.getTitle());

    }

    private void readDisplayStateValues() {
        Intent intent = getIntent();
        int position = intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET);
        mIsNewNote = position == POSITION_NOT_SET;
        if (!mIsNewNote){
            mNote = DataManager.getInstance().getNotes().get(position);
        }
        else{
            createNewNote();
        }

        mNote = DataManager.getInstance().getNotes().get(mNotePosition);
    }

    private void createNewNote() {
        DataManager dm = DataManager.getInstance();
        mNotePosition = dm.createNewNote();
        mNote = dm.getNotes().get(mNotePosition);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mIsCancelling){
            if (mIsNewNote){
                DataManager.getInstance().removeNote(mNotePosition);
            }else {
                storePreviousNoteValues();
            }
        }
        else{
            saveNote();
        }
    }

    private void storePreviousNoteValues() {
        CourseInfo course = DataManager.getInstance().getCourse(mViewModel.mOriginalNOteCourseID);
        mNote.setCourse(course);
        mNote.setTitle(mViewModel.mOriginalNoteTitle);
        mNote.setText(mViewModel.mOriginalNoteText);
    }

    private void saveNote() {
        mNote.setCourse((CourseInfo) mSpinnerCourses.getSelectedItem());
        mNote.setTitle(mTextNoteTitle.getText().toString());
        mNote.setText(mTextNoteText.getText().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send_mail) {
            sendEmail();
            return true;
        }else if (id == R.id.action_cancel){
            mIsCancelling = true;
            finish();
        }else if(id == R.id.action_next){
            moveNext();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_next);
        int lastNoteIndex = DataManager.getInstance().getNotes().size() - 1;
        item.setEnabled(mNotePosition<lastNoteIndex);
        return super.onPrepareOptionsMenu(menu);

    }

    private void moveNext() {
        saveNote();
        ++mNotePosition;
        mNote = DataManager.getInstance().getNotes().get(mNotePosition);
        saveOriginalNoteValues();
        displayNotes(mSpinnerCourses, mTextNoteText, mTextNoteTitle);

        //Calls onPrepareOptionsMenu
        invalidateOptionsMenu();
    }

    private void sendEmail() {
        CourseInfo courses = (CourseInfo) mSpinnerCourses.getSelectedItem();
        String subject = mTextNoteTitle.getText().toString();
        String text = "Checkout what I learned in the PluralSight course\"" + courses.getTitle() + "\"\n" +
                mTextNoteText.getText().toString();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("messsage/rfc2822");
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT,text);
        startActivity(intent);

    }
}
