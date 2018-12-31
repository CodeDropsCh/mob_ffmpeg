#import "MobFfmpegPlugin.h"
#import <mobileffmpeg/MobileFFmpeg.h>

@implementation MobFfmpegPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  FlutterMethodChannel* channel = [FlutterMethodChannel
      methodChannelWithName:@"mob_ffmpeg"
            binaryMessenger:[registrar messenger]];
  MobFfmpegPlugin* instance = [[MobFfmpegPlugin alloc] init];
  [registrar addMethodCallDelegate:instance channel:channel];
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
  if ([@"execute" isEqualToString:call.method]) {
    
    dispatch_async(dispatch_get_main_queue(), ^{
        NSString *path = [NSString stringWithFormat: @"f-hide_banner %@", call.arguments[@"command"]];

        //NSString *path = call.arguments[@"command"];
        //var resp = [MobileFFmpeg execute: @"-hide_banner "+path];
        //int result = [MobileFFmpeg execute:@"-hide_banner "+path delimiter:@" "];

        int resposta = [MobileFFmpeg execute:path delimiter:@" "];

        if (resposta != 0) {
          result(@"erro");
        }
        else{
          result(@"ok");
        }
    });
  
  } 
  else if ([@"getPathTemp" isEqualToString:call.method]) {
    NSString *filePath = NSTemporaryDirectory();
    result(filePath);
  
  }
  
  else {
    result(FlutterMethodNotImplemented);
  }
}

@end
