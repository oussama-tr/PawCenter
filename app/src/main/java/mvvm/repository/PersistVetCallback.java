package mvvm.repository;


import mvvm.model.Vet;

public interface PersistVetCallback {
    void onImageUploaded(String imageUrl, Vet vet, PersistResponseCallback persistResponseCallback, boolean isUpdate);
}
