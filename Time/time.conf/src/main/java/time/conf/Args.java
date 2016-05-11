package time.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.text.StrLookup;
import org.apache.commons.lang3.text.StrSubstitutor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.io.ByteStreams;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Allow building configuration beans.
 * @author slimane
 *
 */
public class Args {

  private static final Logger LOGGER = LogManager.getLogger(Args.class);

  /**
   * @param args
   * @param beanClass
   * @return a bean based on the specified cli args (-conf) and the bean Class
   * @throws ArgumentParserException
   * @throws FileNotFoundException
   * @throws IOException
   */
  public <T> T toBean(final String[] args, final Class<T> beanClass) throws ArgumentParserException, IOException{
    final Namespace namespace = parse(args);
    final String confPath = namespace.getString("conf");
	return toBean(confPath, beanClass);
  }

  /**
   * @param args
   * @return a namespace based on the specified cli args.
   * @throws ArgumentParserException
   */
  public Namespace parse(final String[] args) throws ArgumentParserException{
    return buildArgumentParser().parseArgs(args);
  }
  
  /**
   * Overriden method should be chained
   * @return
   */
  public ArgumentParser buildArgumentParser(){
    final ArgumentParser argParser = ArgumentParsers.newArgumentParser("java -jar path/to/jarname.jar -conf path/to/conf.yml", false);
    argParser.addArgument("-conf").nargs("?").setDefault("conf.yml").help("configuration File");
    return argParser;
  }
  
  /**
   * @param ymlPath
   * @param beanClass
   * @return a bean from a yaml file path and the bean Class
   * @throws FileNotFoundException
   * @throws IOException
   */
  public <T> T toBean(final String ymlPath, Class<T> beanClass) throws IOException{
    final String rawConfig = new String(ByteStreams.toByteArray(new FileInputStream(new File(ymlPath))), StandardCharsets.UTF_8);
    final String substituedConfig = getEnvSubstitutor().replace(rawConfig);
    final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    LOGGER.info("yml: \n" + substituedConfig);

    return mapper.readValue(substituedConfig, beanClass);
  }

  public StrSubstitutor getEnvSubstitutor() {
    return new StrSubstitutor(new StrLookup<Object>() {
      @Override
      public String lookup(String key) {
        final String value = System.getenv(key);
        if(value == null){
          throw new RuntimeException("variable d'environnement manquante : " + key);
        }
        return value;
      }
    });
  }

}

