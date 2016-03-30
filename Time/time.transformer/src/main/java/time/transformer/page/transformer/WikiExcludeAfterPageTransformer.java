package time.transformer.page.transformer;

import java.util.Arrays;
import java.util.OptionalInt;

import org.springframework.stereotype.Component;

import time.repo.bean.Page;

/**
 * Supprime le contenu de la page situé après les mots clés {@link excludeAfter}
 * @author slim
 *
 */
@Component
public class WikiExcludeAfterPageTransformer implements IPageTransformer {

    /**
     * Les mots clés à detecter dans les pages.
     */
    private static final String[] excludeAfter = new String[] { "Notes et références[", "Bibliographie[", "Liens externes[", "Bibliographie[", "Annexes[" };
    
    @Override
    public Page transform(final Page page) {
        final StringBuilder text = page.getText();
        final OptionalInt whereToCut = Arrays.stream(excludeAfter).mapToInt(term -> text.indexOf(term)).filter(v -> v > 0).min();
        if (whereToCut.isPresent()) {
            text.delete(whereToCut.getAsInt(), text.length());
        }
        return page;
    }
    
}
