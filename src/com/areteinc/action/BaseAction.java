package com.areteinc.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import com.areteinc.handler.HandlerUtil;

public class BaseAction implements IWorkbenchWindowActionDelegate {

	IWorkbenchWindow window;
	HandlerUtil handlerUtil = new HandlerUtil();
	
	@Override
	public void run(IAction action) {
		
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}

	@Override
	public void init(IWorkbenchWindow window) {
		// TODO Auto-generated method stub
	}

}
