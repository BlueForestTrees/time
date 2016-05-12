package time.storage.filter;

import java.util.HashSet;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import time.conf.Conf;
import time.domain.Text;

public class TextFilter {

	private HashSet<String> urlsLowerCase = new HashSet<String>();
	private int urlMaxLength;
	private List<String> urlBlackList;

	@Inject
	public TextFilter(@Named("conf") Conf conf) {
		this.urlBlackList = conf.getUrlBlackList();
		this.urlMaxLength = conf.getUrlMaxLength();
	}

    public boolean keep(final Text text){
        if(!isNew(text) || !isValid(text)){
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
		return !urlsLowerCase.contains(normalizedUrl(text));
	}

	private String normalizedUrl(Text text) {
		return text.getUrl().toLowerCase().replace("-", "_");
	}

	private boolean isValid(Text text) {
		final String url = text.getUrl();
		final boolean urlBlackListed = urlBlackList.stream().anyMatch(term -> url.contains(term));
		final boolean urlTooLong = text.getUrl().length() > urlMaxLength;

		return !urlTooLong && !urlBlackListed;
	}


}
