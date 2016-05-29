package time.append;

import com.google.inject.Guice;
import time.messaging.Messager;
import time.messaging.Queue;

public class AppendMain {

	public static void main(final String[] args) throws Exception {
		final AppendRun appendRun = Guice.createInjector(new AppendModule(args)).getInstance(AppendRun.class);

		final Messager messager = new Messager();
		messager.when(Queue.APPEND, Append.class)
					  .then((Append append)-> appendRun.run(append))
                      .thenAccept(toto -> System.out.println(toto));

        messager.signal(Queue.APPEND, new Append());

		//.then((DoneAppend doneAppend) -> messager.signal(doneAppend));

	}

}
