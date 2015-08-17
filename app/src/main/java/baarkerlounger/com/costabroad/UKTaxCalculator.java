package baarkerlounger.com.costabroad;

import java.math.BigDecimal;

/**
 * Class to calculate Gross and Net income based on UK income tax system
 */
public class UKTaxCalculator {

    //Gross to Net Values
    final private static BigDecimal INITIAL_TAX_FREE_THRESHOLD = BigDecimal.valueOf(10600);
    final private static BigDecimal HIGHER_RATE_THRESHOLD = BigDecimal.valueOf(31865);
    final private static BigDecimal ADDITIONAL_RATE_THRESHOLD = BigDecimal.valueOf(150000);
    final private static BigDecimal BASIC_RATE = BigDecimal.valueOf(0.2);
    final private static BigDecimal HIGHER_RATE = BigDecimal.valueOf(0.4);
    final private static BigDecimal ADDITIONAL_RATE = BigDecimal.valueOf(0.45);
    final private static BigDecimal TAX_FREE_REDUCTION_THRESHOLD = BigDecimal.valueOf(100000);

    //Net to Gross values
    final private static BigDecimal NET_HIGHER_RATE_THRESHOLD = BigDecimal.valueOf(27612);

    BigDecimal getUKGross(BigDecimal net){

        BigDecimal TAX_FREE_THRESHOLD = INITIAL_TAX_FREE_THRESHOLD;

        if(net.compareTo(TAX_FREE_THRESHOLD) <= 0){
            return net;
        }

        BigDecimal testNet = BigDecimal.valueOf(0);
        BigDecimal gross = net;

        while(net.compareTo(testNet) != 0){
            testNet = getUKNet(gross);
            gross = gross.add(BigDecimal.valueOf(0.01));
        }
        gross = gross.subtract(BigDecimal.valueOf(0.01));
        return gross;
    }

    BigDecimal getUKNet(BigDecimal gross){

        BigDecimal net;
        BigDecimal tax;
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
            return gross;
        }

        //If Only Basic Rate
        if(taxable.compareTo(HIGHER_RATE_THRESHOLD) <= 0){
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

        return net;
    }
}


