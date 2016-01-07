package com.kankanews.search.task;

public class TaskManager {

	private static Thread incrementIndexTask;

	public Thread getIncrementIndexTask() {
		return incrementIndexTask;
	}

	public void setIncrementIndexTask(Thread incrementIndexTask) {
		this.incrementIndexTask = incrementIndexTask;
	}

}
