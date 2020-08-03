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

public interface VetApi {

    // New vet
    @FormUrlEncoded
    @POST("vet/create")
    Call<ResponseBody> createVet(
            @Field("firstname") String firstname,
            @Field("lastname") String lastname,
            @Field("imageUrl") String imageUrl
    );

    // Get all vets
    @GET("vet/fetchVets")
    Call<ResponseBody> getVets();

    // Upload pet photo to server
    @Multipart
    @POST("upload_image")
    Call<ResponseBody> uploadPhoto(
            @Part MultipartBody.Part image
    );

    // Delete vet
    @DELETE("vet/delete/{id}")
    Call<ResponseBody> deleteVet(@Path("id") int id);

    // Edit vet
    @FormUrlEncoded
    @PATCH("vet/edit/{id}")
    Call<ResponseBody> editVet(
            @Path("id") int id,
            @Field("firstname") String firstname,
            @Field("lastname") String lastname,
            @Field("imageUrl") String imageUrl
    );

}


