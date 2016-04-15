package time.crawler.write;

public interface IWriter {

	void writePage(final String url, final String title, final String metadata, final String text);

}
