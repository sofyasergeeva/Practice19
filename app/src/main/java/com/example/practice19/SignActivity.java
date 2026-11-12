package com.example.practice19;

import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;
import android.view.View;
import android.widget.EditText;
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

import static com.example.practice19.UserStaticInfo.*;

public class SignActivity extends AppCompatActivity {

    private EditText LoginTextView, PasswordTextView;

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
    }

    private void Init() {
        LoginTextView = findViewById(R.id.LoginTextView);
        PasswordTextView = findViewById(R.id.PasswordTextView);
    }

    public void SignIn(View view) {
        String login = LoginTextView.getText().toString().trim();
        String password = PasswordTextView.getText().toString();

        if (login.isEmpty() || password.isEmpty()) {
            Vibrate();

            Toast.makeText(
                    this,
                    R.string.NullParametersMessage,
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance(
                "https://practice19-sofya-default-rtdb.europe-west1.firebasedatabase.app"
        );

        DatabaseReference myRef = database.getReference(USERS_SIGN_IN_INFO)
                .child(login);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String savedPassword = dataSnapshot.child(PASSWORD)
                        .getValue(String.class);

                if (savedPassword != null && savedPassword.equals(password)) {
                    Object profileIdValue = dataSnapshot.child(PROFILE_ID).getValue();

                    if (profileIdValue != null) {
                        goNext(String.valueOf(profileIdValue));
                    } else {
                        CantSignIn();
                    }
                } else {
                    CantSignIn();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(
                        SignActivity.this,
                        error.getMessage(),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void goNext(String profileId) {
        UserStaticInfo.profileId = profileId;

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
