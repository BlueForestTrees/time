package time.conf;

import java.io.IOException;

public class ConfManager {
    public static Conf get(final Confs confKey) throws IOException {
        return new Args().toBean(confKey.getPath(), Conf.class);
    }
    public static <T> T get(final Confs confKey, Class<T> clazz) throws IOException {
        return new Args().toBean(confKey.getPath(), clazz);
    }
    public static void update(final Confs confKey, final ConfChanger changer) throws IOException {
        changer.change(get(confKey));
    }
}
