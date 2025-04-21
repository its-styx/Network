/*
	Christopher Pena
	April 21, 2025
	Purpose:
	Sources:
*/

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadOperation
{
	private final ExecutorService executor;
	
	public ThreadOperation(int numberOfThreads)
	{
		executor = Executors.newFixedThreadPool(numberOfThreads);
	}
	
	public void execute(Runnable task)
	{
		executor.execute(task);
	}
	
	public void shutdown()
	{
		executor.shutdown();
	}
}