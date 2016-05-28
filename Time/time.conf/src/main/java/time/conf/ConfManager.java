package time.conf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.io.ByteStreams;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class ConfManager {

    private static final Logger LOGGER = LogManager.getLogger(ConfManager.class);
    public static final String CONF = "conf";

    private ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

    public Conf get(final ConfEnum confKey) throws IOException {
        return get(confKey.getPath(), Conf.class);
    }
    public Conf get(final String[] args) throws IOException, ArgumentParserException {
        return get(args, Conf.class);
    }
    public <T> T get(final ConfEnum confKey, Class<T> clazz) throws IOException {
        return get(confKey.getPath(), clazz);
    }
    public Conf get(final String[] args, final ConfEnum confEnum) throws IOException, ArgumentParserException {
        return get(args, Conf.class, confEnum.getPath());
    }
    public <T> T get(final String[] args, final ConfEnum confEnum, Class<T> clazz) throws IOException, ArgumentParserException {
        return get(args, clazz, confEnum.getPath());
    }
    public <T> T get(final String[] args, final Class<T> beanClass) throws ArgumentParserException, IOException{
        return get(confInArgs(args, CONF).get(), beanClass);
    }
    public <T> T get(final String[] args, final Class<T> beanClass, final String def) throws ArgumentParserException, IOException{
        return get(confInArgs(args, "conf").orElse(Resolver.get(def)), beanClass);
    }
    public <T> T get(final String ymlPath, Class<T> beanClass) throws IOException{
        final String rawConfig = new String(ByteStreams.toByteArray(new FileInputStream(new File(Resolver.get(ymlPath)))), StandardCharsets.UTF_8);
        final String substituedConfig = Resolver.get(rawConfig);
        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        LOGGER.debug("{} content\n{}", ymlPath, substituedConfig);

        return mapper.readValue(substituedConfig, beanClass);
    }

    public void update(final ConfEnum confKey, final ConfModifier changer) throws IOException {
        final Conf conf = get(confKey);
        changer.change(conf);
        objectMapper.writeValue(new File(Resolver.get(confKey.getPath())), conf);
    }

    public Optional<String> confInArgs(final String[] args, final String key) throws ArgumentParserException {
        final Namespace namespace = parse(args);
        return Optional.ofNullable(namespace.getString(key));
    }

    public Namespace parse(final String[] args) throws ArgumentParserException{
        return buildArgumentParser().parseArgs(args);
    }

    public ArgumentParser buildArgumentParser(){
        final ArgumentParser argParser = ArgumentParsers.newArgumentParser("java -jar path/to/jarname.jar -conf path/to/conf.yml", false);
        argParser.addArgument("-conf").nargs("?").help("configuration File");
        return argParser;
    }
}
