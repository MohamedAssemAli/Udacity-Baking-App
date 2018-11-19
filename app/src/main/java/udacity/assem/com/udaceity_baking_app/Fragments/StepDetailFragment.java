//package udacity.assem.com.udaceity_baking_app.Fragments;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.constraint.ConstraintLayout;
//import android.support.v4.app.Fragment;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.Surface;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.android.exoplayer2.C;
//import com.google.android.exoplayer2.DefaultLoadControl;
//import com.google.android.exoplayer2.DefaultRenderersFactory;
//import com.google.android.exoplayer2.ExoPlayerFactory;
//import com.google.android.exoplayer2.Format;
//import com.google.android.exoplayer2.Player;
//import com.google.android.exoplayer2.SimpleExoPlayer;
//import com.google.android.exoplayer2.audio.AudioRendererEventListener;
//import com.google.android.exoplayer2.decoder.DecoderCounters;
//import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
//import com.google.android.exoplayer2.source.ExtractorMediaSource;
//import com.google.android.exoplayer2.source.MediaSource;
//import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
//import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
//import com.google.android.exoplayer2.trackselection.TrackSelection;
//import com.google.android.exoplayer2.ui.PlayerView;
//import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
//import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
//import com.google.android.exoplayer2.util.Util;
//import com.google.android.exoplayer2.video.VideoRendererEventListener;
//import com.google.gson.Gson;
//
//import org.w3c.dom.Text;
//
//import java.io.Serializable;
//import java.util.List;
//
//import example.example.com.bakingapp.Model.Globals;
//import example.example.com.bakingapp.Model.Recipe;
//import example.example.com.bakingapp.Model.Step;
//import example.example.com.bakingapp.R;
//
//public class StepDetailFragment extends Fragment {
//
//    private ComponentListener componentListener;
//    private Step mStep;
//    private List<Step> mStepsList;
//    private Context mContext;
//    private TextView mNoThumbTxt;
//    private TextView mInstructionView;
//    private TextView mIndexText;
//    private Button nextBtn;
//    private Button previousBtn;
//    private boolean mTwoPane;
//
//    private PlayerView playerView;
//    private SimpleExoPlayer player;
//    private int currentWindow = 0;
//    private boolean playWhenReady = true;
//    private long playbackPosition = 0;
//    private long position = 0;
//
//    //TODO edit to enable rotation video
//    public void setmTwoPane(boolean mTwoPane) {
//        this.mTwoPane = mTwoPane;
//    }
//
//    public void setmStep(Step mStep, List<Step> mStepsList) {
//        this.mStep = mStep;
//        this.mStepsList = mStepsList;
//    }
//
//    public void setmContext(Context mContext) {
//        this.mContext = mContext;
//    }
//
//    public StepDetailFragment() {
//        // Required empty public constructor
//    }
//
//    public String getScreenOrientation(Context context) {
//        final int screenOrientation = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getOrientation();
//        switch (screenOrientation) {
//            case Surface.ROTATION_0:
//                return "SCREEN_ORIENTATION_PORTRAIT";
//            case Surface.ROTATION_90:
//                return "SCREEN_ORIENTATION_LANDSCAPE";
//            case Surface.ROTATION_180:
//                return "SCREEN_ORIENTATION_REVERSE_PORTRAIT";
//            default:
//                return "SCREEN_ORIENTATION_REVERSE_LANDSCAPE";
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_step_detail, container, false);
//        mInstructionView = view.findViewById(R.id.step_detail_instructions);
//        componentListener = new ComponentListener();
//        mIndexText = view.findViewById(R.id.step_detail_index);
//        mNoThumbTxt = view.findViewById(R.id.np_thumb_txt);
//        nextBtn = view.findViewById(R.id.next_btn);
//        previousBtn = view.findViewById(R.id.previous_btn);
//        playerView = view.findViewById(R.id.player_view);
//        if (savedInstanceState != null) {
//            mStep = (Step) savedInstanceState.get(Globals.STEP);
//            mStepsList = (List<Step>) savedInstanceState.get(Globals.STEPLIST);
//            position = savedInstanceState.getLong(Globals.POSITION);
//            mTwoPane = savedInstanceState.getBoolean("mTwoPane");
//        }
//        renderCurrentStep();
//        nextBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int index = mStep.getId();
//                index++;
//                if (index != mStepsList.size()) {
//                    mStep = mStepsList.get(index);
//                    renderCurrentStep();
//                    releasePlayer();
//                    initializePlayer();
//                }
//            }
//        });
//        previousBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int index = mStep.getId();
//                if (index != 0) {
//                    index--;
//                    mStep = mStepsList.get(index);
//                    renderCurrentStep();
//                    releasePlayer();
//                    initializePlayer();
//                }
//            }
//        });
//        return view;
//    }
//
//    private void renderCurrentStep() {
//        mInstructionView.setText(mStep.getDescription());
//
//        if (mTwoPane) {
//            nextBtn.setVisibility(View.GONE);
//            previousBtn.setVisibility(View.GONE);
//            mIndexText.setVisibility(View.GONE);
//        } else {
//            setCurrentIndex();
//        }
//
//        initializePlayer();
//
//    }
//
//
//    private void setCurrentIndex() {
//        int currentStep = mStep.getId();
//        currentStep++;
//        mIndexText.setText(currentStep + "/" + mStepsList.size());
//        if (currentStep == mStepsList.size()) {
//            nextBtn.setClickable(false);
//            nextBtn.setAlpha((float) 0.5);
//        } else {
//            nextBtn.setClickable(true);
//            nextBtn.setAlpha((float) 1);
//        }
//        if (currentStep == 1) {
//            previousBtn.setClickable(false);
//            previousBtn.setAlpha((float) 0.5);
//        } else {
//            previousBtn.setClickable(true);
//            previousBtn.setAlpha((float) 1);
//        }
//    }
//
//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putSerializable("step", mStep);
//        outState.putSerializable(Globals.STEPLIST, (Serializable) mStepsList);
//        outState.putLong(Globals.POSITION, position);
//        outState.putBoolean("mTwoPane",mTwoPane);
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        position = player.getCurrentPosition(); //then, save it on the bundle.
//        releasePlayer();
//
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        releasePlayer();
//    }
//
//    private void initializePlayer() {
//        player = ExoPlayerFactory.newSimpleInstance(
//                new DefaultRenderersFactory(getContext()),
//                new DefaultTrackSelector(), new DefaultLoadControl());
//
//        playerView.setPlayer(player);
//
//        player.setPlayWhenReady(playWhenReady);
//        player.seekTo(currentWindow, playbackPosition);
//        player.addListener(componentListener);
//        player.addVideoDebugListener(componentListener);
//        player.addAudioDebugListener(componentListener);
//        Uri uri = null;
//        if (!TextUtils.isEmpty(mStep.getThumbnailURL())) {
//            uri = Uri.parse(mStep.getThumbnailURL());
//            playerView.setVisibility(View.VISIBLE);
//            mNoThumbTxt.setVisibility(View.INVISIBLE);
//        } else if (!TextUtils.isEmpty(mStep.getVideoURL())) {
//            uri = Uri.parse(mStep.getVideoURL());
//            playerView.setVisibility(View.VISIBLE);
//            mNoThumbTxt.setVisibility(View.INVISIBLE);
//        } else {
//            playerView.setVisibility(View.INVISIBLE);
//            mNoThumbTxt.setVisibility(View.VISIBLE);
//        }
//        MediaSource mediaSource = buildMediaSource(uri);
//        player.prepare(mediaSource, true, false);
//        if (position != C.TIME_UNSET) player.seekTo(position);
//        if (!mTwoPane && playerView.getVisibility() != View.INVISIBLE) {
//            if (getScreenOrientation(getContext()) == "SCREEN_ORIENTATION_PORTRAIT") {
//                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) playerView.getLayoutParams();
//                params.width = params.MATCH_PARENT;
//                params.height = 800;
//                playerView.setLayoutParams(params);
//                nextBtn.setVisibility(View.VISIBLE);
//                previousBtn.setVisibility(View.VISIBLE);
//            } else {
//                hideSystemUi();
//                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) playerView.getLayoutParams();
//                params.width = params.MATCH_PARENT;
//                params.height = params.MATCH_PARENT;
//                playerView.setLayoutParams(params);
//                nextBtn.setVisibility(View.INVISIBLE);
//                previousBtn.setVisibility(View.INVISIBLE);
//
//            }
//        }
//
//    }
//
//    private MediaSource buildMediaSource(Uri uri) {
//        return new ExtractorMediaSource.Factory(
//                new DefaultHttpDataSourceFactory("exoplayer")).
//                createMediaSource(uri);
//    }
//
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        Log.e("positoin on resume", position + "");
//
//    }
//
//    @SuppressLint("InlinedApi")
//    private void hideSystemUi() {
//        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
//                | View.SYSTEM_UI_FLAG_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//
//    }
//
//
//    private void releasePlayer() {
//        if (player != null) {
//            playbackPosition = player.getCurrentPosition();
//            currentWindow = player.getCurrentWindowIndex();
//            playWhenReady = player.getPlayWhenReady();
//            player.release();
//            player = null;
//        }
//    }
//
//    private class ComponentListener extends Player.DefaultEventListener implements
//            VideoRendererEventListener, AudioRendererEventListener {
//
//        @Override
//        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
//            String stateString;
//            switch (playbackState) {
//                case Player.STATE_IDLE:
//                    stateString = "ExoPlayer.STATE_IDLE      -";
//                    break;
//                case Player.STATE_BUFFERING:
//                    stateString = "ExoPlayer.STATE_BUFFERING -";
//                    break;
//                case Player.STATE_READY:
//                    stateString = "ExoPlayer.STATE_READY     -";
//                    break;
//                case Player.STATE_ENDED:
//                    stateString = "ExoPlayer.STATE_ENDED     -";
//                    break;
//                default:
//                    stateString = "UNKNOWN_STATE             -";
//                    break;
//            }
//        }
//
//        @Override
//        public void onAudioEnabled(DecoderCounters counters) {
//
//        }
//
//        @Override
//        public void onAudioSessionId(int audioSessionId) {
//
//        }
//
//        @Override
//        public void onAudioDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {
//
//        }
//
//        @Override
//        public void onAudioInputFormatChanged(Format format) {
//
//        }
//
//        @Override
//        public void onAudioSinkUnderrun(int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs) {
//
//        }
//
//        @Override
//        public void onAudioDisabled(DecoderCounters counters) {
//
//        }
//
//        @Override
//        public void onVideoEnabled(DecoderCounters counters) {
//
//        }
//
//        @Override
//        public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {
//
//        }
//
//        @Override
//        public void onVideoInputFormatChanged(Format format) {
//
//        }
//
//        @Override
//        public void onDroppedFrames(int count, long elapsedMs) {
//
//        }
//
//        @Override
//        public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
//
//        }
//
//        @Override
//        public void onRenderedFirstFrame(Surface surface) {
//
//        }
//
//        @Override
//        public void onVideoDisabled(DecoderCounters counters) {
//
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        releasePlayer();
//    }
//}
