package wiki.test;

import org.joda.time.DateTime;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

import wiki.util.Chrono;

public class ChronoTest {
	@Test
	public void testDivideBy(){
		Chrono chrono = new Chrono("Test");
		DateTime start = DateTime.now();
		DateTime stop = start.plusSeconds(60);
		long dividedBy = 3;
		String expectedResult = "20s";
		
		chrono.withStart(start);
		chrono.withStop(stop);
		
		String actualResult = chrono.toStringDividedBy(dividedBy);
		
		assertThat(actualResult).isEqualTo(expectedResult);
	}
}
