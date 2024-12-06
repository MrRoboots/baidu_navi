package com.baidu.mapclient.liteapp;

import android.app.Application;

import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.FlutterEngineCache;
import io.flutter.embedding.engine.dart.DartExecutor;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // 创建并缓存 Flutter 引擎
        FlutterEngine flutterEngine = new FlutterEngine(this);
        flutterEngine.getNavigationChannel().setInitialRoute("/flutterPage");
        flutterEngine.getDartExecutor().executeDartEntrypoint(DartExecutor.DartEntrypoint.createDefault());

        // 缓存引擎以供后续使用
        FlutterEngineCache.getInstance().put("my_engine_id", flutterEngine);
    }
}

