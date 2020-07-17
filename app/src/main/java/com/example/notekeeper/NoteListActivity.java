package com.example.notekeeper;

import android.content.Intent;
import android.os.Bundle;

import com.example.notekeeper.DataClasses.DataManager;
import com.example.notekeeper.DataClasses.NoteInfo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class NoteListActivity extends AppCompatActivity {


    private NoteRecylerAdapter mNoteRecylerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NoteListActivity.this, NoteActivity.class));
            }
        });

        initialiseDisplayContent();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //REFRESH == table.reloadData()
        mNoteRecylerAdapter.notifyDataSetChanged();
    }

    private void initialiseDisplayContent() {
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list_notes);
        LinearLayoutManager notesLinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(notesLinearLayoutManager);

        List<NoteInfo> notes = DataManager.getInstance().getNotes();
        mNoteRecylerAdapter = new NoteRecylerAdapter(this,notes);
        recyclerView.setAdapter(mNoteRecylerAdapter);

    }

}
