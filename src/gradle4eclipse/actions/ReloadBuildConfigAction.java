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
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gradle4eclipse.model.TreeObject;
import gradle4eclipse.model.TreeParent;
import gradle4eclipse.view.RunCommand;
import gradle4eclipse.view.ViewContentProvider;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.TreeViewer;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class ReloadBuildConfigAction extends Action {
	IProject activeProject; 
	TreeViewer viewer;
	public ReloadBuildConfigAction(TreeViewer viewer) {
		this.viewer = viewer;
		setText("Reload");
		setToolTipText("Reload Gradle build configuration");
		Bundle bundle = FrameworkUtil.getBundle(this.getClass());
		URL url = FileLocator.find(bundle, new Path("icons/reload.gif"),null);
		ImageDescriptor reloadImage = ImageDescriptor.createFromURL(url);
		setImageDescriptor(reloadImage);
	}
	
	public void setActiveProject(IProject project) {
		this.activeProject = project;
	}
	
	public boolean loadProjectPreferencies() {
        if(activeProject == null)
        	return false;
		IScopeContext projectScope = new ProjectScope(activeProject);
		Preferences pref = projectScope.getNode("gradle4eclipse");
		ViewContentProvider content = (ViewContentProvider) viewer.getContentProvider();
		TreeParent root = (TreeParent) content.getRoot();
		root.setName(activeProject.getName());
		TreeObject[] objs = root.getChildren();
		for (TreeObject obj : objs)
			root.removeChild(obj);
		viewer.refresh();
		Preferences tasks = pref.node("tasks");
		if (tasks == null)
			return false;
		if (tasks.get("groups", null) == null)
			return false;
		String groups = tasks.get("groups", null);
		groups = groups.toLowerCase();
		List<String> items = Arrays.asList(groups.split("\\s*,\\s*"));
		for (String group : items) {
			TreeParent parent;
			if (!"other".equals(group)) {
				parent = new TreeParent(group);
				root.addChild(parent);
			} else
				parent = root;
			int idx = 1;
			while (true) {
				String taskName = tasks.get(group + ".task" + idx + ".name",
						null);
				if (taskName != null) {
					TreeObject obj = new TreeObject(taskName);
					obj.setRun(tasks.get(group + ".task" + idx
							+ ".run", null));
					obj.setDescription(tasks.get(group + ".task" + idx
							+ ".desc", null));
					obj.setDefault(tasks.getBoolean(group + ".task" + idx
							+ ".default", false));
					parent.addChild(obj);
				} else
					break;
				idx++;
			}
		}
		// render favorites
		Preferences favorites = pref.node("favorites");
		try {
			for( int i = 0; i < favorites.keys().length - 1; i++) {
				String key = favorites.keys()[i];
				boolean isName = false;
				if(key.indexOf(".name")!=-1)
					isName = true;
				String node = key.substring(0, key.indexOf('.'));
				String favoriteName = favorites.get(node+ ".name", null);
				if (favoriteName != null && isName) {
					TreeObject obj = new TreeObject(favoriteName);
					obj.setRun(favorites.get(node + ".run", null));
					obj.setDescription(favorites.get(node + ".desc", null));
					obj.setDefault(false);
					obj.setFavorite(true);
					root.addChild(obj);
				}
			}
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
		viewer.refresh();
		return true;
	}
	
	public void loadProjectGradleTasks(String tasks) {
		
	}
	
	public void run() {
		try {
			if(activeProject == null)
				return;
			RunCommand.getInstance().getTasks(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void tasks(String text) {
		try {
			IScopeContext projectScope = new ProjectScope(activeProject);
			Preferences prefs = projectScope.getNode("gradle4eclipse");
			prefs.node("tasks").removeNode();
			Preferences tasksPref = prefs.node("tasks");
			Pattern p1 = Pattern
					.compile("((\\n|\\r\\n){2}([\\w ]+)(\\n|\\r\\n))([-]+)(((\\n|\\r\\n)[\\w]+( - {1}.*)?)+)");
			Pattern p2 = Pattern.compile("(\\n|\\r\\n)([\\w]+)( - {1}.*)?");			
			Matcher m1 = p1.matcher(text);
			String categories = "";
			String category = "";
			String tasks = "";
			String task = "";
			String description = "";
			while (m1.find()) {
				category = m1.group(1);
				category = category.toLowerCase();
				if(category != null) {
					category = category.replaceAll("\r", "");
					category = category.replaceAll("\n", "");
				}
				if(category == null || category.indexOf("rules")==0)
					break;     
				if(category.indexOf(" ")!=-1)
					category = category.substring(0, category.indexOf(" "));
				if(!categories.equals(""))
					categories = categories + "," + category;
				else
					categories = category;
				tasks = m1.group(6);
				Matcher m2 = p2.matcher(tasks);
				int count = 1;
				while (m2.find()) {
					task = m2.group(2);
					description = m2.group(3);
					if(null != description && description.length()>3)
						description = description.substring(3, description.length());
					else
						description = "";
					tasksPref.put(category + ".task" + count + ".name", task);
					tasksPref.put(category + ".task" + count + ".run", task);
					tasksPref.put(category + ".task" + count + ".desc", description);
					tasksPref.putBoolean(category + ".task" + count + ".default", false);
					count++;
				}
			}
			tasksPref.put("groups", categories);
			prefs.flush();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
		loadProjectPreferencies();
	}
}