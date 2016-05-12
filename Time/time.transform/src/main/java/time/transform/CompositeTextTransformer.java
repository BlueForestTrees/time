package time.transform;

import com.google.inject.Inject;
import time.domain.Text;

import java.util.ArrayList;
import java.util.List;

public class CompositeTextTransformer implements ITextTransformer{

    private List<ITextTransformer> transformers;

    @Inject
    public CompositeTextTransformer(final WikiExcludeAfterTextTransformer wikiExclude, final WikiUrlDateTextTransformer wikiUrlDate){
        transformers = new ArrayList<>();
        transformers.add(wikiExclude);
        transformers.add(wikiUrlDate);
    }

    @Override
    public void transform(final Text text) {
        transformers.forEach(t->t.transform(text));
    }
}
