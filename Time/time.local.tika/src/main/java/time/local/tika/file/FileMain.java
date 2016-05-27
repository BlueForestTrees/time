package time.local.tika.file;

import com.google.inject.Guice;
import com.google.inject.Injector;
import time.messaging.Messager;
import time.messaging.Queue;

public class FileMain {

	public static void main(final String[] args) throws Exception {
		final FileRun fileRun = Guice.createInjector(new FileModule(args)).getInstance(FileRun.class);

        //TODO messages avec data, pouvoir chainer les then
        new Messager().when(Queue.APPEND).then(()->fileRun.run(append));

	}

}
