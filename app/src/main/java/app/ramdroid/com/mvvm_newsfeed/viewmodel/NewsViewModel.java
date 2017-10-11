package app.ramdroid.com.mvvm_newsfeed.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import app.ramdroid.com.mvvm_newsfeed.model.pojo.NewsEntity;
import app.ramdroid.com.mvvm_newsfeed.repository.NewsRepository;

/**
 * A viewModel class, that provides data to the newsListActivity (the view)
 * handles the communication with the business part
 * it survives the configuration changes
 *
 * Created by ramadanmoustafa on 9/24/17.
 */

public class NewsViewModel extends ViewModel {
    private LiveData<List<NewsEntity>> mNewsList;
    private int mCurrentFeed;
    private NewsRepository mNewsRep;

    public LiveData<List<NewsEntity>> getNews(){
        if(mNewsList == null){
            mNewsRep = new NewsRepository();
            mNewsList = mNewsRep.getNewsList();

        }
        return mNewsList;
    }

    public int getCurrentFeed() {
        return mCurrentFeed;
    }

    public void setCurrentFeed(int currentFeed) {
        this.mCurrentFeed = currentFeed;
    }
}
