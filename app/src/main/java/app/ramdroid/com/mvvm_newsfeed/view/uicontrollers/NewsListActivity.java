package app.ramdroid.com.mvvm_newsfeed.view.uicontrollers;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.List;

import app.ramdroid.com.mvvm_newsfeed.R;
import app.ramdroid.com.mvvm_newsfeed.model.pojo.NewsEntity;
import app.ramdroid.com.mvvm_newsfeed.view.adapter.NewsAdapter;
import app.ramdroid.com.mvvm_newsfeed.view.callback.OnFeedItemClickListener;
import app.ramdroid.com.mvvm_newsfeed.viewmodel.NewsViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A UI Controlller that displays data, returned by the viewmodel, and reacts to the user interactions.
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link NewsDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class NewsListActivity extends AppCompatActivity {

    private static final String TAG = NewsListActivity.class.getSimpleName();
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    @BindView(R.id.item_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    private NewsAdapter mAdapter;
    private NewsViewModel mViewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        ButterKnife.bind(this);
        Fresco.initialize(this);
        initRecyclerView();

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        mProgressBar.setVisibility(View.VISIBLE);
        mViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
        subscribeNewsObserver();
    }


    /**
     * Inites the recyclerview & adds a divider between items
     */
    private void initRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mAdapter = new NewsAdapter(new OnFeedItemClickListener() {
            @Override
            public void onFeedItemClicked(int pos) {
                mViewModel.setCurrentFeed(pos);
                displayFeedDetail();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }


    private void subscribeNewsObserver() {
        // Observe the LiveData observable object to update the news list adapter
        mViewModel.getNews().observe(this, new Observer<List<NewsEntity>>() {
            @Override
            public void onChanged(@Nullable List<NewsEntity> newsEntities) {
                //update ui
                mAdapter.updateDataSet(newsEntities);
                mProgressBar.setVisibility(View.GONE);

            }
        });
    }

    private void displayFeedDetail(){
        if(mTwoPane){
            NewsDetailFragment fragment = new NewsDetailFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container,fragment).commit();

        }
        else{
            NewsEntity entity = mViewModel.getNews().getValue().get(mViewModel.getCurrentFeed());
            if(entity != null) {
                String storyURL = entity.getArticleUrl();
                String title = entity.getTitle();
                String summary = entity.getSummary();
                String imageURL = "";
                if (entity.getMultimedia() != null && entity.getMultimedia().size() > 0)
                    imageURL = entity.getMultimedia().get(0).getUrl();
                Intent intent = new Intent(NewsListActivity.this, NewsDetailActivity.class);
                intent.putExtra(NewsDetailFragment.ARG_STORY_URL, storyURL);
                intent.putExtra(NewsDetailFragment.ARG_TITLE, title);
                intent.putExtra(NewsDetailFragment.ARG_SUMMARY, summary);
                intent.putExtra(NewsDetailFragment.ARG_IMAGE_URL, imageURL);

                startActivity(intent);
            }
        }
    }

}
