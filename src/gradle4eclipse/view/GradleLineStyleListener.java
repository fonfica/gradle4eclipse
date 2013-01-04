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

import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;

public class GradleLineStyleListener implements LineStyleListener {

	public void lineGetStyle(LineStyleEvent lineStyle) {
		// make all upper case lines bold
		if (lineStyle.lineText != null && lineStyle.lineText.length() != 0) {
			if (lineStyle.lineText.equals(lineStyle.lineText.toUpperCase()))
				if (lineStyle.styles!= null && lineStyle.styles.length != 0)
					lineStyle.styles[0].fontStyle = 1; // bold
		}
	}
}
