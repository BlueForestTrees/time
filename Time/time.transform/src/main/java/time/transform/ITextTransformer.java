package time.transform;

import time.domain.Text;

/**
 * Interface des classes qui transforme le contenu des pages pour les adapter au DateFinder.
 * @author slim
 *
 */
@FunctionalInterface
public interface ITextTransformer {
    public void transform(final Text text);
}
