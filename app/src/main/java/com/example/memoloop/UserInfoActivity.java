package com.example.memoloop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UserInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        TextView tvName = findViewById(R.id.tvUserNameVal);
        TextView tvEmail = findViewById(R.id.tvUserEmailVal);
        TextView tvDate = findViewById(R.id.tvUserCreatedVal);
        Button btnSignOut = findViewById(R.id.btnSignOutUser);
        Button btnEdit = findViewById(R.id.btnEditInfo);
        TextView tvGoToDev = findViewById(R.id.tvGoToDev);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            tvName.setText("User Name: " + (user.getDisplayName() != null ? user.getDisplayName() : "New User"));
            tvEmail.setText("Email: " + user.getEmail());
            
            long creationTimestamp = user.getMetadata().getCreationTimestamp();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            tvDate.setText("Account Created date: " + sdf.format(new Date(creationTimestamp)));
        }

        btnSignOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(UserInfoActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        btnEdit.setOnClickListener(v -> {
            startActivity(new Intent(UserInfoActivity.this, EditInfoActivity.class));
        });

        tvGoToDev.setOnClickListener(v -> {
            startActivity(new Intent(UserInfoActivity.this, DeveloperInfoActivity.class));
        });
        
        findViewById(R.id.btnBackUserInfo).setOnClickListener(v -> finish());
    }
}