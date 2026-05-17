package com.example.memoloop;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskListActivity extends AppCompatActivity {

    private RecyclerView rvTasks;
    private ImageView ivProfile;
    private FloatingActionButton fabAddTask;
    private List<Task> taskList;
    private TaskAdapter adapter;
    private FirebaseFirestore db;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getUid();

        rvTasks = findViewById(R.id.rvTasks);
        ivProfile = findViewById(R.id.ivTaskListLogo);
        fabAddTask = findViewById(R.id.fabAddTask);

        taskList = new ArrayList<>();
        adapter = new TaskAdapter(taskList);
        rvTasks.setLayoutManager(new LinearLayoutManager(this));
        rvTasks.setAdapter(adapter);

        loadTasksFromFirestore();

        ivProfile.setOnClickListener(v -> {
            startActivity(new Intent(TaskListActivity.this, UserInfoActivity.class));
        });

        fabAddTask.setOnClickListener(v -> showAddTaskDialog());
    }

    private void loadTasksFromFirestore() {
        if (userId == null) return;

        db.collection("users").document(userId).collection("tasks")
                .addSnapshotListener((value, error) -> {
                    if (error != null) return;

                    taskList.clear();
                    if (value != null) {
                        for (QueryDocumentSnapshot doc : value) {
                            Task task = new Task(
                                    doc.getString("title"),
                                    doc.getString("dueDate"),
                                    doc.getString("status")
                            );
                            taskList.add(task);
                        }
                    }
                    adapter.notifyDataSetChanged();
                });
    }

    private void showAddTaskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_task, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        EditText etTitle = view.findViewById(R.id.etTaskTitle);
        EditText etDate = view.findViewById(R.id.etDueDate);
        Button btnConfirm = view.findViewById(R.id.btnConfirmAdd);
        Button btnCancel = view.findViewById(R.id.btnCancelAdd);

        btnConfirm.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String date = etDate.getText().toString().trim();

            if (!title.isEmpty() && !date.isEmpty()) {
                saveTaskToFirestore(title, date);
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void saveTaskToFirestore(String title, String date) {
        if (userId == null) return;

        Map<String, Object> taskMap = new HashMap<>();
        taskMap.put("title", title);
        taskMap.put("dueDate", date);
        taskMap.put("status", "Pending");

        db.collection("users").document(userId).collection("tasks")
                .add(taskMap)
                .addOnFailureListener(e -> 
                    Toast.makeText(this, "Error saving task", Toast.LENGTH_SHORT).show()
                );
    }
}