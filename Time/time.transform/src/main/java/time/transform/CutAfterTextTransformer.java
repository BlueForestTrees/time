package time.transform;

import java.util.List;
import java.util.OptionalInt;

import com.google.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.domain.Conf;
import time.domain.Text;
import time.domain.Transformer;

public class CutAfterTextTransformer implements ITextTransformer {

    private static final Logger LOGGER = LogManager.getLogger(CutAfterTextTransformer.class);

    private List<String> ignoreTextAfterAny;
    
    @Inject
    public CutAfterTextTransformer(Transformer conf) {
		this.ignoreTextAfterAny = conf.getIgnoreTextAfterAny();
        if(this.ignoreTextAfterAny == null){
            throw new RuntimeException("ignoreTextAfterAny is null");
        }
        LOGGER.info(this);
	}

	@Override
    public void transform(final Text page) {
        final StringBuilder text = page.getText();
        final OptionalInt whereToCut = ignoreTextAfterAny.stream().mapToInt(term -> text.indexOf(term)).filter(v -> v > 0).min();
        if (whereToCut.isPresent()) {
            text.delete(whereToCut.getAsInt(), text.length());
        }
    }

    @Override
    public String toString() {
        return "CutAfterTextTransformer{" +
                "ignoreTextAfterAny=" + ignoreTextAfterAny +
                '}';
    }
}
