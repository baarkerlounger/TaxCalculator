package baarkerlounger.com.costabroad;

import java.math.BigDecimal;

/**
 * Created by dan on 8/10/15
 */
class UKTaxCalculator {

    final private static BigDecimal TAX_FREE_THRESHOLD = BigDecimal.valueOf(10000);
    final private static BigDecimal HIGHER_RATE_THRESHOLD = BigDecimal.valueOf(31865);
    final private static BigDecimal ADDITIONAL_RATE_THRESHOLD = BigDecimal.valueOf(150000);
    final private static BigDecimal BASIC_RATE = BigDecimal.valueOf(0.2);
    final private static BigDecimal HIGHER_RATE = BigDecimal.valueOf(0.4);
    final private static BigDecimal ADDITIONAL_RATE = BigDecimal.valueOf(0.45);

    BigDecimal getUKGross(BigDecimal net){

        if(net.compareTo(TAX_FREE_THRESHOLD) <= 0){
            return net;
        }
        return net;
    }

    BigDecimal getUKNet(BigDecimal gross){

        BigDecimal net;
        BigDecimal tax;
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


