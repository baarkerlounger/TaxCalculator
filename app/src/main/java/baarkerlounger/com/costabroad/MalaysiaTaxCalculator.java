package baarkerlounger.com.costabroad;

import java.math.BigDecimal;

/**
 *Created by dan on 23/08/15
 */
public class MalaysiaTaxCalculator implements TaxCalculatorInterface {

    @Override
    public BigDecimal getNet(BigDecimal gross) {

        final BigDecimal level1 = BigDecimal.valueOf(5001);
        final BigDecimal level2 = BigDecimal.valueOf(10001);
        final BigDecimal level3 = BigDecimal.valueOf(20001);
        final BigDecimal level4 = BigDecimal.valueOf(35001);
        final BigDecimal level5 = BigDecimal.valueOf(50001);
        final BigDecimal level6 = BigDecimal.valueOf(70001);
        final BigDecimal level7 = BigDecimal.valueOf(100000);

        BigDecimal tax;
        BigDecimal taxBase;
        BigDecimal taxPercentage;
        BigDecimal percentageAppliedSalary;
        BigDecimal net;

        if(gross.compareTo(level7) > 0){
            percentageAppliedSalary = gross.subtract(level7);
            taxPercentage           = BigDecimal.valueOf(0.26);
            taxBase                 = BigDecimal.valueOf(13850);
        }
        else if(gross.compareTo(level6) > 0){
            percentageAppliedSalary = gross.subtract(level6);
            taxPercentage           = BigDecimal.valueOf(0.24);
            taxBase                 = BigDecimal.valueOf(6650);
        }
        else if(gross.compareTo(level5) > 0){
            percentageAppliedSalary = gross.subtract(level5);
            taxPercentage           = BigDecimal.valueOf(0.19);
            taxBase                 = BigDecimal.valueOf(2850);
        }
        else if(gross.compareTo(level4) > 0){
            percentageAppliedSalary = gross.subtract(level4);
            taxPercentage           = BigDecimal.valueOf(0.11);
            taxBase                 = BigDecimal.valueOf(1200);
        }
        else if(gross.compareTo(level3) > 0){
            percentageAppliedSalary = gross.subtract(level3);
            taxPercentage           = BigDecimal.valueOf(0.006);
            taxBase                 = BigDecimal.valueOf(300);
        }
        else if(gross.compareTo(level2) > 0){
            percentageAppliedSalary = gross.subtract(level2);
            taxPercentage           = BigDecimal.valueOf(0.02);
            taxBase                 = BigDecimal.valueOf(100);
        }
        else if(gross.compareTo(level1) > 0){
            percentageAppliedSalary = gross.subtract(level1);
            taxPercentage           = BigDecimal.valueOf(0.02);
            taxBase                 = BigDecimal.ZERO;
        }
        else{
            percentageAppliedSalary = BigDecimal.ZERO;
            taxPercentage           = BigDecimal.ZERO;
            taxBase                 = BigDecimal.ZERO;
        }

        tax = (percentageAppliedSalary.multiply(taxPercentage)).add(taxBase);
        net = gross.subtract(tax);

        if(net.compareTo(BigDecimal.ZERO) <= 0){
            net = gross;
        }

        // TODO: 23/08/15
        //Add National Insurance Contribution

        return net;
    }

    @Override
    public BigDecimal getGross(BigDecimal net) {
        // TODO: 23/08/15
        //Implement
        return null;
    }
}
