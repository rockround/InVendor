package com.invendordev.invendor;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class SwipeScreen extends AppCompatActivity{

    private int timesDown = 0;
    private int timesUp = 0;
    private Socket socket;
    private String name;
    PrintWriter out;
    SwipeScreen(){
        name = "";
        timesUp = 0;
        timesDown = 0;
    }

    SwipeScreen(String frameName){
        name = frameName;
        timesUp = 0;
        timesDown = 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_screen);


        String text = String.format(getResources().getString(R.string.frame_name),name);
        TextView mainText = findViewById(R.id.mainText);
        mainText.setText(text);

        final gestureListener gl = new gestureListener();
        final GestureDetector gdt = new GestureDetector(this, gl);
        final View background  = findViewById(R.id.background);
        background.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View view, final MotionEvent event) {

                if(gdt.onTouchEvent(event)){
                    if(!gl.getDown() && gl.getUp()) {
                        background.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                        timesUp++;
                        Log.i("Samuel", "Swiped up: " + timesUp);
                    }
                    else if(gl.getDown() && !gl.getUp()){
                        background.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
                        timesDown++;
                        Log.i("Samuel", "Swiped down: " + timesDown);
                    }
                }
                new Thread(new ClientThread()).start();

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

    public String getName() { return name; }

    public int getTimesUp() {
        return timesUp;
    }
    public int getTimesDown(){
        return timesDown;
    }

    public void setTimesUp(int i){
        timesUp = i;
    }
    public void setTimesDown(int i){
        timesDown = i;
    }
    class ClientThread implements Runnable{
        private static final int SERVERPORT = 1;
        private static final String SERVER_IP = "10.34.7.38";

        @Override
        public void run() {
            try {
                //InetAddress serverAddr = InetAddress.getByName(SERVER_IP);

                socket = new Socket("10.34.16.154", SERVERPORT);
                //Log.i("m1404525",(socket==null)+"");
                //Log.i("m1404525",(socket==null)+" dog");
                Log.i("pswd", "in cllientthread");
                try {

                    out = new PrintWriter(new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream())),
                            true);
                    out.println("Verify AkitoIto dog");
                    out.println("COUNT " + timesUp + " " + timesDown);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            //Log.i("m140","DG");

        }
    }
}
