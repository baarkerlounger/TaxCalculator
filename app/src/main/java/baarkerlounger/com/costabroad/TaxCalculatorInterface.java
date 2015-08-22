package baarkerlounger.com.costabroad;

import java.math.BigDecimal;

/**
 * Interface all country tax calculators will implement
 */
public interface TaxCalculatorInterface {

    BigDecimal getNet(BigDecimal gross);
    //Takes Gross income and returns Net income

    BigDecimal getGross(BigDecimal net);
    //Takes Net Income and returns gross income

}
