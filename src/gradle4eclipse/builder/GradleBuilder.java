package gradle4eclipse.builder;

import gradle4eclipse.view.RunCommand;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public class GradleBuilder extends IncrementalProjectBuilder {
	
	protected void startupOnInitialize() {
		//        
	}
	
	protected void clean(IProgressMonitor monitor) {
		RunCommand.getInstance().run("clean", false);
	}

	protected IProject[] build(int kind, Map<String, String> arguments,
			IProgressMonitor monitor) throws CoreException {
		RunCommand.getInstance().run("build", false);
		return null;
	}
}
