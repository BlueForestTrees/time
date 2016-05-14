package time.storage.filter;

import java.util.HashSet;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.conf.Conf;
import time.domain.Text;
import time.storage.store.TextStore;

public class TextFilter {
	private static final Logger LOGGER = LogManager.getLogger(TextFilter.class);

	private HashSet<String> urlsLowerCase = new HashSet<String>();
	private int urlMaxLength;
	private List<String> urlBlackList;

	@Inject
	public TextFilter(@Named("conf") Conf conf) {
		this.urlBlackList = conf.getUrlBlackList();
		this.urlMaxLength = conf.getUrlMaxLength();
		LOGGER.info(this);
	}

    public boolean keep(final Text text){
		boolean nokeep = !isNew(text) || !isValid(text);
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("keep " + !nokeep);
		}
		if(nokeep){
            return false;
        }else{
            remember(text);
            return true;
        }
    }

	private void remember(Text text) {
		urlsLowerCase.add(normalizedUrl(text));
	}

	private boolean isNew(Text text) {
		boolean isNew = !urlsLowerCase.contains(normalizedUrl(text));
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("is new: " + isNew);
		}
		return isNew;
	}

	private String normalizedUrl(Text text) {
		final String normalized = text.getUrl().toLowerCase().replace("-", "_");
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("normalized url : " + normalized);
		}
		return normalized;
	}

	private boolean isValid(Text text) {
		final String url = text.getUrl();
		final boolean urlBlackListed = urlBlackList.stream().anyMatch(term -> url.contains(term));
		final boolean urlTooLong = text.getUrl().length() > urlMaxLength;

		boolean isValid = !urlTooLong && !urlBlackListed;

		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("urlTooLong " + urlTooLong);
			LOGGER.debug("urlBlackListed " + urlBlackListed);
			LOGGER.debug("isValid " + isValid);
		}
		return isValid;
	}

	@Override
	public String toString() {
		return "TextFilter{" +
				"urlsLowerCase=" + urlsLowerCase +
				", urlMaxLength=" + urlMaxLength +
				", urlBlackList=" + urlBlackList +
				'}';
	}


}
