package mvvm.repository;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import mvvm.model.Pet;
import mvvm.retrofit.Parser;
import mvvm.retrofit.PetApi;
import mvvm.retrofit.RetrofitClient;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PetRepository implements PersistPetCallback
{
    private static PetRepository instance;
    private PetApi petApi;

    public static PetRepository getInstance()
    {
        if(instance == null) instance = new PetRepository();
        return instance;
    }

    public PetRepository() {
        petApi = RetrofitClient.getInstance().getPetApi();
    }

    /* upload pet photo */

    public void uploadPetPhoto(MultipartBody.Part body, final Pet pet, final PersistResponseCallback persistResponseCallback, final boolean isUpdate)
    {
        Call<ResponseBody> call = petApi.uploadPhoto(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        onImageUploaded(jsonObject.getString("filename"), pet, persistResponseCallback, isUpdate);
                    }
                    catch (IOException e) { }
                    catch (JSONException e) { }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    /* add new pet */

    public void createPet(Pet pet, final PersistResponseCallback persistResponseCallback){
        Call<ResponseBody> call = petApi.createPet(pet.getName(), pet.getSpecies(), pet.getGender(), pet.getBreed(), pet.getAge(), pet.getWeight(), pet.getImageUrl(), pet.getOwner().getId(), pet.isNeutered(), pet.isVaccinated());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 201) {
                    try {
                        persistResponseCallback.onAsyncCallFinished(Parser.parsePet(new JSONObject(response.body().string())));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("error", t.getMessage());
                persistResponseCallback.onAsyncCallFinished(null);
            }
        });
    }

    /* edit  pet */

    public void editPet(Pet pet, final PersistResponseCallback persistResponseCallback){
        Call<ResponseBody> call = petApi.editPet(pet.getId(), pet.getName(), pet.getSpecies(), pet.getGender(), pet.getBreed(), pet.getAge(), pet.getWeight(), pet.getImageUrl(), pet.isNeutered(), pet.isVaccinated());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 202) {
                    try {
                        persistResponseCallback.onAsyncCallFinished(Parser.parsePet(new JSONObject(response.body().string())));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("error", t.getMessage());
                persistResponseCallback.onAsyncCallFinished(null);
            }
        });
    }

    /* fetch user pets */

    public MutableLiveData<List<Pet>> getUserPets(int user_id) {
        final MutableLiveData<List<Pet>> petsMutableLiveData = new MutableLiveData<>();

        Call<ResponseBody> call = petApi.getUserPets(user_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        if (jsonArray.length() > 0) {
                            try {
                                petsMutableLiveData.setValue(Parser.parsePetList(jsonArray));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                    } catch (IOException e) {}
                    catch (JSONException e) { e.printStackTrace();}
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Fetch pets error: ", t.getMessage());
            }
        });

        return petsMutableLiveData;
    }

    /* remove pet */

    public void removePet(int id, final PersistResponseCallback persistResponseCallback){
        Call<ResponseBody> call = petApi.deletePet(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 204) {
                    persistResponseCallback.onAsyncCallFinished(true);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("error", t.getMessage());
                persistResponseCallback.onAsyncCallFinished(false);
            }
        });
    }

    @Override
    public void onImageUploaded(String imageUrl, Pet pet, PersistResponseCallback persistResponseCallback, boolean isUpdate) {
        pet.setImageUrl(imageUrl);
        if(isUpdate)
            editPet(pet, persistResponseCallback);
        else
            createPet(pet, persistResponseCallback);
    }
}
