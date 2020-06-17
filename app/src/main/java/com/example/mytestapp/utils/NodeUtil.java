package com.example.mytestapp.utils;

import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.List;



/**
 * @author zhuq
 * Email: zhuq@txtws.com
 * Date : 2019-07-02 14:08
 */
public class NodeUtil {

    private static final String TAG = "NodeUtil";

    public static List<AccessibilityNodeInfo> findByText(AccessibilityNodeInfo nodeInfo, String... text) {
        List<AccessibilityNodeInfo> infos = new ArrayList<>();
        for (String txt : text) {
            infos = nodeInfo.findAccessibilityNodeInfosByText(txt);
            if (infos != null && infos.size() > 0) {
                break;
            }
        }
        return infos;
    }

    public static List<AccessibilityNodeInfo> findByText(AccessibilityNodeInfo nodeInfo, String text, String viewClassName) {
        List<AccessibilityNodeInfo> result = new ArrayList<AccessibilityNodeInfo>();
        List<AccessibilityNodeInfo> infos = nodeInfo.findAccessibilityNodeInfosByText(text);
        if (infos == null || infos.isEmpty()) {
            return null;
        }
        for (AccessibilityNodeInfo item : infos) {
            if (item.getText() != null && text.equals(item.getText().toString())) {
                CharSequence charSequence = item.getClassName();
                Log.d(TAG,"AccessibilityObserver text = " + text + " , charSequence = " + charSequence);
                if (charSequence != null && viewClassName.equals(charSequence.toString())) {
                    result.add(item);
                }
            }
        }
        return result;
    }

    public static List<AccessibilityNodeInfo> findByViewClassName(AccessibilityNodeInfo nodeInfo, String className) {
        List<AccessibilityNodeInfo> allNodeInfo = new ArrayList<AccessibilityNodeInfo>();
        List<AccessibilityNodeInfo> result = new ArrayList<AccessibilityNodeInfo>();
        findAllNodeInfo(nodeInfo, allNodeInfo);
        //显示打开软件访问记录权限的所有子节点
        for (AccessibilityNodeInfo item : allNodeInfo) {
            if (item.getClassName().equals(className)) {
                result.add(item);
            }
        }

        return result;
    }


    private static void findAllNodeInfo(AccessibilityNodeInfo nodeInfo, List<AccessibilityNodeInfo> list) {
        if (nodeInfo != null) {
            int childCount = nodeInfo.getChildCount();
            for (int i = 0; i < childCount; i++) {
                AccessibilityNodeInfo child = nodeInfo.getChild(i);
                if (child != null) {
                    list.add(child);
                    if (child.getChildCount() > 0) {
                        findAllNodeInfo(child, list);
                    }
                }
            }
        }
    }

    public static boolean clickNodeOrParent(AccessibilityNodeInfo nodeInfo) {
        boolean result = nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        if (!result) {
            if (nodeInfo.getParent() != null) {
                result = nodeInfo.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
        if (!result) {
            if (nodeInfo.getParent() != null && nodeInfo.getParent().getParent() != null) {
                result = nodeInfo.getParent().getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
        return result;
    }

    public static boolean click(AccessibilityNodeInfo rootNodeInfo, String text, ViewClassName viewClassName) {
        List<AccessibilityNodeInfo> install = findByText(rootNodeInfo, text);
        if (install == null || install.isEmpty()) {
            return false;
        }

        for (AccessibilityNodeInfo nodeInfo : install) {

            if (nodeInfo.getClassName().equals(viewClassName.getClassName())) {
                if (ViewClassName.BUTTON.getClassName().equals(viewClassName.getClassName()) || nodeInfo.isClickable()) {
                    boolean click = nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    if (!click) {
                        click = nodeInfo.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }
                    return click;
                } else {
                    boolean result = nodeInfo.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    if (!result) {
                        result = nodeInfo.getParent().getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }
                    if (!result) {
                        result = nodeInfo.getParent().getParent().getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }
                    return result;
                }
            }
        }
        return false;
    }

    public static AccessibilityNodeInfo findById(AccessibilityNodeInfo accessibilityNodeInfo, String value) {
        List<AccessibilityNodeInfo> list = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(value);
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }


    public static List<AccessibilityNodeInfo> findOrScrollByViewClassName(AccessibilityNodeInfo nodeInfo, ViewClassName viewClassName) {
        List<AccessibilityNodeInfo> nodeInfos = findByViewClassName(nodeInfo, viewClassName.getClassName());
        try {
            if (scrollForward(nodeInfo)) {
                nodeInfos.addAll(findByViewClassName(nodeInfo, viewClassName.getClassName()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nodeInfos;
    }


    public static int ACTION_SCROLL = AccessibilityNodeInfo.ACTION_SCROLL_FORWARD;
    public static int SCROLL_END_TIMES = 0;


    public static boolean scrollForward(AccessibilityNodeInfo rootNodeInfo) {
        ArrayList<ViewClassName> clsNameList = new ArrayList<ViewClassName>() {{
            add(ViewClassName.LISTVIEW);
            add(ViewClassName.SCROLLVIEW);
            add(ViewClassName.GRIDVIEW);
            add(ViewClassName.RECYCLERVIEW);
            add(ViewClassName.RECYCLERVIEWX);
        }};
        List<AccessibilityNodeInfo> listViewNodeInfos = new ArrayList<>();
        for (ViewClassName clsName : clsNameList) {
            listViewNodeInfos = findByViewClassName(rootNodeInfo, clsName.getClassName());
            if (!listViewNodeInfos.isEmpty()) {
                break;
            }
        }
        if (listViewNodeInfos.isEmpty()) {
            return false;
        }
        for (AccessibilityNodeInfo nodeInfo : listViewNodeInfos) {//list多重嵌套
            if (nodeInfo.performAction(ACTION_SCROLL)) {
                return true;
            }
        }

        if (ACTION_SCROLL == AccessibilityNodeInfo.ACTION_SCROLL_FORWARD) {
            ACTION_SCROLL = AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD;
        } else {
            ACTION_SCROLL = AccessibilityNodeInfo.ACTION_SCROLL_FORWARD;
        }
        SCROLL_END_TIMES++;

        return false;
    }
}
