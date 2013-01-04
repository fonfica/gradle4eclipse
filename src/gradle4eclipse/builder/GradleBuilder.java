/*
Gradle4Eclipse Eclipse plugin
Copyright (C) 2013  Filip Muncan

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
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
