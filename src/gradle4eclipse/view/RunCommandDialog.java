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

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class RunCommandDialog extends Dialog {
	private String value;
	private Shell parent;
	
	public RunCommandDialog(Shell parent) {
		super(parent);
		this.parent = parent;
	}
	
	protected void initializePosition(Shell shell) { 
		Monitor primary = shell.getMonitor(); 
		Rectangle bounds = primary.getBounds (); 
		int x = bounds.width / 2; 
		int y = bounds.height / 2; 
		shell.setLocation (x-200, y-40); 
	}
	
	public String getValue() {
		return this.value;
	}

	public void setValue(String _value) {
		this.value = _value;
	}

	public int open() {
		final Shell shell = new Shell(parent.getDisplay(), SWT.TITLE|SWT.CLOSE|SWT.APPLICATION_MODAL);
		Bundle bundle = FrameworkUtil.getBundle(this.getClass());
		URL url = FileLocator.find(bundle, new Path("icons/sample.gif"),null);
		ImageDescriptor sampleImage = ImageDescriptor.createFromURL(url);
		sampleImage.createImage();
		shell.setImage(sampleImage.createImage());
		shell.setText("Run: gradle ");
		RowLayout rowLayout = new RowLayout(SWT.HORIZONTAL);
        rowLayout.marginTop = 20;
        rowLayout.marginBottom = 20;
        rowLayout.marginLeft = 10;
        rowLayout.marginRight = 10;
        rowLayout.spacing = 10;
		shell.setLayout(rowLayout);

		final Text text = new Text(shell, SWT.SINGLE | SWT.BORDER);
		text.setLayoutData(new RowData(400, 15));

		final Button buttonOK = new Button(shell, SWT.PUSH|SWT.CENTER);
		buttonOK.setText("Run");
		url = FileLocator.find(bundle, new Path("icons/run.gif"),null);
		ImageDescriptor runImage = ImageDescriptor.createFromURL(url);
		runImage.createImage();
		buttonOK.setImage(runImage.createImage());
		buttonOK.setLayoutData(new RowData(80, 22));
		shell.setDefaultButton(buttonOK);

		text.addListener(SWT.Modify, new Listener() {
			public void handleEvent(Event event) {
				try {
					value = text.getText();
					buttonOK.setEnabled(true);
				} catch (Exception e) {
					buttonOK.setEnabled(false);
				}
			}
		});

		buttonOK.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				shell.dispose();
			}
		});
	    
		shell.addListener(SWT.Traverse, new Listener() {
			public void handleEvent(Event event) {
				if(event.detail == SWT.TRAVERSE_ESCAPE)
					event.doit = false;
			}
		});

		text.setText("");
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