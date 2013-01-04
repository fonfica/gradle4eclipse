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

import gradle4eclipse.view.RunCommand;
import gradle4eclipse.view.RunCommandDialog;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class RunCommandAction extends Action {
	TreeViewer viewer; 
	public RunCommandAction(TreeViewer viewer) {
		this.viewer = viewer;
		setText("Run");
		setToolTipText("Run Gradle");
		Bundle bundle = FrameworkUtil.getBundle(this.getClass());
		URL url = FileLocator.find(bundle, new Path("icons/run.gif"),null);
		ImageDescriptor reloadImage = ImageDescriptor.createFromURL(url);
		setImageDescriptor(reloadImage);
	}
	
	public void run() {
		Shell shell = viewer.getControl().getShell();
		RunCommandDialog dialog = new RunCommandDialog(shell);
		dialog.open();
		if(dialog.getValue() != null && !dialog.getValue().equals(""))
			RunCommand.getInstance().run(dialog.getValue(), true);
	}
}

