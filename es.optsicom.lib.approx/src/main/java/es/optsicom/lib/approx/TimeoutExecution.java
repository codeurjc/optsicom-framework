package es.optsicom.lib.approx;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TimeoutExecution {

	public enum TerminationReason {
		TIMEOUT, EXCEPTION, BEFORE_TIMEOUT
	}

	public static class ExecutionInfo {

		private TerminationReason terminationReason;
		private double duration = -1;
		private Throwable exception;

		public ExecutionInfo(TerminationReason terminationReason,
				double duration, Throwable exception) {
			this.terminationReason = terminationReason;
			this.duration = duration;
			this.exception = exception;
		}

		public TerminationReason getTerminationReason() {
			return terminationReason;
		}

		public double getDuration() {
			return duration;
		}

		public Throwable getException() {
			return exception;
		}

	}

	private long timeout;
	private Runnable runnable;

	private TimeoutExecution(long timeout, Runnable runnable) {
		this.timeout = timeout;
		this.runnable = runnable;
	}

	private ExecutionInfo exec() {

		final Exchanger<Throwable> ex = new Exchanger<>();

		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					runnable.run();
					ex.exchange(null);
				} catch (Throwable e) {
					if (!(e instanceof ThreadDeath)) {
						try {
							ex.exchange(e);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});

		long startTime = System.nanoTime();

		t.start();

		try {
			Throwable e = ex.exchange(null, timeout, TimeUnit.MILLISECONDS);

			long duration = (System.nanoTime() - startTime) / 1000000;

			if (e != null) {
				return new ExecutionInfo(TerminationReason.EXCEPTION, duration,
						e);
			} else {
				return new ExecutionInfo(TerminationReason.BEFORE_TIMEOUT,
						duration, null);
			}

		} catch (TimeoutException e1) {

			t.stop();
			return new ExecutionInfo(TerminationReason.TIMEOUT, -1, null);

		} catch (InterruptedException e1) {
			throw new RuntimeException(e1);
		}
	}

	public static ExecutionInfo exec(long timeout, Runnable runnable) {
		return new TimeoutExecution(timeout, runnable).exec();
	}

}
