package com.areteinc.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

/*
 * */
public class HandlerUtil {
	
	public HandlerUtil(){
	}
	
	public IStructuredSelection getSelection() {
		return (IStructuredSelection) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();
	}
	
	public IResource getSelectedResource() throws Exception {
		
		IStructuredSelection selection = getSelection();	
		Object selectedElement = selection.getFirstElement();
		
		
		if (selectedElement instanceof IFile) {
			return (IFile) selectedElement;
		} else if (selectedElement instanceof IResource) {
			return (IResource) selectedElement;
		} else if (selectedElement instanceof IAdaptable) {
			IResource resource = (IResource) ((IAdaptable) selectedElement).getAdapter(IResource.class);
			
			if (resource == null) {
				throw new Exception("unknown selected resource: " + selectedElement.getClass().getName());	
			} else {
				return resource;
			}
		} else {
			throw new Exception("unknown selected resource: " + selectedElement.getClass().getName());
		}
		
	}
	
	
	@SuppressWarnings("unchecked")
	public IProject[] getSelectedProjects() {
		
		IStructuredSelection selection = getSelection();		
		List<Object> selectionList = selection.toList();
		HashMap<String, IProject> projects = new HashMap<String, IProject>();
		Object selectedElement = null;
		IProject project = null;
		IFile file = null;
		
		for (int i = 0; i < selectionList.size(); i++) {
			
			selectedElement = selectionList.get(i);
			if (selectedElement instanceof IProject) {
				project = (IProject) selectedElement;
			} else if (selectedElement instanceof IFile) {
				file = (IFile) selectedElement;
				project = file.getProject();
			} else {
				System.out.println("unknown selected resource: " + selectedElement.getClass().getName());
			}
			
			projects.put(project.getName(), project);
		}
		
		return (IProject[]) projects.values().toArray();
	}
	
	public MessageConsole getMessageConsole(boolean activateConsole){
		
		String consoleName = "Util";
		MessageConsole messageConsole = null;
		IConsoleManager consoleManager = ConsolePlugin.getDefault().getConsoleManager();
		IConsole[] consoles = consoleManager.getConsoles();
		
		for (int i = 0; i < consoles.length; i++){
			if (consoleName.equals(consoles[i].getName())){
				messageConsole = (MessageConsole) consoles[i];
		    }
		}
		
		if (messageConsole == null) {
			messageConsole = new MessageConsole(consoleName, null);
			consoleManager.addConsoles(new IConsole[]{ messageConsole });
		}
		
		if (activateConsole) {
			messageConsole.activate();
		}
		
		return messageConsole;
	}
	
	public void listDir(){
		runCommand("cmd /c dir", null, true);
	}
	
	public void createBranches(){

		String branchName = null;
		String[] commands = new String[3];
		InputDialog inputDialog = new InputDialog(null, "Create Branches", "Please enter branch name", "", null); 

		if(inputDialog.open() == IStatus.OK) { 
			branchName = inputDialog.getValue(); 
		} else {
			return;
		}

		commands[0] = "cmd /c git stash";
		commands[1] = "cmd /c git checkout --quiet -b topic-" + branchName + " master";
		commands[2] = "cmd /c git push --porcelain origin topic-" + branchName;
		runCommands(commands, getSelectedProjects());
		
	}
	
	public void diffFile() {
		
		IResource resource = null;
		IProject project = null;
		String command = null;
		
		try {
			resource = getSelectedResource();
			project = resource.getProject();
			command = "cmd /c git difftool HEAD --no-prompt " + getRelativePath(resource, project);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Thread thread = new Thread(new FileDiffTool(command, project));
		thread.start();
		
	}
	
	private class FileDiffTool implements Runnable {
		
		String command;
		IProject project;
		
		FileDiffTool(String cmd, IProject proj){
			command = cmd;
			project = proj;
		}

		@Override
		public void run() {
			runCommand(command, project, false);
		}
		
	}
	
	public String getRelativePath(IResource resource, IProject project) {
		
		URI resourceURI = resource.getLocationURI();
		URI projectURI = project.getLocationURI();
		
		return projectURI.relativize(resourceURI).getPath();
	}
	
	public String getPath(Object object) throws Exception {		
		
		if (object instanceof IFile) {
			return ((IFile) object).getLocationURI().getPath();
		} else if (object instanceof IProject) {
			return ((IProject) object).getLocationURI().getPath();
		} else {
			throw new Exception("[HandlerUtil.getPath] unknown object: " + object.getClass().getName());
		}
		
	}
	
	public void runCommands(String[] commands, IProject[] projects){
		for (int i = 0; i < projects.length; i++) {
			for (int j = 0; j < commands.length; j++) {
				runCommand(commands[j], projects[i], true);
			}
		}
	}

	public void runCommand(String command, IProject project, boolean activateConsole) {
		
		MessageConsole messageConsole = getMessageConsole(activateConsole);
		MessageConsoleStream messageStream = messageConsole.newMessageStream();
		String outputLine = "";
		BufferedReader inputStream = null;
		BufferedReader errorStream = null;
		Process process = null;
		File workingDir = null; 
		
	    try {
	    	
	    	workingDir = new File(getPath(project));
	    	process = Runtime.getRuntime().exec(command, null, workingDir);
		    inputStream = new BufferedReader(new InputStreamReader(process.getInputStream()));
		    errorStream = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		    
			while ((outputLine = inputStream.readLine()) != null){
				messageStream.println(outputLine);
			}
			
		    while ((outputLine = errorStream.readLine()) != null){
		    	messageStream.println(outputLine);
		    }
		    
		    messageStream.println("[HandlerUtil.runCommand] command: " + command + ", workingDir: " + workingDir.getAbsolutePath());
		    
		    inputStream.close();
		    errorStream.close();
		    messageStream.close();
		    process.waitFor();
		    
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
		}
	    
	}
	
}

