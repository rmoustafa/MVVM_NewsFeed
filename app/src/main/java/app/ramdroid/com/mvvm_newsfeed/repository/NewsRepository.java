package app.ramdroid.com.mvvm_newsfeed.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import java.util.List;

import app.ramdroid.com.mvvm_newsfeed.model.pojo.NewsEntity;
import app.ramdroid.com.mvvm_newsfeed.model.pojo.NewsResponse;
import app.ramdroid.com.mvvm_newsfeed.model.webservice.NewsWebService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Repository module is responsible for handling data operations.
 * it provides clean Api to the service consumers (ViewModel)
 * it knows where to get the data from and what API calls to make when data is updated
 * You can consider it as mediator between different data sources (persistent model, web service, cache, etc.)
 * .
 * Created by ramadanmoustafa on 10/9/17.
 */

public class NewsRepository {
    private NewsWebService mNewsService;

    public NewsRepository(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NewsWebService.NEWS_SERVICE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mNewsService = retrofit.create(NewsWebService.class);
    }

    /**
     * fetches list of users news feeds from a web service
     */
    public LiveData<List<NewsEntity>> getNewsList() {
        final MutableLiveData<List<NewsEntity>> data = new MutableLiveData<>();
        mNewsService.getNews().enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if(response.isSuccessful())
                    data.setValue(response.body().getNewsList());
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                Log.e("Failed",t.getMessage().toString());
                data.setValue(null);

            }
        });
        return data;
    }
}
