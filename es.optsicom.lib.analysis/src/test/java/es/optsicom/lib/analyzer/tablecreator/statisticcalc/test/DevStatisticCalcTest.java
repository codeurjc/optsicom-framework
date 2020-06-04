package es.optsicom.lib.analyzer.tablecreator.statisticcalc.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import es.optsicom.lib.analyzer.report.table.NumericFormat.NumberType;
import es.optsicom.lib.analyzer.tablecreator.statisticcalc.DevStatisticCalc;
import es.optsicom.lib.util.ArraysUtil;
import es.optsicom.lib.util.BestMode;

public class DevStatisticCalcTest {

	@Test
	public void testRelativizeDoubleArrayNumber() {
		Double[] values = { 1.0, 2.0, 4.0 };
		double bestValue = 6.0;
		DevStatisticCalc dev = new DevStatisticCalc(BestMode.MAX_IS_BEST);
		Double[] actualRelValues = dev.relativize(values, bestValue);
		Double[] expectedRelValues = { (bestValue - 1.0) / bestValue, (bestValue - 2.0) / bestValue,
				(bestValue - 4.0) / bestValue };

		for (int index = 0; index < actualRelValues.length; index++) {
			assertEquals(expectedRelValues[index], actualRelValues[index], 0.1);
		}

		values = new Double[] { 2.0, 3.0, 5.0 };
		bestValue = 1.0;
		dev = new DevStatisticCalc(BestMode.MIN_IS_BEST);
		actualRelValues = dev.relativize(values, bestValue);
		expectedRelValues = new Double[] { (2.0 - bestValue) / bestValue, (3.0 - bestValue) / bestValue,
				(5.0 - bestValue) / bestValue };

		for (int index = 0; index < actualRelValues.length; index++) {
			assertEquals(expectedRelValues[index], actualRelValues[index], 0.1);
		}

	}

	@Test
	public void testGetName() {
		DevStatisticCalc dev = new DevStatisticCalc();
		assertEquals("Name is not valid", "Dev. (" + BestMode.MAX_IS_BEST + ")", dev.getName());

		dev = new DevStatisticCalc(BestMode.MIN_IS_BEST);
		assertEquals("Name is not valid", "Dev. (" + BestMode.MIN_IS_BEST + ")", dev.getName());

	}

	@Test
	public void testGetNumberType() {
		DevStatisticCalc dev = new DevStatisticCalc();
		assertEquals("Number type incorrect", NumberType.PERCENT, dev.getNumberType());
	}

	@Test
	public void testGetBestMode() {
		DevStatisticCalc dev = new DevStatisticCalc();
		assertEquals(BestMode.MAX_IS_BEST, dev.getBestMode());

		dev.setBestMode(BestMode.MIN_IS_BEST);
		assertEquals(BestMode.MIN_IS_BEST, dev.getBestMode());

		dev = new DevStatisticCalc(BestMode.MAX_IS_BEST);
		assertEquals(BestMode.MAX_IS_BEST, dev.getBestMode());

		dev.setBestMode(BestMode.MIN_IS_BEST);
		assertEquals(BestMode.MIN_IS_BEST, dev.getBestMode());

		dev = new DevStatisticCalc(BestMode.MIN_IS_BEST);
		assertEquals(BestMode.MIN_IS_BEST, dev.getBestMode());

		dev.setBestMode(BestMode.MAX_IS_BEST);
		assertEquals(BestMode.MAX_IS_BEST, dev.getBestMode());

	}

	@Test
	public void testRelativizeDoubleArray() {
		Double[] values = { 1.0, 2.0, 4.0 };
		DevStatisticCalc dev = new DevStatisticCalc(BestMode.MAX_IS_BEST);
		Double[] actualRelValues = dev.relativize(values);
		Double[] expectedRelValues = { (4.0 - 1.0) / 4.0, (4.0 - 2.0) / 4.0, (4.0 - 4.0) / 4.0 };

		for (int index = 0; index < actualRelValues.length; index++) {
			assertEquals(expectedRelValues[index], actualRelValues[index], 0.1);
		}

		values = new Double[] { 2.0, 3.0, 5.0 };
		dev = new DevStatisticCalc(BestMode.MIN_IS_BEST);
		actualRelValues = dev.relativize(values);
		expectedRelValues = new Double[] { (2.0 - 2.0) / 2.0, (3.0 - 2.0) / 2.0, (5.0 - 2.0) / 2.0 };

		for (int index = 0; index < actualRelValues.length; index++) {
			assertEquals(expectedRelValues[index], actualRelValues[index], 0.1);
		}

	}

	@Test
	public void testSummarize() {
		DevStatisticCalc dev = new DevStatisticCalc();
		Double[] values = new Double[] { 1.0, 2.0, 4.0 };
		double actual = dev.summarize(values);
		double expected = ArraysUtil.sum(values) / values.length;

		assertEquals(expected, actual, 0.1);
	}

	@Test
	public void testGetResultsBestMode() {
		DevStatisticCalc dev = new DevStatisticCalc();
		assertEquals(BestMode.MAX_IS_BEST, dev.getBestMode());

		dev.setBestMode(BestMode.MIN_IS_BEST);
		assertEquals(BestMode.MIN_IS_BEST, dev.getBestMode());

		dev = new DevStatisticCalc();
		assertEquals(BestMode.MIN_IS_BEST, dev.getResultsBestMode());

		dev.setResultsBestMode(BestMode.MAX_IS_BEST);
		assertEquals(BestMode.MAX_IS_BEST, dev.getResultsBestMode());

	}

}
