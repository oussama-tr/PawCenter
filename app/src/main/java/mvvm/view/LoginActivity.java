package mvvm.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;
import com.example.androidminiproject.R;
import com.google.android.material.textfield.TextInputLayout;
import mvvm.model.User;
import mvvm.repository.LoginResponseCallback;
import mvvm.util.Session;
import mvvm.view_model.UserViewModel;
import mvvm.view_model.UserViewModelFactory;


public class LoginActivity extends AppCompatActivity implements LoginResponseCallback {

    UserViewModel userViewModel;
    TextInputLayout username, password;
    CheckBox remember_me;

    @Override
    public void onBackPressed() {
        // do nothing
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userViewModel = ViewModelProviders.of(this, new UserViewModelFactory(this)).get(UserViewModel.class);
        username = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);
        remember_me = findViewById(R.id.remember_me_checkbox);

        findViewById(R.id.register_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });
    }

    public void LoginAction(View view) {
        if(username.getEditText().getText().toString().trim().equals("") || password.getEditText().getText().toString().trim().equals(""))
            Toast.makeText(LoginActivity.this, "Please enter your credentials first !", Toast.LENGTH_SHORT).show();
        else {
            userViewModel.login(
                    username.getEditText().getText().toString(),
                    password.getEditText().getText().toString());
        }
    }

    @Override
    public void onAsyncCallFinished(User result) {
        if(result != null)
        {
            if(result.getRole().equals("admin"))
            {
                Intent adminHomeIntent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                startActivity(adminHomeIntent);
            }
            else
            {
                Session.getInstance().setUser(result);
                Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(homeIntent);
            }

            if(remember_me.isChecked())
            {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("UserPref", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("username", username.getEditText().getText().toString());
                editor.putString("password", password.getEditText().getText().toString());
                editor.commit();
            }
        }
        else{
            Toast.makeText(LoginActivity.this, "Please verify your credentials !", Toast.LENGTH_SHORT).show();
        }
    }

}
