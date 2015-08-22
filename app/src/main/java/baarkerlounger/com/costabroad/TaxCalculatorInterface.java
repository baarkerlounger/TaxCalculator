package baarkerlounger.com.costabroad;

import java.math.BigDecimal;

/**
 * Interface all country tax calculators will implement
 */
public interface TaxCalculatorInterface {

    BigDecimal getNet(BigDecimal gross, boolean NI);
    //Takes Gross income and returns Net income

    BigDecimal getGross(BigDecimal net, boolean NI);
    //Takes Net Income and returns gross income

}
