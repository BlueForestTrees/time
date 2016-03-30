package time.web.bean;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class PeriodFilterTest {
	@Test
	public void build(){
		TermPeriodFilter periodFilter = TermPeriodFilter.build("doudou @200+-10% @100 toto");
		assertThat(periodFilter.getFrom()).isEqualTo(36525L);
		assertThat(periodFilter.getTo()).isEqualTo(80353L);
		assertThat(periodFilter.getWords()).containsExactly("doudou","toto");
	}
	
	@Test
	public void build1(){
		TermPeriodFilter periodFilter = TermPeriodFilter.build("doudou toto");
		assertThat(periodFilter.getFrom()).isNull();
		assertThat(periodFilter.getTo()).isNull();
		assertThat(periodFilter.getWords()).containsExactly("doudou","toto");
	}
	
	@Test
	public void parsePeriodMinMode(){
		assertFromTo("@1984min", 724641L, Long.MAX_VALUE);
		assertFromTo("@1984max", Long.MIN_VALUE, 725005L);
		assertFromTo("@-10max", Long.MIN_VALUE, -3652L + 364L);
		assertFromTo("@-10min", -3652L, Long.MAX_VALUE);
		assertFromTo("@-1", -365L, -1L);
		assertFromTo("@1", 366L, 730L);
		assertFromTo("@1M", -400675000000L, -327825000000L);
		assertFromTo("@1m", -400675000L, -327825000L);
		assertFromTo("@130m", -52087750000L, -42617250000L);
		assertFromTo("@130M", -52087750000000L, -42617250000000L);
		assertFromTo("@1.5m", -601012500L, -491737500L);
		assertFromTo("@1.5M", -601012500000L, -491737500000L);
		assertFromTo("@1,5m", -601012500L, -491737500L);
		assertFromTo("@1,5M", -601012500000L, -491737500000L);
		assertFromTo("@1,5m+-20%", -655650000L, -437100000L);
		assertFromTo("@1,5M+-20%", -655650000000L, -437100000000L);
	}
	
	private void assertFromTo(final String request, long from, long to) {
		final Period period = TermPeriodFilter.parsePeriod(request);
		assertThat(period.getFrom()).isEqualTo(from);
		assertThat(period.getTo()).isEqualTo(to);
	}	
}
