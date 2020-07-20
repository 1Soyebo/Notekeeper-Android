package com.example.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.example.notekeeper.DataClasses.CourseInfo;
import com.example.notekeeper.DataClasses.DataManager;
import com.example.notekeeper.DataClasses.NoteInfo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private AppBarConfiguration mAppBarConfiguration;
    private NoteRecylerAdapter mNoteRecylerAdapter;
    private RecyclerView mRecyclerItems;
    private LinearLayoutManager mNotesLinearLayoutManager;
    private NavigationView mNavigationView;
    private CourseRecylerAdapter mCourseRecylerAdapter;
    private GridLayoutManager mCourseLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intents take the sending activity you are leaving and the activity you' re going to
                startActivity(new Intent(MainActivity.this, NoteActivity.class));

            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_notes, R.id.nav_courses)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(mNavigationView, navController);
        mNavigationView.setNavigationItemSelectedListener(this);


        initialiseDisplayContent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //REFRESH == table.reloadData()
        mNoteRecylerAdapter.notifyDataSetChanged();
    }

    private void initialiseDisplayContent() {
        mRecyclerItems = (RecyclerView) findViewById(R.id.list_items);
        mNotesLinearLayoutManager = new LinearLayoutManager(this);
        mCourseLayoutManager = new GridLayoutManager(this,getResources().getInteger(R.integer.course_grid_span));
        List<NoteInfo> notes = DataManager.getInstance().getNotes();
        mNoteRecylerAdapter = new NoteRecylerAdapter(this,notes);

        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        mCourseRecylerAdapter = new CourseRecylerAdapter(this,courses);



        displayNotes();

    }

    private void displayNotes() {
        mRecyclerItems.setAdapter(mNoteRecylerAdapter);
        mRecyclerItems.setLayoutManager(mNotesLinearLayoutManager);
        selectNavigationMenuitem(R.id.nav_notes);
    }

    private void selectNavigationMenuitem(int id) {
        Menu menu = mNavigationView.getMenu();
        menu.findItem(id).setChecked(true);
    }

    private void displayCourses(){
        mRecyclerItems.setAdapter(mCourseRecylerAdapter);
        mRecyclerItems.setLayoutManager(mCourseLayoutManager);
        selectNavigationMenuitem(R.id.nav_courses);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings){
            startActivity(new Intent(this,SettingsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_notes){
            displayNotes();
        }else if (id == R.id.nav_courses){
            displayCourses();
        }

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true ;
    }

    private void handleNoteSelection(String message) {
        View view = findViewById(R.id.list_items);
        Snackbar.make(view,message,Snackbar.LENGTH_LONG).show();
    }
}
