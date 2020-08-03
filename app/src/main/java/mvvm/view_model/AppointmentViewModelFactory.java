package mvvm.view_model;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import mvvm.repository.PersistResponseCallback;

public class AppointmentViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private PersistResponseCallback persistResponseCallback;

    public AppointmentViewModelFactory() {}
    public AppointmentViewModelFactory(PersistResponseCallback persistResponseCallback)
    {
        this.persistResponseCallback = persistResponseCallback;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(persistResponseCallback != null)
            return (T) new AppointmentViewModel(persistResponseCallback);

        return (T) new AppointmentViewModel();
    }
}
