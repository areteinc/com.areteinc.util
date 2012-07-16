package com.areteinc.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.IStructuredSelection;

public class CreateBranchAction extends BaseAction {

	
	@Override
	public void run(IAction action) {
		handlerUtil.createBranches(); // TODO
	}


}
