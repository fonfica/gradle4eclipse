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
package gradle4eclipse.actions;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IObjectActionDelegate;


public class AddGradleNature implements IObjectActionDelegate {
	
	private LinkedHashSet<IProject> projects = new LinkedHashSet<IProject>();

	public void selectionChanged(IAction _action, ISelection _selection) {
		if (_selection instanceof IStructuredSelection) {
			projects = new LinkedHashSet<IProject>();
			IStructuredSelection selection = (IStructuredSelection) _selection;
			if(!selection.isEmpty()) {
				for (Object element : selection.toArray()) {
					if (element instanceof IProject) {
						projects.add((IProject) element);
					} else if (element instanceof IResource) {
						projects.add(((IResource) element).getProject());
					}
				}
			}
		}
	}

	public void setActivePart(IAction action, IWorkbenchPart part) {
		
	}

	public void run(IAction _arg0) {
		if (!projects.isEmpty()) {
			for (IProject project : projects) {
				IProjectDescription description;
				try {
					description = project.getDescription();
					String[] natures = description.getNatureIds();
					String[] newNatures = new String[natures.length + 1];
					System.arraycopy(natures, 0, newNatures, 0, natures.length);
					newNatures[natures.length] = "gradle4eclipse.gradlenature";
					description.setNatureIds(newNatures);

					ICommand[] cmds = description.getBuildSpec();
					boolean found = false;
					for (int j = 0; j < cmds.length; j++)
						if (cmds[j].getBuilderName().equals("gradle4eclipse.gradlebuilder"))
					         found = true;
					if(!found) {
						ICommand buildCmd = description.newCommand();
						buildCmd.setBuilderName("gradle4eclipse.gradlebuilder");
						List<ICommand> newCmds = new ArrayList<ICommand>();
						newCmds.addAll(Arrays.asList(cmds));
						newCmds.add(buildCmd);
						description.setBuildSpec((ICommand[]) 
							newCmds.toArray(new ICommand[newCmds.size()]));
						IFile gradleFile = project.getFile("build.gradle");
						if(gradleFile != null && !gradleFile.exists()) {
							byte[] bytes = "apply plugin: \"java\"\n".getBytes();
							InputStream source = new ByteArrayInputStream(bytes);
							gradleFile.create(source, IResource.NONE, null);
						}
					}
					project.setDescription(description, null);
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
