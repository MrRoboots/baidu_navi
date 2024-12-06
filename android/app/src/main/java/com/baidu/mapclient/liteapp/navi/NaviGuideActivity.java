package com.baidu.mapclient.liteapp.navi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.fragment.app.FragmentActivity;

import com.baidu.mapclient.liteapp.R;
import com.baidu.navisdk.adapter.BNaviCommonParams;
import com.baidu.navisdk.adapter.BaiduNaviManagerFactory;
import com.baidu.navisdk.adapter.IBNRouteGuideManager;
import com.baidu.navisdk.adapter.IBNaviListener;
import com.baidu.navisdk.adapter.IBNaviViewListener;
import com.baidu.navisdk.adapter.struct.BNGuideConfig;
import com.baidu.navisdk.adapter.struct.BNRoutePlanInfos;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.android.FlutterFragment;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

/**
 * 诱导界面
 */
public class NaviGuideActivity extends FragmentActivity {

    private static final String TAG = NaviGuideActivity.class.getName();

    private IBNRouteGuideManager mRouteGuideManager;
    private IBNaviListener.DayNightMode mMode = IBNaviListener.DayNightMode.DAY;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean fullScreen = supportFullScreen();
        Bundle params = new Bundle();

        params.putBoolean(BNaviCommonParams.ProGuideKey.IS_SUPPORT_FULL_SCREEN, fullScreen);
        mRouteGuideManager = BaiduNaviManagerFactory.getRouteGuideManager();

        //设置退出按钮无效
//        BNSettingManager.setQuitNaviEnable(false);

        BNGuideConfig config = new BNGuideConfig.Builder().params(params).build();
        View view = mRouteGuideManager.onCreate(this, config);

        if (view != null) {
            setContentView(R.layout.activity_main);
            LinearLayout rootView = findViewById(R.id.rootView);

            //flutter页面
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                    FlutterFragment.withCachedEngine("my_engine_id").build(), "flutter_fragment").commit();

            rootView.addView(view);
        }


        routeGuideEvent();
    }

    // 导航过程事件监听
    private void routeGuideEvent() {
        BaiduNaviManagerFactory.getRouteGuideManager().setNaviListener(new IBNaviListener() {

            @Override
            public void onNaviGuideEnd() {
                NaviGuideActivity.this.finish();
            }

            @Override
            public void onYawingSuccess() {
                super.onYawingSuccess();
                Log.e(TAG, "onYawingSuccess");
                BNRoutePlanInfos routePlanInfo = BaiduNaviManagerFactory.getRoutePlanManager().getRoutePlanInfo();
            }
        });

        BaiduNaviManagerFactory.getRouteGuideManager().setNaviViewListener(new IBNaviViewListener() {

            @Override
            public void onMainInfoPanCLick() {

            }

            @Override
            public void onNaviTurnClick() {

            }

            @Override
            public void onFullViewButtonClick(boolean b) {

            }

            @Override
            public void onFullViewWindowClick(boolean b) {

            }

            @Override
            public void onNaviBackClick() {
                NaviGuideActivity.this.finish();
            }

            @Override
            public void onBottomBarClick(Action action) {

            }

            @Override
            public void onNaviSettingClick() {

            }

            @Override
            public void onRefreshBtnClick() {

            }

            @Override
            public void onZoomLevelChange(int i) {

            }

            @Override
            public void onMapClicked(double v, double v1) {

            }

            @Override
            public void onMapMoved() {
                Log.e(TAG, "onMapMoved");
            }

            @Override
            public void onFloatViewClicked() {
                try {
                    Intent intent = new Intent();
                    intent.setPackage(getPackageName());
                    intent.setClass(NaviGuideActivity.this, Class.forName(NaviGuideActivity.class.getName()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    startActivity(intent);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mRouteGuideManager.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mRouteGuideManager.onResume();
    }

    protected void onPause() {
        super.onPause();
        mRouteGuideManager.onPause();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mRouteGuideManager.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRouteGuideManager.onDestroy(false);
        mRouteGuideManager = null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mRouteGuideManager.onBackPressed(false, true);
    }

    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mRouteGuideManager.onConfigurationChanged(newConfig);
    }

    @Override
    public void setRequestedOrientation(int requestedOrientation) {

    }

    @Override
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (!mRouteGuideManager.onKeyDown(keyCode, event)) {
            return super.onKeyDown(keyCode, event);
        }
        return true;

    }

    private boolean supportFullScreen() {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            int color;
            if (Build.VERSION.SDK_INT >= 23) {
                color = Color.TRANSPARENT;
            } else {
                color = 0x2d000000;
            }
            window.setStatusBarColor(color);

            if (Build.VERSION.SDK_INT >= 23) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                int uiVisibility = window.getDecorView().getSystemUiVisibility();
                if (mMode == IBNaviListener.DayNightMode.DAY) {
                    uiVisibility |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
                window.getDecorView().setSystemUiVisibility(uiVisibility);
            } else {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            return true;
        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mRouteGuideManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mRouteGuideManager.onActivityResult(requestCode, resultCode, data);
    }

    public void onMethodCall(MethodCall call, MethodChannel.Result result) {
//        String method = call.method;
//        if (method.equals("jumpPhoneCode")) {
//            LinearLayout layout = findViewById(R.id.new_view);
//            layout.setVisibility(View.VISIBLE);
//
//            FlutterFragment flutterFragment = FlutterFragment.withNewEngine().initialRoute("/inputPhoneCode").build();
//            getSupportFragmentManager().beginTransaction().replace(R.id.new_view, flutterFragment).commit();
//        }
    }


}
