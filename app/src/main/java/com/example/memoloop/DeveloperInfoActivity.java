package com.example.memoloop;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class DeveloperInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_info);

        Button btnExit = findViewById(R.id.btnDevExit);
        btnExit.setOnClickListener(v -> finish());
    }
}