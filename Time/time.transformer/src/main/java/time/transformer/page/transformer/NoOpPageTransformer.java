package time.transformer.page.transformer;

import time.repo.bean.Page;

public class NoOpPageTransformer implements IPageTransformer{

	@Override
	public Page transform(Page page) {
		return page;
	}

}
