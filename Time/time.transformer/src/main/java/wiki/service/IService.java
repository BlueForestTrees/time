package wiki.service;

import java.io.IOException;

import wiki.component.reader.FinDuScanException;

public interface IService {

	public void onStart();
	public void run(long pageCount) throws IOException, FinDuScanException;
	public void onEnd();

}