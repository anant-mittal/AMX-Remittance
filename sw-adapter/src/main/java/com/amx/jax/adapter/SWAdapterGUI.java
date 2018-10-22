package com.amx.jax.adapter;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.amx.jax")
public class SWAdapterGUI extends JFrame {

	private static final long serialVersionUID = 2703873832309041808L;

	public SWAdapterGUI() {
		initUI();
	}

	JLabel statusReader = new JLabel("• Readr");
	JLabel statusCard = new JLabel("• Card");
	JLabel statusData = new JLabel("• Data");
	JLabel statusSync = new JLabel("• Sync");

	JLabel labelDescription = new JLabel("....");

	private void initUI() {

		setTitle("Al Mulla Exchange - Branch Desktop Client");
		setSize(400, 200);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// create a new panel with GridBagLayout manager
		JPanel newPanel = new JPanel();
		newPanel.setLayout(new GridBagLayout());

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(10, 10, 10, 10);

		// add components to the panel
		constraints.gridx = 0;
		constraints.gridy = 0;
		newPanel.add(statusReader, constraints);
		constraints.gridx = 1;
		newPanel.add(statusCard, constraints);
		constraints.gridx = 2;
		newPanel.add(statusData, constraints);
		constraints.gridx = 3;
		newPanel.add(statusSync, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.CENTER;
		newPanel.add(labelDescription, constraints);

		// Button
		JButton quitButton = new JButton("Quit");
		quitButton.addActionListener((ActionEvent event) -> {
			System.exit(0);
		});
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.CENTER;
		newPanel.add(quitButton, constraints);

		statusReader.setEnabled(false);
		statusCard.setEnabled(false);
		statusData.setEnabled(false);
		statusSync.setEnabled(false);

		newPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Card Reader Status"));
		add(newPanel);

	}

	public void foundReader(boolean found) {
		statusReader.setEnabled(found);
		if (found) {
			labelDescription.setText("Reader Detected");
		} else {
			labelDescription.setText("Reader No Connected");
		}
	}

	public void foundCard(boolean found) {
		statusCard.setEnabled(found);
		if (found) {
			labelDescription.setText("Card Scanned");
		}
	}

	public void foundData(boolean found) {
		statusData.setEnabled(found);
		labelDescription.setText("Data Found");
	}

	public void foundSync(boolean found) {
		statusSync.setEnabled(found);
		labelDescription.setText("Syncing...");
	}

	public void log(String message) {
		labelDescription.setText(message);
	}

}