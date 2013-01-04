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