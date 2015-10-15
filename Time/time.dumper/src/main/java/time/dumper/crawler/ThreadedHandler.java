package time.dumper.crawler;

public abstract class ThreadedHandler extends BasePageHandler implements Runnable {

	private Thread thread;
	
	private boolean end;

	public ThreadedHandler() {
		super();
		end = false;
	}

	public void start() {
		if(thread != null)return;
		thread = new Thread(this);
		thread.start();
	}
	
	public boolean isEnd(){
		return end;
	}
	public void end() {
		end  = true;
	}

}