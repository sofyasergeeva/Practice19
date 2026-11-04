package com.example.practice19;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UserActivity extends AppCompatActivity {

    private User activeUser;
    private EditText nameView;
    private EditText stateView;
    private EditText ageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user);

        int position = getIntent().getIntExtra(UserStaticInfo.POSITION, 0);
        activeUser = UserStaticInfo.users.get(position);

        nameView = findViewById(R.id.NameTextView);
        stateView = findViewById(R.id.StateTextView);
        ageView = findViewById(R.id.AgeTextView);

        nameView.setText(activeUser.getName());
        stateView.setText(activeUser.getState());
        ageView.setText(String.valueOf(activeUser.getAge()));

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(android.R.id.content),
                (v, insets) -> {
                    Insets systemBars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars()
                    );

                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return insets;
                }
        );
    }

    public void Back(View view) {
        onBackPressed();
    }

    public void Save(View view) {
        activeUser.setName(nameView.getText().toString());
        activeUser.setState(stateView.getText().toString());

        String age = ageView.getText().toString();

        try {
            activeUser.setAge(Integer.parseInt(age));
        } catch (NumberFormatException e) {
            activeUser.setAge(activeUser.getAge());
        }

        MainActivity.UpdateListAndUserPanel(activeUser);
        finish();
    }
}
