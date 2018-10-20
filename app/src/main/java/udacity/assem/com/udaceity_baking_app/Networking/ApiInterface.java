package udacity.assem.com.udaceity_baking_app.Networking;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import udacity.assem.com.udaceity_baking_app.App.AppConfig;
import udacity.assem.com.udaceity_baking_app.Models.RecipeModel;

public interface ApiInterface {

    @GET("baking.json")
    Call<List<RecipeModel>> RecipeModel();
}
