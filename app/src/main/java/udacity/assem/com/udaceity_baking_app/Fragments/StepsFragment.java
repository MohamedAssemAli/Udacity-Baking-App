package udacity.assem.com.udaceity_baking_app.Fragments;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tests.assem.com.udaceity_baking_app.R;
import udacity.assem.com.udaceity_baking_app.Adapters.IngredientAdapter;
import udacity.assem.com.udaceity_baking_app.Adapters.StepAdapter;
import udacity.assem.com.udaceity_baking_app.App.AppConfig;
import udacity.assem.com.udaceity_baking_app.Models.IngredientModel;
import udacity.assem.com.udaceity_baking_app.Models.RecipeModel;
import udacity.assem.com.udaceity_baking_app.Models.StepModel;
import udacity.assem.com.udaceity_baking_app.Utils.BuildViews;
import udacity.assem.com.udaceity_baking_app.Utils.Imageutility;

import static tests.assem.com.udaceity_baking_app.R.layout.fragment_steps;


public class StepsFragment extends Fragment {

    // Vars
    private final String TAG = StepsFragment.class.getSimpleName();
    private ArrayList<StepModel> stepModelArrayList;
    private StepModel stepModel;
    private ExoPlayer player;
    private int index, currentWindow = 0;
    private long playbackPosition = 0;
    private boolean playWhenReady = true;
    // Views
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
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

    // OnClicks
    @OnClick(R.id.steps_fragment_next_btn)
    void increaseIndex() {
        if (index != stepModelArrayList.size() - 1) {
            index = index + 1;
            toggleIndicators(index);
            stepModel = stepModelArrayList.get(index);
            populateUi(stepModel);
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
            initializePlayer(stepModel);
        }
    }

    public StepsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(fragment_steps, container, false);
        ButterKnife.bind(this, view);
        if (getArguments() != null) {
            stepModelArrayList = (ArrayList<StepModel>) getArguments().getSerializable(AppConfig.INTENT_BUNDLE_KEY);
            index = getArguments().getInt(AppConfig.INTENT_STEP_INDEX);
            toggleIndicators(index);
            stepModel = stepModelArrayList.get(index);
            populateUi(stepModel);
        } else {
            closeOnError();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if (player == null)
            initializePlayer(stepModel);
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

    @Override
    public void onPause() {
        super.onPause();
        if (player != null) {
            releasePlayer();
        }
    }

    private void initializePlayer(StepModel stepModel) {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getContext()),
                new DefaultTrackSelector(), new DefaultLoadControl());
        playerView.setPlayer(player);
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        player.prepare(buildMediaSource(stepModel.getVideoURL()), true, false);
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
        toolbarTitle.setText(stepModel.getShortDescription());
        stepShortDescTxt.setText(stepModel.getShortDescription());
        stepsDescTxt.setText(stepModel.getDescription());
        if (stepModel.getVideoURL().isEmpty()) {
            noVideoPlaceHolder.setVisibility(View.VISIBLE);
            playerView.setVisibility(View.GONE);
        }
    }

    private MediaSource buildMediaSource(String url) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab"))
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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) playerView.getLayoutParams();
            params.width = params.MATCH_PARENT;
            params.height = params.MATCH_PARENT;
            playerView.setLayoutParams(params);
            Toast.makeText(getContext(), "ORIENTATION_LANDSCAPE", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) playerView.getLayoutParams();
            params.width = params.MATCH_PARENT;
            params.height = 280;
            playerView.setLayoutParams(params);
            Toast.makeText(getContext(), "ORIENTATION_PORTRAIT", Toast.LENGTH_SHORT).show();
        }
    }

    private void closeOnError() {
        Objects.requireNonNull(getActivity()).finish();
        Toast.makeText(requireContext(), R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }
}

/*
       if (savedInstanceState != null) {
            mStep = (Step) savedInstanceState.get(Globals.STEP);
            mStepsList = (List<Step>) savedInstanceState.get(Globals.STEPLIST);
            position = savedInstanceState.getLong(Globals.POSITION);
            mTwoPane = savedInstanceState.getBoolean("mTwoPane");
        }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("step", mStep);
        outState.putSerializable(Globals.STEPLIST, (Serializable) mStepsList);
        outState.putLong(Globals.POSITION, position);
        outState.putBoolean("mTwoPane",mTwoPane);
    }

 */