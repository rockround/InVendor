package com.invendordev.invendor;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.mobile.auth.ui.SignInUI;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
public class SwipeScreen extends AppCompatActivity{
    DynamoDBMapper dynamoDBMapper;
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
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {
                SignInUI signin = (SignInUI) AWSMobileClient.getInstance().getClient(
                        SwipeScreen.this,
                        SignInUI.class);
                signin.login(
                        SwipeScreen.this,
                        SwipeScreen.class).execute();
            }
        }).execute();

        AWSCredentialsProvider credentialsProvider = AWSMobileClient.getInstance().getCredentialsProvider();
        AWSConfiguration configuration = AWSMobileClient.getInstance().getConfiguration();


        // Add code to instantiate a AmazonDynamoDBClient
        AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(credentialsProvider);

        this.dynamoDBMapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(configuration)
                .build();
        createNews(credentialsProvider.getCredentials().getAWSAccessKeyId());
        setContentView(R.layout.activity_swipe_screen);


        final View background  = findViewById(R.id.background);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        final gestureListener gl = new gestureListener();
        final GestureDetector gdt = new GestureDetector(this, gl);

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

        }
    }
    public void createNews(String uid) {
        final ProjectDataDO newsItem = new ProjectDataDO();

        newsItem.setUserId(uid);
        newsItem.setYoutubeData("This is the article content");

        new Thread(new Runnable() {
            @Override
            public void run() {
                dynamoDBMapper.save(newsItem);
                // Item saved
            }
        }).start();
    }
    public void readNews(final String uid) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                ProjectDataDO newsItem = dynamoDBMapper.load(
                        ProjectDataDO.class,
                        uid);

                // Item read
                // Log.d("News Item:", newsItem.toString());
            }
        }).start();
    }
}
