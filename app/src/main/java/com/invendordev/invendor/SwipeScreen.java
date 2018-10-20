package com.invendordev.invendor;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class SwipeScreen extends AppCompatActivity{

    GestureDetector gestureScanner;
    private int timesDown = 0;
    private int timesUp = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_screen);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //mail icon button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        final GestureDetector gdt = new GestureDetector(this, new gestureListener());
        final View background  = findViewById(R.id.background);
        background.setOnTouchListener(new View.OnTouchListener() {
            boolean downed = false;
            public boolean onTouch(View view, final MotionEvent event) {

                if(gdt.onTouchEvent(event)){
                    background.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                    timesUp++;
                    Log.i("Samuel", "Swiped up: " + timesUp);
                }else if(!gdt.onTouchEvent(event) && !downed){
                    background.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
                    timesDown++;
                    Log.i("Samuel", "Swiped down: " + timesDown);
                    downed = true;
                }
                return true;
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_swipe_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
