package time.transformer.service;

import java.io.IOException;

import time.transformer.component.reader.FinDuScanException;

public interface IModule {

	public void onStart();
	public long run(long pageCount) throws IOException, FinDuScanException;
	public void onEnd();

}