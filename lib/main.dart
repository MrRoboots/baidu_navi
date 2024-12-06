import 'package:flutter/material.dart';
import 'package:permission_handler/permission_handler.dart';

import 'model/baidu_config.dart';
import 'model/navi_lat_lng.dart';
import 'navi/flutter_baidu_map_navi_method_channel.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.deepPurple),
        useMaterial3: true,
      ),
      initialRoute: '/',
      routes: {
        '/': (context) => const MyHomePage(title: 'MyHomePage'),
        '/flutterPage': (context) => const FlutterPage(),
      },
      // home: const MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class FlutterPage extends StatelessWidget {
  const FlutterPage({super.key});

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      height: 125,
      child: Scaffold(
        // backgroundColor: Colors.transparent,
        body: Stack(
          children: [
            Align(
              alignment: Alignment.bottomCenter,
              child: Container(
                color: Colors.red,
                width: double.maxFinite,
                height: 125,
                child: Column(
                  children: [
                    Text("Welcome to Flutter Page"),
                    // ElevatedButton(onPressed: () {
                    //   print('点击事件');
                    // }, child: Text('点击事件'))
                  ],
                ),
              ),
            )
          ],
        ),
      ),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  final MethodChannelFlutterBaiduMapNavi _flutterBaiduMapNaviPlugin =
      MethodChannelFlutterBaiduMapNavi();

  @override
  void initState() {
    super.initState();

    requestLocationPermission();
  }

  Future<void> requestLocationPermission() async {
    var status = await Permission.location.request();
    if (status.isGranted) {
      // 权限已授予
      BaiDuConfig config = BaiDuConfig();
      config.apiKey = "gT2XSUgoMFysCzwLCUtrIItTUdclThsf";
      config.ttsAppId = "11213224";
      config.ttsAppKey = "gT2XSUgoMFysCzwLCUtrIItTUdclThsf";
      config.ttsAuthSn = "8092f102-684cde5d-01-0050-006d-0091-01";
      config.ttsSecretKey = "MEokc3O8y95Lh9fOLX7lrxY1jD9OkWFf";
      _flutterBaiduMapNaviPlugin.init(config);
    } else if (status.isDenied) {
      // 权限被拒绝
    } else if (status.isPermanentlyDenied) {
      // 权限被永久拒绝，引导用户到设置页面
      openAppSettings();
    }
  }

  ///开始导航
  void calculate() {
    _flutterBaiduMapNaviPlugin.calculate(
        NaviLatLng(latitude: 39.90960, longitude: 116.39742),
        NaviLatLng(latitude: 40.90960, longitude: 117.39742));
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
        title: Text(widget.title),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            ElevatedButton(onPressed: calculate, child: const Text('开始导航')),
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () {},
        tooltip: 'Increment',
        child: const Icon(Icons.add),
      ), // This trailing comma makes auto-formatting nicer for build methods.
    );
  }
}
