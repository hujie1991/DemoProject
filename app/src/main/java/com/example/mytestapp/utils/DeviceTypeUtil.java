package com.example.mytestapp.utils;

import android.os.Build;
import android.text.TextUtils;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * 设备类型
 */
public class DeviceTypeUtil {
    private static final String DEVICE_COOLPAD = "coolpad";
    private static final String DEVICE_HUAWEI = "huawei";
    private static final String DEVICE_LG = "lge";
    private static final String DEVICE_MEIZU = "meizu";
    private static final String DEVICE_OPPO = "oppo";
    private static final String DEVICE_SAMSUNG = "samsung";
    private static final String DEVICE_XIAOMI = "xiaomi";
    private static final String DEVICE_ZTE = "zte";
    private static final String DEVICE_VIVO = "vivo";
    private static final String DEVICE_LS = "Le";
    private static final String DEVICE_GIOEEN = "gionee";

    public static final int FUNTOUCHOS = 0;//VIVO
    public static final int COLOROS = 1;//OPPO
    public static final int MIFAVORUI = 2;//ZTE 中兴
    public static final int EUI = 3;//乐视
    public static final int EMUI = 4;//华为
    public static final int MIUI = 5;//小米
    public static final int FLYME = 6;//魅族
    public static final int SAMSUNG = 7;//三星
    public static final int ZUI = 8;//zuk
    public static final int LENOVO = 9;//LENOVO
    public static final int SMARTISANOS = 10;//锤子
    public static final int H2OS = 11;//一加
    public static final int COOLPAD = 12; //COOLPAD
    public static final int DEVICE360 = 13; //360
    public static final int PURENEXUS = 14;//Nexus
    public static final int MEITUOS = 15;//美图
    public static final int GOOGLE = 17;//谷歌原生
    public static final int OTHER = 999;

    private static final HashMap<String, DeviceType> sDeviceMap;
    private static DeviceType sDeviceType;

    public enum DeviceType {
        TYPE_UNKOWN,
        TYPE_XIAOMI,
        TYPE_OPPO,
        TYPE_COOLPAD,
        TYPE_MEIZU,
        TYPE_ZTE,
        TYPE_SAMSUNG,
        TYPE_HUAWEI,
        TYPE_LG,
        TYPE_VIVO,
        TYPE_LE,
        TYPE_GIOEEN
    }

    static {
        sDeviceMap = new HashMap<>();
        sDeviceMap.put(DEVICE_XIAOMI, DeviceType.TYPE_XIAOMI);
        sDeviceMap.put(DEVICE_OPPO, DeviceType.TYPE_OPPO);
        sDeviceMap.put(DEVICE_COOLPAD, DeviceType.TYPE_COOLPAD);
        sDeviceMap.put(DEVICE_MEIZU, DeviceType.TYPE_MEIZU);
        sDeviceMap.put(DEVICE_ZTE, DeviceType.TYPE_ZTE);
        sDeviceMap.put(DEVICE_SAMSUNG, DeviceType.TYPE_SAMSUNG);
        sDeviceMap.put(DEVICE_HUAWEI, DeviceType.TYPE_HUAWEI);
        sDeviceMap.put(DEVICE_LG, DeviceType.TYPE_LG);
        sDeviceMap.put(DEVICE_VIVO, DeviceType.TYPE_VIVO);
        sDeviceMap.put(DEVICE_LS, DeviceType.TYPE_LE);
        sDeviceMap.put(DEVICE_GIOEEN, DeviceType.TYPE_GIOEEN);
    }

    public static DeviceType getDeviceType() {
        if (sDeviceType == null) {
            sDeviceType = DeviceType.TYPE_UNKOWN;
            String manufacturer = Build.MANUFACTURER;
            if (!TextUtils.isEmpty(manufacturer)) {
                String lower = manufacturer.toLowerCase();
                for (String key : sDeviceMap.keySet()) {
                    if (lower.contains(key)) {
                        sDeviceType = (DeviceType) sDeviceMap.get(key);
                        break;
                    }
                }
            }
        }
        return sDeviceType;
    }

    public static String getBrand() {
        return Build.BRAND;
    }

    public static boolean isXiaomiDevice() {
        return getDeviceType() == DeviceType.TYPE_XIAOMI;
    }

