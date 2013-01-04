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

import gradle4eclipse.model.TreeObject;
import gradle4eclipse.model.TreeParent;
import gradle4eclipse.view.RunCommand;

import java.net.URL;


import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class RunInfoTaskAction extends Action {
	TreeViewer viewer; 
	public RunInfoTaskAction(TreeViewer viewer) {
		this.viewer = viewer;
		setText("Run --info");
		setToolTipText("Set log level to info.");
		Bundle bundle = FrameworkUtil.getBundle(this.getClass());
		URL url = FileLocator.find(bundle, new Path("icons/task.gif"),null);
		ImageDescriptor reloadImage = ImageDescriptor.createFromURL(url);
		setImageDescriptor(reloadImage);
	}
	
	public void run() {
		ISelection selection = viewer.getSelection();
		Object obj = ((IStructuredSelection) selection)
				.getFirstElement();
		if(obj instanceof TreeObject && !(obj instanceof TreeParent))
			RunCommand.getInstance().run("--info "+((TreeObject)obj).getRun(), true);
	}
}
