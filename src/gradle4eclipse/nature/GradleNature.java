package gradle4eclipse.nature;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;


public class GradleNature implements IProjectNature {

    private IJavaProject  project;

    public void configure() throws CoreException {
       // Add nature-specific information
       // for the project, such as adding a builder
       // to a project's build spec.
    }
    
    public void deconfigure() throws CoreException {
       // Remove the nature-specific information here.
    }
    
    public IProject getProject() {
       return project.getProject();
    }
    
    public void setProject(IProject project) {
    	this.project = JavaCore.create(project);
    }
 }