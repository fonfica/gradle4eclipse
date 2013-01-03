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