package mvvm.repository;

import mvvm.model.Pet;

public interface PersistPetCallback {
    void onImageUploaded(String imageUrl, Pet pet, PersistResponseCallback persistResponseCallback, boolean isUpdate);
}
