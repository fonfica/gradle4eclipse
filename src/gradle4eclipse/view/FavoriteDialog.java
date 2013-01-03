package gradle4eclipse.view;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
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

public class FavoriteDialog extends Dialog {
	private String name;
	private String run;
	private String desc;
	private boolean enabled = true;
	
	private Shell parent;
	int returnVal;
	
	public FavoriteDialog(Shell parent) {
		super(parent);
		this.parent = parent;
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
		if(!name.equals(""))
			this.enabled = false;
	}

	public String getRun() {
		return this.run;
	}

	public void setRun(String run) {
		this.run = run;
	}

	public String getDesc() {
		return this.desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}


	protected void initializePosition(Shell shell) { 
		Monitor primary = shell.getMonitor(); 
		Rectangle bounds = primary.getBounds (); 
		int x = bounds.width / 2; 
		int y = bounds.height / 2; 
		shell.setLocation (x-140, y-60); 
	}

	public int open() {
		final Shell shell = new Shell(parent.getDisplay(), SWT.TITLE|SWT.CLOSE|SWT.APPLICATION_MODAL);
		Bundle bundle = FrameworkUtil.getBundle(this.getClass());
		URL url = FileLocator.find(bundle, new Path("icons/sample.gif"),null);
		ImageDescriptor sampleImage = ImageDescriptor.createFromURL(url);
		sampleImage.createImage();
		shell.setImage(sampleImage.createImage());
		shell.setText("Gradle favorite");
		shell.setLayout(new GridLayout(2, false));
		
		GridData gridTextData = new GridData();
		gridTextData.horizontalSpan = 2;
		gridTextData.horizontalAlignment = SWT.FILL;
		gridTextData.grabExcessHorizontalSpace = true;
		gridTextData.verticalAlignment = SWT.FILL;
		gridTextData.grabExcessVerticalSpace = true;
		gridTextData.minimumWidth = 350;
		
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

		final Label nameLabel = new Label(shell, SWT.NONE);
		nameLabel.setText("Name:");
		nameLabel.setEnabled(enabled);
		nameLabel.setLayoutData(gridLabelData);
		
		final Text nameCtrl = new Text(shell, SWT.SINGLE | SWT.BORDER);
		nameCtrl.setLayoutData(gridTextData);
		nameCtrl.setEnabled(enabled);
		nameCtrl.setTextLimit(12);
		
		final Label runLabel = new Label(shell, SWT.NONE);
		runLabel.setText("Command:");
		runLabel.setLayoutData(gridLabelData);
		
		final Text runCtrl = new Text(shell, SWT.SINGLE | SWT.BORDER);
		runCtrl.setLayoutData(gridTextData);
		runCtrl.setTextLimit(120);
		
		final Label descLabel = new Label(shell, SWT.NONE);
		descLabel.setText("Description:");
		descLabel.setLayoutData(gridLabelData);
		
		final Text descCtrl = new Text(shell, SWT.SINGLE | SWT.BORDER);
		descCtrl.setLayoutData(gridTextData);
		descCtrl.setTextLimit(160);
		
		final Button buttonOK = new Button(shell, SWT.PUSH|SWT.CENTER);
		buttonOK.setText("  Save  ");
		buttonOK.setEnabled(false);
		buttonOK.setLayoutData(gridButtonData);
		shell.setDefaultButton(buttonOK);

		nameCtrl.addListener(SWT.Modify, new Listener() {
			public void handleEvent(Event event) {
				try {
					name = nameCtrl.getText();
					buttonOK.setEnabled(true);
				} catch (Exception e) {
					buttonOK.setEnabled(false);
				}
			}
		});
		
		runCtrl.addListener(SWT.Modify, new Listener() {
			public void handleEvent(Event event) {
				try {
					run = runCtrl.getText();
					buttonOK.setEnabled(true);
				} catch (Exception e) {
					buttonOK.setEnabled(false);
				}
			}
		});
		
		descCtrl.addListener(SWT.Modify, new Listener() {
			public void handleEvent(Event event) {
				try {
					desc = descCtrl.getText();
					buttonOK.setEnabled(true);
				} catch (Exception e) {
					buttonOK.setEnabled(false);
				}
			}
		});

		buttonOK.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				returnVal = 1;
				shell.dispose();
			}
		});
	    
		shell.addListener(SWT.Traverse, new Listener() {
			public void handleEvent(Event event) {
				if(event.detail == SWT.TRAVERSE_ESCAPE)
					event.doit = false;
			}
		});

		nameCtrl.setText(this.name);
		runCtrl.setText(this.run);
		descCtrl.setText(this.desc);
		initializePosition(shell);
		shell.pack();
		shell.open();

		Display display = parent.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		return returnVal;
	}
}