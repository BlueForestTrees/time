package time.transformer.page.transformer;

import time.repo.bean.Text;

public class NoOpPageTransformer implements IPageTransformer{

	@Override
	public Text transform(Text text) {
		return text;
	}

}
