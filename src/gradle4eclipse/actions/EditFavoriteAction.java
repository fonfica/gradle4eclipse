package gradle4eclipse.actions;

import gradle4eclipse.model.TreeObject;
import gradle4eclipse.model.TreeParent;
import gradle4eclipse.view.FavoriteDialog;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Shell;
import org.osgi.service.prefs.Preferences;


public class EditFavoriteAction extends Action{
	TreeViewer viewer; 
	IProject activeProject;
	
	public EditFavoriteAction(TreeViewer viewer) {
		this.viewer = viewer;
		setText("Edit favorite");
		setToolTipText("Edit favorite");
	}
	
	public void setActiveProject(IProject project) {
		this.activeProject = project;
	}
	
	public void run() {
        IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
        if(selection.getFirstElement() instanceof TreeObject && !(selection.getFirstElement() instanceof TreeParent)) {
        	TreeObject object = (TreeObject)selection.getFirstElement();
        	if(object != null) {
        		Shell shell = viewer.getControl().getShell();
        		FavoriteDialog dialog = new FavoriteDialog(shell);
        		dialog.setName(object.getName());
        		dialog.setRun(object.getRun());
        		dialog.setDesc(object.getDescription());
        		int val = dialog.open();
        		if(val==1 && dialog.getName() != null && activeProject != null) {
            		IScopeContext projectScope = new ProjectScope(activeProject);
            		Preferences pref = projectScope.getNode("gradle4eclipse");
            		Preferences favorites = pref.node("favorites");
            		try {
						for( int i = 0; i < favorites.keys().length - 1; i++) {
							String key = favorites.keys()[i];
							if(key.endsWith(".name") && favorites.get(key, "").equals(dialog.getName())) {
								String node = key.substring(0, key.indexOf('.'));
								favorites.putBoolean(node+".default", false);
								favorites.put(node+".run", dialog.getRun());
								favorites.put(node+".desc", dialog.getDesc());
								object.setRun(dialog.getRun());
								object.setDescription(dialog.getDesc());
								object.setDefault(false);
								favorites.flush();
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
            		viewer.refresh();
        		}
        	}
        }
	}
}
