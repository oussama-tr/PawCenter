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
import mvvm.model.Vet;
import mvvm.retrofit.Parser;
import mvvm.retrofit.PetApi;
import mvvm.retrofit.RetrofitClient;
import mvvm.retrofit.VetApi;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VetRepository implements PersistVetCallback
{
    private static VetRepository instance;
    private VetApi vetApi;

    public static VetRepository getInstance()
    {
        if(instance == null) instance = new VetRepository();
        return instance;
    }

    public VetRepository() {
        vetApi = RetrofitClient.getInstance().getVetApi();
    }

    /* upload vet photo */

    public void uploadVetPhoto(MultipartBody.Part body, final Vet vet, final PersistResponseCallback persistResponseCallback, final boolean isUpdate)
    {
        Call<ResponseBody> call = vetApi.uploadPhoto(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        onImageUploaded(jsonObject.getString("filename"), vet, persistResponseCallback, isUpdate);
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

    public void createVet(Vet vet, final PersistResponseCallback persistResponseCallback){
        Call<ResponseBody> call = vetApi.createVet(vet.getFirstname(), vet.getLastname(), vet.getImageUrl());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 201) {
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

    /* edit  pet */

    public void editPet(Vet vet, final PersistResponseCallback persistResponseCallback){
        Call<ResponseBody> call = vetApi.editVet(vet.getId(), vet.getFirstname(), vet.getLastname(), vet.getImageUrl());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 202) {
                    persistResponseCallback.onAsyncCallFinished(true);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("error", t.getMessage());
                persistResponseCallback.onAsyncCallFinished(true);
            }
        });
    }

    /* fetch user pets */

    public MutableLiveData<List<Vet>> getVets() {
        final MutableLiveData<List<Vet>> vetsMutableLiveData = new MutableLiveData<>();

        Call<ResponseBody> call = vetApi.getVets();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        if (jsonArray.length() > 0) {
                            try {
                                vetsMutableLiveData.setValue(Parser.parseVetList(jsonArray));
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
                Log.e("Fetch vets error: ", t.getMessage());
            }
        });

        return vetsMutableLiveData;
    }

    /* remove pet */

    public void removePet(int id, final PersistResponseCallback persistResponseCallback){
        Call<ResponseBody> call = vetApi.deleteVet(id);
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
    public void onImageUploaded(String imageUrl, Vet vet, PersistResponseCallback persistResponseCallback, boolean isUpdate) {
        vet.setImageUrl(imageUrl);
        if(isUpdate)
            editPet(vet, persistResponseCallback);
        else
            createVet(vet, persistResponseCallback);
    }
}
