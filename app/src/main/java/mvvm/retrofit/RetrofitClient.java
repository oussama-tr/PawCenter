package mvvm.retrofit;

import mvvm.util.Common;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = Common.BASE_URL;
    private static RetrofitClient mInstance;
    private Retrofit retrofit;

    private RetrofitClient()
    {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitClient getInstance(){
        if(mInstance == null){
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    public UserApi getUserApi(){
        return retrofit.create(UserApi.class);
    }
    public PetApi getPetApi(){
        return retrofit.create(PetApi.class);
    }
    public VetApi getVetApi(){
        return retrofit.create(VetApi.class);
    }
    public AppointmentApi getAppointmentApi(){
        return retrofit.create(AppointmentApi.class);
    }
    public NotificationApi getNotificationApi(){
        return retrofit.create(NotificationApi.class);
    }

}
