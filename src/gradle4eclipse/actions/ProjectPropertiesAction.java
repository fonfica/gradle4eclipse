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
