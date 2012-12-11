package mobi.monaca.framework.template.test;


import android.test.suitebuilder.TestSuiteBuilder;
import junit.framework.TestSuite;

public class AllTests extends TestSuite {

	public static TestSuite suite() {
		return new TestSuiteBuilder(AllTests.class)
			.includeAllPackagesUnderHere()
			.build();
	}

}
