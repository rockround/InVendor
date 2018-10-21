package com.invendordev.invendor;
/**
 * Horizontal and Vertical ViewPager taken from
 * https://github.com/mohitsvnit/Vertical-ViewPager-Horizontal-ViewPager-Android
 *
 */


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


public class MainActivity extends AppCompatActivity {

    public MainActivity() {
    }

    public ViewPagerAdapter viewPagerAdapter;
    public VerticalViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.i("Main11", "We in boissss");
        setContentView(R.layout.main_content);
        //Log.i("Main11", "Right before initiating viewPager");
        initViewPager();
    }



    public void initViewPager(){
        viewPager = (VerticalViewPager) findViewById(R.id.viewPager);
        //Log.i("Main11", "We found the viewPagerView");
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        //Log.i("Main11", "We instantiated the viewpageradapter");
        viewPager.setAdapter(viewPagerAdapter);

    }

}
