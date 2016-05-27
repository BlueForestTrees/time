package time.append;

import com.google.inject.Guice;
import com.google.inject.Injector;
import time.messaging.Messager;
import time.messaging.Queue;

public class FileMain {

	public static void main(final String[] args) throws Exception {
		final FileRun fileRun = Guice.createInjector(new FileModule(args)).getInstance(FileRun.class);

		final Messager messager = new Messager();
		messager.when(Queue.APPEND, Append.class)
					  .then((Append append)->fileRun.run(append))
                      .thenAccept(toto -> System.out.println(toto));

        messager.signal(Queue.APPEND, new Append());

		//.then((DoneAppend doneAppend) -> messager.signal(doneAppend));

	}

}
