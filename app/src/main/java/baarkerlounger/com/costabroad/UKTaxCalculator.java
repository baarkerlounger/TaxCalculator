package baarkerlounger.com.costabroad;

import java.math.BigDecimal;

/**
 * Class to calculate Gross and Net income based on UK income tax system
 */
public class UKTaxCalculator implements TaxCalculatorInterface {

    //Gross to Net Values
    final private static BigDecimal INITIAL_TAX_FREE_THRESHOLD = BigDecimal.valueOf(10600);
    final private static BigDecimal HIGHER_RATE_THRESHOLD = BigDecimal.valueOf(31865);
    final private static BigDecimal ADDITIONAL_RATE_THRESHOLD = BigDecimal.valueOf(150000);
    final private static BigDecimal BASIC_RATE = BigDecimal.valueOf(0.2);
    final private static BigDecimal HIGHER_RATE = BigDecimal.valueOf(0.4);
    final private static BigDecimal ADDITIONAL_RATE = BigDecimal.valueOf(0.45);
    final private static BigDecimal TAX_FREE_REDUCTION_THRESHOLD = BigDecimal.valueOf(100000);
    final private static BigDecimal NI_LOWER_THRESHOLD = BigDecimal.valueOf(8064);
    final private static BigDecimal NI_HIGHER_THRESHOLD = BigDecimal.valueOf(42384);
    final private static BigDecimal NI_LOWER_RATE = BigDecimal.valueOf(0.12);
    final private static BigDecimal NI_HIGHER_RATE = BigDecimal.valueOf(0.02);


    //Net to Gross values
    final private static BigDecimal NET_HIGHER_RATE_THRESHOLD = BigDecimal.valueOf(27612);
    final private static BigDecimal NET_ADDITIONAL_RATE_THRESHOLD = BigDecimal.valueOf(78643);
    final private static BigDecimal AMOUNT_UNDER_HIGHER_RATE = BigDecimal.valueOf(4253);


    public BigDecimal getGross(BigDecimal net, boolean NI){

        BigDecimal TAX_FREE_THRESHOLD = INITIAL_TAX_FREE_THRESHOLD;
        BigDecimal step1;
        BigDecimal step2;
        BigDecimal step3;
        BigDecimal gross = BigDecimal.valueOf(0);

        if(net.compareTo(TAX_FREE_THRESHOLD) <= 0){
            return net;
        }

        //Basic Rate
        if(net.compareTo(NET_HIGHER_RATE_THRESHOLD) <= 0){
            //Get taxable Net
            step1 = net.subtract(TAX_FREE_THRESHOLD);
            //Divide by 80% to get 1% of Original
            step2 = step1.divide((BigDecimal.valueOf(1).subtract(BASIC_RATE)), 2, BigDecimal.ROUND_HALF_UP);
            //Multiply by 100% to get Original
            step3 = step2.multiply(BigDecimal.valueOf(1));
            gross = step3.add(TAX_FREE_THRESHOLD);
        }
        //Basic Rate + Higher Rate
        else if(net.compareTo(NET_ADDITIONAL_RATE_THRESHOLD) <= 0) {

        }

        return gross;
    }

    public BigDecimal getNet(BigDecimal gross, boolean NI){

        BigDecimal net;
        BigDecimal tax;
        BigDecimal niAmount = BigDecimal.ZERO;
        BigDecimal TAX_FREE_THRESHOLD = INITIAL_TAX_FREE_THRESHOLD;

        //For every GBP2 over 100,000 earned, Tax Free Allowance reduces by GBP1
        if (gross.compareTo(TAX_FREE_REDUCTION_THRESHOLD) > 0){
            BigDecimal amountOverReductionThreshold = gross.subtract(TAX_FREE_REDUCTION_THRESHOLD);
            if (amountOverReductionThreshold.compareTo(TAX_FREE_THRESHOLD) > 0) {
                TAX_FREE_THRESHOLD = TAX_FREE_THRESHOLD.subtract(amountOverReductionThreshold);
            }
            else{
                TAX_FREE_THRESHOLD = BigDecimal.ZERO;
            }
        }

        BigDecimal taxable = gross.subtract(TAX_FREE_THRESHOLD);

        //If Gross is less than Tax Free Threshold do nothing
        //compareTo method returns 0 for equal to operator
        if(taxable.compareTo(BigDecimal.valueOf(0)) <= 0){
            tax = BigDecimal.ZERO;
        }
        //If Only Basic Rate
        else if(taxable.compareTo(HIGHER_RATE_THRESHOLD) <= 0){
            tax = taxable.multiply(BASIC_RATE);
        }
        //If Basic Rate + Higher Rate
        else if(taxable.compareTo(ADDITIONAL_RATE_THRESHOLD) <= 0){
                  //Basic Rate Component
            tax = (HIGHER_RATE_THRESHOLD.multiply(BASIC_RATE));
                 //Higher Rate Component
            tax = tax.add(((taxable.subtract(HIGHER_RATE_THRESHOLD)).multiply(HIGHER_RATE)));
        }
        //If Basic Rate + Higher Rate + Additional Rate
        else{
            //Basic Rate Component
            tax = (HIGHER_RATE_THRESHOLD.multiply(BASIC_RATE));
            //Higher Rate Component
            tax = tax.add(((ADDITIONAL_RATE_THRESHOLD.subtract(HIGHER_RATE_THRESHOLD)).multiply(HIGHER_RATE)));
            //Additional Rate Component
            tax = tax.add((taxable.subtract(ADDITIONAL_RATE_THRESHOLD)).multiply(ADDITIONAL_RATE));
        }

        net = gross.subtract(tax);

        //If National Insurance is included
        if(NI){
            if(gross.compareTo(NI_HIGHER_THRESHOLD) >= 0){
                niAmount = gross.multiply(NI_HIGHER_RATE);
            }
            else if(gross.compareTo(NI_LOWER_THRESHOLD) >= 0){
                niAmount = gross.multiply(NI_LOWER_RATE);
            }
            net = net.subtract(niAmount);
        }

        return net;
    }
}


