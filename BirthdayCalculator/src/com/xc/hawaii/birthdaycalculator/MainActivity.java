package com.xc.hawaii.birthdaycalculator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The main interface.
 * 
 * @author X. Chen
 * @since 6/30/2014
 *
 */
public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);
		setContentView(R.layout.fragment_main);

		//if (savedInstanceState == null) {
		//	getSupportFragmentManager().beginTransaction()
		//			.add(R.id.container, new PlaceholderFragment()).commit();
		//}
		
		init();
	}
	
	/**
	 * Save the state of this activity.
	 * Note savedInstanceState is not safe to use, should use onPause().
	 */
	@Override
	protected void onPause() {
		super.onPause();
		SharedPreferences pref = getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		
		editor.putBoolean(getString(R.string.KEY_INIT), true);
		editor.putString(getString(R.string.KEY_SubTotal), this.txt_Bill.getText().toString().trim());
		editor.putBoolean(getString(R.string.KEY_IncTaxForTip), this.includeTaxForTip());
		editor.putInt(getString(R.string.KEY_TipChoice), this.btn_Tip_selected);
		editor.putString(getString(R.string.KEY_TipCustom), this.txt_TipCustomInput.getText().toString());
		editor.putString(getString(R.string.KEY_Persons), this.txt_NumberOfPersons.getText().toString());
		
		editor.commit();
	}
	
	/**
	 * Recover the state of this activity.
	 */
	@Override
	protected void onResume() {
		super.onResume();
		
		try {
			SharedPreferences pref = getPreferences(MODE_PRIVATE);
			Boolean initialized = pref.getBoolean(getString(R.string.KEY_INIT), false);
			
			if (initialized) {
				this.setState(
						pref.getString(getString(R.string.KEY_SubTotal), ""),
						pref.getBoolean(getString(R.string.KEY_IncTaxForTip), false),
						pref.getInt(getString(R.string.KEY_TipChoice), 0),				
						pref.getString(getString(R.string.KEY_TipCustom), "17"),
						pref.getString(getString(R.string.KEY_Persons), "1")
				);
			}
			Util.Log("onResume", "subtotal retrived: ");
			
		} catch (Exception ex) {
			Util.Log("onResume", "error: " + ex.getMessage());
		}
	}
	
	/**
	 * Set the state of this activity.
	 * @param subTotal
	 * @param incTaxForTip
	 * @param tipChoice
	 * @param tipCustom
	 * @param persons
	 */
	private void setState(String subTotal, boolean incTaxForTip, 
			int tipChoice, String tipCustom, String persons) {
		this.txt_Bill.setText( subTotal );
		this.cbIncludeTaxForTip.setChecked( incTaxForTip );
		this.txt_TipCustomInput.setText( tipCustom );
		this.txt_NumberOfPersons.setText( persons );
		// put this behind custom input, since custom inupt change will select the custom tip.
		this.setTipChoice( tipChoice ); 
	}
	
	/**
	 * Set the tip rate.
	 * @param tipChoice
	 */
	private void setTipChoice(int tipChoice) {
		//Log.i("setTipChoice", "choice: " + tipChoice);
		switch (tipChoice) {
		case 0:
			onClick_Tip0(this.btn_Tip0);
			break;
		case 1:
			onClick_Tip1(this.btn_Tip1);
			break;
		case 2:
			onClick_Tip2(this.btn_Tip2);
			break;
		case 3:
			onClick_Tip3(this.btn_Tip3);
			break;
		case 4:
			onClick_TipCustom(this.btn_TipCustom);
			break;
		default:
			onClick_Tip0(this.btn_Tip0);
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
		else if (id == R.id.action_clear) {
			this.setState("", false, 0, "17", "1");
			Toast.makeText(getApplicationContext(), 
					getString(R.string.msg_billInfoCleared), 
					Toast.LENGTH_SHORT).show();
			return true;
		}
		else if (id == R.id.action_about) {
			Util.showDialog("About", getString(R.string.msg_about), this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			
			return rootView;
		}
	}

	
	
	/**********************************************************************
	 * Start major sections of functions.
	 **********************************************************************/
		
	
	public void onClick_Tip0(View view) {
		this.btn_Tip_selected = 0;
		this.clear_Tips();
		this.btn_Tip0.setChecked(true);
		this.tip_rate = this.tip_rate_0;
		this.calcBillPerPerson();
	}
	
	public void onClick_Tip1(View view) {
		this.btn_Tip_selected = 1;
		this.clear_Tips();
		this.btn_Tip1.setChecked(true);		
		this.tip_rate = this.tip_rate_1;		
		this.calcBillPerPerson();
	}

	public void onClick_Tip2(View view) {
		this.btn_Tip_selected = 2;
		this.clear_Tips();
		this.btn_Tip2.setChecked(true);	
		this.tip_rate = this.tip_rate_2;
		this.calcBillPerPerson();
	}

	public void onClick_Tip3(View view) {
		this.btn_Tip_selected = 3;
		this.clear_Tips();
		this.btn_Tip3.setChecked(true);	
		this.tip_rate = this.tip_rate_3;
		this.calcBillPerPerson();
	}

	public void onClick_TipCustom(View view) {
		this.btn_Tip_selected = 4;
		this.clear_Tips();
		this.btn_TipCustom.setChecked(true);
		//this.getCustomTipRate();
		this.tip_rate = this.tip_rate_custom;
		this.calcBillPerPerson();
	}
	
	private void getCustomTipRate() {
		String v = this.txt_TipCustomInput.getText().toString().trim();
		this.tip_rate_custom = ((v.equals("") || v.equals("0")) ? 0 : Double.parseDouble(v)) / 100.0;
	}
	
	private void clear_Tips() {
		this.btn_Tip0.setChecked(false);
		this.btn_Tip1.setChecked(false);
		this.btn_Tip2.setChecked(false);
		this.btn_Tip3.setChecked(false);
		this.btn_TipCustom.setChecked(false);
	}
	
	/**
	 * Initialize this activity.
	 */
	private void init() {
		this.tax_rate = SettingActivity.getTaxRate();
		
		this.txt_Tax = (TextView) findViewById(R.id.txtTax);
		this.txt_Total = (TextView) findViewById(R.id.txtTotal);
		this.cbIncludeTaxForTip = (CheckBox) findViewById(R.id.cbIncludeTaxForTip);
		this.cbIncludeTaxForTip.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String msg;
				if (((CheckBox) v).isChecked()) {
					msg = "Include tax for tip";
				}
				else {
					msg = "Exclude tax from tip";
				}
				// LENGTH_SHORT: 2s, LENGTH_LONG: 3.5s.
				MainActivity.this.re_calculate();
				Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show(); 
			}
		});
		
		this.btn_Tip0 = (RadioButton) findViewById(R.id.radio0);
		this.btn_Tip1 = (RadioButton) findViewById(R.id.radio1);
		this.btn_Tip2 = (RadioButton) findViewById(R.id.radio2);
		this.btn_Tip3 = (RadioButton) findViewById(R.id.radio3);
		this.btn_TipCustom = (RadioButton) findViewById(R.id.radioCustom);
		clear_Tips();
		this.btn_Tip0.setChecked(true);
		this.tip_rate = 0.1;
		
		this.txt_Tip0 = (TextView) findViewById(R.id.txt_Tip0);
		this.txt_Tip1 = (TextView) findViewById(R.id.txt_Tip1);
		this.txt_Tip2 = (TextView) findViewById(R.id.txt_Tip2);
		this.txt_Tip3 = (TextView) findViewById(R.id.txt_Tip3);
		this.txt_TipCustom = (TextView) findViewById(R.id.txt_TipCustom);

		this.txt_Total0 = (TextView) findViewById(R.id.txt_Total0);
		this.txt_Total1 = (TextView) findViewById(R.id.txt_Total1);
		this.txt_Total2 = (TextView) findViewById(R.id.txt_Total2);
		this.txt_Total3 = (TextView) findViewById(R.id.txt_Total3);
		this.txt_TotalCustom = (TextView) findViewById(R.id.txt_TotalCustom);
		
		this.txt_TipCustomInput = (EditText) findViewById(R.id.txt_TipCustomInput);
		///this.txt_TipCustomInput.setOnKeyListener(this);
		//this.txt_TipCustomInput.setOnFocusChangeListener(this);
		// http://stackoverflow.com/questions/3291897/how-can-an-java-event-listener-defined-as-an-anonymous-inner-class-use-a-variabl
		this.txt_TipCustomInput.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				final MainActivity this$0 = MainActivity.this;
				
				if (hasFocus) {
					this$0.getCustomTipRate();
					this$0.onClick_TipCustom(v);
				}
				else {
					if (this$0.txt_TipCustomInput.getText().toString().equals("")) {
						int rate = (int) MainActivity.this.tip_rate_custom * 100;
						this$0.txt_TipCustomInput.setText("" + rate);
					}
				}
			}
		});
		this.txt_TipCustomInput.setOnKeyListener(new EditText.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) return true; // don't accept this input.
				return false; // accept this input.
			}
		});
		this.txt_TipCustomInput.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				if (! isValidTipRate()) return; 
				
				final MainActivity this$0 = MainActivity.this;
				this$0.getCustomTipRate();
				this$0.updateTipAndTotal(this$0.txt_TipCustom, this$0.txt_TotalCustom, this$0.tip_rate_custom);
				this$0.onClick_TipCustom(this$0.btn_TipCustom);
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {};
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
		});
		
		this.txt_Bill = (EditText) findViewById(R.id.txtBill);
		this.txt_Bill.setOnKeyListener(new EditText.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) return true; // don't accept this input.
				return false; // accept this input.
			}
		});
		this.txt_Bill.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				if (had2Decimals() || billOutsideRange()) return; // false;

				final MainActivity this$0 = MainActivity.this;

				this$0.updateTaxAndTotal();
				this$0.re_calculate();
								
				// Only on txt_Bill, for KeyUp event, do the following.
				String msg =  "key: input = " + this$0.txt_Bill.getText();
				Util.Log("MainActivity", msg);
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {};
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.length()>0 && s.subSequence(s.length()-1, s.length()).toString().equalsIgnoreCase("\n")) {
					Util.Log("MainActivity", "ENTER pressed");
			    }
			}
		});
				
		this.txt_NumberOfPersons = (EditText) findViewById(R.id.txt_NumberOfPersons);
		this.txt_NumberOfPersons.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				if (personOutsideRange()) return; 
				MainActivity.this.calcBillPerPerson();
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {};
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
		});
		
		this.txt_BillPerPerson = (TextView) findViewById(R.id.txt_BillPerPerson);
		
		this.btn_IncPerson = (Button) findViewById(R.id.btn_inc_person);
		this.btn_DecPerson = (Button) findViewById(R.id.btn_dec_person);		
	}
	
	/**
	 * Update all tips, totals and per person bills.
	 */
	public void re_calculate() {
		this.updateTipAndTotal(this.txt_Tip0, this.txt_Total0, this.tip_rate_0);
		this.updateTipAndTotal(this.txt_Tip1, this.txt_Total1, this.tip_rate_1);
		this.updateTipAndTotal(this.txt_Tip2, this.txt_Total2, this.tip_rate_2);
		this.updateTipAndTotal(this.txt_Tip3, this.txt_Total3, this.tip_rate_3);
		this.updateTipAndTotal(this.txt_TipCustom, this.txt_TotalCustom, this.tip_rate_custom);
		this.calcBillPerPerson();
	}
		
		
	/**
	 * Tip rate must be in the range of 0-99.
	 * @return boolean
	 */
	private boolean isValidTipRate() {
		String v = this.txt_TipCustomInput.getText().toString().trim();
		if (v.equals("")) {
			//this.txt_TipCustomInput.setText("0");
			return false;
		}
		else if (v.length() > 2) {
			this.txt_TipCustomInput.setText(v.substring(0, 2));
			this.txt_TipCustomInput.setSelection(2);
			return false;
		}
		return true;
	}
	
	/**
	 * Don't allow more than 2 decimals for the sub-total value.
	 * 
	 * If the sub-total value has more than 2 decimals, truncate the extra digit,
	 * and return true. Otherwise, return false.
	 * 
	 * @return boolean
	 */
	private boolean had2Decimals() {
		String strTotalBill = this.txt_Bill.getText().toString().trim();
		
		int dot_pos = strTotalBill.indexOf(".") + 1;
		if (dot_pos > 0 && strTotalBill.length() - dot_pos > 2) {
			Util.Log("MainActivity.had2Decimals()", "more than 2 decimals, do truncate");
			
			// do truncation.
			strTotalBill = strTotalBill.substring(0, strTotalBill.length() - 1);
			this.txt_Bill.setText(strTotalBill);
			this.txt_Bill.setSelection(strTotalBill.length());
			return true;
		}		
		return false;
	}
	
	/**
	 * If the bill is too large to be meaningful, truncate it and return true.
	 * Otherwise return false.
	 * 
	 * The threshold is set at right under 1 million, i.e., 999999.
	 * 
	 * @return boolean
	 */
	private boolean billOutsideRange() {
		String v = this.txt_Bill.getText().toString().trim();
		if (v.equals("") || v.equals(".")) return false;
		double val = Double.parseDouble(v);
		if (val > 999999.0) {
			// do truncation.
			v = v.substring(0, v.length() - 1);
			this.txt_Bill.setText(v);
			this.txt_Bill.setSelection(v.length());
			return true;
		}
		return false;
	}
	
	/**
	 * If the number of persons in party is more than threshold, truncate and return true.
	 * Otherwise return false.
	 * 
	 * The threshold is set at 999999.
	 * 
	 * @return boolean
	 */
	private boolean personOutsideRange() {
		String v = this.txt_NumberOfPersons.getText().toString().trim();
		if (v.equals("") || v.equals(".")) return false;
		double val = Double.parseDouble(v);
		if (val > 999999.0) {
			// do truncation.
			v = v.substring(0, v.length() - 1);
			this.txt_NumberOfPersons.setText(v);
			this.txt_NumberOfPersons.setSelection(v.length());
			return true;
		}
		return false;		
	}
	
	/**
	 * Calculate the per person bill.
	 */
	private void calcBillPerPerson() {
		String strTotalBill = this.txt_Bill.getText().toString().trim();
		
		if (strTotalBill.equals("") || strTotalBill.equals(".")) {
			this.txt_Tax.setText("$0.00");
			this.txt_Total.setText("$0.00");

			this.txt_Tip0.setText("$0.00");
			this.txt_Tip1.setText("$0.00");
			this.txt_Tip2.setText("$0.00");
			this.txt_Tip3.setText("$0.00");
			this.txt_TipCustom.setText("$0.00");

			this.txt_Total0.setText("$0.00");
			this.txt_Total1.setText("$0.00");
			this.txt_Total2.setText("$0.00");
			this.txt_Total3.setText("$0.00");
			this.txt_TotalCustom.setText("$0.00");
			
			this.txt_BillPerPerson.setText("$0.00");

			return;
		}
		
		// Get sub-total, tax, tip and total bill.
		double subTotal = (strTotalBill == ".") ? 0 : Double.parseDouble(strTotalBill);
		double tax = subTotal * this.tax_rate;
		double tip = (this.includeTaxForTip()) ?
				((subTotal + tax) * this.tip_rate) : (subTotal * this.tip_rate);
		double totalBill = subTotal + tip + tax;

		// Get number of people
		String strNumberOfPersons = this.txt_NumberOfPersons.getText().toString().trim();
		if (strNumberOfPersons.equals("")) return;
		else if (strNumberOfPersons.equals("0")) {
			this.txt_BillPerPerson.setText("$infinity");
			return;
		}
		int numOfPersons = Integer.parseInt(strNumberOfPersons);
		
		// Calculate per person bill.
		double billPerPerson = totalBill / numOfPersons;
		this.txt_BillPerPerson.setText("$" + String.format("%.2f", billPerPerson));
	}
	
	/**
	 * Where to include tax for tip calculation.
	 * @return boolean
	 */
	private boolean includeTaxForTip() {
		return this.cbIncludeTaxForTip.isChecked();
	}
	
	/**
	 * Update tax and total bill: sub-total + tax.
	 */
	private void updateTaxAndTotal() {
		String strTotalBill = this.txt_Bill.getText().toString().trim();
		if (strTotalBill.equals("") || strTotalBill.equals(".")) {
			this.txt_Tax.setText("$0.00");
			this.txt_Total.setText("$0.00");
		}
		else {
			double bill = Double.parseDouble(strTotalBill);
			double tax = bill * this.tax_rate;
			
			this.txt_Tax.setText("$" + String.format("%.2f", tax));
			this.txt_Total.setText("$" + String.format("%.2f", bill + tax));
		}
	}
	
	/**
	 * Update tip and total bill for each tip rate: sub-total + tax + tip.
	 * @param txtTip
	 * @param txtTotal
	 * @param rate
	 */
	private void updateTipAndTotal(TextView txtTip, TextView txtTotal, double rate) {
		String strTotalBill = this.txt_Bill.getText().toString().trim();
		if (strTotalBill.equals("") || strTotalBill.equals(".")) {
			txtTip.setText("$0.00");
			txtTotal.setText("$0.00");
		}
		else {
			double subTotal = Double.parseDouble(strTotalBill);
			double tax = subTotal * this.tax_rate;
			double tip = (this.includeTaxForTip()) ? ((subTotal + tax) * rate) : subTotal * rate;
			double total = subTotal + tax + tip;
			
			txtTip.setText("$" + String.format("%.2f", tip));
			txtTotal.setText("$" + String.format("%.2f", total));
		}
	}
	
	/**
	 * Increment the number of people in party.
	 * @param view
	 */
	public void btn_IncPerson_OnClick(View view) {
		String v = this.txt_NumberOfPersons.getText().toString().trim();
		int persons = v.equals("") ? 1 : Integer.parseInt(v) + 1;
		this.txt_NumberOfPersons.setText("" + persons);
		this.calcBillPerPerson();
	}
	
	/**
	 * Decrement the number of people in party.
	 * @param view
	 */
	public void btn_DecPerson_OnClick(View view) {
		String v = this.txt_NumberOfPersons.getText().toString().trim();
		int persons = (v.equals("") || v.equals("0")) ? 1 : Integer.parseInt(v) - 1;
		if (persons > 0) {
			this.txt_NumberOfPersons.setText("" + persons);
			this.calcBillPerPerson();
		}
	}

	
	/***********************************************************************
	 * Begin definition for instance variables.
	 ***********************************************************************/
	
	private RadioButton btn_Tip0;
	private RadioButton btn_Tip1;
	private RadioButton btn_Tip2;
	private RadioButton btn_Tip3;
	private RadioButton btn_TipCustom;
	private int btn_Tip_selected;
	
	private TextView txt_Tip0;
	private TextView txt_Tip1;
	private TextView txt_Tip2;
	private TextView txt_Tip3;
	private TextView txt_TipCustom;

	private TextView txt_Total0;
	private TextView txt_Total1;
	private TextView txt_Total2;
	private TextView txt_Total3;
	private TextView txt_TotalCustom;

	private EditText txt_TipCustomInput;
	
	private EditText txt_Bill;
	private double tip_rate;
	private EditText txt_NumberOfPersons;
	private TextView txt_BillPerPerson;
	
	@SuppressWarnings("unused")
	private Button btn_IncPerson;
	
	@SuppressWarnings("unused")
	private Button btn_DecPerson;
	
	private TextView txt_Tax;
	private TextView txt_Total; // sub-total + tax.
	private CheckBox cbIncludeTaxForTip;
	
	private final double tip_rate_0 = 0.1;
	private final double tip_rate_1 = 0.15;
	private final double tip_rate_2 = 0.2;
	private final double tip_rate_3 = 0.25;
	private double tip_rate_custom = 0.17;
	private double tax_rate = 0.04712;
}
