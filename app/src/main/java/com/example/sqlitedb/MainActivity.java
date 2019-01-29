package com.example.sqlitedb;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //importDB();
    }

    private void importDB(){
        // read babynames.sql into database named "babynames"
        SQLiteDatabase db = this.openOrCreateDatabase("babynames.db" , this.MODE_PRIVATE, null);
        Scanner scan = new Scanner(getResources()
                .openRawResource(R.raw.babynames));
        String query = "";
        while (scan.hasNextLine()) { // build and execute queries
            query += scan.nextLine() + "\n";
            if (query.trim().endsWith(";")) {
                db.execSQL(query);
                query = "";
            }
        }
    }

    public void showGraphClicked(View view) {
        String inputName = ((EditText)findViewById(R.id.et_input_name)).getText().toString();
        Switch sw = findViewById(R.id.sw_gender);
        String gender = (sw.isChecked())? "M": "F";
        SQLiteDatabase db = this.openOrCreateDatabase("babynames.db" , this.MODE_PRIVATE, null);

        GraphView graph = findViewById(R.id.gv_graph);

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();


        String sqlQuery = "SELECT year, rank FROM ranks WHERE name = "
                + "'" + inputName +"'" + " AND " + "sex = " + "'" + gender + "'";

        Cursor cursor = db.rawQuery(sqlQuery, null);
        if (cursor.moveToFirst()){
            do {
                int year = cursor.getInt(cursor.getColumnIndex("year"));
                int rank = cursor.getInt(cursor.getColumnIndex("rank"));

                DataPoint point = new DataPoint(year, rank);
                series.appendData(point, false, 100);

            } while (cursor.moveToNext());
            cursor.close();
        }else{
            Log.v("MainActivity", "Cursor Empty----------");
        }
        graph.addSeries(series);
    }

}
