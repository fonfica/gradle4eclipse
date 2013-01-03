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

