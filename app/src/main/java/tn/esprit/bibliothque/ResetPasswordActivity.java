package tn.esprit.bibliothque;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class ResetPasswordActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_resetpassword);
    }
    public void login(View view) {
        startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
    }
    public void mainActivity(View view) {
        startActivity(new Intent(ResetPasswordActivity.this, MainActivity.class));
        }
    public void register(View view) {
        startActivity(new Intent(ResetPasswordActivity.this, RegistrationActivity.class));
    }
    public void changepassword(View view) {
        startActivity(new Intent(ResetPasswordActivity.this, ResetPasswordActivity.class));
    }
    public void forgetpassword(View view) {
        startActivity(new Intent(ResetPasswordActivity.this, ResetPasswordActivity.class));
    }
    

}
