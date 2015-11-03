package time.dumper.writer.layout;

import java.nio.charset.Charset;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;

public class PageLayout extends AbstractStringLayout {

    public PageLayout() {
        super(Charset.defaultCharset());
    }

    /**
	 * 
	 */
    private static final long serialVersionUID = 2961171351622684601L;

    public String toSerializable(LogEvent event) {
        return event.getMessage().getFormattedMessage();
    }

}
