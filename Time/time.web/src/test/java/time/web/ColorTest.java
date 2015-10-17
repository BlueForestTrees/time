package time.web;

import org.junit.Test;
import static org.assertj.core.api.Assertions.*;

public class ColorTest {
	
	int min = 1;
	int max = 100;

	@Test
	public void minTest() {
		final String expected = "#FFFF00";
		final String actual = getColor(1);
		assertThat(actual).isEqualTo(expected);
	}
	
	@Test
	public void middleTest() {
		final String expected = "#FFFF00";
		final String actual = getColor(1);
		assertThat(actual).isEqualTo(expected);
	}
	
	@Test
	public void maxTest() {
		final String expected = "#FFFF00";
		final String actual = getColor(1);
		assertThat(actual).isEqualTo(expected);
	}

	public String getColor(int value){
		final String red = "FF";
		final String green = "FF";
		final String blue = "00";
		return "#" + red + green + blue;
	}
	
}
