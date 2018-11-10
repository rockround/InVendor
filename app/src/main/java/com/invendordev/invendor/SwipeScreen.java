package com.invendordev.invendor;

import android.app.ActionBar;
import android.media.MediaPlayer;
import android.net.Uri;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;


import java.io.PrintWriter;
import java.util.List;
import java.util.Stack;


import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
public class SwipeScreen extends AppCompatActivity implements View.OnClickListener{
    DynamoDBMapper dynamoDBMapper;
    private int timesDown = 0;
    private int timesUp = 0;
    private String name;
    PrintWriter out;
    VideoView videoView;
    ImageButton btnPlayPause;
    Stack<String> urls = new Stack<>();
    Stack<String> projIDs = new Stack<>();
    final String videoHead = "http://dl6ebpednx63u.cloudfront.net/";
    String currentProjID="";
    String currentVidID="";
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



        AWSCredentialsProvider credentialsProvider = AWSMobileClient.getInstance().getCredentialsProvider();
        AWSConfiguration configuration = AWSMobileClient.getInstance().getConfiguration();

        // Add code to instantiate a AmazonDynamoDBClient
        AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(credentialsProvider);

        this.dynamoDBMapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(configuration)
                .build();
        setContentView(R.layout.activity_swipe_screen);
        loadProjects();

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
                        sendCommit();
                        urls.pop();
                        projIDs.pop();
                        currentProjID = projIDs.peek();
                        currentVidID = urls.peek();
                    }
                    else if(gl.getDown() && !gl.getUp()){
                        background.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
                        timesDown++;
                        Log.i("Samuel", "Swiped down: " + timesDown);
                    }
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
    public void sendCommit() {
        final ProjectDataDO projectItem = new ProjectDataDO();

        projectItem.setUserId(currentProjID);
        projectItem.setCommitData(1);///make this better

        new Thread(new Runnable() {
            @Override
            public void run() {

                dynamoDBMapper.save(projectItem);

                // Item updated
            }
        }).start();
    }
    public void loadProjects() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
                for(ProjectDataDO d: dynamoDBMapper.scan(ProjectDataDO.class, scanExpression)){
                    urls.add(d.getYoutubeData());
                    projIDs.add(d.getUserId());
                    Log.i("dataf",""+d.getCommitData());
                }
                currentProjID = projIDs.peek();
                currentVidID = urls.peek();
                videoView = findViewById(R.id.videoView);
                btnPlayPause = findViewById(R.id.btn_play_pause);
                btnPlayPause.setOnClickListener(SwipeScreen.this);
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        try{
            if(!videoView.isPlaying()) {
                Uri uri = Uri.parse(videoHead+urls.pop());
                videoView.setVideoURI(uri);
                videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        btnPlayPause.setImageResource(R.drawable.bt_play);
                    }
                });
            }else{
                videoView.pause();
                btnPlayPause.setImageResource(R.drawable.bt_play);
            }
        }catch(Exception e){

        }
        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.start();
                btnPlayPause.setImageResource(R.drawable.bt_pause);
            }
        });
    }
}
