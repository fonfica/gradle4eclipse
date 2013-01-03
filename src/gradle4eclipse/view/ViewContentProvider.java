package gradle4eclipse.view;

import gradle4eclipse.model.TreeObject;
import gradle4eclipse.model.TreeParent;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.part.ViewPart;

public class ViewContentProvider implements IStructuredContentProvider,
		ITreeContentProvider {
	private TreeParent invisibleRoot;
	private ViewPart viewPart;

	public ViewContentProvider(ViewPart viewPart) {
		this.viewPart = viewPart;
	}

	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
	}

	public void dispose() {
	}

	public TreeObject getRoot() {
		if (invisibleRoot == null) {
			invisibleRoot = new TreeParent("");
		}
		if (invisibleRoot.getChildren() == null
				|| invisibleRoot.getChildren().length == 0) {
			TreeParent root = new TreeParent("");
			invisibleRoot.addChild(root);
		}
		return invisibleRoot.getChildren()[0];
	}

	public Object[] getElements(Object parent) {
		if (parent.equals(viewPart.getViewSite()))
			return getChildren(invisibleRoot);
		return getChildren(parent);
	}

	public Object getParent(Object child) {
		if (child instanceof TreeObject) {
			return ((TreeObject) child).getParent();
		}
		return null;
	}

	public Object[] getChildren(Object parent) {
		if (parent instanceof TreeParent) {
			return ((TreeParent) parent).getChildren();
		}
		return new Object[0];
	}

	public boolean hasChildren(Object parent) {
		if (parent instanceof TreeParent)
			return ((TreeParent) parent).hasChildren();
		return false;
	}
}