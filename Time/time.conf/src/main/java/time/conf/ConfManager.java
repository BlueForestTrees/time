package time.conf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;

public class ConfManager {

    private static ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

    public static Conf get(final Confs confKey) throws IOException {
        return new Args().toBean(confKey.getPath(), Conf.class);
    }
    public static <T> T get(final Confs confKey, Class<T> clazz) throws IOException {
        return new Args().toBean(confKey.getPath(), clazz);
    }
    public static void update(final Confs confKey, final ConfChanger changer) throws IOException {
        final Conf conf = get(confKey);
        changer.change(conf);
        objectMapper.writeValue(new File(Resolver.get(confKey.getPath())), conf);
    }
}
