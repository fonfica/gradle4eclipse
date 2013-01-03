package gradle4eclipse.view;

import org.eclipse.core.runtime.jobs.Job;

public abstract class RunJob extends Job{
	private String command;
	public RunJob(String name, String command) {
		super(name);
		this.command = command;
	}
	public String getCommand() {
		return this.command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
}
