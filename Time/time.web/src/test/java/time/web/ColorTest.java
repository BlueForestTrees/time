package time.web;

import org.junit.Test;
import static org.assertj.core.api.Assertions.*;

public class ColorTest {
	
	int min = 1;
	int middle = 500;
	int max = 1000;

	@Test
	public void minTest() {
		final int expectedRed = 255;
		final int actualRed = getRed(50);
		assertThat(actualRed).isEqualTo(expectedRed);
	}
	


	public int getRed(int value){
		return 255 - ();
	}
	
}
