package mvvm.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;
import com.example.androidminiproject.R;
import com.google.android.material.textfield.TextInputLayout;
import mvvm.repository.PersistResponseCallback;
import mvvm.util.Common;
import mvvm.view.dialog.PersistSuccessDialog;
import mvvm.view.dialog.TosDialog;
import mvvm.view_model.UserViewModel;
import mvvm.view_model.UserViewModelFactory;

public class RegisterActivity extends AppCompatActivity implements PersistResponseCallback {

    UserViewModel userViewModel;
    TextInputLayout username, firstname, lastname, email, number, password;
    CheckBox tosCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userViewModel = ViewModelProviders.of(this, new UserViewModelFactory(this)).get(UserViewModel.class);
        username = findViewById(R.id.register_username);
        firstname = findViewById(R.id.register_firstName);
        lastname = findViewById(R.id.register_lastName);
        email = findViewById(R.id.register_email);
        number = findViewById(R.id.register_number);
        password = findViewById(R.id.register_password);
        tosCheckbox = findViewById(R.id.register_checkbox);

        findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });

        findViewById(R.id.tos_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tosIntent = new Intent(RegisterActivity.this, TosDialog.class);
                startActivity(tosIntent);
            }
        });
    }

    private boolean isFormValid()
    {
        if(username.getEditText().getText().toString().trim().equals("")){
            Toast.makeText(RegisterActivity.this, "Please enter a valid username !", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!Common.isNameValid(firstname.getEditText().getText().toString().trim())){
            Toast.makeText(RegisterActivity.this, "Please enter a valid firstname !", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!Common.isNameValid(lastname.getEditText().getText().toString().trim())){
            Toast.makeText(RegisterActivity.this, "Please enter a valid lastname !", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!Common.isEmailValid(email.getEditText().getText().toString().trim())){
            Toast.makeText(RegisterActivity.this, "Please verify your email !", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!Common.isPhoneNumberValid(number.getEditText().getText().toString().trim())){
            Toast.makeText(RegisterActivity.this, "Please verify your phone number !", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!Common.isPasswordValid(password.getEditText().getText().toString().trim())){
            Toast.makeText(RegisterActivity.this, "Password should be at least 8 characters long, contains at least a digit, a special character and an upper case character !", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!tosCheckbox.isChecked())
        {
            Toast.makeText(RegisterActivity.this, "Please read and accept our terms of service !", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void RegisterAction(View view) {
        if(isFormValid())
        {
           userViewModel.registerUser(
                username.getEditText().getText().toString(),
                firstname.getEditText().getText().toString(),
                lastname.getEditText().getText().toString(),
                email.getEditText().getText().toString(),
                Integer.valueOf(number.getEditText().getText().toString()),
                password.getEditText().getText().toString());
        }
    }

    @Override
    public void onAsyncCallFinished(Object result) {
        if((boolean) result){
            Intent registerSuccessIntent = new Intent(RegisterActivity.this, PersistSuccessDialog.class);
            registerSuccessIntent.putExtra("REDIRECT_TO", "LOGIN");
            registerSuccessIntent.putExtra("MESSAGE", getResources().getString(R.string.register_success_dialog));
            registerSuccessIntent.putExtra("BTN_TEXT", "Sign in");
            startActivity(registerSuccessIntent);
        }
        else
            Toast.makeText(RegisterActivity.this, "An unexpected error occured !", Toast.LENGTH_SHORT).show();
    }
}
