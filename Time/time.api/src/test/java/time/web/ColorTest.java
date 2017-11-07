package time.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.JVM)
public class ColorTest {

	double min = 0;
	double max = 1000;
	double quart = min + (max - min) / 4;
	double middle = min + (max - min) / 2;
	double quart3 = min + (max - min) / 2 + (max - min) / 4;

	double minColor = 0;
	double maxColor = 255;
	double middleColor = minColor + (maxColor - minColor) / 2;

	@Test
	public void testRedMin() {
		final double count = min;
		final double expectedRed = maxColor;
		final double actualRed = getRed(count);
		assertThat(actualRed).isEqualTo(expectedRed);
	}

	@Test
	public void testRedQuart() {
		final double count = quart;
		final double expectedRed = maxColor;
		final double actualRed = getRed(count);
		assertThat(actualRed).isEqualTo(expectedRed);
	}

	@Test
	public void testRedMiddle() {
		final double count = middle;
		final double expectedRed = maxColor;
		final double actualRed = getRed(count);
		assertThat(actualRed).isEqualTo(expectedRed);
	}

	@Test
	public void testRedQuart3() {
		final double count = quart3;
		final double expectedRed = middleColor;
		final double actualRed = getRed(count);
		assertThat(actualRed).isEqualTo(expectedRed);
	}

	@Test
	public void testRedMax() {
		final double count = max;
		final double expectedRed = minColor;
		final double actualRed = getRed(count);
		assertThat(actualRed).isEqualTo(expectedRed);
	}

	@Test
	public void testGreenMin() {
		final double count = min;
		final double expectedGreen = maxColor;
		final double actualGreen = getGreen(count);
		assertThat(actualGreen).isEqualTo(expectedGreen);
	}

	@Test
	public void testGreenQuart() {
		final double count = quart;
		final double expectedGreen = middleColor;
		final double actualGreen = getGreen(count);
		assertThat(actualGreen).isEqualTo(expectedGreen);
	}

	@Test
	public void testGreenMiddle() {
		final double count = middle;
		final double expectedGreen = minColor;
		final double actualGreen = getGreen(count);
		assertThat(actualGreen).isEqualTo(expectedGreen);
	}

	@Test
	public void testGreenQuart3() {
		final double count = quart3;
		final double expectedGreen = minColor;
		final double actualGreen = getGreen(count);
		assertThat(actualGreen).isEqualTo(expectedGreen);
	}

	@Test
	public void testGreenMax() {
		final double count = max;
		final double expectedGreen = minColor;
		final double actualGreen = getGreen(count);
		assertThat(actualGreen).isEqualTo(expectedGreen);
	}


	public double getGreen(double count) {
		return Math.max(minColor, maxColor - count / max * (maxColor - minColor) * 2);
	}

	public double getRed(double count) {
		return maxColor - Math.max(0, count - middle) / max * (maxColor - minColor) * 2;
	}

	public double getBlue(double count) {
		return 0;
	}


}