    public static boolean isOppoDevice() {
        return getDeviceType() == DeviceType.TYPE_OPPO;
    }

    public static boolean isCoolpadDevice() {
        return getDeviceType() == DeviceType.TYPE_COOLPAD;
    }

    public static boolean isHuaweiDevice() {
        return getDeviceType() == DeviceType.TYPE_HUAWEI;
    }

    public static boolean isVivoDevice() {
        return getDeviceType() == DeviceType.TYPE_VIVO;
    }

    public static boolean isGIONEEDevice() {
        return getDeviceType() == DeviceType.TYPE_GIOEEN;
    }

    public static boolean isSamsungDevice() {
        return getDeviceType() == DeviceType.TYPE_SAMSUNG;
    }

    public static boolean isLeDevice() {
        return getDeviceType() == DeviceType.TYPE_LE;
    }

    public static boolean isZTEDevice() {
        return getDeviceType() == DeviceType.TYPE_ZTE;
    }

    public static boolean is360Device() {
        return Build.MANUFACTURER.contains("360");
    }

    public static boolean isGoogleDevice() {
        return Build.MANUFACTURER.equals("Google");
    }

    public static boolean isLenvovDevice() {
        return Build.MANUFACTURER.contains("LENOVO");
    }

    private static Class classSystemProperties;
    private static Method methodGetProperty;

