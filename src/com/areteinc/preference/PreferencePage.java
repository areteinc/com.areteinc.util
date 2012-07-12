package com.areteinc.preference;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.areteinc.action.Activator;

public class PreferencePage extends org.eclipse.jface.preference.FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {
	
	@Override
	protected void createFieldEditors() {
		addField(new DirectoryFieldEditor("project.directory.1", "Project Directory 1:", getFieldEditorParent()));
		addField(new DirectoryFieldEditor("project.directory.2", "Project Directory 2:", getFieldEditorParent()));
		addField(new DirectoryFieldEditor("project.directory.3", "Project Directory 3:", getFieldEditorParent()));
		addField(new DirectoryFieldEditor("project.directory.4", "Project Directory 4:", getFieldEditorParent()));
	}

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

}
