package com.areteinc.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;




/**
 * Our sample action implements workbench action delegate.
 * The action proxy will be created by the workbench and
 * shown in the UI. When the user tries to use the action,
 * this delegate will be created and execution will be 
 * delegated to it.
 * @see IWorkbenchWindowActionDelegate
 */
public class SampleAction extends BaseAction {
	

	public void run(IAction action) {
		IStructuredSelection selection = (IStructuredSelection) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();	
		System.out.println(selection.getFirstElement());
		System.out.println(selection.getFirstElement().getClass().getName());
//		HandlerUtil sampleHandler = new HandlerUtil();
//		sampleHandler.listDir();
		
	}

}