package com.xc.hawaii.birthdaycalculator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * The settings interface.
 * 
 * @author X. Chen
 * @since 7/1/2014
 * 
 */
public class SettingActivity extends ActionBarActivity {

	//@TargetApi(Build.VERSION_CODES.FROYO) // version 8
	//@TargetApi(Build.VERSION_CODES.HONEYCOMB) // version 11.
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		init();
	}
	
	private void init() {
	    String strTaxRate = "0";
		try {
			SharedPreferences sharedPref = SettingActivity.this.getPreferences(Context.MODE_PRIVATE);
			boolean hasPref = sharedPref.getBoolean(getString(R.string.KEY_INIT), false);
			if (hasPref) {
				strTaxRate = sharedPref.getString(getString(R.string.KEY_TAXRATE), "");
			}
			Util.Log("init setting", "ok: read value = [" + strTaxRate + "]");
		} catch (Exception ex) {
			Util.Log("init setting", "error: " + ex.getMessage());
		} finally {
			_tax_rate = (strTaxRate.equals("")) ? CONST_TAX_RATE : Double.parseDouble(strTaxRate) / 100.0;  
		}

		this.txtTaxRate = (EditText) findViewById(R.id.txtTaxRate);
		String rate = String.format("%.3f", _tax_rate * 100);
		this.txtTaxRate.setText(rate);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.setting, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent intent = new Intent(this, SettingActivity.class);
			startActivity(intent);		

			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Save setting variables.
	 * @param view
	 */
	public void btnSave_onClick(View view) {
		String v = this.txtTaxRate.getText().toString().trim();
		if (v.equals("") || v.equals(".")) {
			v = "0.0";
			this.txtTaxRate.setText(v);
		}
		
		_tax_rate = Double.parseDouble(v) / 100.0;
		
		// save to persistent storage.
		try {
			SharedPreferences sharedPref = SettingActivity.this.getPreferences(Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.putBoolean(getString(R.string.KEY_INIT), true);
			editor.putString(getString(R.string.KEY_TAXRATE), v);
			editor.commit();
		} catch (Exception ex) {
			Log.i("save onclik", "save error: " + ex.getMessage());
		}
		
		String msg = "The tax rate has been updated";
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show(); 
	}
	
	/**
	 * Allows other classes to access the tax rate setting.
	 * @return
	 */
	public static double getTaxRate() {
		return _tax_rate;
	}
	
	
	/***********************************************************************
	 * Begin definition for instance variables.
	 ***********************************************************************/
	
	private EditText txtTaxRate;
	private static double _tax_rate = 0.04712;
	private static double CONST_TAX_RATE = 0.04712;
}
