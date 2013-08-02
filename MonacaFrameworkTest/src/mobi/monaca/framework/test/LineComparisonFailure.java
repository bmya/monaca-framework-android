package mobi.monaca.framework.test;

import android.test.ComparisonFailure;

public class LineComparisonFailure extends ComparisonFailure {
	private static final long serialVersionUID = 1L;
	protected String expected, actual;
	public LineComparisonFailure(String expected, String actual) {
		super("", expected, actual);
		this.expected = expected;
		this.actual = actual;
	}
	@Override
	public String toString() {
		return "expected: " + expected + "\nactual: " + actual;
	}
	
	public static void doAssert(String expected, String actual) {
		if (!expected.equals(actual)) {
			throw new LineComparisonFailure(expected, actual);
		}
	}
}