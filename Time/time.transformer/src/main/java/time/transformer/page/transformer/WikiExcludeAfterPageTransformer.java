package time.transformer.page.transformer;

import java.util.List;
import java.util.OptionalInt;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import time.conf.Conf;
import time.repo.bean.Text;

/**
 * Supprime le contenu de la page situé après les mots clés {@link excludeAfter}
 * @author slim
 *
 */
public class WikiExcludeAfterPageTransformer implements IPageTransformer {

    private List<String> excludeAfterList;
    
    @Inject
    public WikiExcludeAfterPageTransformer(@Named("conf") Conf conf) {
		this.excludeAfterList = conf.getExcludeAfterList();
	}

	@Override
    public Text transform(final Text page) {
        final StringBuilder text = page.getText();
        final OptionalInt whereToCut = excludeAfterList.stream().mapToInt(term -> text.indexOf(term)).filter(v -> v > 0).min();
        if (whereToCut.isPresent()) {
            text.delete(whereToCut.getAsInt(), text.length());
        }
        return page;
    }
    
}
