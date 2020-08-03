package mvvm.view_model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;

import mvvm.model.Vet;
import mvvm.repository.PersistResponseCallback;
import mvvm.repository.VetRepository;
import okhttp3.MultipartBody;

public class VetViewModel extends ViewModel {

    private VetRepository vetRepository = VetRepository.getInstance();
    private PersistResponseCallback persistResponseCallback;

    public VetViewModel(){}

    public VetViewModel(PersistResponseCallback persistResponseCallback){
        this.persistResponseCallback = persistResponseCallback;
    }

    public void createVetWithImage(MultipartBody.Part body, Vet vet)
    {
        vetRepository.uploadVetPhoto(body, vet, persistResponseCallback, false);
    }


    public void editVetWithImage(MultipartBody.Part body, Vet vet)
    {
        vetRepository.uploadVetPhoto(body, vet, persistResponseCallback, true);
    }

    public void editVetWithoutImage(Vet vet)
    {
        vetRepository.editPet(vet, persistResponseCallback);
    }


    public void removePet(Vet vet)
    {
        vetRepository.removePet(vet.getId(), persistResponseCallback);
    }

    public MutableLiveData<List<Vet>> getVets() {
        return vetRepository.getVets();
    }

}
