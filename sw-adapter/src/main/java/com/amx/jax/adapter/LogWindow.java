package com.amx.jax.adapter;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

class LogWindow extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1464722458487930139L;

	private int width;

	private int height;

	private JTextArea textArea = null;

	private JScrollPane pane = null;

	public LogWindow(String title, int width, int height) {
		super(title);
		setSize(width, height);
		textArea = new JTextArea();
		pane = new JScrollPane(textArea);
		getContentPane().add(pane);
		setVisible(true);
	}

	/**
	 * This method appends the data to the text area.
	 * 
	 * @param data the Logging information data
	 */
	public void showInfo(String data) {
		textArea.append(data);
		this.getContentPane().validate();
	}
}
