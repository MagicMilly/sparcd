package model.util;

import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import model.SanimalData;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.util.function.Consumer;

/**
 * Wrapper task that is aware of errors
 *
 * @param <V> Decides the task return value
 */
public abstract class ErrorTask<V> extends Task<V>
{
	public ErrorTask()
	{
		super();
		EventHandler<WorkerStateEvent> handler = event ->
		{
			SanimalData.getInstance().getErrorDisplay().printError("Task failed! Error was: ");
			Worker source = event.getSource();
			if (source != null)
			{
				SanimalData.getInstance().getErrorDisplay().printError("Error Message: " + source.getMessage());
				Throwable exception = source.getException();
				if (exception != null)
					SanimalData.getInstance().getErrorDisplay().printError("Stack trace: " + ExceptionUtils.getStackTrace(exception));
			}
		};
		// When the task fails print out the failure
		this.setOnFailed(handler);
	}
}