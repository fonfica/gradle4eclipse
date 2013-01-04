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

import org.eclipse.swt.custom.StyledText;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsolePageParticipant;
import org.eclipse.ui.part.IPageBookViewPage;

public class GradlePageParticipant implements IConsolePageParticipant {

	public void activated() {

	}

	public void deactivated() {

	}

	public void dispose() {

	}
	
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		return null;
	}

	public void init(IPageBookViewPage page, IConsole console) {
		if (page.getControl() instanceof StyledText) {
			StyledText styledText = (StyledText) (page.getControl());

			GradleLineStyleListener gradleLineStyleListener = new GradleLineStyleListener();
			styledText.addLineStyleListener(gradleLineStyleListener);
		}

	}
}
