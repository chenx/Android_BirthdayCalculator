package com.xc.hi.birthdaycalculator;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
               
    	this.setting = Setting.getInstance(this.getApplicationContext());
    	this.setSettings();

        this.txtTaxRate.addTextChangedListener(new MyTextWatcher(
        		this.txtTaxRate, this.btnSaveTax, Util.RATE_TYPE.TAX));
        this.txtTipRate0.addTextChangedListener(new MyTextWatcher(
        		this.txtTipRate0, this.btnSaveTip0, Util.RATE_TYPE.TIP));
        this.txtTipRate1.addTextChangedListener(new MyTextWatcher(
        		this.txtTipRate1, this.btnSaveTip1, Util.RATE_TYPE.TIP));
        this.txtTipRate2.addTextChangedListener(new MyTextWatcher(
        		this.txtTipRate2, this.btnSaveTip2, Util.RATE_TYPE.TIP));
        this.txtTipRate3.addTextChangedListener(new MyTextWatcher(
        		this.txtTipRate3, this.btnSaveTip3, Util.RATE_TYPE.TIP));

        // set cursor to end of string.
        this.txtTaxRate.setSelection(this.txtTaxRate.getText().length());        
    }
    
    private void setSettings() {
        this.txtTaxRate.setText( String.format("%.3f", this.setting.getTaxRate()) );
        this.txtTipRate0.setText( "" + this.setting.getTipRate0() );
        this.txtTipRate1.setText( "" + this.setting.getTipRate1() );
        this.txtTipRate2.setText( "" + this.setting.getTipRate2() );
        this.txtTipRate3.setText( "" + this.setting.getTipRate3() );
        
        this.btnSaveTax.setEnabled(false);
        this.btnSaveTip0.setEnabled(false);
        this.btnSaveTip1.setEnabled(false);
        this.btnSaveTip2.setEnabled(false);
        this.btnSaveTip3.setEnabled(false);        
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
        if (id == R.id.action_reset) {
        	new AlertDialog.Builder(this)
            .setTitle("Confirm")
            .setMessage("Reset settings to default?")
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	SettingActivity.this.setting.reset();
                	SettingActivity.this.setSettings();

                    Toast.makeText(getApplicationContext(), 
                            getString(R.string.msg_settingReset), 
                            Toast.LENGTH_SHORT).show();
                }
            })
            .setNegativeButton(android.R.string.no, null).show();

            return true;
        }
        return super.onOptionsItemSelected(item);
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
        if (v.equals("") || v.equals(".")) {
            //this.txt_TipCustomInput.setText("0");
            return false;
        }
        else {
            double val = Double.parseDouble(v);
            if (val >= 100) {
                int pos = e.getSelectionStart();
                // positions here are tested and work properly.
                e.setText(v.substring(0, pos-1) + v.substring(pos, v.length()));
                e.setSelection(pos-1);
                return false;
            }
        }
        return true;
    }
    

    /**
     * Save setting variables to persistent storage.
     * @param view
     */
    public void btnSaveTax_onClick(View view) {
        ((Button) view).setEnabled(false);
        
        String v = this.txtTaxRate.getText().toString().trim();
        if (v.equals("") || v.equals(".")) {
            v = "0.0";
            this.txtTaxRate.setText(v);
        }
        
        this.setting.save(R.string.KEY_TAXRATE, v);
        
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
            
        this.setting.save(key, v);
        
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

    
    ////////////////////////////////////////////////////////////////////////
    // Begin definition for instance variables.
    ////////////////////////////////////////////////////////////////////////

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
    
    private Setting setting;
}
