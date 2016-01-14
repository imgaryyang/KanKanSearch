package com.kankanews.search.task;

public class TaskManager {

	private Thread incrementIndexTaskThread;
	private IncrementIndexTask incrementIndexTask;

	public void start() {
		if (incrementIndexTaskThread == null) {
			incrementIndexTaskThread = new Thread(incrementIndexTask);
		}
		incrementIndexTaskThread.start();
	}

	public void destroy() {
		if (incrementIndexTaskThread != null) {
			incrementIndexTaskThread.interrupt();
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

}
