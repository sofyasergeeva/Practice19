package com.example.practice19;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import static com.example.practice19.Transform.APP_PREFERENCES;
import static com.example.practice19.Transform.SaveUser;
import static com.example.practice19.UserStaticInfo.*;

public class SignActivity extends AppCompatActivity {

    private static final String DATABASE_URL =
            "https://practice19-sofya-default-rtdb.europe-west1.firebasedatabase.app";

    private EditText LoginTextView, PasswordTextView;
    private EditText NewLoginTextView, NewPasswordTextView;
    private EditText NewAgeTextView, NewNameTextView, NewStateTextView;

    private TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        View rootView = findViewById(android.R.id.content);

        ViewCompat.setOnApplyWindowInsetsListener(rootView, (view, windowInsets) -> {
            androidx.core.graphics.Insets insets = windowInsets.getInsets(
                    WindowInsetsCompat.Type.systemBars()
            );

            view.setPadding(
                    insets.left,
                    insets.top,
                    insets.right,
                    insets.bottom
            );

            return windowInsets;
        });

        ViewCompat.requestApplyInsets(rootView);

        Init();
        CheckSignInInfo();
    }

    private void Init() {
        tabHost = findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setContent(R.id.tabSignIn);
        tabSpec.setIndicator("Вход");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setContent(R.id.tabSignUp);
        tabSpec.setIndicator("Регистрация");
        tabHost.addTab(tabSpec);

        tabHost.setCurrentTab(0);

        LoginTextView = findViewById(R.id.LoginTextView);
        PasswordTextView = findViewById(R.id.PasswordTextView);

        NewLoginTextView = findViewById(R.id.NewLoginTextView);
        NewPasswordTextView = findViewById(R.id.NewPasswordTextView);
        NewAgeTextView = findViewById(R.id.NewAgeTextView);
        NewNameTextView = findViewById(R.id.NewNameTextView);
        NewStateTextView = findViewById(R.id.NewStateTextView);
    }

    private void CheckSignInInfo() {
        SharedPreferences sp = getSharedPreferences(
                APP_PREFERENCES,
                Context.MODE_PRIVATE
        );

        String login = sp.getString(LOGIN, "");
        String password = sp.getString(PASSWORD, "");

        LoginTextView.setText(login);
        PasswordTextView.setText(password);
    }

    private boolean EditTextNoNullWithAnimation(EditText editText) {
        boolean hasText = !editText.getText().toString().trim().isEmpty();

        if (!hasText) {
            Animation animation = AnimationUtils.loadAnimation(
                    this,
                    R.anim.error_edit
            );

            editText.startAnimation(animation);
        }

        return hasText;
    }

    public void SignIn(View view) {
        boolean valid = true;

        valid &= EditTextNoNullWithAnimation(LoginTextView);
        valid &= EditTextNoNullWithAnimation(PasswordTextView);

        if (!valid) {
            ShowEmptyFields();
            return;
        }

        String login = LoginTextView.getText().toString().trim();
        String password = PasswordTextView.getText().toString();

        FirebaseDatabase database = FirebaseDatabase.getInstance(DATABASE_URL);
        DatabaseReference myRef = database.getReference(USERS_SIGN_IN_INFO)
                .child(login);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String savedPassword = dataSnapshot.child(PASSWORD)
                        .getValue(String.class);

                if (savedPassword != null && savedPassword.equals(password)) {
                    Object id = dataSnapshot.child(PROFILE_ID).getValue();

                    if (id != null) {
                        goNext(String.valueOf(id), login, password);
                    } else {
                        CantSignIn();
                    }
                } else {
                    CantSignIn();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                ShowDatabaseError(error);
            }
        });
    }

    private String getNewLogin() {
        return NewLoginTextView.getText().toString().trim();
    }

    private String getNewPassword() {
        return NewPasswordTextView.getText().toString();
    }

    private int getNewAge() {
        return Transform.parseIntOrDefault(
                NewAgeTextView.getText().toString(),
                0
        );
    }

    private String getNewName() {
        return NewNameTextView.getText().toString().trim();
    }

    private String getNewState() {
        return NewStateTextView.getText().toString().trim();
    }

    public void SignUp(View view) {
        boolean valid = true;

        valid &= EditTextNoNullWithAnimation(NewLoginTextView);
        valid &= EditTextNoNullWithAnimation(NewPasswordTextView);
        valid &= EditTextNoNullWithAnimation(NewNameTextView);
        valid &= EditTextNoNullWithAnimation(NewStateTextView);

        if (!valid) {
            ShowEmptyFields();
            return;
        }

        String login = getNewLogin();
        String password = getNewPassword();
        String name = getNewName();
        String state = getNewState();
        int age = getNewAge();

        FirebaseDatabase database = FirebaseDatabase.getInstance(DATABASE_URL);

        DatabaseReference signInRef = database
                .getReference(USERS_SIGN_IN_INFO)
                .child(login);

        signInRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(
                            SignActivity.this,
                            R.string.UserExistMessage,
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }

                DatabaseReference profileRef = database
                        .getReference(USERS_PROFILE_INFO)
                        .push();

                String id = profileRef.getKey();

                if (id == null) {
                    Toast.makeText(
                            SignActivity.this,
                            "Не удалось создать профиль",
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }

                Map<String, Object> profile = new HashMap<>();
                profile.put(AGE, age);
                profile.put(NAME, name);
                profile.put(STATE, state);

                Map<String, Object> signInInfo = new HashMap<>();
                signInInfo.put(PASSWORD, password);
                signInInfo.put(PROFILE_ID, id);

                profileRef.setValue(profile)
                        .addOnSuccessListener(unused -> {
                            signInRef.setValue(signInInfo)
                                    .addOnSuccessListener(result -> {
                                        UserStaticInfo.profileId = id;

                                        Toast.makeText(
                                                SignActivity.this,
                                                "Пользователь зарегистрирован",
                                                Toast.LENGTH_SHORT
                                        ).show();
                                    })
                                    .addOnFailureListener(
                                            error -> ShowRegistrationError(error)
                                    );
                        })
                        .addOnFailureListener(
                                error -> ShowRegistrationError(error)
                        );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                ShowDatabaseError(error);
            }
        });
    }

    private void goNext(String profileId, String login, String password) {
        UserStaticInfo.profileId = profileId;

        SharedPreferences sp = getSharedPreferences(
                APP_PREFERENCES,
                Context.MODE_PRIVATE
        );

        SaveUser(sp, login, password);

        Toast.makeText(
                this,
                "Вход выполнен успешно",
                Toast.LENGTH_SHORT
        ).show();
    }

    private void CantSignIn() {
        Toast.makeText(
                this,
                R.string.CantSignInMessage,
                Toast.LENGTH_SHORT
        ).show();
    }

    private void ShowEmptyFields() {
        Vibrate();

        Toast.makeText(
                this,
                R.string.NullParametersMessage,
                Toast.LENGTH_SHORT
        ).show();
    }

    private void ShowDatabaseError(DatabaseError error) {
        Toast.makeText(
                this,
                error.getMessage(),
                Toast.LENGTH_SHORT
        ).show();
    }

    private void ShowRegistrationError(Exception error) {
        Toast.makeText(
                this,
                "Ошибка регистрации: " + error.getMessage(),
                Toast.LENGTH_LONG
        ).show();
    }

    private void Vibrate() {
        VibratorManager manager =
                (VibratorManager) getSystemService(VIBRATOR_MANAGER_SERVICE);

        if (manager != null) {
            Vibrator vibrator = manager.getDefaultVibrator();

            if (vibrator.hasVibrator()) {
                vibrator.vibrate(
                        VibrationEffect.createOneShot(
                                500,
                                VibrationEffect.DEFAULT_AMPLITUDE
                        )
                );
            }
        }
    }
}
