package wiki.test;

import static org.junit.Assert.assertEquals;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

public class XTest extends BaseTest{

	@Test
	public void testNewLine(){
		String data = "oskdioj\nnzedi";
		Pattern p = Pattern.compile("oj[\r\n]+nz", Pattern.MULTILINE);
		Matcher matcher = p.matcher(data);
		
		int found = 0;

		while (matcher.find()) {
			found++;
			log.info(matcher.group());
		}

		assertEquals(1, found);
		
		
	}
}
