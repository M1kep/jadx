package jadx.fxgui.jobs;

import jadx.api.JavaClass;
import jadx.fxgui.JadxWrapper;

public class DecompileJob extends BackgroundJob {

	public DecompileJob(JadxWrapper wrapper, int threadsCount) {
		super(wrapper, threadsCount);
	}

	protected void runJob() {
		for (final JavaClass cls : wrapper.getClasses()) {
			addTask(cls::decompile);
		}
	}

	@Override
	public String getInfoString() {
		return "Decompiling: ";
	}

}
