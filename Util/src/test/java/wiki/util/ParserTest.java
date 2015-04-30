package wiki.util;

import org.junit.Test;

import wiki.tool.parser.AnneeParser;
import wiki.tool.parser.MilliardParser;
import wiki.tool.parser.MillionParser;
import wiki.tool.parser.RomainParser;
import static org.assertj.core.api.Assertions.assertThat;

public class ParserTest {
	@Test
	public void testMilliardParser(){
		MilliardParser parser = new MilliardParser();
		String valueS = "65"; 
		Long valueL = parser.from(valueS);
		String valueS2 = parser.to(valueL);
		
		assertThat(valueS2).isEqualTo("il y a 65 milliards d'années");
		assertThat(valueL).isEqualTo(-23676250000000L);
	}
	
	@Test
	public void testMillionParser(){
		MillionParser parser = new MillionParser();
		String valueS = "65"; 
		Long valueL = parser.from(valueS);
		String valueS2 = parser.to(valueL);
		
		assertThat(valueS2).isEqualTo("il y a 65 millions d'années");
		assertThat(valueL).isEqualTo(-23676250000L);
	}
	
	@Test
	public void testAnneeParser(){
		AnneeParser parser = new AnneeParser();
		String valueS = "65"; 
		Long valueL = parser.from(valueS);
		String valueS2 = parser.to(valueL);
		
		assertThat(valueS2).isEqualTo("en 65");
		assertThat(valueL).isEqualTo(23676L);
	}
	
	@Test
	public void testAnneeParserNeg(){
		AnneeParser parser = new AnneeParser();
		String valueS = "-65"; 
		Long valueL = parser.from(valueS);
		String valueS2 = parser.to(valueL);
		
		assertThat(valueS2).isEqualTo("en -65 av. JC");
		assertThat(valueL).isEqualTo(-23676L);
	}
	
	@Test
	public void testRomainParser(){
		RomainParser parser = new RomainParser();
		String valueS = "xvi"; 
		Long valueL = parser.from(valueS);
		String valueS2 = parser.to(valueL);
		
		assertThat(valueS2).isEqualTo("au XVIe siècle");
		assertThat(valueL).isEqualTo(582800L);
	}
}
