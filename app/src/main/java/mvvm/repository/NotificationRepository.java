package mvvm.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import mvvm.model.Notification;
import mvvm.model.Vet;
import mvvm.retrofit.NotificationApi;
import mvvm.retrofit.Parser;
import mvvm.retrofit.RetrofitClient;
import mvvm.retrofit.VetApi;
import mvvm.util.Session;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationRepository
{
    private static NotificationRepository instance;
    private NotificationApi notificationApi;

    public static NotificationRepository getInstance()
    {
        if(instance == null) instance = new NotificationRepository();
        return instance;
    }

    public NotificationRepository() {
        notificationApi = RetrofitClient.getInstance().getNotificationApi();
    }

    /* add new admin appointment notification */

    public void createAdminAppointmentNotification(Notification notification){
        Call<ResponseBody> call = notificationApi.createAdminAppointmentNotification(notification.getUser().getId(), notification.getContent());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 201) {
                    Log.i("NOTIFICATION", "notification persisted");
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("NOTIFICATION_ERROR", "notification persist failed !");

            }
        });
    }

    /* add new user appointment notification */

    public void createUserAppointmentNotification(Notification notification){
        Call<ResponseBody> call = notificationApi.createUserAppointmentNotification(notification.getUser().getId(), notification.getContent());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 201) {
                    Log.i("NOTIFICATION", "notification persisted");
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("NOTIFICATION_ERROR", "notification persist failed !");
            }
        });
    }

    /* fetch user notifications */

    public MutableLiveData<List<Notification>> getUserNotifications() {
        final MutableLiveData<List<Notification>> notificationsMutableLiveData = new MutableLiveData<>();

        Call<ResponseBody> call = notificationApi.getUserNotifications(Session.getInstance().getUser().getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        if (jsonArray.length() > 0) {
                            try {
                                notificationsMutableLiveData.setValue(Parser.parseNotificationList(jsonArray));
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
                Log.e("Fetch appoin error: ", t.getMessage());
            }
        });

        return notificationsMutableLiveData;
    }

    /* fetch admin notifications */

    public MutableLiveData<List<Notification>> getAdminNotifications() {
        final MutableLiveData<List<Notification>> notificationsMutableLiveData = new MutableLiveData<>();

        Call<ResponseBody> call = notificationApi.getAdminNotifications();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        if (jsonArray.length() > 0) {
                            try {
                                notificationsMutableLiveData.setValue(Parser.parseNotificationList(jsonArray));
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
                Log.e("Fetch appoin error: ", t.getMessage());
            }
        });

        return notificationsMutableLiveData;
    }
}
