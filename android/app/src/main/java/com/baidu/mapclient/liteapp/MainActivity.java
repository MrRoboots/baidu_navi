package com.baidu.mapclient.liteapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

//import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.common.BaiduMapSDKException;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviCommonParams;
import com.baidu.navisdk.adapter.BaiduNaviManagerFactory;
import com.baidu.navisdk.adapter.IBNOuterSettingParams;
import com.baidu.navisdk.adapter.IBNRoutePlanManager;
import com.baidu.navisdk.adapter.IBaiduNaviManager;
import com.baidu.navisdk.adapter.struct.BNRoutePlanInfos;
import com.baidu.navisdk.adapter.struct.BNTTsInitConfig;
import com.baidu.navisdk.adapter.struct.BNaviInitConfig;
import com.baidu.navisdk.comapi.setting.SettingParams;
import com.baidu.mapclient.liteapp.model.BaiDuConfig;
import com.baidu.mapclient.liteapp.model.NaviLatLng;
import com.baidu.mapclient.liteapp.navi.NaviGuideActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.flutter.Log;

//public class MainActivity extends FlutterActivity {
//}


import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.android.FlutterFragment;
import io.flutter.embedding.android.FlutterFragmentActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public class MainActivity extends FlutterFragmentActivity implements MethodChannel.MethodCallHandler {

    String TAG = MainActivity.class.getSimpleName();

    NaviGuideActivity naviGuideActivity = new NaviGuideActivity();

    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);
        MethodChannel channel = new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), "flutter_baidu_map_navi");
        channel.setMethodCallHandler(this);
    }

    @Override
    public void cleanUpFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.cleanUpFlutterEngine(flutterEngine);
    }

    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_START:
                    Log.e(TAG, "算路开始");
                    break;
                case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_SUCCESS:
                    Log.e(TAG, "算路成功");
                    // 躲避限行消息
                    Bundle infoBundle = (Bundle) msg.obj;
                    if (infoBundle != null) {
                        String info = infoBundle.getString(BNaviCommonParams.BNRouteInfoKey.TRAFFIC_LIMIT_INFO);
                        Log.e("OnSdkDemo", "info = " + info);
                    }
                    BNRoutePlanInfos routePlanInfo = BaiduNaviManagerFactory.getRoutePlanManager().getRoutePlanInfo();
                    break;
                case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_FAILED:
                    Log.e(TAG, "算路失败");
                    break;
                case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_TO_NAVI:
                    Log.e(TAG, "算路成功准备进入导航");

                    Intent it = new Intent(getApplicationContext(), naviGuideActivity.getClass());
                    it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(it);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        Context context = getApplicationContext();
        if (call.method.equals("init")) {
            String json = call.argument("config");
            if (!TextUtils.isEmpty(json)) {
                Gson gson = new Gson();
                BaiDuConfig config = gson.fromJson(json, BaiDuConfig.class);
                initMap(context, config);
            }
        } else if (call.method.equals("calculate")) {
            //算路
            String startJson = call.argument("location");
            String endJson = call.argument("endLatlng");

            if (!TextUtils.isEmpty(startJson) && !TextUtils.isEmpty(endJson)) {
                NaviLatLng startLatLng = new Gson().fromJson(startJson, NaviLatLng.class);
                NaviLatLng endLatLng = new Gson().fromJson(endJson, NaviLatLng.class);

                List<BNRoutePlanNode> list = new ArrayList<>();
                BNRoutePlanNode start = new BNRoutePlanNode.Builder().longitude(startLatLng.longitude).latitude(startLatLng.latitude).build();
                BNRoutePlanNode end = new BNRoutePlanNode.Builder().longitude(endLatLng.longitude).latitude(endLatLng.latitude).build();
                list.add(start);
                list.add(end);
                BaiduNaviManagerFactory.getRoutePlanManager().routePlan(list, IBNRoutePlanManager.RoutePlanPreference.ROUTE_PLAN_PREFERENCE_DEFAULT, null, handler);
            }
        }

