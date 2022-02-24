package me.invertmc.sql;

import me.invertmc.utils.StackUtil;

public class SQLThread extends Thread {

	/* 6 */ private boolean running = false;
	private SQLManager sql;
	private long delay;

	public SQLThread(SQLManager sql, long delay) {
		/* 11 */ this.sql = sql;
		/* 12 */ this.delay = delay;
	}

	public void run() {
		try {
			/* 17 */ while (this.running) {
				try {
					/* 19 */ this.sql.close();
					/* 20 */ this.sql.open();
				} catch (Throwable t) {
					/* 22 */ StackUtil.dumpStack(t);
				}
				try {
					/* 26 */ Thread.sleep(this.delay * 1000L);
				} catch (InterruptedException e) {
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void start(long delay) {
		/* 34 */ this.delay = delay;

		/* 36 */ if (this.running) {
			/* 37 */ interrupt();
		}
		/* 39 */ start();
	}

	public void start() {
		/* 44 */ if (this.running) {
			/* 45 */ return;
		}
		/* 47 */ this.running = true;
		/* 48 */ super.start();
	}

	public void interrupt() {
		/* 53 */ if (!this.running) {
			/* 54 */ return;
		}
		/* 56 */ this.running = false;
		/* 57 */ super.interrupt();
	}

	public boolean isRunning() {
		/* 61 */ return this.running;
	}

}