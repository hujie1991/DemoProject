package com.example.mytestapp.utils;

import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * @author hujie
 * Email: hujie1991@126.com
 * Date : 2020-05-25 09:47
 */
public class AccessibilityLogUtils {

    public static void dfsnode(AccessibilityNodeInfo node , int num){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0 ;i < num ; i++){
            stringBuilder.append("__ ");    //父子节点之间的缩进
        }
        Log.i("####",stringBuilder.toString() + node.toString());   //打印
        for(int i = 0 ; i < node.getChildCount()  ; i++){      //遍历子节点
            if (node != null) {
                dfsnode(node.getChild(i),num+1);
            }
        }
    }
}
