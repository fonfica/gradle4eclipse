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
package gradle4eclipse.view;

import gradle4eclipse.actions.ReloadBuildConfigAction;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.osgi.service.prefs.Preferences;

public class RunCommand {

	private IWorkbenchPage page;
	private IProject project;
	private static final String CONSOLE_ID = "Gradle4Eclipse";
	private static final RunCommand instance = new RunCommand();
	
	private RunCommand() {}
	
    public static RunCommand getInstance() {
        return instance;
    }
	
	public void setPage(IWorkbenchPage page) {
		this.page = page;
	}
	
	public void setProject(IProject project) {
		this.project = project;
	}
	
	private String getProjectPath() {
		if(project == null)
			return null;
		IPath location = project.getLocation();
		if(location == null)
			return null;
		return location.toPortableString();
	}
	
	protected MessageConsole getGradleConsole () {
		MessageConsole console = null;
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager conMan = plugin.getConsoleManager();
		IConsole[] existing = conMan.getConsoles();
		for (int i = 0; i < existing.length; i++)
			if (CONSOLE_ID.equals(existing[i].getName()))
				console = (MessageConsole) existing[i];
		// if no console found create a new one
		if(console == null) {
			console = new MessageConsole(CONSOLE_ID, null);	
			conMan.addConsoles(new IConsole[] {console});
		}
		return console;
	}
	
	public void getTasks(final ReloadBuildConfigAction action) throws IOException {
		getGradleConsole().clearConsole();
		Job tasksJob = new Job("Gradle4Eclipse::getTasks") {
			protected IStatus run(IProgressMonitor monitor) {
				try {
					IScopeContext projectScope = new ProjectScope(project);
					Preferences pref = projectScope.getNode("gradle4eclipse");
					String buildFile = pref.get("buildFile", "build.gradle");
					String cmd = "gradle.bat -b " + buildFile + " tasks"; //$NON-NLS-1$
					Process process = Runtime.getRuntime().exec(cmd, null, new File(getProjectPath()));
					BufferedInputStream bis = new BufferedInputStream(process.getInputStream());
					ByteArrayOutputStream buf = new ByteArrayOutputStream();
					int result = bis.read();
					while(result != -1) {
						byte b = (byte)result;
						buf.write(b);
						result = bis.read(); 	
					}
					final String data = buf.toString();
					buf.close();
					Display.getDefault().syncExec(new Runnable() { public void run() {
						try {
							MessageConsoleStream out = getGradleConsole().newMessageStream();
							out.setColor(new Color(null, 0, 0, 255));
							out.println(data); 
							out.close();
							action.tasks(data);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}});
				} catch (IOException e) {
					e.printStackTrace();
				}
			    return Status.OK_STATUS;
			}
		};
		tasksJob.setPriority(Job.BUILD);
		tasksJob.schedule();
	}

	public void run(String command, final boolean clearConsole) {
		if(getProjectPath()==null)
			return;
		RunJob runJob = new RunJob("Gradle4Eclipse:run", command) {
			protected IStatus run(IProgressMonitor monitor) {
				try {
					IScopeContext projectScope = new ProjectScope(project);
					Preferences pref = projectScope.getNode("gradle4eclipse");
					String buildFile = pref.get("buildFile", "build.gradle");
					String settings = "";
					String init = "";
					if(pref.get("settingsFile", null)!=null)
						settings = " -c " + pref.get("settingsFile", null);
					if(pref.get("initFile", null)!=null)
						settings = " -I " + pref.get("initFile", null);
					final String cmd = "gradle.bat -b " + buildFile + " " + getCommand() + settings + init;
					final Process process = Runtime.getRuntime().exec(cmd, null, new File(getProjectPath()));
					// try to open/focus console view
					Display.getDefault().syncExec( new Runnable() {  public void run() {
						try {
							MessageConsole console = getGradleConsole();
							if(clearConsole)
								console.clearConsole();
							String id = IConsoleConstants.ID_CONSOLE_VIEW;
							IConsoleView view = (IConsoleView)page.showView(id);
							view.display(console);
							MessageConsoleStream out = console.newMessageStream();
							out.setFontStyle(1);
							out.println("PATH: " + getProjectPath());
							out.println("COMMAND: " + cmd);
							out.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					} });

					new Thread(new Runnable() {
						public void run() {
							try {
								byte[] buffer = new byte[1024];
								int bytesRead;
								while ((bytesRead = process.getInputStream().read(buffer)) != -1) {
									final byte[] fbuffer = buffer;
									final int fbytesRead = bytesRead;
									Display.getDefault().syncExec( new Runnable() {  public void run() {
										try {
											MessageConsole console = getGradleConsole();
											MessageConsoleStream out = console.newMessageStream();
											out.setColor(new Color(null, 0, 0, 255));
											out.write(fbuffer, 0, fbytesRead);
											out.close();
										} catch (Exception e) {
											e.printStackTrace();
										}
									} });
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}, "gradleProcessTreadInput").start();
									
					new Thread(new Runnable() {
						public void run() {
							try {
								byte[] buffer = new byte[1024];
								int bytesRead;
								while ((bytesRead = process.getErrorStream().read(buffer)) != -1) {
									final byte[] fbuffer = buffer;
									final int fbytesRead = bytesRead;
									Display.getDefault().syncExec( new Runnable() {  public void run() {
										try {
											MessageConsole console = getGradleConsole();
											MessageConsoleStream out = console.newMessageStream();
											out.setColor(new Color(null, 255, 0, 0));
											out.write(fbuffer, 0, fbytesRead);
											out.close();
										} catch (Exception e) {
											e.printStackTrace();
										}
									} });
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}, "gradleProcessTreadError").start();
					Thread.sleep(5000);
				} catch(Exception e) {
					e.printStackTrace();
				}
				return Status.OK_STATUS;
			}
		};
		runJob.setPriority(Job.BUILD);
		runJob.schedule();
	}
}