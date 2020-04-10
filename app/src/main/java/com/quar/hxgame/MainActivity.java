package com.quar.hxgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    ArrayAdapter adapter;
    int p=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        AddDatabaseListView(p);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search, menu);

        MenuItem item = menu.findItem(R.id.search);

        SearchView searchView = (SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.button) {
            if(p==1) {
                p=2;
                AddDatabaseListView(p);
                Toast.makeText(MainActivity.this, "Kirilcha", Toast.LENGTH_SHORT).show();
            } else if(p==2){
                p=1;
                AddDatabaseListView(p);
                Toast.makeText(MainActivity.this, "Lotincha", Toast.LENGTH_SHORT).show();
            }
            return  true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void AddDatabaseListView(int i){

        ListView listView =findViewById(R.id.listview);


        DatabaseHelper myDbHelper = new DatabaseHelper(MainActivity.this);
        ArrayList<String> listDataSuz = new ArrayList<>();
        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        try {
            myDbHelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }
        //  Toast.makeText(MainActivity.this, "Successfully Imported", Toast.LENGTH_SHORT).show();
        Cursor myData = myDbHelper.query("hxdata", null, null, null, null, null, null);

       // Cursor myData = myDbHelper.QueryABC();

        while (myData.moveToNext()) {
            listDataSuz.add(myData.getString(i));
        }

        adapter = new ArrayAdapter<String>(this, R.layout.raw, R.id.label, listDataSuz);
        listView.setAdapter(adapter);

        myData.close();
        myDbHelper.close();

    }

}
