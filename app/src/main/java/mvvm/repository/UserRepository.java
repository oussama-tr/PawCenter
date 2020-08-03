package mvvm.repository;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import mvvm.retrofit.Parser;
import mvvm.retrofit.RetrofitClient;
import mvvm.retrofit.UserApi;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository
{
    private static UserRepository instance;
    private UserApi userApi;

    public static UserRepository getInstance()
    {
        if(instance == null) instance = new UserRepository();
        return instance;
    }

    public UserRepository() {
        userApi = RetrofitClient.getInstance().getUserApi();
    }

    /* register new user */
    public void registerUser(String username, String firstName, String lastName, String email, int number, String password, final PersistResponseCallback persistResponseCallback){
        Call<ResponseBody> call = userApi.createUser(username, firstName, lastName, email, number, password);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 201) {
                    persistResponseCallback.onAsyncCallFinished(true);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Register error", t.getMessage());
                persistResponseCallback.onAsyncCallFinished(false);
            }
        });
    }

    /* login user */
    public void login(String username, String password, final LoginResponseCallback loginResponseCallback){
        Call<ResponseBody> call = userApi.login(username, password);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        loginResponseCallback.onAsyncCallFinished(Parser.parseUser(new JSONObject(response.body().string())));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else
                    loginResponseCallback.onAsyncCallFinished(null);
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Login error", t.getMessage());
                loginResponseCallback.onAsyncCallFinished(null);
            }
        });
    }

    /* Update user info */
   /* public boolean updateUser(int id, String username, String firstName,
                              String lastName, String email, String bio,
                              boolean isPrivate){
        Call<ResponseBody> call = userApi.updateUser(id, username, firstName, lastName, email, bio, isPrivate);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 204) result = true;
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) { }
        });

        return result;
    }*/



    /* Update user profile photo */
   /* private boolean updateImageUrl(int id, String imageUrl){
        result = false;
        Call<ResponseBody> call = userApi.updateImageUrl(id, imageUrl);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 204) result = true;
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) { }
        });

        return result;
    }*/

    /*public boolean uploadProfilePhoto(MultipartBody.Part body)
    {
        result = false;
        Call<ResponseBody> call = userApi.uploadPhoto(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        updateImageUrl(Session.getInstance().getUser().getId(), jsonObject.getString("filename"));
                            result = true;
                            Session.getInstance().getUser().setImage_url(jsonObject.getString("filename"));

                    }
                    catch (IOException e)
                    {
                    } catch (JSONException e) {

                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
        return result;
    }*/



}
