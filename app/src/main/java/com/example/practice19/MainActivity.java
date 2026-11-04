package com.example.practice19;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.FrameLayout;
import java.util.List;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    LayoutInflater layoutInflater;
    FrameLayout userPanel;
    List<User> users = UserStaticInfo.users;
    static UserListAdapter userListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(android.R.id.content),
                (view, insets) -> {
                    androidx.core.graphics.Insets systemBars =
                            insets.getInsets(WindowInsetsCompat.Type.systemBars());

                    view.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return insets;
                }
        );

        new UserStaticInfo();
        Init();
    }


    private void Init() {
        listView = findViewById(R.id.listview);
        layoutInflater = LayoutInflater.from(this);

        userListAdapter = new UserListAdapter();
        listView.setAdapter(userListAdapter);

        userPanel = findViewById(R.id.userPanel);
    }


    public void BackToList(View view) {
        UserVisibility(false);
    }

    private void UserVisibility(boolean visible) {
        userPanel.setVisibility(visible ? View.VISIBLE : View.GONE);
    }


    private void InitPanel(User user) {
        TextView nameView = userPanel.findViewById(R.id.NameTextView);
        TextView stateView = userPanel.findViewById(R.id.StateTextView);
        TextView ageView = userPanel.findViewById(R.id.AgeTextView);

        nameView.setText(user.getName());
        stateView.setText(user.getState());
        ageView.setText(String.valueOf(user.getAge()));
    }

    public void GoToUserProfile(int position) {
        Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra(UserStaticInfo.POSITION, position);
        startActivity(intent);
    }


    public static void UpdateList() {
        userListAdapter.notifyDataSetChanged();
    }

    private class UserListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return users.size();
        }

        @Override
        public User getItem(int position) {
            return users.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View currentView, ViewGroup parent) {
            User currentUser = getItem(position);

            currentView = layoutInflater.inflate(
                    R.layout.item_user,
                    parent,
                    false
            );

            TextView nameView = currentView.findViewById(R.id.NameTextView);
            TextView stateView = currentView.findViewById(R.id.StateTextView);
            FrameLayout stateRound = currentView.findViewById(R.id.StateRound);

            nameView.setText(currentUser.getName());
            stateView.setText(currentUser.getState());

            switch (currentUser.getStateSignal()) {
                case 0:
                    stateRound.setBackgroundResource(R.drawable.back_offline);
                    break;
                case 1:
                    stateRound.setBackgroundResource(R.drawable.back_online);
                    break;
                case 2:
                    stateRound.setBackgroundResource(R.drawable.back_departed);
                    break;
            }

            currentView.setOnClickListener(v -> {
                GoToUserProfile(position);
            });

            return currentView;
        }

    }
}
