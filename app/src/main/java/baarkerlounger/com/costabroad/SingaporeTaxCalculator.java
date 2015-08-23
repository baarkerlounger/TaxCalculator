package baarkerlounger.com.costabroad;

import java.math.BigDecimal;

/**
 *Created by dan on 23/08/15.
 */
public class SingaporeTaxCalculator implements TaxCalculatorInterface {

    @Override
    public BigDecimal getNet(BigDecimal gross) {

        final BigDecimal level1 = BigDecimal.valueOf(20000);
        final BigDecimal level2 = BigDecimal.valueOf(30000);
        final BigDecimal level3 = BigDecimal.valueOf(40000);
        final BigDecimal level4 = BigDecimal.valueOf(80000);
        final BigDecimal level5 = BigDecimal.valueOf(120000);
        final BigDecimal level6 = BigDecimal.valueOf(160000);
        final BigDecimal level7 = BigDecimal.valueOf(200000);
        final BigDecimal level8 = BigDecimal.valueOf(320000);

        BigDecimal tax;
        BigDecimal taxBase;
        BigDecimal taxPercentage;
        BigDecimal percentageAppliedSalary;
        BigDecimal net;

        if(gross.compareTo(level8) > 0){
            percentageAppliedSalary = gross.subtract(level8);
            taxPercentage           = BigDecimal.valueOf(0.2);
            taxBase                 = BigDecimal.valueOf(42350);
        }
        else if(gross.compareTo(level7) > 0){
            percentageAppliedSalary = gross.subtract(level7);
            taxPercentage           = BigDecimal.valueOf(0.18);
            taxBase                 = BigDecimal.valueOf(20750);
        }
        else if(gross.compareTo(level6) > 0){
            percentageAppliedSalary = gross.subtract(level6);
            taxPercentage           = BigDecimal.valueOf(0.17);
            taxBase                 = BigDecimal.valueOf(13950);
        }
        else if(gross.compareTo(level5) > 0){
            percentageAppliedSalary = gross.subtract(level5);
            taxPercentage           = BigDecimal.valueOf(0.15);
            taxBase                 = BigDecimal.valueOf(7950);
        }
        else if(gross.compareTo(level4) > 0){
            percentageAppliedSalary = gross.subtract(level4);
            taxPercentage           = BigDecimal.valueOf(0.115);
            taxBase                 = BigDecimal.valueOf(3350);
        }
        else if(gross.compareTo(level3) > 0){
            percentageAppliedSalary = gross.subtract(level3);
            taxPercentage           = BigDecimal.valueOf(0.07);
            taxBase                 = BigDecimal.valueOf(550);
        }
        else if(gross.compareTo(level2) > 0){
            percentageAppliedSalary = gross.subtract(level2);
            taxPercentage           = BigDecimal.valueOf(0.035);
            taxBase                 = BigDecimal.valueOf(200);
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

        return net;
    }

    @Override
    public BigDecimal getGross(BigDecimal net) {
        return null;
    }
}
