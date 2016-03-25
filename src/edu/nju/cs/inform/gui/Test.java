package edu.nju.cs.inform.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;

public class Test {
	
	static Menu menu;
	
	public static void main(String[] args){
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setBounds(200, 200, 400, 400);
		menu = new Menu(shell,SWT.BAR);
		shell.setMenuBar(menu);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}
