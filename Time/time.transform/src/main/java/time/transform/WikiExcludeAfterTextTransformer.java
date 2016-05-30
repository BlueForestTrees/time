package time.transform;

import java.util.List;
import java.util.OptionalInt;

import com.google.inject.Inject;

import time.domain.Conf;
import time.domain.Text;

public class WikiExcludeAfterTextTransformer implements ITextTransformer {

    private List<String> ignoreTextAfterAny;
    
    @Inject
    public WikiExcludeAfterTextTransformer(Conf conf) {
		this.ignoreTextAfterAny = conf.getIgnoreTextAfterAny();
        if(this.ignoreTextAfterAny == null){
            throw new RuntimeException("ignoreTextAfterAny is null");
        }
	}

	@Override
    public void transform(final Text page) {
        final StringBuilder text = page.getText();
        final OptionalInt whereToCut = ignoreTextAfterAny.stream().mapToInt(term -> text.indexOf(term)).filter(v -> v > 0).min();
        if (whereToCut.isPresent()) {
            text.delete(whereToCut.getAsInt(), text.length());
        }
    }
    
}
