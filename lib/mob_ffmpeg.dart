import 'dart:async';

import 'package:flutter/services.dart';

class MobFfmpeg {
  static const MethodChannel _channel =
      const MethodChannel('mob_ffmpeg');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }


  static Future<String> execute(String command) async {
    final String resultado = await _channel.invokeMethod('execute', <String, dynamic>{
        'command': command
      });
    return resultado;
  }

   static Future<String> getPathTemp() async {
    final String resultado = await _channel.invokeMethod('getPathTemp');
    return resultado;
  }
}
