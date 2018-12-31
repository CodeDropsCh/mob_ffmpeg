package br.net.sinet.mobffmpeg;

import br.net.sinet.util.AsynchronousTaskService;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.arthenica.mobileffmpeg.util.RunCallback;


import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;
import java.io.File;
import android.os.Environment;


/** MobFfmpegPlugin */
public class MobFfmpegPlugin implements MethodCallHandler {
  
  protected static final AsynchronousTaskService asynchronousTaskService = new AsynchronousTaskService();

  private Result resultado;
  
  /** Plugin registration. */
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "mob_ffmpeg");
    channel.setMethodCallHandler(new MobFfmpegPlugin());
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    this.resultado = result;

    if (call.method.equals("execute")) {
  
        String command = call.argument("command");

        runFFmpegAsync("-hide_banner "+command);
      }
      else if (call.method.equals("getPathTemp")) {
  
        result.success(getPathTemp());
      }


    //runFFmpegAsync("-version");
    //runFFmpegAsync("-hide_banner -i /storage/emulated/0/Download/b.mp4 -crf 24 /storage/emulated/0/Download/d.mp4");
    //runFFmpegAsync("-hide_banner -i /storage/emulated/0/DCIM/Camera/20181231_092217.mp4 -crf 24 /storage/emulated/0/Download/e.mp4");

    //FFmpeg.execute("-hide_banner -version");
    //FFmpeg.execute("-hide_banner -i /storage/emulated/0/Download/b.mp4 -t 10 /storage/emulated/0/Download/d.mp4");

    // int rc = FFmpeg.getLastReturnCode();
    // String output = FFmpeg.getLastCommandOutput();

    
    // if (call.method.equals("getPlatformVersion")) {
    //   result.success("Android " + output);
    // } else {
    //   result.notImplemented();
    // }
  }


  public String getPathTemp(){
    String tempDirPath = Environment.getExternalStorageDirectory()
    + File.separator + "TempImgs" + File.separator;

    File tempDir = new File(tempDirPath);

    if (!tempDir.exists()){
        tempDir.mkdirs();
    }

    return tempDirPath;
  }

  public Future executeAsync(final RunCallback runCallback, final String arguments) {
    return asynchronousTaskService.runAsynchronously(new Callable<Integer>() {

        @Override
        public Integer call() {
            int returnCode = FFmpeg.execute(arguments, " ");
            if (runCallback != null) {
                runCallback.apply(returnCode);
            }

            return returnCode;
        }
    });
}


    public void runFFmpegAsync(String comando) {

        final String ffmpegCommand = String.format("-hide_banner %s", comando);


        executeAsync(new RunCallback() {

            @Override
            public void apply(int result) {

                if (result != 0) {
                    String output = FFmpeg.getLastCommandOutput();
                    resultado.success(output);
                }
                else{
                    String output = FFmpeg.getLastCommandOutput();
                    resultado.success(output);
                }

            }
        }, ffmpegCommand);
    }




}
