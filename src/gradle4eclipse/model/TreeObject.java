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
package gradle4eclipse.model;

import org.eclipse.core.runtime.IAdaptable;

public class TreeObject implements IAdaptable {
	private String name;
	private String run;
	private String description;
	private boolean defaultTask;
	private boolean isFavorite;
	
	private TreeParent parent;

	public TreeObject(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRun() {
		return this.run;
	}

	public void setRun(String run) {
		this.run = run;
	}

	public void setParent(TreeParent parent) {
		this.parent = parent;
	}

	public TreeParent getParent() {
		return parent;
	}

	public String toString() {
		return getName();
	}
	
	public boolean isFavorite() {
		return this.isFavorite;
	}

	public void setFavorite(boolean _isFavorite) {
		this.isFavorite = _isFavorite;
	}

	public Object getAdapter(@SuppressWarnings("rawtypes") Class key) {
		return null;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean getDefault() {
		return this.defaultTask;
	}

	public void setDefault(boolean defaultTask) {
		this.defaultTask = defaultTask;
	}
}