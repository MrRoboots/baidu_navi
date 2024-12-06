// To parse this JSON data, do
//
//     final naviLatLng = naviLatLngFromMap(jsonString);

import 'dart:convert';

NaviLatLng naviLatLngFromMap(String str) =>
    NaviLatLng.fromMap(json.decode(str));

String naviLatLngToMap(NaviLatLng data) => json.encode(data.toMap());

class NaviLatLng {
  double? latitude;
  double? longitude;

  NaviLatLng({
    this.latitude,
    this.longitude,
  });

  factory NaviLatLng.fromMap(Map<String, dynamic> json) => NaviLatLng(
        latitude: json["latitude"],
        longitude: json["longitude"],
      );

  Map<String, dynamic> toMap() => {
        "latitude": latitude,
        "longitude": longitude,
      };

  String toJson() => json.encode(toMap());
}
