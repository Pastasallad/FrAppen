package com.example.tfk17mhn.frappen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * @author Marcus Hermansson, tfk17mhn
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sthActivity(View caller) {
        startActivity(new Intent(this, com.example.tfk17mhn.frappen.SthActivity.class));
    }

    public void conversionActivity(View caller) {
        startActivity(new Intent(this, com.example.tfk17mhn.frappen.ConversionActivity.class));
    }

    public void restActivity(View caller) {
        startActivity(new Intent(this, com.example.tfk17mhn.frappen.RestActivity.class));
    }

    public void quit(View caller) {
        finish();
    }
}
