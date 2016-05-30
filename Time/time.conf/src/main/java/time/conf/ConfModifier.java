package time.conf;

import time.domain.Conf;

@FunctionalInterface
public interface ConfModifier {
    void change(final Conf conf);
}
