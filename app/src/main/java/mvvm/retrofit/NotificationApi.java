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

public interface NotificationApi {

    // New admin notification
    @FormUrlEncoded
    @POST("notification/createForAdmin")
    Call<ResponseBody> createAdminAppointmentNotification(
            @Field("user_id") int user_id,
            @Field("content") String content
    );

    // New user notification
    @FormUrlEncoded
    @POST("notification/createForUser")
    Call<ResponseBody> createUserAppointmentNotification(
            @Field("user_id") int user_id,
            @Field("content") String content
    );

    // Get all notifications for user
    @GET("notification/fetchForUser/{id}")
    Call<ResponseBody> getUserNotifications(@Path("id") int user_id);

    // Get all notifications for admin
    @GET("notification/fetchForAdmin")
    Call<ResponseBody> getAdminNotifications();

    // Get new notifications count for user
    @GET("notification/fetchCountForUser/{id}")
    Call<ResponseBody> getUserNotificationsCount(@Path("id") int user_id);
}


