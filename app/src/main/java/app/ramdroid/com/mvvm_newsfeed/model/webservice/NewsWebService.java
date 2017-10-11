package app.ramdroid.com.mvvm_newsfeed.model.webservice;

import app.ramdroid.com.mvvm_newsfeed.model.pojo.NewsResponse;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by ramadanmoustafa on 9/26/17.
 */

public interface NewsWebService {
    static final String NEWS_SERVICE_BASE_URL = "https://raw.githubusercontent.com/rmoustafa/NewsExample/master/";
    @GET("news.json")
    Call<NewsResponse> getNews();
}
