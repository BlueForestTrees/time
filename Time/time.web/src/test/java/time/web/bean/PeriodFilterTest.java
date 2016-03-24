package time.web.bean;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class PeriodFilterTest {
	@Test
	public void build(){
		TermPeriodFilter periodFilter = TermPeriodFilter.build("doudou @200+-10% @100 toto");
		assertThat(periodFilter.getFrom()).isEqualTo(100);
		assertThat(periodFilter.getTo()).isEqualTo(220);
		assertThat(periodFilter.getWords()).containsExactly("doudou","toto");
	}
	@Test
	public void parsePeriod1(){
		assertParsePeriod("@-1", new Period(-1L, 0));
	}
	@Test
	public void parsePeriod2(){
		assertParsePeriod("@-1M", new Period(-364250000000L, 0));
	}
	@Test
	public void parsePeriod3(){
		assertParsePeriod("@-130M", new Period(-130000000000L, 0));
	}
	@Test
	public void parsePeriod4(){
		assertParsePeriod("@-1.43M", new Period(-1430000000L, 0));
	}
	@Test
	public void parsePeriod5(){
		assertParsePeriod("@-1,3M", new Period(-1300000000L, 0));
	}
	@Test
	public void parsePeriod6(){
		assertParsePeriod("@1", new Period(1L, 0));
	}
	@Test
	public void parsePeriod7(){
		assertParsePeriod("@1M", new Period(364250000000L, 0));
	}
	@Test
	public void parsePeriod8(){
		assertParsePeriod("@130M", new Period(130000000000L, 0));
	}
	@Test
	public void parsePeriod9(){
		assertParsePeriod("@1.43M", new Period(1430000000L, 0));
	}
	@Test
	public void parsePeriod10(){
		assertParsePeriod("@1,3M", new Period(1300000000L, 0));
	}
	@Test
	public void parsePeriod11(){		
		assertParsePeriod("@-1m", new Period(-1000000L, 0));
	}
	@Test
	public void parsePeriod12(){
		assertParsePeriod("@-130m", new Period(-130000000L, 0));
	}
	@Test
	public void parsePeriod13(){
		assertParsePeriod("@-1.43m", new Period(-1430000L, 0));
	}
	@Test
	public void parsePeriod14(){
		assertParsePeriod("@-1,3m", new Period(-1300000L, 0));
	}
	@Test
	public void parsePeriod15(){
		assertParsePeriod("@1m", new Period(1000000L, 0));
	}
	@Test
	public void parsePeriod16(){
		assertParsePeriod("@130m", new Period(130000000L, 0));
	}
	@Test
	public void parsePeriod17(){
		assertParsePeriod("@1.43m", new Period(1430000L, 0));
	}
	@Test
	public void parsePeriod18(){
		assertParsePeriod("@1,3m", new Period(1300000L, 0));
	}
	@Test
	public void parsePeriod19(){				
		assertParsePeriod("@1,3M+-10%", new Period(1300000000L, 10));
	}
	@Test
	public void parsePeriod20(){		
//		assertParsePeriod("@1.2M", new Period(-1200000L, null));
//		assertParsePeriod("@17M", new Period(-1000000L, null));
//		assertParsePeriod("@17.56M", new Period(-1000000L, null));
//		assertParsePeriod("@1m", new Period(-1000000L, null));
//		assertParsePeriod("@1.2m", new Period(-1200000L, null));
//		assertParsePeriod("@17m", new Period(-1000000L, null));
//		assertParsePeriod("@17.56m", new Period(-1000000L, null));
//		assertParsePeriod("@1k", new Period(-1000000L, null));
//		assertParsePeriod("@1.2k", new Period(-1200000L, null));
//		assertParsePeriod("@17k", new Period(-1000000L, null));
//		assertParsePeriod("@17.56k", new Period(-1000000L, null));
//		
//		assertParsePeriod("@-1M", new Period(-1000000L, null));
//		assertParsePeriod("@-1.2M", new Period(-1200000L, null));
//		assertParsePeriod("@-17M", new Period(-1000000L, null));
//		assertParsePeriod("@-17.56M", new Period(-1000000L, null));
//		assertParsePeriod("@-1m", new Period(-1000000L, null));
//		assertParsePeriod("@-1.2m", new Period(-1200000L, null));
//		assertParsePeriod("@-17m", new Period(-1000000L, null));
//		assertParsePeriod("@-17.56m", new Period(-1000000L, null));
//		assertParsePeriod("@-1k", new Period(-1000000L, null));
//		assertParsePeriod("@-1.2k", new Period(-1200000L, null));
//		assertParsePeriod("@-17k", new Period(-1000000L, null));
//		assertParsePeriod("@-17.56k", new Period(-1000000L, null));
//
//		assertParsePeriod("@1M@10%", new Period(-1000000L, null));
//		assertParsePeriod("@1.2M@10%", new Period(-1200000L, null));
//		assertParsePeriod("@17M@10%", new Period(-1000000L, null));
//		assertParsePeriod("@17.56M@10%", new Period(-1000000L, null));
//		assertParsePeriod("@1m@10%", new Period(-1000000L, null));
//		assertParsePeriod("@1.2m@10%", new Period(-1200000L, null));
//		assertParsePeriod("@17m@10%", new Period(-1000000L, null));
//		assertParsePeriod("@17.56m@10%", new Period(-1000000L, null));
//		assertParsePeriod("@1k@10%", new Period(-1000000L, null));
//		assertParsePeriod("@1.2k@10%", new Period(-1200000L, null));
//		assertParsePeriod("@17k@10%", new Period(-1000000L, null));
//		assertParsePeriod("@17.56k@10%", new Period(-1000000L, null));
//		
//		assertParsePeriod("@-1M@10%", new Period(-1000000L, null));
//		assertParsePeriod("@-1.2M@10%", new Period(-1200000L, null));
//		assertParsePeriod("@-17M@10%", new Period(-1000000L, null));
//		assertParsePeriod("@-17.56M@10%", new Period(-1000000L, null));
//		assertParsePeriod("@-1m@10%", new Period(-1000000L, null));
//		assertParsePeriod("@-1.2m@10%", new Period(-1200000L, null));
//		assertParsePeriod("@-17m@10%", new Period(-1000000L, null));
//		assertParsePeriod("@-17.56m@10%", new Period(-1000000L, null));
//		assertParsePeriod("@-1k@10%", new Period(-1000000L, null));
//		assertParsePeriod("@-1.2k@10%", new Period(-1200000L, null));
//		assertParsePeriod("@-17k@10%", new Period(-1000000L, null));
//		assertParsePeriod("@-17.56k@10%", new Period(-1000000L, null));
//		
//		//années
//		assertParsePeriod("@2000", new Period(-1000000L, null));
//		assertParsePeriod("@-2000", new Period(-1000000L, null));
//		assertParsePeriod("@2000@10%", new Period(-1000000L, null));
//		assertParsePeriod("@-2000@10%", new Period(-1000000L, null));
//		//jours
//		assertParsePeriod("@1000000", new Period(-1000000L, null));
//		assertParsePeriod("@-1000000", new Period(-1000000L, null));
//		assertParsePeriod("@1000000@10%", new Period(-1000000L, null));
//		assertParsePeriod("@-1000000@10%", new Period(-1000000L, null));
	}

	private void assertParsePeriod(final String part, final Period expectedPeriod) {
		assertThat(TermPeriodFilter.parsePeriod(part)).isEqualToComparingFieldByField(expectedPeriod);
	}
	
}
