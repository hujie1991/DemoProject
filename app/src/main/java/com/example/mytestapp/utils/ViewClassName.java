package com.example.mytestapp.utils;

public enum ViewClassName {

    BUTTON("android.widget.Button"),
    IMAGEVIEW("android.widget.ImageView"),
    TEXTVIEW("android.widget.TextView"),
    CHECKEDTEXTVIEW("android.widget.CheckedTextView"),//miui9
    CHECKBOX("android.widget.CheckBox"),
    LISTVIEW("android.widget.ListView"),
    EXPANDABLE_LISTVIEW("android.widget.ExpandableListView"),
    SCROLLVIEW("android.widget.ScrollView"),
    GRIDVIEW("android.widget.GridView"),
    RECYCLERVIEW("android.support.v7.widget.RecyclerView"),
    RECYCLERVIEWX("androidx.recyclerview.widget.RecyclerView"),
    SWITCH("android.widget.Switch"),
    LESWITCH("com.letv.leui.widget.LeSwitch"),
    OPPOSWITCH("com.oppo.widget.OppoSwitch"),
    SWITCHEX("smartisanos.widget.SwitchEx"),
    MZSWITCH("com.meizu.common.widget.Switch"),
    RADIOBUTTON("android.widget.RadioButton"),
    PACKAGE_NAME("packageName"), 
    ALERTDIALOG("android.app.AlertDialog"),
    AMIGO_BUTTON("android.widget.AmigoButton"),
    VIEW("android.view.View"),
    COMPOUNDBUTTION("android.widget.CompoundButton");

    ViewClassName(String className) {
        this.className = className;
    }
    private String className;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public static ViewClassName findViewClassName(String value) {
        ViewClassName[] classNames = ViewClassName.values();
        for (ViewClassName className : classNames) {
            if (className.getClassName().equals(value)) {
                return className;
            }
        }
        return null;
    }
}
