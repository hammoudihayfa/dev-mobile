package tn.esprit.bibliothque;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class ChangePasswordActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_changepassword);
    }
    public void mainActivity(View view) {
        startActivity(new Intent(ChangePasswordActivity.this, MainActivity.class));
    }
    public void login(View view) {
        startActivity(new Intent(ChangePasswordActivity.this, LoginActivity.class));
    }
    public void forgetpassword(View view) {
        startActivity(new Intent(ChangePasswordActivity.this, ResetPasswordActivity.class));
    }
    public void register(View view) {
        startActivity(new Intent(ChangePasswordActivity.this, RegistrationActivity.class));
    }
    public void changepassword(View view) {
        startActivity(new Intent(ChangePasswordActivity.this, ResetPasswordActivity.class));
    }


}
