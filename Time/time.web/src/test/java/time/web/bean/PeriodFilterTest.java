package time.web.bean;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PeriodFilterTest {
	
	@Test
	public void parsePeriod1(){
		assertParsePeriod("@1M", new Period(-1000000L, null));
		assertParsePeriod("@1.2M", new Period(-1200000L, null));
		assertParsePeriod("@17M", new Period(-1000000L, null));
		assertParsePeriod("@17.56M", new Period(-1000000L, null));
		assertParsePeriod("@1m", new Period(-1000000L, null));
		assertParsePeriod("@1.2m", new Period(-1200000L, null));
		assertParsePeriod("@17m", new Period(-1000000L, null));
		assertParsePeriod("@17.56m", new Period(-1000000L, null));
		assertParsePeriod("@1k", new Period(-1000000L, null));
		assertParsePeriod("@1.2k", new Period(-1200000L, null));
		assertParsePeriod("@17k", new Period(-1000000L, null));
		assertParsePeriod("@17.56k", new Period(-1000000L, null));
		
		assertParsePeriod("@-1M", new Period(-1000000L, null));
		assertParsePeriod("@-1.2M", new Period(-1200000L, null));
		assertParsePeriod("@-17M", new Period(-1000000L, null));
		assertParsePeriod("@-17.56M", new Period(-1000000L, null));
		assertParsePeriod("@-1m", new Period(-1000000L, null));
		assertParsePeriod("@-1.2m", new Period(-1200000L, null));
		assertParsePeriod("@-17m", new Period(-1000000L, null));
		assertParsePeriod("@-17.56m", new Period(-1000000L, null));
		assertParsePeriod("@-1k", new Period(-1000000L, null));
		assertParsePeriod("@-1.2k", new Period(-1200000L, null));
		assertParsePeriod("@-17k", new Period(-1000000L, null));
		assertParsePeriod("@-17.56k", new Period(-1000000L, null));

		assertParsePeriod("@1M@10%", new Period(-1000000L, null));
		assertParsePeriod("@1.2M@10%", new Period(-1200000L, null));
		assertParsePeriod("@17M@10%", new Period(-1000000L, null));
		assertParsePeriod("@17.56M@10%", new Period(-1000000L, null));
		assertParsePeriod("@1m@10%", new Period(-1000000L, null));
		assertParsePeriod("@1.2m@10%", new Period(-1200000L, null));
		assertParsePeriod("@17m@10%", new Period(-1000000L, null));
		assertParsePeriod("@17.56m@10%", new Period(-1000000L, null));
		assertParsePeriod("@1k@10%", new Period(-1000000L, null));
		assertParsePeriod("@1.2k@10%", new Period(-1200000L, null));
		assertParsePeriod("@17k@10%", new Period(-1000000L, null));
		assertParsePeriod("@17.56k@10%", new Period(-1000000L, null));
		
		assertParsePeriod("@-1M@10%", new Period(-1000000L, null));
		assertParsePeriod("@-1.2M@10%", new Period(-1200000L, null));
		assertParsePeriod("@-17M@10%", new Period(-1000000L, null));
		assertParsePeriod("@-17.56M@10%", new Period(-1000000L, null));
		assertParsePeriod("@-1m@10%", new Period(-1000000L, null));
		assertParsePeriod("@-1.2m@10%", new Period(-1200000L, null));
		assertParsePeriod("@-17m@10%", new Period(-1000000L, null));
		assertParsePeriod("@-17.56m@10%", new Period(-1000000L, null));
		assertParsePeriod("@-1k@10%", new Period(-1000000L, null));
		assertParsePeriod("@-1.2k@10%", new Period(-1200000L, null));
		assertParsePeriod("@-17k@10%", new Period(-1000000L, null));
		assertParsePeriod("@-17.56k@10%", new Period(-1000000L, null));
		
		//années
		assertParsePeriod("@2000", new Period(-1000000L, null));
		assertParsePeriod("@-2000", new Period(-1000000L, null));
		assertParsePeriod("@2000@10%", new Period(-1000000L, null));
		assertParsePeriod("@-2000@10%", new Period(-1000000L, null));
		//jours
		assertParsePeriod("@1000000", new Period(-1000000L, null));
		assertParsePeriod("@-1000000", new Period(-1000000L, null));
		assertParsePeriod("@1000000@10%", new Period(-1000000L, null));
		assertParsePeriod("@-1000000@10%", new Period(-1000000L, null));
	}

	private void assertParsePeriod(final String part, final Period expectedPeriod) {
		assertThat(PeriodFilter.parsePeriod(part)).isEqualToComparingFieldByField(expectedPeriod);
	}
	
}
