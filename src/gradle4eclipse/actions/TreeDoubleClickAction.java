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


import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

public class TreeDoubleClickAction extends Action {

	private TreeViewer viewer;
	
	public TreeDoubleClickAction(TreeViewer viewer) {
		this.viewer = viewer;
	}
	
	public void addListener() {
		this.viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				run();
			}
		});
	}
	
	public void run() {
		ISelection selection = viewer.getSelection();
		Object obj = ((IStructuredSelection) selection)
				.getFirstElement();
		if(obj instanceof TreeObject && !(obj instanceof TreeParent))
			RunCommand.getInstance().run(((TreeObject)obj).getRun(), true);
		if (viewer.getExpandedState(obj))
			viewer.collapseToLevel(obj, AbstractTreeViewer.ALL_LEVELS); 
		else
			viewer.expandToLevel(obj, 1); 
	}
} 