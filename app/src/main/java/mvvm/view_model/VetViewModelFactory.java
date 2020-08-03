package mvvm.view_model;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import mvvm.repository.PersistResponseCallback;

public class VetViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private PersistResponseCallback persistResponseCallback;

    public VetViewModelFactory() {}
    public VetViewModelFactory(PersistResponseCallback persistResponseCallback)
    {
        this.persistResponseCallback = persistResponseCallback;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(persistResponseCallback != null)
            return (T) new VetViewModel(persistResponseCallback);

        return (T) new VetViewModel();
    }
}
