package mvvm.view.fragment;

import androidx.fragment.app.Fragment;

import mvvm.model.Vet;

public interface VetFragmentCallback {
    void deleteVet(Vet vet);
    void editVet(Vet vet);
}
