package time.storage.transform;

import time.repo.bean.Text;

/**
 * Interface des classes qui trnasforme le contenu des pages pour les adapter au DateFinder.
 * @author slim
 *
 */
@FunctionalInterface
public interface ITextTransformer {
    public Text transform(final Text text);
}
