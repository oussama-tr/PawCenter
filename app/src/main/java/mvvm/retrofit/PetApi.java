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

public interface PetApi {

    // New pet
    @FormUrlEncoded
    @POST("pet/create/{neutered}/{vaccinated}")
    Call<ResponseBody> createPet(
            @Field("name") String name,
            @Field("species") String species,
            @Field("gender") String gender,
            @Field("breed") String breed,
            @Field("age") int number,
            @Field("weight") float weight,
            @Field("imageUrl") String imageUrl,
            @Field("user_id") int user_id,
            @Path("neutered") boolean neutered,
            @Path("vaccinated") boolean vaccinated
    );

    // Upload pet photo to server
    @Multipart
    @POST("upload_image")
    Call<ResponseBody> uploadPhoto(
            @Part MultipartBody.Part image
    );

    // Get user pets
    @GET("pet/fetchPets/{id}")
    Call<ResponseBody> getUserPets(@Path("id") int user_id);

    // Delete pet
    @DELETE("pet/delete/{id}")
    Call<ResponseBody> deletePet(@Path("id") int id);

    // Edit pet
    @FormUrlEncoded
    @PATCH("pet/edit/{id}/{neutered}/{vaccinated}")
    Call<ResponseBody> editPet(
            @Path("id") int id,
            @Field("name") String name,
            @Field("species") String species,
            @Field("gender") String gender,
            @Field("breed") String breed,
            @Field("age") int number,
            @Field("weight") float weight,
            @Field("imageUrl") String imageUrl,
            @Path("neutered") boolean neutered,
            @Path("vaccinated") boolean vaccinated
    );

}


