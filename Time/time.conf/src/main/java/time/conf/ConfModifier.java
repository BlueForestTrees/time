package time.conf;

@FunctionalInterface
public interface ConfModifier {
    void change(final Conf conf);
}
