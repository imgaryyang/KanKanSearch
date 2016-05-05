package com.kankanews.search.task;

public class TaskManager {

	private Thread incrementIndexTaskThread;
	private IncrementIndexTask incrementIndexTask;
	private Thread incrementAppIndexTaskThread;
	private AppIncrementIndexTask appIncrementIndexTask;

	public void start() {
		if (incrementIndexTaskThread == null) {
			incrementIndexTaskThread = new Thread(incrementIndexTask);
		}
		incrementIndexTaskThread.start();
		if (incrementAppIndexTaskThread == null) {
			incrementAppIndexTaskThread = new Thread(appIncrementIndexTask);
		}
		incrementAppIndexTaskThread.start();
	}

	public void destroy() {
		if (incrementIndexTaskThread != null) {
			incrementIndexTaskThread.interrupt();
		}
		if (incrementAppIndexTaskThread != null) {
			incrementAppIndexTaskThread.interrupt();
		}
	}

	public String getTaskInfo() {
		return "IncrementIndexTask " + incrementIndexTaskThread.getState()
				+ " " + incrementIndexTaskThread.isAlive();
	}

	public IncrementIndexTask getIncrementIndexTask() {
		return incrementIndexTask;
	}

	public void setIncrementIndexTask(IncrementIndexTask incrementIndexTask) {
		this.incrementIndexTask = incrementIndexTask;
	}

	public AppIncrementIndexTask getAppIncrementIndexTask() {
		return appIncrementIndexTask;
	}

	public void setAppIncrementIndexTask(
			AppIncrementIndexTask appIncrementIndexTask) {
		this.appIncrementIndexTask = appIncrementIndexTask;
	}

}
