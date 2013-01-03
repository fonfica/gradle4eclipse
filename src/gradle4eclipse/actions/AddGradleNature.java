package gradle4eclipse.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IObjectActionDelegate;


public class AddGradleNature implements IObjectActionDelegate {
	
	private LinkedHashSet<IProject> projects = new LinkedHashSet<IProject>();

	public void selectionChanged(IAction _action, ISelection _selection) {
		if (_selection instanceof IStructuredSelection) {
			projects = new LinkedHashSet<IProject>();
			IStructuredSelection selection = (IStructuredSelection) _selection;
			if(!selection.isEmpty()) {
				for (Object element : selection.toArray()) {
					if (element instanceof IProject) {
						projects.add((IProject) element);
					} else if (element instanceof IResource) {
						projects.add(((IResource) element).getProject());
					}
				}
			}
		}
	}

	public void setActivePart(IAction action, IWorkbenchPart part) {
		
	}

	public void run(IAction _arg0) {
		if (!projects.isEmpty()) {
			for (IProject project : projects) {
				IProjectDescription description;
				try {
					description = project.getDescription();
					String[] natures = description.getNatureIds();
					String[] newNatures = new String[natures.length + 1];
					System.arraycopy(natures, 0, newNatures, 0, natures.length);
					newNatures[natures.length] = "Gradle4Eclipse.gradlenature";
					description.setNatureIds(newNatures);

					ICommand[] cmds = description.getBuildSpec();
					boolean found = false;
					for (int j = 0; j < cmds.length; j++)
						if (cmds[j].getBuilderName().equals("Gradle4Eclipse.gradlebuilder"))
					         found = true;
					if(!found) {
						ICommand buildCmd = description.newCommand();
						buildCmd.setBuilderName("Gradle4Eclipse.gradlebuilder");
						List<ICommand> newCmds = new ArrayList<ICommand>();
						newCmds.addAll(Arrays.asList(cmds));
						newCmds.add(buildCmd);
						description.setBuildSpec((ICommand[]) 
							newCmds.toArray(new ICommand[newCmds.size()]));
					}
					project.setDescription(description, null);
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
