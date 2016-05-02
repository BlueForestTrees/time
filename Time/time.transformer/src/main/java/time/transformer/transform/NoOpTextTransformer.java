package time.transformer.transform;

import time.repo.bean.Text;

public class NoOpTextTransformer implements ITextTransformer {

	@Override
	public Text transform(Text text) {
		return text;
	}

}
