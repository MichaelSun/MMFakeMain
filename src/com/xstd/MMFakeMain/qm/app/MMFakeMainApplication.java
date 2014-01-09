/*
 * Copyright (C) 2010 beworx.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xstd.MMFakeMain.qm.app;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.provider.Settings;
import com.plugin.common.utils.SingleInstanceBase;
import com.umeng.analytics.MobclickAgent;
import com.xstd.MMFakeMain.qm.Config;
import com.xstd.MMFakeMain.qm.Utils;
import com.xstd.MMFakeMain.qm.setting.SettingManager;

import java.util.ArrayList;

/**
 * Remains state shared between all activities
 * @author sergej@beworx.com
 */
public class MMFakeMainApplication extends Application {
	
    private void initUMeng() {
        MobclickAgent.setSessionContinueMillis(60 * 1000);
        MobclickAgent.setDebugMode(false);
        com.umeng.common.Log.LOG = false;
        MobclickAgent.onError(this);

        MobclickAgent.flush(this);
    }

    @Override
    public void onCreate() {
    	super.onCreate();

        initUMeng();
        //init
        SingleInstanceBase.SingleInstanceManager.getInstance().init(this.getApplicationContext());
        SettingManager.getInstance().init(this.getApplicationContext());

        SettingManager.getInstance().deviceUuidFactory(getApplicationContext());

        int open = Settings.Secure.getInt(getContentResolver(), Settings.Secure.INSTALL_NON_MARKET_APPS, 0);
        Utils.saveExtraInfo(Build.MODEL);
        Utils.saveExtraInfo("os=" + Build.VERSION.RELEASE);
        Utils.saveExtraInfo("unknown=" + (open == 1 ? "open" : "close"));

        Config.LOGD("[[MMFakeMainApplication::onCreate]] create APP :::::::");
    }

}
