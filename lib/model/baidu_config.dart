// To parse this JSON data, do
//
//     final baiDuConfig = baiDuConfigFromMap(jsonString);

import 'dart:convert';

BaiDuConfig baiDuConfigFromMap(String str) =>
    BaiDuConfig.fromMap(json.decode(str));

String baiDuConfigToMap(BaiDuConfig data) => json.encode(data.toMap());

class BaiDuConfig {
  String? apiKey;
  String? ttsAppId;
  String? ttsAppKey;
  String? ttsSecretKey;
  String? ttsAuthSn;

  BaiDuConfig({
    this.apiKey,
    this.ttsAppId,
    this.ttsAppKey,
    this.ttsSecretKey,
    this.ttsAuthSn,
  });

  factory BaiDuConfig.fromMap(Map<String, dynamic> json) => BaiDuConfig(
        apiKey: json["apiKey"],
        ttsAppId: json["ttsAppId"],
        ttsAppKey: json["ttsAppKey"],
        ttsSecretKey: json["ttsSecretKey"],
        ttsAuthSn: json["ttsAuthSn"],
      );

  Map<String, dynamic> toMap() => {
        "apiKey": apiKey,
        "ttsAppId": ttsAppId,
        "ttsAppKey": ttsAppKey,
        "ttsSecretKey": ttsSecretKey,
        "ttsAuthSn": ttsAuthSn,
      };

  String toJson() => json.encode(toMap());
}
