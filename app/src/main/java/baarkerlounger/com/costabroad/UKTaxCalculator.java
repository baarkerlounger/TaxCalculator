package baarkerlounger.com.costabroad;

import java.math.BigDecimal;

/**
 * Class to calculate Gross and Net income based on UK income tax system
 */
public class UKTaxCalculator implements TaxCalculatorInterface {

    //Gross to Net Values
    final private static BigDecimal INITIAL_TAX_FREE_THRESHOLD = BigDecimal.valueOf(10600);
    final private static BigDecimal HIGHER_RATE_THRESHOLD = BigDecimal.valueOf(31785);
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
    final private static BigDecimal INITIAL_NET_HIGHER_RATE_THRESHOLD = BigDecimal.valueOf(36028);
    private static BigDecimal NET_HIGHER_RATE_THRESHOLD = INITIAL_NET_HIGHER_RATE_THRESHOLD;
    final private static BigDecimal NET_ADDITIONAL_RATE_THRESHOLD = BigDecimal.valueOf(96357);
    final private static BigDecimal NET_TAX_FREE_REDUCTION_THRESHOLD = BigDecimal.valueOf(70597);
    final private static BigDecimal NET_NI_LOWER_THRESHOLD = NI_LOWER_THRESHOLD.multiply(NI_LOWER_RATE);
    final private static BigDecimal NET_NI_HIGHER_THRESHOLD = NI_HIGHER_THRESHOLD.multiply(NI_HIGHER_RATE);

    final private boolean NI;

    //Constructor - Determines whether National Insurance should be included or not
    public UKTaxCalculator(boolean NI){this.NI = NI;}


    public BigDecimal getGross(BigDecimal net){

        BigDecimal gross;
        BigDecimal niAmount = BigDecimal.ZERO;

        //Check if TAX Free amount needs to be reduced
        BigDecimal taxFreeThreshold = adjustTaxFreeThreshold(net, NET_TAX_FREE_REDUCTION_THRESHOLD);

        if(net.compareTo(taxFreeThreshold) <= 0){
            gross = net;
        }
        //Basic Rate
        else if(net.compareTo(NET_HIGHER_RATE_THRESHOLD) <= 0){
            gross = calculateBasicGrossFromNet(net, taxFreeThreshold);
        }
        //Basic Rate + Higher Rate
        else if(net.compareTo(NET_ADDITIONAL_RATE_THRESHOLD) <= 0) {
            gross = calculateHigherGrossFromNet(net, taxFreeThreshold);
        }
        //Basic Rate + Higher Rate + Additional Rate
        else{
            gross = calculateAdditionalGrossFromNet(net, taxFreeThreshold);
        }

        //If National Insurance is included
        if(NI){
            if(net.compareTo(NET_NI_HIGHER_THRESHOLD) >= 0){
                niAmount = reversePercentage(net, NI_HIGHER_RATE).subtract(net);
            }
            else if(net.compareTo(NET_NI_LOWER_THRESHOLD) >= 0){
                niAmount = reversePercentage(net, NI_LOWER_RATE).subtract(net);
            }
            gross = gross.add(niAmount);
        }

        return gross;
    }

    public BigDecimal getNet(BigDecimal gross){

        BigDecimal net;
        BigDecimal tax;
        BigDecimal niAmount = BigDecimal.ZERO;

        //For every GBP2 over 100,000 earned, Tax Free Allowance reduces by GBP1
        //Check if TAX Free amount needs to be reduced
        BigDecimal taxFreeThreshold = adjustTaxFreeThreshold(gross, TAX_FREE_REDUCTION_THRESHOLD);

        BigDecimal taxable = gross.subtract(taxFreeThreshold);

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

    private BigDecimal adjustTaxFreeThreshold (BigDecimal salary, BigDecimal Threshold){

        BigDecimal TAX_FREE_THRESHOLD = INITIAL_TAX_FREE_THRESHOLD;

        //For every GBP2 over 100,000 earned, Tax Free Allowance reduces by GBP1
        //This also reduces the NET_HIGHER_RATE_THRESHOLD
        if (salary.compareTo(Threshold) > 0){
            BigDecimal amountOverReductionThreshold = (salary.subtract(Threshold)).multiply(BigDecimal.valueOf(0.5));
            if (amountOverReductionThreshold.compareTo(INITIAL_TAX_FREE_THRESHOLD) < 0) {
                TAX_FREE_THRESHOLD = TAX_FREE_THRESHOLD.subtract(amountOverReductionThreshold);
                NET_HIGHER_RATE_THRESHOLD = INITIAL_NET_HIGHER_RATE_THRESHOLD.subtract(amountOverReductionThreshold);
            }
            else{
                TAX_FREE_THRESHOLD = BigDecimal.ZERO;
                NET_HIGHER_RATE_THRESHOLD = INITIAL_NET_HIGHER_RATE_THRESHOLD.subtract(INITIAL_TAX_FREE_THRESHOLD);
            }
        }
        return  TAX_FREE_THRESHOLD;
    }

    private BigDecimal calculateBasicGrossFromNet(BigDecimal net, BigDecimal taxFreeThreshold){

        BigDecimal step1;
        BigDecimal step2;

        //Get taxable Net
        step1 = net.subtract(taxFreeThreshold);
        //Divide by 80% to get 1% of Original and Multiply by 100% to get original
        step2 = reversePercentage(step1, BASIC_RATE);
        return (step2.add(taxFreeThreshold));
    }

    private BigDecimal calculateHigherGrossFromNet(BigDecimal net, BigDecimal taxFreeThreshold){
        BigDecimal amountOverHigher = net.subtract(NET_HIGHER_RATE_THRESHOLD);
        BigDecimal basicGross = calculateBasicGrossFromNet(NET_HIGHER_RATE_THRESHOLD, taxFreeThreshold);
        BigDecimal higherGross = reversePercentage(amountOverHigher, HIGHER_RATE);
        return basicGross.add(higherGross);
    }

    private BigDecimal calculateAdditionalGrossFromNet(BigDecimal net, BigDecimal taxFreeThreshold){
        BigDecimal amountOverAdditional = net.subtract(NET_ADDITIONAL_RATE_THRESHOLD);
        BigDecimal higherGross = calculateHigherGrossFromNet(NET_ADDITIONAL_RATE_THRESHOLD, taxFreeThreshold);
        BigDecimal additionalGross = reversePercentage(amountOverAdditional, ADDITIONAL_RATE);
        return higherGross.add(additionalGross);
    }

    private BigDecimal reversePercentage(BigDecimal amount, BigDecimal rate){

        //Get 1% of Original
        BigDecimal divisor = (BigDecimal.valueOf(1).subtract(rate));
        return amount.divide(divisor, 2, BigDecimal.ROUND_HALF_UP);
    }
}

