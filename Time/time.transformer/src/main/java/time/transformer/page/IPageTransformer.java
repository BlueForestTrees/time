package time.transformer.page;

import time.repo.bean.Page;

/**
 * Interface des classes qui trnasforme le contenu des pages pour les adapter au DateFinder.
 * @author slim
 *
 */
public interface IPageTransformer {
    public Page transform(final Page page);
    //TODO si https://fr.wikipedia.org/wiki/141_av._J.-C. => années négatives (_av._J.-C. négative)
    //TODO si https://fr.wikipedia.org/wiki/Ann%C3%A9es_100 => décennies (_av._J.-C. négative)
    //TODO si https://fr.wikipedia.org/wiki/IIe_si%C3%A8cle => siècle (_av._J.-C. négative)
    //TODO si https://fr.wikipedia.org/wiki/Ier_mill%C3%A9naire => millenaire (_av._J.-C. négative)
}
