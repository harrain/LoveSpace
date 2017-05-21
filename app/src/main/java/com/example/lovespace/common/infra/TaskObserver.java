package com.example.lovespace.common.infra;

public interface TaskObserver {
	/**
	 * on task result
	 * @param task
	 * @param results
	 */
	public void onTaskResult(com.example.lovespace.common.infra.Task task, Object[] results);

	/**
	 * on task progress
	 * @param task
	 * @param params
	 */
	public void onTaskProgress(com.example.lovespace.common.infra.Task task, Object[] params);
}
