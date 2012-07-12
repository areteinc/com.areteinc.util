package com.areteinc.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import com.areteinc.action.Activator;

public class SampleHandler {
	
	File[] projectDirs;
	
	public SampleHandler(){

		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();

		projectDirs = new File[4];

		for (int i = 0; i < projectDirs.length; i++) {
			projectDirs[i] = new File(preferenceStore.getString("project.directory." + (i+1)).replace("\\", "\\\\"));
		}
		
	}
	
	public MessageConsole getMessageConsole(){
		
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
		
		messageConsole.activate();
		
		return messageConsole;
	}
	
	public void listDir(){
		runCommand("cmd /c dir", null);
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
		runCommands(commands);
		
	}
	
	public void runCommands(String[] commands){
		for (int i = 0; i < projectDirs.length; i++) {
			for (int j = 0; j < commands.length; j++) {
				runCommand(commands[j], projectDirs[i]);
			}
		}
	}

	public void runCommand(String command, File workingDir) {
		
		MessageConsole messageConsole = getMessageConsole();
		MessageConsoleStream messageStream = messageConsole.newMessageStream();
		String outputLine = "";
		BufferedReader inputStream = null;
		BufferedReader errorStream = null;
		Process process = null;
		
	    try {
	    	
	    	process = Runtime.getRuntime().exec(command, null, workingDir);
		    inputStream = new BufferedReader(new InputStreamReader(process.getInputStream()));
		    errorStream = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		    
			while ((outputLine = inputStream.readLine()) != null ){
				messageStream.println(outputLine);
			}
			
		    while ((outputLine = errorStream.readLine()) != null){
		    	messageStream.println(outputLine);
		    }
		    
		    inputStream.close();
		    errorStream.close();
		    messageStream.close();
		    process.waitFor();
		    
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			
		}
	    
	}
	
}
