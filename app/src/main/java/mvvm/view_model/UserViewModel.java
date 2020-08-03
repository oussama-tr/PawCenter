package mvvm.view_model;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import mvvm.repository.LoginResponseCallback;
import mvvm.repository.PersistResponseCallback;
import mvvm.repository.UserRepository;

public class UserViewModel extends ViewModel {

    private UserRepository userRepository = UserRepository.getInstance();
    private LoginResponseCallback loginResponseCallback;
    private PersistResponseCallback persistResponseCallback;

    public UserViewModel(){}

    public UserViewModel(LoginResponseCallback loginResponseCallback){
        this.loginResponseCallback = loginResponseCallback;
    }

    public UserViewModel(PersistResponseCallback persistResponseCallback){
        this.persistResponseCallback = persistResponseCallback;
    }

    public void login(String username, String password)
    {
         userRepository.login(username, password, loginResponseCallback);
    }

    public void registerUser(String username, String firstName, String lastName, String email, int number, String password)
    {
        userRepository.registerUser(username, firstName, lastName, email, number, password, persistResponseCallback);
    }

}
