package mvvm.retrofit;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface AppointmentApi {

    // New appointment
    @FormUrlEncoded
    @POST("appointment/create")
    Call<ResponseBody> createVet(
            @Field("pet_id") int pet_id,
            @Field("vet_id") int vet_id,
            @Field("date") String date,
            @Field("reason") String reason,
            @Field("user_id") int user_id
    );

    // Get all appointment for user
    @GET("appointment/fetchForUser/{id}")
    Call<ResponseBody> getUserAppointments(@Path("id") int user_id);

    // Get all appointment for admin
    @GET("appointment/fetchForAdmin")
    Call<ResponseBody> getAdminAppointments();

    // Delete appointment
    @DELETE("appointment/delete/{id}")
    Call<ResponseBody> removeAppointment(@Path("id") int id);

    // Confirm appointment
    @PATCH("appointment/confirm/{id}")
    Call<ResponseBody> confirmAppointment(@Path("id") int id);
}


