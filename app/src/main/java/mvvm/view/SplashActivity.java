package mvvm.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.androidminiproject.R;

import mvvm.model.User;
import mvvm.repository.LoginResponseCallback;
import mvvm.util.Session;
import mvvm.view_model.UserViewModel;
import mvvm.view_model.UserViewModelFactory;

public class SplashActivity extends AppCompatActivity implements LoginResponseCallback {
    private static int SPLASH_TIME_OUT = 3000;
    Animation bottomAnim;
    ImageView logo;
    UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        logo = findViewById(R.id.logo);

        logo.setAnimation(bottomAnim);
        userViewModel = ViewModelProviders.of(this, new UserViewModelFactory(this)).get(UserViewModel.class);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("UserPref", 0);
                SharedPreferences.Editor editor = pref.edit();

                String username = pref.getString("username", null);
                String password = pref.getString("password", null);

                if(username != null && password != null)
                {
                    userViewModel.login(username, password);
                }

                else
                {
                    Intent loginIntent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    public void onAsyncCallFinished(User result) {
        if(result != null)
        {
            if(result.getRole().equals("admin"))
            {
                Intent adminHomeIntent = new Intent(SplashActivity.this, AdminHomeActivity.class);
                startActivity(adminHomeIntent);
            }
            else
            {
                Session.getInstance().setUser(result);
                Intent homeIntent = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(homeIntent);
            }

        }
        else{
            Intent loginIntent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }

    }
}