/*        else if (call.method.equals("finishNavi")) {
            if (naviGuideActivity != null) {
                naviGuideActivity.finish();
            }
        } */

        else {
            result.notImplemented();
        }
    }

    private void initMap(Context context, BaiDuConfig config) {
        try {
            // 是否同意隐私政策，默认为false
            SDKInitializer.setAgreePrivacy(context, true);
            SDKInitializer.setApiKey(config.getApiKey());
            // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
            SDKInitializer.initialize(context);
            SDKInitializer.setCoordType(CoordType.GCJ02);
            initNavi(config);
        } catch (BaiduMapSDKException ignored) {
        }
    }

    private void initNavi(BaiDuConfig bdConfig) {
        if (BaiduNaviManagerFactory.getBaiduNaviManager().isInited()) {
            return;
        }

        BNaviInitConfig config = new BNaviInitConfig.Builder().naviInitListener(new IBaiduNaviManager.INaviInitListener() {
            @Override
            public void onAuthResult(int status, String msg) {
                String result;
                if (0 == status) {
                    result = "key校验成功!";
                } else {
                    result = "key校验失败, " + msg;
                }
                Log.e(TAG, result);
            }

            @Override
            public void initStart() {
                Log.e(TAG, "initStart");
                BaiduNaviManagerFactory.getBaiduNaviManager().enableOutLog(true);
            }

            @Override
            public void initSuccess() {
                Log.e(TAG, "initSuccess cuid = " + BaiduNaviManagerFactory.getBaiduNaviManager().getCUID());
                // 初始化tts
                BNTTsInitConfig config = new BNTTsInitConfig.Builder().context(getApplicationContext()).appId(bdConfig.getTtsAppId()).appKey(bdConfig.getTtsAppKey()).secretKey(bdConfig.getTtsSecretKey()).authSn(bdConfig.getTtsAuthSn()).build();
                BaiduNaviManagerFactory.getTTSManager().initTTS(config);
                sendBroadcast(new Intent("com.navi.ready"));

                initUserConfig();
            }

            @Override
            public void initFailed(int errCode) {
                Log.e(TAG, "initFailed = " + errCode);
            }
        }).build();
        BaiduNaviManagerFactory.getBaiduNaviManager().init(getApplicationContext(), config);
    }

    /**
     * 导航设置
     */
    private void initUserConfig() {
        //跟随车头
        BaiduNaviManagerFactory.getProfessionalNaviSettingManager().setGuideViewMode(SettingParams.MapMode.CAR_3D, IBNRoutePlanManager.Vehicle.CAR);
        //日夜模式 自动
        BaiduNaviManagerFactory.getProfessionalNaviSettingManager().setDayNightMode(SettingParams.Action.DAY_NIGHT_MODE_AUTO, IBNRoutePlanManager.Vehicle.CAR);
        //路况条
        BaiduNaviManagerFactory.getProfessionalNaviSettingManager().setFullViewMode(IBNOuterSettingParams.PreViewMode.RoadBar, IBNRoutePlanManager.Vehicle.CAR);
        //地图智能缩放
        BaiduNaviManagerFactory.getProfessionalNaviSettingManager().setAutoScale(true, IBNRoutePlanManager.Vehicle.CAR);
        //多路线推荐
        BaiduNaviManagerFactory.getCommonSettingManager().setMultiRouteEnable(false);
        //当前道路名
        BaiduNaviManagerFactory.getProfessionalNaviSettingManager().setRoadNameEnable(true);
        //车道线
        BaiduNaviManagerFactory.getProfessionalNaviSettingManager().setLaneLineEnable(true);
        //区间测速
        BaiduNaviManagerFactory.getProfessionalNaviSettingManager().setMeasurementEnable(true);
        //高速面板
        BaiduNaviManagerFactory.getProfessionalNaviSettingManager().setHighwayEnable(true);
        //主辅路 桥上桥下 按钮
        BaiduNaviManagerFactory.getProfessionalNaviSettingManager().setShowMainAuxiliaryOrBridge(true);
        //工具箱更多
        BaiduNaviManagerFactory.getProfessionalNaviSettingManager().enableBottomBarOpen(false);
        //路况按钮
        BaiduNaviManagerFactory.getProfessionalNaviSettingManager().setRoadConditionButtonVisible(false);
        //设置按钮
        BaiduNaviManagerFactory.getProfessionalNaviSettingManager().setSettingButtonVisible(false);
        //icon显示
        BaiduNaviManagerFactory.getCommonSettingManager().setDIYImageStatus(true, IBNOuterSettingParams.DIYImageType.CarLogo);
        //终点连线
        BaiduNaviManagerFactory.getProfessionalNaviSettingManager().setShowCarLogoToEndRedLine(true, IBNRoutePlanManager.Vehicle.CAR);
        //路口放大图
        BaiduNaviManagerFactory.getProfessionalNaviSettingManager().setShowRoadEnlargeView(true, IBNRoutePlanManager.Vehicle.CAR);
        //到达终点自动退出
        BaiduNaviManagerFactory.getProfessionalNaviSettingManager().setIsAutoQuitWhenArrived(false);
    }


    //        @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

//        // Retrieve the FlutterEngine from cache.
//        FlutterEngine flutterEngine = FlutterEngineCache.getInstance().get("my_engine_id");
//
//        // Create and attach a FlutterFragment in the host's container.
//        getSupportFragmentManager().beginTransaction()
//                .add(R.id.some_container, FlutterFragment.withCachedEngine("my_engine_id").build(), "flutter_fragment")
//                .commit();

//        startActivity(FlutterActivity.withCachedEngine("my_engine_id").build(MainActivity.this));


//        FlutterFragment flutterFragment = FlutterFragment.withNewEngine().initialRoute("/flutterPage").build();
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, flutterFragment).commit();
//    }

    //FlutterEngine flutterEngine = new FlutterEngine(this);
    //flutterEngine.getNavigationChannel().setInitialRoute("/flutterPage");
    //flutterEngine.getDartExecutor().executeDartEntrypoint(
    //    DartExecutor.DartEntrypoint.createDefault()
    //);
    //
    //FlutterView flutterView = new FlutterView(this);
    //flutterView.attachToFlutterEngine(flutterEngine);
    //
    //FrameLayout container = findViewById(R.id.flutter_view_container);
    //container.addView(flutterView);
}
