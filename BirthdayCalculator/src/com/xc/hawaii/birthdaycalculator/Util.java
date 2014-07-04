package com.xc.hawaii.birthdaycalculator;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

/**
 * Utility functions.
 * 
 * @author X. Chen
 * @since 7/2/2014
 * 
 */
public class Util extends ActionBarActivity  {
	private static boolean DEBUG = false;
	public static String CONST_SharedPrefName = "BirthdayCalculator";
	
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
		if (! DEBUG) return;
		Log.d(tag, msg);
	}
	
	public static void Log(String tag, String msg, String type) {
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
}
