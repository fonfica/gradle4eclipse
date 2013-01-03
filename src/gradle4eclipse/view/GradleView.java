package gradle4eclipse.view;

import gradle4eclipse.actions.AddFavoriteAction;
import gradle4eclipse.actions.EditFavoriteAction;
import gradle4eclipse.actions.ProjectPropertiesAction;
import gradle4eclipse.actions.ReloadBuildConfigAction;
import gradle4eclipse.actions.RemoveFavoriteAction;
import gradle4eclipse.actions.RunCommandAction;
import gradle4eclipse.actions.RunDebugTaskAction;
import gradle4eclipse.actions.RunInfoTaskAction;
import gradle4eclipse.actions.RunQuietTaskAction;
import gradle4eclipse.actions.RunTaskAction;
import gradle4eclipse.actions.TreeDoubleClickAction;
import gradle4eclipse.model.TreeObject;
import gradle4eclipse.model.TreeParent;


import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.*;
import org.eclipse.ui.part.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.jface.action.*;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ProjectScope;
import org.osgi.service.prefs.Preferences;

public class GradleView extends ViewPart {

	public static final String ID = "gradle4eclipse.views.GradleView";

	private String PROJECT_ROOT = "";
	private IProject activeProject;
	private TreeViewer viewer;
	private ISelectionListener selectionListener;
	// actions
	private RunTaskAction runTaskAction;
	private RunQuietTaskAction runQuietTaskAction;
	private RunInfoTaskAction runInfoTaskAction;
	private RunDebugTaskAction runDebugTaskAction;
	private ReloadBuildConfigAction reloadBuildConfigAction;
	private TreeDoubleClickAction doubleClickAction;
	private ProjectPropertiesAction projectPropertiesAction;
	private RunCommandAction runCommandAction;
	private AddFavoriteAction addFavoriteAction;
	private EditFavoriteAction editFavoriteAction;
	private RemoveFavoriteAction removeFavoriteAction;

	public GradleView() {
		//
	}

	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new ViewContentProvider(this));
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setInput(getViewSite());
		ColumnViewerToolTipSupport.enableFor(viewer);
		reloadBuildConfigAction = new ReloadBuildConfigAction(viewer);
		doubleClickAction = new TreeDoubleClickAction(viewer);
		doubleClickAction.addListener();
		runTaskAction = new RunTaskAction(viewer);
		runQuietTaskAction = new RunQuietTaskAction(viewer);
		runInfoTaskAction = new RunInfoTaskAction(viewer);
		runDebugTaskAction = new RunDebugTaskAction(viewer);
		projectPropertiesAction = new ProjectPropertiesAction(viewer);
		runCommandAction = new RunCommandAction(viewer);
		addFavoriteAction = new AddFavoriteAction(viewer);
		editFavoriteAction = new EditFavoriteAction(viewer);
		removeFavoriteAction = new RemoveFavoriteAction(viewer);
		hookContextMenu();
		contributeToActionBars();
		selectedProjectListener();
	}

	private void selectedProjectListener() {
		if (selectionListener != null)
			return;
		selectionListener = new ISelectionListener() {
			public void selectionChanged(IWorkbenchPart part, ISelection sel) {
				if (!(sel instanceof IStructuredSelection))
					return;
				IStructuredSelection ss = (IStructuredSelection) sel;
				if (ss == null || !(ss.getFirstElement() instanceof IResource))
					return;
				IResource res = (IResource) ss.getFirstElement();
				activeProject = (IProject) res.getProject();
				RunCommand.getInstance().setProject(activeProject);
				reloadBuildConfigAction.setActiveProject(activeProject);
				projectPropertiesAction.setActiveProject(activeProject);
				removeFavoriteAction.setActiveProject(activeProject);
				addFavoriteAction.setActiveProject(activeProject);
				editFavoriteAction.setActiveProject(activeProject);
				IPath location = activeProject.getLocation();
				String path = location.toPortableString();

				boolean buildFileExists = false;
				IScopeContext projectScope = new ProjectScope(activeProject);
				Preferences pref = projectScope.getNode("gradle4eclipse");
				if(projectScope != null) {
					String buildFilePath = pref.get("buildFile", "build.gradle");
					IFile buildFile = activeProject.getFile(buildFilePath);
					buildFileExists = buildFile.exists();
				}
				if (!PROJECT_ROOT.equals(path) && buildFileExists) {
					PROJECT_ROOT = path;
					Preferences tasks = pref.node("tasks");
					if(tasks == null || tasks.get("groups", null)==null)
						reloadBuildConfigAction.run();
					reloadBuildConfigAction.loadProjectPreferencies();
					runCommandAction.setEnabled(true);
					addFavoriteAction.setEnabled(true);
					projectPropertiesAction.setEnabled(true);
					reloadBuildConfigAction.setEnabled(true);
				} else if (!buildFileExists) {
					PROJECT_ROOT = path;
					ViewContentProvider content = (ViewContentProvider) viewer
							.getContentProvider();
					TreeParent root = (TreeParent) content.getRoot();
					root.setName("empty");
					TreeObject[] objs = root.getChildren();
					for (TreeObject obj : objs)
						root.removeChild(obj);
					runCommandAction.setEnabled(false);
					addFavoriteAction.setEnabled(false);
					projectPropertiesAction.setEnabled(false);
					reloadBuildConfigAction.setEnabled(false);
					viewer.refresh();
				}
			}
		};
		IWorkbenchPage page = getSite().getPage();
		RunCommand.getInstance().setPage(page);
		selectionListener.selectionChanged(page.getActivePart(), page.getSelection());
		page.addSelectionListener(selectionListener);
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("GradlePopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				if (viewer.getSelection().isEmpty()) {
                    return;
                }
                if (viewer.getSelection() instanceof IStructuredSelection) {
                    IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
                    if(selection.getFirstElement() instanceof TreeObject && !(selection.getFirstElement() instanceof TreeParent)) {
                    	TreeObject object = (TreeObject)selection.getFirstElement();
        				manager.add(runTaskAction);
        				manager.add(runQuietTaskAction);
        				manager.add(runInfoTaskAction);
        				manager.add(runDebugTaskAction);
        				if(object.isFavorite()) {
        					manager.add(editFavoriteAction);
        					manager.add(removeFavoriteAction);
        				}
                    }
                }
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		IToolBarManager manager = bars.getToolBarManager();
		manager.add(runCommandAction);
		manager.add(addFavoriteAction);
		manager.add(projectPropertiesAction);
		manager.add(reloadBuildConfigAction);
	}

	public void setFocus() {
		viewer.getControl().setFocus();
	}
}