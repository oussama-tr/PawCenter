package mvvm.view_model;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import mvvm.repository.LoginResponseCallback;
import mvvm.repository.PersistResponseCallback;

public class UserViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private LoginResponseCallback loginResponseCallback;
    private PersistResponseCallback persistResponseCallback;

    public UserViewModelFactory(LoginResponseCallback loginResponseCallback)
    {
        this.loginResponseCallback = loginResponseCallback;
    }

    public UserViewModelFactory(PersistResponseCallback persistResponseCallback)
    {
        this.persistResponseCallback = persistResponseCallback;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(loginResponseCallback != null)
            return (T) new UserViewModel(loginResponseCallback);
        else if(persistResponseCallback != null)
            return (T) new UserViewModel(persistResponseCallback);

        return (T) new UserViewModel();
    }
}
