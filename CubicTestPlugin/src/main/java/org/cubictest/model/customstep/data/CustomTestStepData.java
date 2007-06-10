package org.cubictest.model.customstep.data;

import java.util.ArrayList;
import java.util.List;

import org.cubictest.model.customstep.data.CustomTestStepDataEvent.EventType;

public final class CustomTestStepData {

	private String displayText = "";
	private String path = "";
	private List<ICustomTestStepDataListener> listeners = new ArrayList<ICustomTestStepDataListener>();

	public String getDisplayText() {
		return displayText;
	}

	public String getPath() {
		return path;
	}

	public void setDisplayText(String text) {
		String oldText = this.displayText;
		this.displayText = text;
		fireEvent(CustomTestStepDataEvent.EventType.DISPLAY_TEXT,oldText,text);
	}

	private void fireEvent(EventType type, String oldText, String text) {
		CustomTestStepDataEvent event = new CustomTestStepDataEvent(this,type,oldText,text);
		for(ICustomTestStepDataListener listener: listeners){
			listener.handleEvent(event);
		}
	}

	public void setPath(String path) {
		String oldPath = this.path;
		this.path = path;
		fireEvent(CustomTestStepDataEvent.EventType.PATH,oldPath,path);
	}

	public void addChangeListener(ICustomTestStepDataListener listener) {
		listeners.add(listener);
	}
}