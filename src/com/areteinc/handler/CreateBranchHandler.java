package com.areteinc.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public class CreateBranchHandler extends BaseHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		
		handlerUtil.createBranches();
		
		return null;
	}

	

}