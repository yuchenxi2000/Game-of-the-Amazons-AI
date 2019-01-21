package gameUI;

import java.awt.EventQueue;

public class Launcher {
	/*
	 * Launch the application.
	 */

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					String spath = args[0];
					MainFrame frame = new MainFrame(spath);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
}