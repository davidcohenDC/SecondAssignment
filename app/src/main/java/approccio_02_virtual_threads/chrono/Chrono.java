package approccio_02_virtual_threads.chrono;

public class Chrono {

	private boolean running;
	private long startTime;

	public Chrono() {
		running = false;
	}
	
	public void start() {
		running = true;
		startTime = System.currentTimeMillis();
	}
	
	public void stop() {
		startTime = getTime();
		running = false;
	}
	
	public long getTime() {
		if (running) {
			return 	System.currentTimeMillis() - startTime;
		} else {
			return startTime;
		}
	}
}
