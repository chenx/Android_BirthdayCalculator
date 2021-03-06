package com.xc.hi.birthdaycalculator;


import com.xc.hi.birthdaycalculator.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ViewConfiguration;

/**
 * Utility functions.
 * 
 * @author X. Chen
 * @since 7/2/2014
 * 
 */
public class Util {
    private static boolean DEBUG = false;
    
    public Util() {
        // Get debug variable from values/strings.xml.
    }
    
    public static void setDebugMode(boolean DebugMode) { DEBUG = DebugMode; }
    public static boolean getDebugMode() { return DEBUG; }
    
    /**
     * Display a dialog.
     * 
     * @param strTitle
     * @param strErr
     * @param me
     */
    public static void showDialog(String strTitle, String strErr, ActionBarActivity me) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(me); 
        alertDialogBuilder.setTitle(strTitle);
        alertDialogBuilder.setMessage(strErr);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {                        
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //MainActivity.this.finish();
                        dialog.cancel();
                    }
                });
        
        AlertDialog alertDialog = alertDialogBuilder.create();
        
        alertDialog.show();        
    }
    
    /**
     * This dialog needs a layout xml. To be implemented.
     * 
     * @param strTitle
     * @param strErr
     * @param me
     */
    public static void showDialog2(String strTitle, String strErr, ActionBarActivity me) {
        final Dialog dialog = new Dialog(me);
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        dialog.setTitle(strTitle);
        dialog.setContentView(R.layout.activity_setting);
        Drawable d = new ColorDrawable(Color.BLACK); 
        d.setAlpha(130);
        dialog.getWindow().setBackgroundDrawable(d);
        
        dialog.show();        
    }
        
    /**
     * Central place for logging.
     * @param tag
     * @param text
     */
    public static void Log(String tag, String msg) {
        Util.Log(tag, msg, "d");
    }
    
    public static void Log(String tag, String msg, String type) {
    	if (! DEBUG) return;
        if (type == "e") { Log.e(tag, msg); }
        else if (type == "w") { Log.w(tag, msg); }
        else if (type == "i") { Log.i(tag, msg); }
        else if (type == "d") { Log.d(tag, msg); }
        else { Log.v(tag, msg); }
    }
    
    public static enum RATE_TYPE {
        TAX, /* 0-99, double  */
        TIP  /* 0-99, integer */
    }
    
    /**
     * Disable this feature of a Android phone:
     * System automatically detect the existence of physical menu button, and if it exists,
     * do not display ActionBar overflow button (in the upper right corner).
     * 
     * By disabling this feature, the ActionBar overflow button always shows,
     * this makes user experience consistent on different brands of Android phones.
     * 
     * @see http://blog.csdn.net/guolin_blog/article/details/18234477
     * @since 7/10/2014
     */
    public static void setOverflowShowingAlways(ActionBarActivity this$0) {  
        try {  
            ViewConfiguration config = ViewConfiguration.get(this$0);  
            java.lang.reflect.Field menuKeyField = 
            		ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");  
            menuKeyField.setAccessible(true);  
            menuKeyField.setBoolean(config, false);  
        } catch (Exception e) {  
            //e.printStackTrace();
            Log(this$0.getLocalClassName(), "Util.setOverflowShowingAlways() exception: " + e.getMessage());
        }  
    }      
}
