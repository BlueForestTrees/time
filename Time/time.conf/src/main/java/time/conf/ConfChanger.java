package time.conf;

@FunctionalInterface
public interface ConfChanger {
    void change(final Conf conf);
}
