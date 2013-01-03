package gradle4eclipse.view;

import java.net.URL;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.prefs.Preferences;

public class ProjectPropertiesDialog extends Dialog {
	private Shell parent;
	private IProject project;
	private String buildFile;
	private String settingsFile;
	private String initFile;
	
	public ProjectPropertiesDialog(Shell parent, IProject project) {
		super(parent);
		this.parent = parent;
		this.project = project;
	}
	
	protected void initializePosition(Shell shell) { 
		Monitor primary = shell.getMonitor(); 
		Rectangle bounds = primary.getBounds (); 
		int x = bounds.width / 2; 
		int y = bounds.height / 2; 
		shell.setLocation (x-180, y-60); 
	}

	public int open() {
		final Shell shell = new Shell(parent.getDisplay(), SWT.TITLE|SWT.CLOSE|SWT.APPLICATION_MODAL);
		Bundle bundle = FrameworkUtil.getBundle(this.getClass());
		URL url = FileLocator.find(bundle, new Path("icons/sample.gif"),null);
		ImageDescriptor sampleImage = ImageDescriptor.createFromURL(url);
		sampleImage.createImage();
		shell.setImage(sampleImage.createImage());
		shell.setText("Project properties");
		shell.setLayout(new GridLayout(2, false));
		
		GridData gridTextData = new GridData();
		gridTextData.horizontalSpan = 2;
		gridTextData.horizontalAlignment = SWT.FILL;
		gridTextData.grabExcessHorizontalSpace = true;
		gridTextData.verticalAlignment = SWT.FILL;
		gridTextData.grabExcessVerticalSpace = true;
		gridTextData.minimumWidth = 450;
		
		GridData gridLabelData = new GridData();
		gridLabelData.horizontalSpan = 2;
		gridLabelData.horizontalAlignment = SWT.FILL;
		gridLabelData.grabExcessHorizontalSpace = true;
		gridLabelData.verticalAlignment = SWT.NONE;
		gridLabelData.grabExcessVerticalSpace = false;
		
		GridData gridButtonData = new GridData();
		gridButtonData = new GridData(GridData.END, GridData.CENTER, false, false);
		gridButtonData.horizontalSpan = 2;
		gridButtonData.verticalAlignment = SWT.FILL;
		gridButtonData.grabExcessVerticalSpace = true;

		final Label buildFileLabel = new Label(shell, SWT.NONE);
		buildFileLabel.setText("Build file (default is build.gradle):");
		buildFileLabel.setLayoutData(gridLabelData);
		
		final Text buildFileCtrl = new Text(shell, SWT.SINGLE | SWT.BORDER);
		buildFileCtrl.setLayoutData(gridTextData);
		buildFileCtrl.setTextLimit(20);
		
		final Label settingsFileLabel = new Label(shell, SWT.NONE);
		settingsFileLabel.setText("Settings file (default is ../settings.gradle or ../master/settings.gradle):");
		settingsFileLabel.setLayoutData(gridLabelData);
		
		final Text settingsFileCtrl = new Text(shell, SWT.SINGLE | SWT.BORDER);
		settingsFileCtrl.setLayoutData(gridTextData);
		settingsFileCtrl.setTextLimit(120);
		
		final Label initFileLabel = new Label(shell, SWT.NONE);
		initFileLabel.setText("Init script (optional):");
		initFileLabel.setLayoutData(gridLabelData);
		
		final Text initFileCtrl = new Text(shell, SWT.SINGLE | SWT.BORDER);
		initFileCtrl.setLayoutData(gridTextData);
		initFileCtrl.setTextLimit(160);
		
		final Button buttonOK = new Button(shell, SWT.PUSH|SWT.CENTER);
		buttonOK.setText("  Save  ");
		buttonOK.setLayoutData(gridButtonData);
		shell.setDefaultButton(buttonOK);
		
		buildFileCtrl.addListener(SWT.Modify, new Listener() {
			public void handleEvent(Event event) {
				buildFile = buildFileCtrl.getText();
			}
		});
		
		settingsFileCtrl.addListener(SWT.Modify, new Listener() {
			public void handleEvent(Event event) {
				settingsFile = settingsFileCtrl.getText();
			}
		});
		
		initFileCtrl.addListener(SWT.Modify, new Listener() {
			public void handleEvent(Event event) {
				initFile = initFileCtrl.getText();
			}
		});

		buttonOK.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if(project != null) {
					IScopeContext projectScope = new ProjectScope(project);
					Preferences pref = projectScope.getNode("gradle4eclipse");
					if(!"".equals(buildFile))
						pref.put("buildFile", buildFile);
					if("build.gradle".equals(buildFile))
						pref.remove("buildFile");
					if("".equals(buildFile))
						pref.remove("buildFile");
					if(!"".equals(settingsFile))
						pref.put("settingsFile", settingsFile);
					if("".equals(settingsFile))
						pref.remove("settingsFile");
					if("settings.gradle".equals(settingsFile))
						pref.remove("settingsFile");
					if("../master/settings.gradle".equals(settingsFile))
						pref.remove("settingsFile");
					if("..\\master\\settings.gradle".equals(settingsFile))
						pref.remove("settingsFile");
					if(!"".equals(initFile))
						pref.put("initFile", initFile);
					if("".equals(initFile))
						pref.remove("initFile");
					try {
						pref.flush();
					} catch (Exception e) {
						
					}
				}
				shell.dispose();
			}
		});
	    
		shell.addListener(SWT.Traverse, new Listener() {
			public void handleEvent(Event event) {
				if(event.detail == SWT.TRAVERSE_ESCAPE)
					event.doit = false;
			}
		});

		IScopeContext projectScope = new ProjectScope(project);
		Preferences pref = projectScope.getNode("gradle4eclipse");
		buildFileCtrl.setText("");
		if(pref.get("buildFile", null)!=null)
			buildFileCtrl.setText(pref.get("buildFile", null));
		settingsFileCtrl.setText("");
		if(pref.get("settingsFile", null)!=null)
			settingsFileCtrl.setText(pref.get("settingsFile", null));
		initFileCtrl.setText("");
		if(pref.get("initFile", null)!=null)
			initFileCtrl.setText(pref.get("initFile", null));
		initializePosition(shell);
		shell.pack();
		shell.open();

		Display display = parent.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		return 0;
	}
}