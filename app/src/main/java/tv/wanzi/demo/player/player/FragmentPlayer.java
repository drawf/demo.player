package tv.wanzi.demo.player.player;

import android.app.Activity;
import android.app.Fragment;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.pili.pldroid.player.AVOptions;

import tv.wanzi.demo.player.R;
import tv.wanzi.demo.player.databinding.FragmentPlayerBinding;
import tv.wanzi.demo.player.player.widget.MediaController;


/**
 * Created by drawf on 17/1/20.
 * ------------------------------
 */

public class FragmentPlayer extends Fragment {

    public static FragmentPlayer newInstance() {
        FragmentPlayer fragmentPlayer = new FragmentPlayer();
        return fragmentPlayer;
    }

    private Activity mActivity;
    private FragmentPlayerBinding mBinding;
    private int mOriginOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = mActivity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
        mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        changeScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_player, null, false);
        mBinding.videoView.setBufferingIndicator(mBinding.loadingView);
        mBinding.videoView.setAVOptions(getAVOptions());
        mBinding.videoView.setMediaController(new MediaController(mActivity));

        String url = "https://o558dvxry.qnssl.com/mobileL/mobileL_586e5866065b7e9d714295f7.m3u8";
        mBinding.videoView.setVideoPath(url);

        return mBinding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        changeScreenOrientation(mOriginOrientation);
    }

    /**
     * 在开始播放之前配置
     *
     * @return
     */
    private AVOptions getAVOptions() {
        AVOptions options = new AVOptions();

        /*解码方式:*/
        /*codec＝AVOptions.MEDIA_CODEC_HW_DECODE，硬解*/
        /*codec= MEDIA_CODEC_SW_DECODE, 软解*/
        /*codec=AVOptions.MEDIA_CODEC_AUTO, 硬解优先，失败后自动切换到软解*/
        /*默认值是：MEDIA_CODEC_SW_DECODE*/
        options.setInteger(AVOptions.KEY_MEDIACODEC, AVOptions.MEDIA_CODEC_SW_DECODE);

        /*准备超时时间，包括创建资源、建立连接、请求码流等，单位是 ms*/
        /*默认值是：无*/
        options.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);

        /*读取视频流超时时间，单位是 ms*/
        /*默认值是：10 * 1000*/
        options.setInteger(AVOptions.KEY_GET_AV_FRAME_TIMEOUT, 10 * 1000);

        /*当前播放的是否为在线直播，如果是，则底层会有一些播放优化*/
        /*默认值是：0*/
        options.setInteger(AVOptions.KEY_LIVE_STREAMING, 0);

        /*是否开启"延时优化"，只在在线直播流中有效*/
        /*默认值是：0*/
        options.setInteger(AVOptions.KEY_DELAY_OPTIMIZATION, 0);

        /*默认的缓存大小，单位是 ms*/
        /*默认值是：2000*/
        options.setInteger(AVOptions.KEY_CACHE_BUFFER_DURATION, 2000);

        /*最大的缓存大小，单位是 ms*/
        /*默认值是：4000*/
        options.setInteger(AVOptions.KEY_MAX_CACHE_BUFFER_DURATION, 4000);

        /*是否自动启动播放，如果设置为 1，则在调用 `prepareAsync` 或者 `setVideoPath` 之后自动启动播放，无需调用 `start()`*/
        /*默认值是：1*/
        options.setInteger(AVOptions.KEY_START_ON_PREPARED, 1);

        /*播放前最大探测流的字节数，单位是 byte*/
        /*默认值是：128 * 1024*/
        options.setInteger(AVOptions.KEY_PROBESIZE, 128 * 1024);

        return options;
    }

    /**
     * 改变屏幕方向
     *
     * @param orientation
     */
    private void changeScreenOrientation(int orientation) {
        if (mActivity == null) return;
        if (orientation < ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                || orientation > ActivityInfo.SCREEN_ORIENTATION_LOCKED)
            return;

        mOriginOrientation = mActivity.getRequestedOrientation();
        if (mOriginOrientation != orientation)
            mActivity.setRequestedOrientation(orientation);
    }
}
