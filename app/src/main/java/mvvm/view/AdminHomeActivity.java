package mvvm.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.androidminiproject.R;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import mvvm.util.Session;
import mvvm.view.fragment.AdminAppointmentFragment;
import mvvm.view.fragment.NotificationFragment;
import mvvm.view.fragment.ProfileFragment;
import mvvm.view.fragment.VetFragment;

public class AdminHomeActivity extends AppCompatActivity{

    MeowBottomNavigation bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);

        setupBottomNavigationView();
        loadFragment(new AdminAppointmentFragment(AdminHomeActivity.this));
        bottomNavigation.show(1, true);

        findViewById(R.id.logout_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("UserPref", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();

                Intent loginIntent = new Intent(AdminHomeActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });

    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_main_container,fragment);
        fragmentTransaction.commit();
    }

    /**
     * BottomNavigationView setup
     */
    private void setupBottomNavigationView()
    {
        bottomNavigation = findViewById(R.id.buttomNavigation);
        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_paw));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_bell));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_user));

        bottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                switch (model.getId()) {
                    case 1:
                        loadFragment(new AdminAppointmentFragment(AdminHomeActivity.this));
                        break;
                    case 2:
                        loadFragment(new NotificationFragment(AdminHomeActivity.this, true));
                        break;
                    case 3:
                        loadFragment(new VetFragment(AdminHomeActivity.this));
                        break;
                }
                return null;
            }
        });
    }

    @Override
    public void onBackPressed() {

    }


}
