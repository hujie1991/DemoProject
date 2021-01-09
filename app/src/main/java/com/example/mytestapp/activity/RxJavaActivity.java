package com.example.mytestapp.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;
import com.example.mytestapp.entity.BaseItemEntity;
import com.example.mytestapp.utils.DeviceTypeUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RxJavaActivity extends BaseListActivity {

    ImageView imageView;
    ImageView imageView1;

    String[] actionArr = {"android.settings.SETTINGS",
            "oppo.settings.main.search",
            "android.intent.action.USER_INITIALIZE",
            "android.intent.action.PRE_BOOT_COMPLETED",
            "android.intent.action.CREATE_SHORTCUT",
            "android.settings.VOICE_CONTROL_AIRPLANE_MODE",
            "com.android.settings.SEARCH_RESULT_TRAMPOLINE",
            "android.settings.ASSIST_GESTURE_SETTINGS",
            "android.settings.FACE_SETTINGS",
            "android.settings.FINGERPRINT_SETTINGS",
            "android.settings.TETHER_PROVISIONING_UI",
            "android.settings.DATE_SETTINGS",
            "android.intent.action.QUICK_CLOCK",
            "android.settings.LOCALE_SETTINGS",
            "android.settings.INPUT_METHOD_SETTINGS",
            "android.settings.VOICE_INPUT_SETTINGS",
            "android.settings.HARD_KEYBOARD_SETTINGS",
            "android.settings.INPUT_METHOD_SUBTYPE_SETTINGS",
            "android.settings.USER_DICTIONARY_SETTINGS",
            "com.android.settings.USER_DICTIONARY_INSERT",
            "android.settings.ZEN_MODE_SETTINGS",
            "android.settings.ZEN_MODE_PRIORITY_SETTINGS",
            "android.settings.ZEN_MODE_ONBOARDING",
            "android.settings.ZEN_MODE_AUTOMATION_SETTINGS",
            "android.settings.ACTION_CONDITION_PROVIDER_SETTINGS",
            "android.settings.ZEN_MODE_SCHEDULE_RULE_SETTINGS",
            "android.settings.ZEN_MODE_EVENT_RULE_SETTINGS",
            "com.android.settings.DISPLAY_SETTINGS",
            "android.settings.DISPLAY_SETTINGS",
            "android.settings.DEVICE_INFO_SETTINGS",
            "android.settings.LICENSE",
            "android.settings.APPLICATION_SETTINGS",
            "android.settings.MANAGE_APPLICATIONS_SETTINGS",
            "android.settings.MANAGE_ALL_APPLICATIONS_SETTINGS",
            "android.settings.MANAGE_DOMAIN_URLS",
            "android.settings.APP_MEMORY_USAGE",
            "android.settings.IGNORE_BATTERY_OPTIMIZATION_SETTINGS",
            "android.settings.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS",
            "android.settings.APPLICATION_DETAILS_SETTINGS",
            "com.android.settings.APP_OPEN_BY_DEFAULT_SETTINGS",
            "android.intent.action.MANAGE_PACKAGE_STORAGE",
            "android.settings.VOICE_CONTROL_DO_NOT_DISTURB_MODE",
            "android.settings.SECURITY_SETTINGS",
            "android.credentials.UNLOCK",
            "com.android.settings.MONITORING_CERT_INFO",
            "android.settings.PRIVACY_SETTINGS",
            "android.settings.REQUEST_ENABLE_CONTENT_CAPTURE",
            "android.settings.USAGE_ACCESS_SETTINGS",
            "android.settings.ACCESSIBILITY_DETAILS_SETTINGS",
            "android.settings.ACCESSIBILITY_SETTINGS_FOR_SUW",
            "com.android.settings.ACCESSIBILITY_COLOR_SPACE_SETTINGS",
            "android.settings.ENTERPRISE_PRIVACY_SETTINGS",
            "android.app.action.CONFIRM_DEVICE_CREDENTIAL",
            "android.app.action.CONFIRM_FRP_CREDENTIAL",
            "android.app.action.CONFIRM_DEVICE_CREDENTIAL_WITH_USER",
            "android.settings.ACTION_APP_NOTIFICATION_REDACTION",
            "android.settings.BIOMETRIC_ENROLL",
            "android.settings.FINGERPRINT_ENROLL",
            "android.settings.FINGERPRINT_SETUP",
            "com.android.settings.SETUP_LOCK_SCREEN",
            "android.app.action.SET_NEW_PASSWORD",
            "android.app.action.SET_NEW_PARENT_PROFILE_PASSWORD",
            "oppo.app.action.SET_NEW_PASSWORD",
            "android.settings.INTERNAL_STORAGE_SETTINGS",
            "android.settings.MEMORY_CARD_SETTINGS",
            "android.provider.action.DOCUMENT_ROOT_SETTINGS",
            "android.intent.action.EDIT",
            "android.intent.action.INSERT",
            "android.settings.APPLICATION_DEVELOPMENT_SETTINGS",
            "com.android.settings.APPLICATION_DEVELOPMENT_SETTINGS",
            "android.settings.ACTION_PRINT_SETTINGS",
            "oppo.intent.action.SETTINGS_PRINT_COMPONENT",
            "android.settings.WEBVIEW_SETTINGS",
            "android.settings.SHOW_REMOTE_BUGREPORT_DIALOG",
            "android.intent.action.PICK_ACTIVITY",
            "android.settings.NFCSHARING_SETTINGS",
            "android.provider.Telephony.SECRET_CODE",
            "android.appwidget.action.APPWIDGET_PICK",
            "android.appwidget.action.APPWIDGET_BIND",
            "android.settings.VOICE_CONTROL_BATTERY_SAVER_MODE",
            "android.settings.ACCOUNT_SYNC_SETTINGS",
            "android.settings.MANAGED_PROFILE_SETTINGS",
            "android.settings.ADD_ACCOUNT_SETTINGS",
            "android.app.action.START_ENCRYPTION",
            "android.settings.MOBILE_DATA_USAGE",
            "android.settings.DREAM_SETTINGS",
            "android.settings.USER_SETTINGS",
            "android.settings.NFC_PAYMENT_SETTINGS",
            "android.settings.NOTIFICATION_ASSISTANT_SETTINGS",
            "android.settings.VR_LISTENER_SETTINGS",
            "android.settings.PICTURE_IN_PICTURE_SETTINGS",
            "android.settings.PICTURE_IN_PICTURE_SETTINGS",
            "android.settings.NOTIFICATION_POLICY_ACCESS_DETAIL_SETTINGS",
            "android.settings.NOTIFICATION_POLICY_ACCESS_SETTINGS",
            "android.settings.NOTIFICATION_SETTINGS",
            "android.settings.APP_NOTIFICATION_BUBBLE_SETTINGS",
            "android.settings.CHANNEL_NOTIFICATION_SETTINGS",
            "android.settings.SHOW_MANUAL",
            "android.settings.SHOW_REGULATORY_INFO",
            "android.provider.Contacts.PROFILE_CHANGED",
            "android.content.action.SEARCH_INDEXABLES_PROVIDER",
            "com.android.settings.action.SUGGESTION_STATE_PROVIDER",
            "android.settings.action.MANAGE_OVERLAY_PERMISSION",
            "android.settings.action.MANAGE_OVERLAY_PERMISSION",
            "android.settings.action.MANAGE_WRITE_SETTINGS",
            "android.settings.action.MANAGE_WRITE_SETTINGS",
            "android.settings.MANAGE_UNKNOWN_APP_SOURCES",
            "android.settings.MANAGE_UNKNOWN_APP_SOURCES",
            "android.settings.SHOW_ADMIN_SUPPORT_DETAILS",
            "com.android.settings.action.IA_SETTINGS",
            "android.settings.STORAGE_MANAGER_SETTINGS",
            "android.settings.SYNC_SETTINGS",
            "com.android.settings.action.SUPPORT_SETTINGS",
            "android.service.quicksettings.action.QS_TILE",
            "android.settings.REQUEST_SET_AUTOFILL_SERVICE",
            "android.app.action.STATSD_STARTED",
            "android.intent.action.BOOT_COMPLETED",
            "android.content.action.SETTINGS_HOMEPAGE_DATA",
            "com.android.settings.BATTERY_SAVER_SCHEDULE_SETTINGS",
            "android.settings.TIMEPOWER_SETTINGS",
            "oppo.settings.VIDEO_BEAUTY_SETTINGS",
            "oppo.settings.AOD_SETTINGS",
            "oppo.settings.AOD_REALME_SETTINGS",
            "coloros.settings.recoverydata",
            "oppo.intent.settings.othersettings",
            "com.android.settings.POWER_ON",
            "com.android.settings.POWER_OFF",
            "android.intent.action.TIME_SET",
            "com.android.settings.SET_CHANGED",
            "com.coloros.bootreg",
            "android.intent.action.LOCKED_BOOT_COMPLETED",
            "com.heytap.cloud.action.SYNC_MODULE",
            "oppo.intent.action.SETTINGS_WEATHER_ADD_CITY",
            "android.app.action.ADD_DEVICE_ADMIN",
            "android.app.action.SET_PROFILE_OWNER",
            "com.android.settings.MEDIA_FORMAT",
            "com.coloros.partners.dirac.MainActivity",
            "com.coloros.partners.dirac.DiracMainActivity",
            "com.oppo.dirac.MainActivity",
            "android.media.action.DISPLAY_AUDIO_EFFECT_CONTROL_PANEL",
            "com.coloros.partners.dolby.DolbyMainActivity",
            "com.oppo.dolby.DolbyMainActivity",
            "android.media.action.DISPLAY_AUDIO_EFFECT_CONTROL_PANEL",
            "com.coloros.partners.dirac.SevenDiracMainActivity",
            "android.service.quicksettings.action.QS_TILE",
            "android.service.quicksettings.action.QS_TILE_PREFERENCES",
            "oppo.settings.elable",
            "oppo.settings.regulatory",
            "android.settings.NETWORK_ACCESS_LICENCE",
            "oppo.intent.action.PHONE_NAME_SETTINGS",
            "com.coloros.br.service",
            "oppo.intent.action.SCREEN_LOCK",
            "oppo.intent.action.FINGER_LOCK",
            "oppo.intent.action.FINGERPRINT",
            "oppo.intent.action.EnrollFingerprint",
            "oppo.intent.action.SETTINGS_RESET_FINGER",
            "oppo.intent.action.OPPO_FONT_SETTINGS",
            "oppo.intent.action.MAGNIFICATION_SETTINGS",
            "oppo.intent.action.CLOSE_FINGERPRINT_UNLOCK",
            "oppo.intent.action.PASSWORD_QUALITY_UNSPECIFIED",
            "oppo.intent.action.DELETE_FINGERPRINTS",
            "oppo.intent.action.SET_UNLOCK_PASSWORD",
            "oppo.intent.action.FACE_SETTINGS",
            "oppo.settings.privacy.confirm",
            "oppo.intent.action.PRIVACY_SETTINGS",
            "com.oppo.music.set_ringtone",
            "com.heytap.usercenter.account_logout",
            "oppo.privacy.password.delete",
            "com.heytap.usercenter.account_login",
            "com.heytap.usercenter.account_logout",
            "com.heytap.usercenter.modify_name",
            "com.usercenter.action.receiver.SUPPORT_LOGIN",
            "android.intent.action.BOOT_COMPLETED",
            "android.intent.action.ACTION_SHUTDOWN",
            "oppo.intent.action.OPPO_DCS_PERIOD_UPLOAD",
            "oppo.intent.action.DCS_INIT_PERIOD_STATIC_DATA",
            "com.coloros.bootreg",
            "oppo.intent.action.BOOT_COMPLETED_SERVICE",
            "oppo.intent.action.ASSSIST_GUIDE",
            "com.oppo.wizard.STATEMENTPAGE",
            "com.oppo.wizard.RegionPicker",
            "oppo.intent.action.ACTION_MUSIC_SELECT",
            "android.intent.action.RINGTONE_PICKER",
            "oppo.intent.action.BIOMETRIC_ENROLL_GUIDE",
            "oppo.settings.action.SIM_LOCK_STATE",
            "oppo.intent.action.APP_SUGGEST_SETTINGS",
            "oppo.intent.action.OTA_CORNER_MARK_CHANGED",
            "oppo.intent.action.OPPO_RECOVER_UPDATE_SUCCESSED",
            "oppo.intent.action.OPPO_OTA_UPDATE_SUCCESSED",
            "com.oppo.usercenter.account_logout",
            "com.heytap.usercenter.account_logout",
            "oppo.intent.action.simsettings.SIM_CARD_TYPE_CHANGE",
            "oppo.intent.action.OPPO_START_CUSTOMIZE",
            "oppo.intent.action.REGION_PICKER_INIT",
            "android.settings.WIFI_SETTINGS",
            "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS",
            "android.settings.APP_NOTIFICATION_SETTINGS",
            "android.settings.ALL_APPS_NOTIFICATION_SETTINGS",
            "android.settings.DATA_USAGE_SETTINGS",
            "android.settings.BATTERY_SAVER_SETTINGS",
            "android.intent.action.POWER_USAGE_SUMMARY",
            "android.settings.protecteyes.settings",
            "android.settings.NIGHT_DISPLAY_SETTINGS",
            "com.android.settings.Shutdown",
            "com.android.settings.ShutdownWhenLocked",
            "android.settings.APPLICATION_DEVELOPMENT_SETTINGS",
            "com.android.settings.APPLICATION_DEVELOPMENT_SETTINGS",
            "android.settings.panel.action.VOLUME",
            "android.settings.panel.action.INTERNET_CONNECTIVITY",
            "android.settings.panel.action.NFC",
            "android.settings.panel.action.WIFI",
            "com.android.settings.panel.action.MEDIA_OUTPUT",
            "com.android.settings.SOUND_SETTINGS",
            "android.settings.SOUND_SETTINGS",
            "android.settings.ACTION_OTHER_SOUND_SETTINGS",
            "com.android.credentials.INSTALL",
            "com.android.credentials.RESET",
            "android.settings.LOCATION_SOURCE_SETTINGS",
            "android.settings.LOCATION_SCANNING_SETTINGS",
            "com.android.settings.TRUSTED_CREDENTIALS",
            "com.android.settings.TRUSTED_CREDENTIALS_USER",
            "android.settings.ACCESSIBILITY_SETTINGS",
            "android.settings.CAPTIONING_SETTINGS",
            "com.android.settings.TTS_SETTINGS",
            "android.settings.WIRELESS_SETTINGS",
            "android.settings.AIRPLANE_MODE_SETTINGS",
            "android.provider.Telephony.ACTION_CHANGE_DEFAULT",
            "android.settings.double.earphone.settings",
            "android.service.quicksettings.action.QS_TILE",
            "com.oppo.action_dissable_development",
            "com.oppo.settings.action.EXP_UNINSTALL_MULTI_APP",
            "oppo.intent.action.ROM_UPDATE_CONFIG_SUCCESS",
            "oppo.intent.action.OPPO_OTA_UPDATE_SUCCESSED"};

    ArrayList<String> actionList;

    int index = 0;

    @Override
    public void initData(List<BaseItemEntity> datas) {

        List<String> strings = Arrays.asList(actionArr);
        Log.d(TAG, "strings.size = " + strings.size());
        LinkedHashSet<String> hashSet = new LinkedHashSet<>(strings);
        Log.d(TAG, "hashSet.size = " + hashSet.size());
        actionList = new ArrayList<>(hashSet);

        index = getSharedPreferences("test", Context.MODE_PRIVATE).getInt("index", index);

        datas.add(new BaseItemEntity("RxJava", "0"));
        datas.add(new BaseItemEntity("nexs", "1"));
        datas.add(new BaseItemEntity("onClick2", "2"));
        datas.add(new BaseItemEntity("onClick3", "3"));
        datas.add(new BaseItemEntity("onClick4", "4"));
    }

    @Override
    public void initView() {
        super.initView();
        imageView = new ImageView(this);
        imageView.setPadding(0, 20, 0, 0);
        llContentView.addView(imageView);
        imageView1 = new ImageView(this);
        imageView1.setPadding(0, 20, 0, 0);
        llContentView.addView(imageView1);
    }

    @Override
    public void onClickItem(int position, String value) {
        switch (position) {
            case 0:
                onClick0();
                break;

            case 1:
                String action = actionList.get(index);
                Log.d("RxJavaActivity-action", "index = " + index + " , action = " + action);
                index++;
                getSharedPreferences("test", Context.MODE_PRIVATE).edit().putInt("index", index).apply();
                try {
                    Intent intent1 = new Intent();
                    intent1.addCategory(Intent.CATEGORY_DEFAULT);
                    intent1.setAction(action);
                    startActivity(intent1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case 2:
                try {
                    PackageManager pm = getPackageManager();
                    PackageInfo pi = pm.getPackageInfo("com.coloros.colordirectservice", 0);
                    String name =  pi == null ? null : pi.applicationInfo.loadLabel(pm).toString();
                    Log.d(TAG, "name = " + name);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                break;

            case 3:
                break;

            case 4:
                Intent intent = new Intent("android.settings.DISPLAY_SETTINGS");
                startActivity(intent);
                break;
        }
    }

    public StringBuffer buildGuideFileName() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(Build.MANUFACTURER).append("_").append(Build.VERSION.SDK_INT).append("_").append(DeviceTypeUtil.getCustomOsVersion());
        return stringBuffer;
    }

    private void onClick0() {

        long id = Thread.currentThread().getId();
        Log.d(TAG, "main id = " + id);

        Observable<Integer> objectObservable = Observable.create(emitter -> {
            emitter.onNext(1);
            emitter.onNext(2);
            long threadId = Thread.currentThread().getId();
            Log.d(TAG, "Observable threadId = " + threadId);
//            emitter.onError(new Throwable("dfsfsdf"));
        });

//        objectObservable.subscribeOn(Schedulers.newThread()).
//                observeOn(Schedulers.io())


        Disposable subscribe = objectObservable
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
        .subscribe(consun -> {
            long threadId = Thread.currentThread().getId();
            Log.d(TAG, "consun = " + consun + " , threadId = " + threadId);
        }, error -> {
            Log.d(TAG, "error = " + error.getMessage());
        });


        Observer observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                long threadId = Thread.currentThread().getId();
                Log.d(TAG, "integer = " + integer + " , threadId = " + threadId);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        objectObservable.subscribe(observer);
    }

    private void onClick1() {

    }
}
