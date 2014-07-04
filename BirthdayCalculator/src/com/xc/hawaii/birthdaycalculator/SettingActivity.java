package com.xc.hawaii.birthdaycalculator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
        
        this.btnSaveTax = (Button) findViewById(R.id.btnSaveTax);
        this.btnSaveTip0 = (Button) findViewById(R.id.btnSaveTip0);
        this.btnSaveTip1 = (Button) findViewById(R.id.btnSaveTip1);
        this.btnSaveTip2 = (Button) findViewById(R.id.btnSaveTip2);
        this.btnSaveTip3 = (Button) findViewById(R.id.btnSaveTip3);
            
        this.txtTaxRate = (EditText) findViewById(R.id.txtTaxRate);
        this.txtTipRate0 = (EditText) findViewById(R.id.txtTipRate0);
        this.txtTipRate1 = (EditText) findViewById(R.id.txtTipRate1);
        this.txtTipRate2 = (EditText) findViewById(R.id.txtTipRate2);
        this.txtTipRate3 = (EditText) findViewById(R.id.txtTipRate3);
        
        this.txtTaxRate.addTextChangedListener(new MyTextWatcher(this.txtTaxRate, this.btnSaveTax, Util.RATE_TYPE.TAX));
        this.txtTipRate0.addTextChangedListener(new MyTextWatcher(this.txtTipRate0, this.btnSaveTip0, Util.RATE_TYPE.TIP));
        this.txtTipRate1.addTextChangedListener(new MyTextWatcher(this.txtTipRate1, this.btnSaveTip1, Util.RATE_TYPE.TIP));
        this.txtTipRate2.addTextChangedListener(new MyTextWatcher(this.txtTipRate2, this.btnSaveTip2, Util.RATE_TYPE.TIP));
        this.txtTipRate3.addTextChangedListener(new MyTextWatcher(this.txtTipRate3, this.btnSaveTip3, Util.RATE_TYPE.TIP));
        
        init();
        
        this.btnSaveTax.setEnabled(false);
        this.btnSaveTip0.setEnabled(false);
        this.btnSaveTip1.setEnabled(false);
        this.btnSaveTip2.setEnabled(false);
        this.btnSaveTip3.setEnabled(false);        
        
        // set cursor to end of string.
        this.txtTaxRate.setSelection(this.txtTaxRate.getText().length());        
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
     * Populate setting parameters from persistent storage.
     */
    private void init() {
        String strTaxRate = "", tip_rate0 = "", tip_rate1 = "", tip_rate2 = "", tip_rate3 = "";
        try {
            SharedPreferences sharedPref = this.getSharedPreferences(Util.CONST_SharedPrefName, Context.MODE_PRIVATE);
            boolean hasPref = sharedPref.getBoolean(getString(R.string.KEY_INIT), false);
            if (hasPref) {
                strTaxRate = sharedPref.getString(getString(R.string.KEY_TAXRATE), "");
                tip_rate0 = sharedPref.getString(getString(R.string.KEY_TipRate0), "");
                tip_rate1 = sharedPref.getString(getString(R.string.KEY_TipRate1), "");
                tip_rate2 = sharedPref.getString(getString(R.string.KEY_TipRate2), "");
                tip_rate3 = sharedPref.getString(getString(R.string.KEY_TipRate3), "");
            }
        } catch (Exception ex) {
            Util.Log("SettingActivity.init()", "error: " + ex.getMessage(), "e");
        } 

        _tax_rate = (strTaxRate.equals("")) ? CONST_TAX_RATE : Double.parseDouble(strTaxRate);
        _tip_rate0 = (tip_rate0.equals("")) ? CONST_TIP_RATE0 : Integer.parseInt(tip_rate0);
        _tip_rate1 = (tip_rate1.equals("")) ? CONST_TIP_RATE1 : Integer.parseInt(tip_rate1);
        _tip_rate2 = (tip_rate2.equals("")) ? CONST_TIP_RATE2 : Integer.parseInt(tip_rate2);
        _tip_rate3 = (tip_rate3.equals("")) ? CONST_TIP_RATE3 : Integer.parseInt(tip_rate3);
        
        this.txtTaxRate.setText( String.format("%.3f", _tax_rate) );
        this.txtTipRate0.setText( "" + _tip_rate0 );
        this.txtTipRate1.setText( "" + _tip_rate1 );
        this.txtTipRate2.setText( "" + _tip_rate2 );
        this.txtTipRate3.setText( "" + _tip_rate3 );
    }
    

    /**
     * Use this to save code.
     * @author X. Chen
     * @reference: http://stackoverflow.com/questions/4283062/textwatcher-for-more-than-one-edittext
     */
    private class MyTextWatcher implements TextWatcher {
        private EditText mEditText;
        private Util.RATE_TYPE mType;
        private Button mBtn;

        public MyTextWatcher(EditText e, Button b, Util.RATE_TYPE type) { 
            mEditText = e;
            mBtn = b;
            mType = type;
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        public void afterTextChanged(Editable s) { 
            mBtn.setEnabled(true);
            if (this.mType == Util.RATE_TYPE.TIP) { validateTipRate(this.mEditText); }
            else if (this.mType == Util.RATE_TYPE.TAX) { validateTaxRate(this.mEditText); }
        }
    }
    
    /**
     * Validate if the tip rate is at most 2 digits in length, i.e., 0-99 only.
     * If longer than 2 digits, truncate.
     * @param s
     * @return boolean
     */
    private boolean validateTipRate(EditText s) {
        EditText e = (EditText) s;
        String v = e.getText().toString().trim();
        if (v.equals("")) {
            //this.txt_TipCustomInput.setText("0");
            return false;
        }
        else if (v.length() > 2) {
            e.setText(v.substring(0, 2));
            e.setSelection(2);
            return false;
        }
        return true;
    }
    
    /**
     * Validate if the tax rate is less than 100.0%.
     * If out of range, truncate the most recent input character.
     * @param s
     * @return boolean
     */
    private boolean validateTaxRate(EditText s) {
        EditText e = (EditText) s;
        String v = e.getText().toString().trim();
        //Util.Log("SettingActivity", "validateTaxRate: " + v);
        if (v.equals("") || v.equals(".")) {
            //this.txt_TipCustomInput.setText("0");
            return false;
        }
        else {
            double val = Double.parseDouble(v);
            if (val >= 100) {
                int pos = e.getSelectionStart();
                //Util.Log("SettingActivity", "tax overflow: " + val + ", pos = " + pos);
                // positions here are tested and work properly.
                e.setText(v.substring(0, pos-1) + v.substring(pos, v.length()));
                e.setSelection(pos-1);
                return false;
            }
        }
        return true;
    }
    
    private void do_save(int key, String v) {
        try {
            SharedPreferences sharedPref = SettingActivity.this.getSharedPreferences(
                    Util.CONST_SharedPrefName, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(getString(R.string.KEY_INIT), true);
            editor.putString(getString(key), v);
            editor.commit();
        } catch (Exception ex) {
            Util.Log("SettingActivity.do_save()", "error: " + ex.getMessage());
        }
    }
    
    /**
     * Save setting variables to persistent storage.
     * @param view
     */
    public void btnSave_onClick(View view) {
        ((Button) view).setEnabled(false);
        
        String v = this.txtTaxRate.getText().toString().trim();
        if (v.equals("") || v.equals(".")) {
            v = "0.0";
            this.txtTaxRate.setText(v);
        }
        
        _tax_rate = Double.parseDouble(v);
        this.do_save(R.string.KEY_TAXRATE, v);
        
        String msg = "The tax rate has been updated";
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show(); 
    }
    
    /**
     * Save tip for the given EditText.
     * @param view
     * @param key
     */
    private void saveTip(EditText view, int key, String tipID) {
        String v = view.getText().toString().trim();
        if (v.equals("")) {
            v = "0";
            view.setText(v);
        }
            
        this.do_save(key, v);
        
        String msg = "Tip rate " + tipID + " has been updated";
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
    
    public void btnSaveTip0_onClick(View view) {
        ((Button) view).setEnabled(false);
        saveTip(this.txtTipRate0, R.string.KEY_TipRate0, "1");
    }

    public void btnSaveTip1_onClick(View view) {
        ((Button) view).setEnabled(false);
        saveTip(this.txtTipRate1, R.string.KEY_TipRate1, "2");
    }

    public void btnSaveTip2_onClick(View view) {
        ((Button) view).setEnabled(false);
        saveTip(this.txtTipRate2, R.string.KEY_TipRate2, "3");
    }

    public void btnSaveTip3_onClick(View view) {
        ((Button) view).setEnabled(false);
        saveTip(this.txtTipRate3, R.string.KEY_TipRate3, "4");
    }

    /**
     * Allows other classes to access the tax rate setting.
     * @return
     */
    public static double getTaxRate() { return _tax_rate; }
    public static double getTipRate0() { return _tip_rate0; }
    public static double getTipRate1() { return _tip_rate1; }
    public static double getTipRate2() { return _tip_rate2; }
    public static double getTipRate3() { return _tip_rate3; }
    
    
    /***********************************************************************
     * Begin definition for instance variables.
     ***********************************************************************/
    
    private Button btnSaveTax;
    private Button btnSaveTip0;
    private Button btnSaveTip1;
    private Button btnSaveTip2;
    private Button btnSaveTip3;
    
    private EditText txtTaxRate;
    private EditText txtTipRate0;
    private EditText txtTipRate1;
    private EditText txtTipRate2;
    private EditText txtTipRate3;

    private static double _tax_rate;
    private static int _tip_rate0;
    private static int _tip_rate1;
    private static int _tip_rate2;
    private static int _tip_rate3;
    
    public final static double CONST_TAX_RATE = 4.712;
    public final static int CONST_TIP_RATE0 = 10;
    public final static int CONST_TIP_RATE1 = 15;
    public final static int CONST_TIP_RATE2 = 20;
    public final static int CONST_TIP_RATE3 = 25;
}
