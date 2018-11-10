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
        setContentView(R.layout.main_content);
        initViewPager();
    }



    public void initViewPager(){
        viewPager = findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

    }

}
