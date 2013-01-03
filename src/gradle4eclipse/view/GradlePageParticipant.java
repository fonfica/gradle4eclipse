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
