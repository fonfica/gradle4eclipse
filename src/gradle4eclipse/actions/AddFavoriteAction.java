package gradle4eclipse.actions;

import java.net.URL;

import gradle4eclipse.model.TreeObject;
import gradle4eclipse.model.TreeParent;
import gradle4eclipse.view.FavoriteDialog;
import gradle4eclipse.view.ViewContentProvider;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class AddFavoriteAction extends Action{
	TreeViewer viewer; 
	IProject activeProject;
	
	public AddFavoriteAction(TreeViewer viewer) {
		this.viewer = viewer;
		setText("New favorite");
		setToolTipText("Add new favorite");
		Bundle bundle = FrameworkUtil.getBundle(this.getClass());
		URL url = FileLocator.find(bundle, new Path("icons/add.gif"),null);
		ImageDescriptor reloadImage = ImageDescriptor.createFromURL(url);
		setImageDescriptor(reloadImage);
	}
	
	public void setActiveProject(IProject project) {
		this.activeProject = project;
	}
	
	public void run() {
		ViewContentProvider content = (ViewContentProvider) viewer.getContentProvider();
		TreeParent root = (TreeParent) content.getRoot();
		Shell shell = viewer.getControl().getShell();
		FavoriteDialog dialog = new FavoriteDialog(shell);
		dialog.setName("");
		dialog.setRun("");
		dialog.setDesc("");
		int val = dialog.open();
		if(val==1 && dialog.getName() != null) {
    		IScopeContext projectScope = new ProjectScope(activeProject);
    		Preferences pref = projectScope.getNode("gradle4eclipse");
    		Preferences favorites = pref.node("favorites");
    		try {
    			for( int i = 0; i < favorites.keys().length - 1; i++) {
    				String key = favorites.keys()[i];
    				if(key.indexOf(".name")!=-1){
    					if(favorites.get(key, null)!=null)
    						if(favorites.get(key, null).equals(dialog.getName())){
    							MessageBox msg = 
    								new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
    								msg.setText("Already exists");
    								msg.setMessage("Favorite name already exists!");
    								msg.open(); 
    							return;
    						}
    							
    				}
    			}
    			int i = 1;
    			while(true){
    				if(favorites.get("favorite"+i+".name", null)!= null)
    					i++;
    				else
    					break;
    			}
    			favorites.put("favorite"+i+".name", dialog.getName());
    			favorites.put("favorite"+i+".run", dialog.getRun());
    			favorites.put("favorite"+i+".desc", dialog.getDesc());
    			favorites.putBoolean("favorite"+i+".default", false);
    			favorites.flush();
				TreeObject obj = new TreeObject(dialog.getName());
				obj.setRun(dialog.getRun());
				obj.setDescription(dialog.getDesc());
				obj.setDefault(false);
				obj.setFavorite(true);
				root.addChild(obj);
    			
			} catch (BackingStoreException e) {
				e.printStackTrace();
			}
    		viewer.refresh();
		}
	}
}
