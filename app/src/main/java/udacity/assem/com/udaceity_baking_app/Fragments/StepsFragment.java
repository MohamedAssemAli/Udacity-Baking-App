package udacity.assem.com.udaceity_baking_app.Fragments;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tests.assem.com.udaceity_baking_app.R;
import udacity.assem.com.udaceity_baking_app.App.AppConfig;
import udacity.assem.com.udaceity_baking_app.Models.StepModel;

import static tests.assem.com.udaceity_baking_app.R.layout.fragment_steps;


public class StepsFragment extends Fragment {

    // Vars
    private final String TAG = StepsFragment.class.getSimpleName();
    private ArrayList<StepModel> stepModelArrayList;
    private StepModel stepModel;
    private ExoPlayer player;
    private int index, currentWindow = 0;
    private long playbackPosition = 0, pos;
    private boolean isTwoPane, playWhenReady = true;
    // Views
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.steps_fragment_video_player)
    PlayerView playerView;
    @BindView(R.id.steps_fragment_no_video_placeholder)
    ImageView noVideoPlaceHolder;
    @BindView(R.id.steps_fragment_steps_short_desc)
    TextView stepShortDescTxt;
    @BindView(R.id.steps_fragment_steps_desc)
    TextView stepsDescTxt;
    @BindView(R.id.steps_fragment_previous_btn)
    Button previousBtn;
    @BindView(R.id.steps_fragment_next_btn)
    Button nextBtn;
    @BindView(R.id.steps_fragment_buttons_layout)
    RelativeLayout buttonsLayout;

    // OnClicks
    @OnClick(R.id.steps_fragment_next_btn)
    void increaseIndex() {
        if (index != stepModelArrayList.size() - 1) {
            index = index + 1;
            toggleIndicators(index);
            stepModel = stepModelArrayList.get(index);
            populateUi(stepModel);
            releasePlayer();
            initializePlayer(stepModel);
        }
    }

    @OnClick(R.id.steps_fragment_previous_btn)
    void decreaseIndex() {
        if (index != 0) {
            index = index - 1;
            toggleIndicators(index);
            stepModel = stepModelArrayList.get(index);
            populateUi(stepModel);
            releasePlayer();
            initializePlayer(stepModel);
        }
    }

    public StepsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(fragment_steps, container, false);
        ButterKnife.bind(this, view);
        stepModelArrayList = new ArrayList<>();
        if (getArguments() != null) {
            stepModelArrayList = (ArrayList<StepModel>) getArguments().getSerializable(AppConfig.INTENT_BUNDLE_KEY);
            isTwoPane = getArguments().getBoolean(AppConfig.INTENT_TWO_PANE_FLAG);
            index = getArguments().getInt(AppConfig.INTENT_STEP_INDEX);
            toggleIndicators(index);
            stepModel = stepModelArrayList.get(index);
            populateUi(stepModel);
        } else {
            closeOnError();
        }

        if (savedInstanceState != null) {
            currentWindow = savedInstanceState.getInt(AppConfig.SAVED_INSTANCE_CURRENT_WINDOW);
            playbackPosition = savedInstanceState.getLong(AppConfig.SAVED_INSTANCE_PLAYBACK_POS);
            pos = savedInstanceState.getLong("test");
            playWhenReady = savedInstanceState.getBoolean(AppConfig.SAVED_INSTANCE_PLAY_WHEN_READY);
            index = savedInstanceState.getInt(AppConfig.SAVED_INSTANCE_INDEX);
            toggleIndicators(index);
            stepModel = stepModelArrayList.get(index);
            populateUi(stepModel);
        }
        checkOrientation();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (player == null) {
            initializePlayer(stepModel);
        }
    }

    //    @Override
//    public void onPause() {
//        super.onPause();
//        if (player != null) {
//            playbackPosition = player.getCurrentPosition();
//            player.release();
//        }
//    }
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.stop();
            player.release();
            player = null;
        }
    }

    @SuppressLint("SetTextI18n")
    private void populateUi(StepModel stepModel) {
        stepShortDescTxt.setText(stepModel.getShortDescription());
        stepsDescTxt.setText(stepModel.getDescription());
        if (stepModel.getVideoURL().isEmpty() && stepModel.getThumbnailURL().isEmpty()) {
            noVideoPlaceHolder.setVisibility(View.VISIBLE);
            playerView.setVisibility(View.GONE);
        } else {
            noVideoPlaceHolder.setVisibility(View.GONE);
            playerView.setVisibility(View.VISIBLE);
        }
    }

    private MediaSource buildMediaSource(StepModel stepModel) {
        String url = "";
        if (!stepModel.getVideoURL().isEmpty())
            url = stepModel.getVideoURL();
        if (!stepModel.getThumbnailURL().isEmpty())
            url = stepModel.getThumbnailURL();
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("udacity_baking-app"))
                .createMediaSource(Uri.parse(url));
//                .createMediaSource(Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"));
    }

    private void toggleIndicators(int index) {
        if (index == 0) {
            previousBtn.setVisibility(View.GONE);
        } else if (index == stepModelArrayList.size() - 1) {
            nextBtn.setVisibility(View.GONE);
        } else {
            previousBtn.setVisibility(View.VISIBLE);
            nextBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(AppConfig.SAVED_INSTANCE_INDEX, index);
        outState.putInt(AppConfig.SAVED_INSTANCE_CURRENT_WINDOW, currentWindow);
        outState.putLong(AppConfig.SAVED_INSTANCE_PLAYBACK_POS, playbackPosition);
        outState.putLong("test", pos);
        outState.putBoolean(AppConfig.SAVED_INSTANCE_PLAY_WHEN_READY, playWhenReady);
    }

    private void closeOnError() {
        Objects.requireNonNull(getActivity()).finish();
        Toast.makeText(requireContext(), R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void checkOrientation() {
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE && !isTwoPane) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) playerView.getLayoutParams();
            params.width = params.MATCH_PARENT;
            params.height = params.MATCH_PARENT;
            playerView.setLayoutParams(params);
            hideSystemUi();
            toolbar.setVisibility(View.GONE);
            buttonsLayout.setVisibility(View.GONE);
        }
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void initializePlayer(StepModel stepModel) {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getContext()),
                new DefaultTrackSelector(), new DefaultLoadControl());
        playerView.setPlayer(player);
        player.seekTo(playbackPosition);
        player.setPlayWhenReady(playWhenReady);
        player.prepare(buildMediaSource(stepModel), false, false);
    }
}
