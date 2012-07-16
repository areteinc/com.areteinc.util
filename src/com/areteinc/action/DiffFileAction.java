package com.areteinc.action;

import org.eclipse.jface.action.IAction;

public class DiffFileAction extends BaseAction {
	
	@Override
	public void run(IAction action) {
		handlerUtil.diffFile();
	}


}
