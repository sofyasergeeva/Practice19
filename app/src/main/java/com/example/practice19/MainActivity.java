
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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    LayoutInflater layoutInflater;
    List<User> users = new ArrayList<>();
    UserListAdapter userListAdapter;

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

        AddUsersInList();
        Init();
    }

    private void AddUsersInList() {
        users.add(new User("Софья Сергеева", "В сети", 18));
        users.add(new User("Анна Миронова", "Занята", 20));
        users.add(new User("Максим Орлов", "На занятиях", 18));
        users.add(new User("Екатерина Волкова", "Доступна", 19));
    }

    private void Init() {
        listView = findViewById(R.id.listview);
        layoutInflater = LayoutInflater.from(this);

        userListAdapter = new UserListAdapter();
        listView.setAdapter(userListAdapter);
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

            nameView.setText(currentUser.getName());
            stateView.setText(currentUser.getState());

            return currentView;
        }
    }
}
