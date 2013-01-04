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

import java.net.URL;

import gradle4eclipse.view.ProjectPropertiesDialog;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class ProjectPropertiesAction extends Action {
	private TreeViewer viewer;
	private IProject activeProject;
	public ProjectPropertiesAction(TreeViewer viewer) {
		this.viewer = viewer;
		setText("Project properties");
		setToolTipText("Edit project properties");
		Bundle bundle = FrameworkUtil.getBundle(this.getClass());
		URL url = FileLocator.find(bundle, new Path("icons/prop.gif"),null);
		ImageDescriptor reloadImage = ImageDescriptor.createFromURL(url);
		setImageDescriptor(reloadImage);
	}
	
	public void setActiveProject(IProject project) {
		this.activeProject = project;
	}
	
	public void run() {
        if(activeProject == null)
        	return;
		Shell shell = viewer.getControl().getShell();
		ProjectPropertiesDialog dialog = new ProjectPropertiesDialog(shell, activeProject);
		dialog.open();
	}
}
