package time.transform;

import java.util.List;
import java.util.OptionalInt;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import time.conf.Conf;
import time.domain.Text;

public class WikiExcludeAfterTextTransformer implements ITextTransformer {

    private List<String> excludeAfterList;
    
    @Inject
    public WikiExcludeAfterTextTransformer(@Named("conf") Conf conf) {
		this.excludeAfterList = conf.getExcludeAfterList();
        if(this.excludeAfterList == null){
            throw new RuntimeException("excludeAfterList null");
        }
	}

	@Override
    public void transform(final Text page) {
        final StringBuilder text = page.getText();
        final OptionalInt whereToCut = excludeAfterList.stream().mapToInt(term -> text.indexOf(term)).filter(v -> v > 0).min();
        if (whereToCut.isPresent()) {
            text.delete(whereToCut.getAsInt(), text.length());
        }
    }
    
}
