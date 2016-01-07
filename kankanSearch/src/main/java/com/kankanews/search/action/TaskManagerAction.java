package com.kankanews.search.action;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kankanews.search.config.GlobalConfig;
import com.kankanews.search.task.TaskManager;

@Controller
@RequestMapping("/task")
public class TaskManagerAction {

	Logger logger = Logger.getLogger(TaskManagerAction.class);

	@Autowired
	private TaskManager taskManager;

	@RequestMapping("/getTaskInfo")
	@ResponseBody
	public String getTaskInfo() {
		Thread incrementIndexTask = taskManager.getIncrementIndexTask();
		return "IncrementIndexTask " + incrementIndexTask.getState() + " "
				+ incrementIndexTask.isAlive();
	}
}
