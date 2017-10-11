package app.ramdroid.com.mvvm_newsfeed.view.uicontrollers;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeView;
import com.facebook.imagepipeline.request.ImageRequest;

import app.ramdroid.com.mvvm_newsfeed.R;
import app.ramdroid.com.mvvm_newsfeed.model.pojo.NewsEntity;
import app.ramdroid.com.mvvm_newsfeed.viewmodel.NewsViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link NewsListActivity}
 * in two-pane mode (on tablets) or a {@link NewsDetailActivity}
 * on handsets.
 */
public class NewsDetailFragment extends Fragment {
    private static final String TAG = NewsDetailFragment.class.getSimpleName();
    @BindView(R.id.title)
    TextView mTitleView;
    @BindView(R.id.news_image)
    DraweeView mImageView;
    @BindView(R.id.summary_content)
    TextView mSummaryView;

    private String mStoryURL = "";
    private String mTitle = "";
    private String mSummary = "";
    private String mImageURL = "";

    public static final String ARG_STORY_URL = "mStoryURL";
    public static final String ARG_TITLE = "mTitle";
    public static final String ARG_SUMMARY = "mSummary";
    public static final String ARG_IMAGE_URL = "mImageURL";
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NewsDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getActivity() instanceof NewsListActivity){// Two-pane mode
            NewsViewModel viewModel = ViewModelProviders.of(getActivity()).get(NewsViewModel.class);
            if(viewModel.getNews() != null){
                NewsEntity entity = viewModel.getNews().getValue().get(viewModel.getCurrentFeed());
                if(entity != null){
                    mStoryURL = entity.getArticleUrl();
                    mTitle = entity.getTitle();
                    mSummary = entity.getSummary();
                    if(entity.getMultimedia() != null && entity.getMultimedia().size() >0)
                        mImageURL= entity.getMultimedia().get(0).getUrl();
                }
            }


        }
        else if (getArguments().containsKey(ARG_TITLE)) {
            Bundle extras = getArguments();
            mStoryURL = extras.getString(ARG_STORY_URL);
            mTitle = extras.getString(ARG_TITLE);
            mSummary = extras.getString(ARG_SUMMARY);
            mImageURL = extras.getString(ARG_IMAGE_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_detail, container, false);
        ButterKnife.bind(this, rootView);

        mTitleView.setText(mTitle);
        mSummaryView.setText(mSummary);
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setImageRequest(ImageRequest.fromUri(Uri.parse(mImageURL)))
                .setOldController(mImageView.getController()).build();
        mImageView.setController(draweeController);

        return rootView;
    }

    @OnClick(R.id.full_story_link)
    public void onFullStoryClicked() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(mStoryURL));
        startActivity(intent);
    }

}
