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
