package mvvm.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.androidminiproject.R;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import mvvm.util.Session;
import mvvm.view.fragment.AppointmentFragment;
import mvvm.view.fragment.NotificationFragment;
import mvvm.view.fragment.ProfileFragment;

public class HomeActivity extends AppCompatActivity{

    MeowBottomNavigation bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setupBottomNavigationView();
        loadFragment(new AppointmentFragment(HomeActivity.this));
        bottomNavigation.show(1, true);

       // bottomNavigation.setCount(2, "2");

    }

    @Override
    public void onBackPressed() {

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
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_profile));
        bottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                switch (model.getId()) {
                    case 1:
                        loadFragment(new AppointmentFragment(HomeActivity.this));
                        break;
                    case 2:
                        loadFragment(new NotificationFragment(HomeActivity.this, false));
                        break;
                    case 3:
                        loadFragment(new ProfileFragment(HomeActivity.this, Session.getInstance().getUser()));
                        break;
                }
                return null;
            }
        });
    }

}
