package mvvm.view_model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;
import mvvm.model.Pet;
import mvvm.repository.PersistResponseCallback;
import mvvm.repository.PetRepository;
import okhttp3.MultipartBody;

public class PetViewModel extends ViewModel {

    private PetRepository petRepository = PetRepository.getInstance();
    private PersistResponseCallback persistResponseCallback;

    public PetViewModel(){}

    public PetViewModel(PersistResponseCallback persistResponseCallback){
        this.persistResponseCallback = persistResponseCallback;
    }

    public void createPetWithImage(MultipartBody.Part body, Pet pet)
    {
        petRepository.uploadPetPhoto(body, pet, persistResponseCallback, false);
    }

    public void createPetWithoutImage(Pet pet)
    {
        petRepository.createPet(pet, persistResponseCallback);
    }

    public void editPetWithImage(MultipartBody.Part body, Pet pet)
    {
        petRepository.uploadPetPhoto(body, pet, persistResponseCallback, true);
    }

    public void editPetWithoutImage(Pet pet)
    {
        petRepository.editPet(pet, persistResponseCallback);
    }

    public void removePet(Pet pet)
    {
        petRepository.removePet(pet.getId(), persistResponseCallback);
    }

    public MutableLiveData<List<Pet>> getUserPets(int user_id) {
        return petRepository.getUserPets(user_id);
    }

}
