package com.xc.hi.birthdaycalculator;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Handles the retrieval and saving of setting variables.
 * @author X. Chen
 * @since 7/7/2014
 */
public class Setting {
    private static Context ctx;
	
    private static double _tax_rate;
    private static int _tip_rate0;
    private static int _tip_rate1;
    private static int _tip_rate2;
    private static int _tip_rate3;
    private static int _tip_rateCustom;
    
    private final static double CONST_TAX_RATE = 4.712;
    private final static int CONST_TIP_RATE0 = 10;
    private final static int CONST_TIP_RATE1 = 15;
    private final static int CONST_TIP_RATE2 = 20;
    private final static int CONST_TIP_RATE3 = 25;
    private final static int CONST_TIP_RATE_CUSTOM = 17;
    
    private static Setting setting = null;
    private static String CONST_SharedPrefName = "com_xc_hi_birthdaycalculator";
    
    private Setting() {
    	Util.Log("Setting()", "constructor");
    }
       
    public static Setting getInstance(Context context) {
    	Util.Log("Setting.getInstance()", "enter. setting is null? " + (setting == null));
    	if (setting == null) {
    		setting = new Setting();
    	}
    	ctx = context;
    	init();
    	return setting;
    }
    
    private static void init() {
    	Util.Log("Setting.init()", "enter");
        String strTaxRate = "", tip_rate0 = "", tip_rate1 = "", 
        		tip_rate2 = "", tip_rate3 = "", tip_rateCustom = "";
        boolean hasPref = false;
        try {
            SharedPreferences sharedPref = ctx.getSharedPreferences(CONST_SharedPrefName, Context.MODE_PRIVATE);
            hasPref = sharedPref.getBoolean(ctx.getString(R.string.KEY_INIT), false);
            if (hasPref) {
                strTaxRate = sharedPref.getString(ctx.getString(R.string.KEY_TAXRATE), "");
                tip_rate0 = sharedPref.getString(ctx.getString(R.string.KEY_TipRate0), "");
                tip_rate1 = sharedPref.getString(ctx.getString(R.string.KEY_TipRate1), "");
                tip_rate2 = sharedPref.getString(ctx.getString(R.string.KEY_TipRate2), "");
                tip_rate3 = sharedPref.getString(ctx.getString(R.string.KEY_TipRate3), "");
                tip_rateCustom = sharedPref.getString(ctx.getString(R.string.KEY_TipRateCustom), "");
            }
        } catch (Exception ex) {
            Util.Log("Setting.init()", "error: " + ex.getMessage(), "e");
        } 

        _tax_rate = (strTaxRate.equals("")) ? CONST_TAX_RATE : Double.parseDouble(strTaxRate);
        _tip_rate0 = (tip_rate0.equals("")) ? CONST_TIP_RATE0 : Integer.parseInt(tip_rate0);
        _tip_rate1 = (tip_rate1.equals("")) ? CONST_TIP_RATE1 : Integer.parseInt(tip_rate1);
        _tip_rate2 = (tip_rate2.equals("")) ? CONST_TIP_RATE2 : Integer.parseInt(tip_rate2);
        _tip_rate3 = (tip_rate3.equals("")) ? CONST_TIP_RATE3 : Integer.parseInt(tip_rate3);
        _tip_rateCustom = (tip_rateCustom.equals("")) ? CONST_TIP_RATE_CUSTOM : Integer.parseInt(tip_rateCustom);
        
        if (! hasPref) {
            saveAll();
        }
    }
    
    /**
     * Reset to default values.
     */
    public void reset() {
    	_tax_rate = CONST_TAX_RATE;
    	_tip_rate0 = CONST_TIP_RATE0;
    	_tip_rate1 = CONST_TIP_RATE1;
    	_tip_rate2 = CONST_TIP_RATE2;
    	_tip_rate3 = CONST_TIP_RATE3;
    	_tip_rateCustom = CONST_TIP_RATE_CUSTOM;
    	saveAll();
    }
    
    private static void saveAll() {
        try {
            SharedPreferences sharedPref = ctx.getSharedPreferences(CONST_SharedPrefName, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(ctx.getString(R.string.KEY_INIT), true);
            editor.putString(ctx.getString(R.string.KEY_TAXRATE), "" + _tax_rate);
            editor.putString(ctx.getString(R.string.KEY_TipRate0), "" + _tip_rate0);
            editor.putString(ctx.getString(R.string.KEY_TipRate1), "" + _tip_rate1);
            editor.putString(ctx.getString(R.string.KEY_TipRate2), "" + _tip_rate2);
            editor.putString(ctx.getString(R.string.KEY_TipRate3), "" + _tip_rate3);
            editor.putString(ctx.getString(R.string.KEY_TipRateCustom), "" + _tip_rateCustom);
            editor.commit();
        } catch (Exception ex) {
            Util.Log("Setting.saveAll()", "error: " + ex.getMessage());
        }    	
    }
    
    public void save(int key, String v) {
        try {
            SharedPreferences sharedPref = ctx.getSharedPreferences(CONST_SharedPrefName, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(ctx.getString(key), v);
            editor.commit();
        } catch (Exception ex) {
            Util.Log("Setting.save(" + ctx.getString(key)  + ", " + v + ")", "error: " + ex.getMessage());
        }
    }
    
    public double getTaxRate() { return _tax_rate; }
    public void setTaxRate(double tax_rate) { 
    	_tax_rate = tax_rate; 
    	this.save(R.string.KEY_TAXRATE, "" + tax_rate); 
    }
    
    public int getTipRate0() { return _tip_rate0; }
    public void setTipRate0(int tip_rate) { 
    	_tip_rate0 = tip_rate; 
    	this.save(R.string.KEY_TipRate0, "" + tip_rate); 
   	}

    public int getTipRate1() { return _tip_rate1; }
    public void setTipRate1(int tip_rate) { 
    	_tip_rate1 = tip_rate; 
    	this.save(R.string.KEY_TipRate1, "" + tip_rate); 
    }

    public int getTipRate2() { return _tip_rate2; }
    public void setTipRate2(int tip_rate) { 
    	_tip_rate2 = tip_rate; 
    	this.save(R.string.KEY_TipRate2, "" + tip_rate); 
    }

    public int getTipRate3() { return _tip_rate3; }
    public void setTipRate3(int tip_rate) { 
    	_tip_rate3 = tip_rate; 
    	this.save(R.string.KEY_TipRate3, "" + tip_rate); 
    }
    
    public int getTipRateCustom() { return _tip_rateCustom; }
    public void setTipRateCustom(int tip_rate) {
    	_tip_rateCustom = tip_rate;
    	this.save(R.string.KEY_TipRateCustom, "" + tip_rate);
    }
}
