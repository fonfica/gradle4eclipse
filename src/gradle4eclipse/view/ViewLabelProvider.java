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

import gradle4eclipse.model.TreeObject;
import gradle4eclipse.model.TreeParent;

import java.net.URL;


import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class ViewLabelProvider extends ColumnLabelProvider {
	private Image elementImage;
	private Image folderImage;
	private Image favoriteImage;

	public String getText(Object obj) {
		return obj.toString();
	}

	public Image getImage(Object obj) {
		if (elementImage == null) {
			Bundle bundle = FrameworkUtil.getBundle(this.getClass());
			URL url = FileLocator
					.find(bundle, new Path("icons/task.gif"), null);
			ImageDescriptor imageDescriptor = ImageDescriptor
					.createFromURL(url);
			elementImage = imageDescriptor.createImage();
		}
		
		if (favoriteImage == null) {
			Bundle bundle = FrameworkUtil.getBundle(this.getClass());
			URL url = FileLocator
					.find(bundle, new Path("icons/favorite.gif"), null);
			ImageDescriptor imageDescriptor = ImageDescriptor
					.createFromURL(url);
			favoriteImage = imageDescriptor.createImage();
		}

		if (folderImage == null) {
			Bundle bundle = FrameworkUtil.getBundle(this.getClass());
			URL url = FileLocator.find(bundle, new Path("icons/folder.gif"),
					null);
			ImageDescriptor imageDescriptor = ImageDescriptor
					.createFromURL(url);
			folderImage = imageDescriptor.createImage();
		}

		if (obj instanceof TreeParent) {
			return folderImage;
		}
		else if(obj instanceof TreeObject) {
			TreeObject treeObj = (TreeObject)obj;
			if(treeObj.isFavorite())
				return favoriteImage;
		}
		return elementImage;

	}

	public String getToolTipText(Object element) {
		if (element instanceof TreeObject)
			return ((TreeObject) element).getDescription();
		return "";
	}

	public Point getToolTipShift(Object object) {
		return new Point(5, 5);
	}

	public int getToolTipDisplayDelayTime(Object object) {
		return 1000;
	}

	public int getToolTipTimeDisplayed(Object object) {
		return 5000;
	}

	public void update(ViewerCell cell) {
		cell.setText(getText(cell.getElement()));
		cell.setImage(getImage(cell.getElement()));
	}
}