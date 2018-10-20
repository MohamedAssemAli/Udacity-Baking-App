package udacity.assem.com.udaceity_baking_app.Networking;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import udacity.assem.com.udaceity_baking_app.App.AppConfig;

public class ApiClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(AppConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}