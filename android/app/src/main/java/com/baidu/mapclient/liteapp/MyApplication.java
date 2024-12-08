package com.baidu.mapclient.liteapp;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.FlutterEngineCache;
import io.flutter.embedding.engine.dart.DartExecutor;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public class MyApplication extends Application {

    public static ArrayList<Activity> activities = new ArrayList<>();
    public static  FlutterEngine flutterEngine;
    @Override
    public void onCreate() {
        super.onCreate();

        // 创建并缓存 Flutter 引擎
        flutterEngine  = new FlutterEngine(this);
        flutterEngine.getNavigationChannel().setInitialRoute("/flutterPage");
        flutterEngine.getDartExecutor().executeDartEntrypoint(DartExecutor.DartEntrypoint.createDefault());
        // 缓存引擎以供后续使用
        FlutterEngineCache.getInstance().put("my_engine_id", flutterEngine);

//        FlutterEngine myEng  =  FlutterEngineCache.getInstance().get("my_engine_id");
//        myEng.getDartExecutor().getBinaryMessenger();

    }
}

