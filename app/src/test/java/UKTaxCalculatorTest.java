import junit.framework.Assert;

import org.junit.Test;

import java.math.BigDecimal;

import baarkerlounger.com.costabroad.TaxCalculatorInterface;
import baarkerlounger.com.costabroad.UKTaxCalculator;

/**
 * Test the UKTaxCalculator Class
 */
public class UKTaxCalculatorTest {


    @Test
    public void testBasic(){
        //Test from Gross to Net and Back
        TaxCalculatorInterface taxCalculator = new UKTaxCalculator(false);
        BigDecimal grossInput = BigDecimal.valueOf(18000.00);
        BigDecimal net = taxCalculator.getNet(grossInput);
        Assert.assertEquals(16520.00, net.doubleValue());
        BigDecimal gross = taxCalculator.getGross(net);
        Assert.assertEquals(gross.doubleValue(), grossInput.doubleValue());

        //Test from Net to Gross and Back
        BigDecimal netInput = BigDecimal.valueOf(16520.00);
                   gross = taxCalculator.getGross(netInput);
                   net = taxCalculator.getNet(gross);

        Assert.assertEquals(grossInput.doubleValue(), gross.doubleValue());
        Assert.assertEquals(net.doubleValue(), netInput.doubleValue());

    }

    @Test
    public void testHigher(){
        //Test from Gross to Net and Back
        TaxCalculatorInterface taxCalculator = new UKTaxCalculator(false);
        BigDecimal grossInput = BigDecimal.valueOf(48500.00);
        BigDecimal net = taxCalculator.getNet(grossInput);
        Assert.assertEquals(39697.00, net.doubleValue());
        BigDecimal gross = taxCalculator.getGross(net);
        Assert.assertEquals(gross.doubleValue(), grossInput.doubleValue());

        //Test from Net to Gross and Back
        BigDecimal netInput = BigDecimal.valueOf(39697.00);
        gross = taxCalculator.getGross(netInput);
        Assert.assertEquals(grossInput.doubleValue(), gross.doubleValue());
        net = taxCalculator.getNet(gross);
        Assert.assertEquals(net.doubleValue(), netInput.doubleValue());

    }


}
