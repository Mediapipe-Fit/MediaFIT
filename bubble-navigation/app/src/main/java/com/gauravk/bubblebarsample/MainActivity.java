package com.gauravk.bubblebarsample;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button btnBottomNavigation = findViewById(R.id.open_bottom_navigation_bar);


        btnBottomNavigation.setOnClickListener(v -> launchBottomBarActivity());
    }

    private void launchBottomBarActivity() {
        Intent intent = new Intent(this, BottomBarActivity.class);
        startActivity(intent);
    }


}
