package baarkerlounger.com.costabroad;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

public class IncomeTaxComparator extends AppCompatActivity {

    EditText inputNet;
    EditText inputGross;
    TaxCalculatorInterface taxCalculator = new UKTaxCalculator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_tax_comparator);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_income_tax_comparator, menu);
        inputNet = (EditText) findViewById(R.id.inputNet);
        inputGross = (EditText) findViewById(R.id.inputGross);
        setInitialValues();
        setTextBoxHandlers();

        return true;
    }

    private void setTextBoxHandlers() {
        //Set Text watcher for the input boxes
        inputNet.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateInputGross();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        inputGross.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateInputNet();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void updateInputNet() {
        if(!inputNet.hasFocus()){
            BigDecimal inputGross = getInputGross();
            BigDecimal valueNet = taxCalculator.getNet(inputGross, false);
            setInputNet(valueNet.doubleValue());
        }
    }

    private void updateInputGross() {
        if(!inputGross.hasFocus()){
            BigDecimal inputNet = getInputNet();
            BigDecimal valueGross = taxCalculator.getGross(inputNet, false);
            setInputGross(valueGross.doubleValue());
        }
    }

    private void setInitialValues() {
        setInputDefaults();
    }

    private void setInputDefaults() {
        BigDecimal grossDefault = BigDecimal.valueOf(30000.00);
        setInputGross(grossDefault.doubleValue());
        BigDecimal netDefault = taxCalculator.getNet(grossDefault, false);
        setInputNet(netDefault.doubleValue());
    }

    private void setInputNet(double value){
        String output = convertToCurrency(value);
        inputNet.setText(output);
        inputNet.setSelection(output.length());
    }

    private BigDecimal getInputNet(){
        String valueString = inputNet.getText().toString();
        valueString = sanitizeCurrencyString(valueString);
        try{
            return new BigDecimal(valueString);
        }catch (Exception e){
            return BigDecimal.valueOf(0);
        }
    }

    private void setInputGross(double value){
        String output = convertToCurrency(value);
        inputGross.setText(output);
        inputGross.setSelection(output.length());
    }

    private BigDecimal getInputGross(){
        String valueString = inputGross.getText().toString();
        valueString = sanitizeCurrencyString(valueString);
        try{
            return new BigDecimal(valueString);
        }catch (Exception e){
            return BigDecimal.valueOf(0);
        }
    }

    private String sanitizeCurrencyString (String currency){

        try {
            Number number = NumberFormat.getCurrencyInstance().parse(currency);
            currency = number.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return currency;
    }

    private String convertToCurrency(Double value){
        return DecimalFormat.getCurrencyInstance().format(value);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
