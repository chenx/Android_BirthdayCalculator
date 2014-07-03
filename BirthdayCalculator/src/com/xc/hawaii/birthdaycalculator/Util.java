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
	private static boolean DEBUG;
	
	public Util() {
		// Get debug variable from values/strings.xml.
		try {
			DEBUG = Boolean.parseBoolean(getString(R.string.DEBUG));
		} catch (Exception ex) {
			Log.e("BirthdayCalculator.Util", "Wrong setting for variable DEBUG");
			DEBUG = false;
		}
	}
	
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
		
		//Drawable d = new ColorDrawable(Color.BLACK); // android.graphics.Color.TRANSPARENT
		//d.setAlpha(130);
		//alertDialog.getWindow().setBackgroundDrawable(d);
		//alertDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		
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
		Log.i(tag, msg);
	}
	
	public static void Log(String tag, String msg, String type) {
		if (type == "e") {
			Log.e(tag, msg);
		}
		else {
			Log.i(tag, msg);
		}
	}
}
