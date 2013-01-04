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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class RemoveFavoriteAction extends Action{
	IProject activeProject;
	TreeViewer viewer; 
	
	public RemoveFavoriteAction(TreeViewer viewer) {
		this.viewer = viewer;
		setText("Remove favorite");
		setToolTipText("Remove favorite");
	}
	
	public void setActiveProject(IProject project) {
		this.activeProject = project;
	}
	
	public void run() {
        IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
        if(activeProject == null)
        	return;
        if(selection.getFirstElement() instanceof TreeObject && !(selection.getFirstElement() instanceof TreeParent)) {
        	TreeObject object = (TreeObject)selection.getFirstElement();
    		IScopeContext projectScope = new ProjectScope(activeProject);
    		Preferences pref = projectScope.getNode("gradle4eclipse");
    		Preferences favorites = pref.node("favorites");
    		try {
				for( int i = 0; i < favorites.keys().length - 1; i++) {
					String key = favorites.keys()[i];
					if(key.endsWith(".name") && favorites.get(key, "").equals(object.getName())) {
						String node = key.substring(0, key.indexOf('.'));
						favorites.remove(node+".name");
						favorites.remove(node+".default");
						favorites.remove(node+".desc");
						favorites.remove(node+".run");
						favorites.flush();
						TreeParent parent = object.getParent();
						if(parent != null)
							parent.removeChild(object);
						viewer.refresh();
					}
				}
			} catch (BackingStoreException e) {
				e.printStackTrace();
			}
        }
	}
}
