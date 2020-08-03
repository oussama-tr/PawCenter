package mvvm.view_model;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import mvvm.repository.PersistResponseCallback;

public class PetViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private PersistResponseCallback persistResponseCallback;

    public PetViewModelFactory() {}
    public PetViewModelFactory(PersistResponseCallback persistResponseCallback)
    {
        this.persistResponseCallback = persistResponseCallback;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(persistResponseCallback != null)
            return (T) new PetViewModel(persistResponseCallback);

        return (T) new PetViewModel();
    }
}
