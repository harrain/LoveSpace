package com.example.lovespace.common.infra;

public class TraceTaskScheduler extends WrapTaskScheduler {
	public TraceTaskScheduler(com.example.lovespace.common.infra.TaskScheduler wrap) {
		super(wrap);
	}

	@Override
	public void reschedule(com.example.lovespace.common.infra.Task task) {
		trace("reschedule " + task.dump(true));
		
		super.reschedule(task);
	}
	
	private final void trace(String msg) {

	}
}
