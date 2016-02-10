package time.transformer.page;

import time.repo.bean.Page;

/**
 * Interface des classes qui trnasforme le contenu des pages pour les adapter au DateFinder.
 * @author slim
 *
 */
public interface IPageTransformer {
    public Page transform(final Page page);
}
