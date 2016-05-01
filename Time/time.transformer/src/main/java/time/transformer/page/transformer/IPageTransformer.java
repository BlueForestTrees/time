package time.transformer.page.transformer;

import time.repo.bean.Text;

/**
 * Interface des classes qui trnasforme le contenu des pages pour les adapter au DateFinder.
 * @author slim
 *
 */
public interface IPageTransformer {
    public Text transform(final Text text);
}
