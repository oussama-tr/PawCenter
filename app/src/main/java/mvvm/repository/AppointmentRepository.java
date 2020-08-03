package mvvm.repository;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import mvvm.model.Appointment;
import mvvm.retrofit.AppointmentApi;
import mvvm.retrofit.Parser;
import mvvm.retrofit.RetrofitClient;
import mvvm.util.Session;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentRepository
{
    private static AppointmentRepository instance;
    private AppointmentApi appointmentApi;

    public static AppointmentRepository getInstance()
    {
        if(instance == null) instance = new AppointmentRepository();
        return instance;
    }

    public AppointmentRepository() {
        appointmentApi = RetrofitClient.getInstance().getAppointmentApi();
    }

    /* add new appointment */

    public void createAppointment(Appointment appointment, final PersistResponseCallback persistResponseCallback){
        Call<ResponseBody> call = appointmentApi.createVet(appointment.getPet().getId(), appointment.getVet().getId(), appointment.getDate(), appointment.getReason(), Session.getInstance().getUser().getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 201) {
                    try {
                        persistResponseCallback.onAsyncCallFinished(Parser.parseAppointment(new JSONObject(response.body().string())));
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

    /* fetch user appointments */

    public MutableLiveData<List<Appointment>> getUserAppointments() {
        final MutableLiveData<List<Appointment>> appointmentsMutableLiveData = new MutableLiveData<>();

        Call<ResponseBody> call = appointmentApi.getUserAppointments(Session.getInstance().getUser().getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        if (jsonArray.length() > 0) {
                            try {
                                appointmentsMutableLiveData.setValue(Parser.parseAppointmentList(jsonArray));
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

        return appointmentsMutableLiveData;
    }

    /* fetch user appointments */

    public MutableLiveData<List<Appointment>> getAdminAppointments() {
        final MutableLiveData<List<Appointment>> appointmentsMutableLiveData = new MutableLiveData<>();

        Call<ResponseBody> call = appointmentApi.getAdminAppointments();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        if (jsonArray.length() > 0) {
                            try {
                                appointmentsMutableLiveData.setValue(Parser.parseAppointmentList(jsonArray));
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

        return appointmentsMutableLiveData;
    }

    /* remove pet */

    public void removeAppointment(int id, final PersistResponseCallback persistResponseCallback){
        Call<ResponseBody> call = appointmentApi.removeAppointment(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("Fetch appoin error: ", response.toString());

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

    /* confirm appointment */

    public void confirmAppointment(int id, final PersistResponseCallback persistResponseCallback){
        Call<ResponseBody> call = appointmentApi.confirmAppointment(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("Fetch appoin error: ", response.toString());

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
}
