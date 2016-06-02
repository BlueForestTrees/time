package time.storage.filter;

import java.util.HashSet;
import java.util.List;

import com.google.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.domain.Conf;
import time.domain.Text;

public class TextFilter {
	private static final Logger LOGGER = LogManager.getLogger(TextFilter.class);

	private HashSet<String> urlsLowerCase = new HashSet<String>();
	private int urlMaxLength;
	protected List<String> urlMustNotContain;

	@Inject
	public TextFilter(final Conf conf) {
		this.urlMaxLength = conf.getUrlMaxLength();
		this.urlMustNotContain = conf.getUrlMustNotContain();
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
		final String url = text.getUrl().toLowerCase();
		final boolean urlTooLong = text.getUrl().length() > urlMaxLength;
		final boolean isUrlMustNotContainExcluded = urlMustNotContain != null && urlMustNotContain.stream().anyMatch(url::contains);


		boolean isValid = !urlTooLong && !isUrlMustNotContainExcluded;

		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("urlTooLong " + urlTooLong);
			LOGGER.debug("isUrlMustNotContainExcluded" + isUrlMustNotContainExcluded);
			LOGGER.debug("isValid " + isValid);
		}
		return isValid;
	}

	@Override
	public String toString() {
		return "TextFilter{" +
				"urlsLowerCase=" + urlsLowerCase +
				", urlMaxLength=" + urlMaxLength +
				'}';
	}


}