    static {
        try {
            classSystemProperties = Class.forName("android.os.SystemProperties");
            methodGetProperty = classSystemProperties.getMethod("get", new Class[]{String.class});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getSystemProperty(String p1) {
        if (TextUtils.isEmpty(p1)) {
            return null;
        }
        try {
            return (String) methodGetProperty.invoke(0x0, new Object[]{p1});
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getProperty(String key, String defaultValue) {
        String value = defaultValue;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            value = (String) (get.invoke(c, key, "unknown"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static boolean isMiui() {
        return (!TextUtils.isEmpty(getSystemProperty("ro.miui.ui.version.code")));
    }

    public static boolean isFlyme() {
        return getMeizuFlymeOSFlag().toLowerCase().contains("flyme");
    }

    public static String getMeizuFlymeOSFlag() {
        return getSystemProperty("ro.build.display.id");
    }

    //Letv
    public static boolean isEUI() {
        return (!TextUtils.isEmpty(getSystemProperty("ro.letv.release.version")));
    }

    //VIVO
    public static boolean isFuntouchOS() {
        return (!TextUtils.isEmpty(getSystemProperty("ro.vivo.os.version")));
    }

    //OPPO
    public static boolean isColorOS() {
        return (!TextUtils.isEmpty(getSystemProperty("ro.build.version.opporom")));
    }

    //ZTE
    public static boolean isMifavorUI() {
        //部分中兴机型(B880_0336)搭盖 MifavorUI 找不到ro.build.MiFavor_version  大致判断中兴机型5.0以上就算搭载  影响： 引导界面->耗电保护选项
        String str = getSystemProperty("ro.build.version.sdk");
        int apiVersion = Integer.parseInt(TextUtils.isEmpty(str) ? "0" : str);
        if (apiVersion >= 21) {
            if (isZTEDevice()) {
                return true;
            }
        }
        return (!TextUtils.isEmpty(getSystemProperty("ro.build.MiFavor_version")));
    }

    //华为
    public static boolean isEmui() {
        return (!TextUtils.isEmpty(getSystemProperty("ro.build.version.emui")));
    }

    //Lenovo
    public static boolean isLenovo() {
        String type = getSystemProperty("ro.lenovo.device");
        return TextUtils.isEmpty(type) ? false : ("phone".equals(type) ? true : false);
    }

    //锤子
    public static boolean isSmartisanOS() {
        return (!TextUtils.isEmpty(getSystemProperty("ro.smartisan.version")));
    }

    //美图
    public static boolean isMeiTuOS() {
        return (!TextUtils.isEmpty(getSystemProperty("ro.build.version.meios")));
    }

    public static boolean isPureNexus() {
        return (!TextUtils.isEmpty(getSystemProperty("ro.pure.version")));
    }

    //zuk zui
    public static boolean isZuiOS() {
        return (!TextUtils.isEmpty(getSystemProperty("ro.com.zui.version")));
    }

    public static boolean isYunOS() {
        String version = getSystemProperty("ro.yunos.version");
        String vmName = getSystemProperty("java.vm.name");
        return null != vmName && vmName.toLowerCase().contains("lemur") || !TextUtils.isEmpty(version);
    }

    public static boolean isH2OS() {
        String version = getSystemProperty("ro.rom.version");
        return null != version && version.contains("H2OS");
    }


    public static int getRom() {
        if (isFuntouchOS()) {
            return FUNTOUCHOS;
        } else if (isColorOS()) {
            return COLOROS;
        } else if (isEUI()) {
            return EUI;
        } else if (isEmui()) {
            return EMUI;
        } else if (isMiui()) {
            return MIUI;
        } else if (isFlyme()) {
            return FLYME;
        } else if (isZuiOS()) {
            return ZUI;
        } else if (isSmartisanOS()) {
            return SMARTISANOS;
        } else if (isH2OS()) {
            return H2OS;
        } else if (isMifavorUI()) {
            return MIFAVORUI;
        } else if (isSamsungDevice()) {
            return SAMSUNG;
        } else if (isLenovo()) {
            return LENOVO;
        } else if (isCoolpadDevice()) {
            return COOLPAD;
        } else if (is360Device()) {
            return DEVICE360;
        } else if (isPureNexus()) {
            return PURENEXUS;
        } else if (isMeiTuOS()) {
            return MEITUOS;
        } else if (isGoogleDevice()) {
            return GOOGLE;
        } else {
            return OTHER;
        }
    }

    /**
     * 获取厂商自定义os名称
     *
     * @return
     */
    public static String getCustomOsNameAsString(int deviceType) {
        switch (deviceType) {
            case FUNTOUCHOS:
                return "FuntouchOS";
            case COLOROS:
                return "ColorOS";
            case EMUI:
                return "EmotionUI";
            case MIUI:
                return "MIUI";
            case FLYME:
                return "Flyme";
            case GOOGLE:
                return "Nexus";
        }
        return "other";
    }

    /**
     * 获取厂商自定义os version
     *
     * @return
     */
    public static String getCustomOsVersion() {
        if (isMiui()) {
            return DeviceTypeUtil.getProperty("ro.miui.ui.version.code", "7");
        } else if (isEmui()) {
            return DeviceTypeUtil.getSystemProperty("ro.build.version.emui");
        } else if (isFuntouchOS()) {
            return DeviceTypeUtil.getSystemProperty("ro.vivo.os.version");
        } else if (isColorOS()) {
            return DeviceTypeUtil.getSystemProperty("ro.build.version.opporom");
        } else if (isMeiTuOS()) {
            return getSystemProperty("ro.build.version.meios");
        } else if (isFlyme()) {
            return getSystemProperty("ro.build.display.id");
        } else if (isEUI()) {
            return getSystemProperty("ro.letv.release.version");
        } else if (isGoogleDevice()) {
            return String.valueOf(Build.VERSION.SDK_INT);
        }
        return "other";
    }

    /**
     * 获取厂商系统名称和版本:用于二维码验证、设置展示
     */
    public static String getRomNameAndVersionString() {
        try {
            String osVersion = getCustomOsVersion();
            if (isEmui()) {
                //EmotionUI_X.x.x -> EMUI X.x.x
                osVersion = osVersion.replaceFirst("EmotionUI_", "EMUI ");
            } else if (isFuntouchOS()) {
                //x.x -> FuntouchOS x.x
                try {
                    osVersion = Float.parseFloat(osVersion) + "";
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                osVersion = getCustomOsNameAsString(FUNTOUCHOS) + " " + osVersion;
            } else if (isMiui()) {
                //Vx -> MIUI x
                osVersion = getCustomOsNameAsString(MIUI) + " " + (DeviceTypeUtil.getProperty("ro.miui.ui.version.name", "E").replaceFirst("V", ""));
            } else if (isColorOS()) {
                //VX.x.x -> ColorOS X.x.x
                osVersion = (getCustomOsNameAsString(COLOROS) + " " + osVersion).replaceFirst("V", "");
            }
            return osVersion;
        } catch (Exception e) {
            e.printStackTrace();
            return "UnSupport";
        }
    }
}