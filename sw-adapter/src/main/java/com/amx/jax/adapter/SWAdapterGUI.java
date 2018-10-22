package com.amx.jax.adapter;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.springframework.context.annotation.ComponentScan;

import com.amx.jax.adapter.ACardReaderService.CardStatus;
import com.amx.jax.adapter.ACardReaderService.DataStatus;
import com.amx.jax.adapter.ACardReaderService.DeviceStatus;

@ComponentScan("com.amx.jax")
public class SWAdapterGUI extends JFrame {

	private static final long serialVersionUID = 2703873832309041808L;

	public SWAdapterGUI() {
		initUI();
	}

	JLabel statusPing = new JLabel("•");
	JLabel statusDevice = new JLabel("Device");
	JLabel statusCard = new JLabel("Card");
	JLabel statusData = new JLabel("Data");

	JLabel labelDescription = new JLabel("....");
	public static SWAdapterGUI CONTEXT = null;

	private void initUI() {

		setTitle("Al Mulla Exchange - Branch Desktop Client");
		setSize(400, 400);
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
		newPanel.add(statusDevice, constraints);
		constraints.gridx = 0;
		constraints.gridy = 1;
		newPanel.add(statusCard, constraints);
		constraints.gridx = 0;
		constraints.gridy = 2;
		newPanel.add(statusData, constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 4;
		statusPing.setForeground(Color.BLUE);
		newPanel.add(statusPing, constraints);

		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridwidth = 4;
		newPanel.add(labelDescription, constraints);

		// Button
		JButton quitButton = new JButton("Quit");
		quitButton.addActionListener((ActionEvent event) -> {
			System.exit(0);
		});
		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.gridwidth = 4;
		constraints.anchor = GridBagConstraints.CENTER;
		newPanel.add(quitButton, constraints);

		statusDevice.setEnabled(false);
		statusDevice.setBackground(Color.LIGHT_GRAY);
		statusCard.setEnabled(false);
		statusCard.setBackground(Color.LIGHT_GRAY);
		statusData.setEnabled(false);
		statusData.setBackground(Color.LIGHT_GRAY);

		newPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Card Reader Status"));
		add(newPanel);

	}

	private String getDots(int count) {
		StringBuilder sb = new StringBuilder(count);
		for (int i = 0; i < count; i++) {
			sb.append("•");
		}
		return sb.toString();
	}

	public void updateDeviceHealthStatus(int count) {
		statusPing.setText(getDots(count));
	}

	public void progress(DeviceStatus deviceStatus, CardStatus cardStatusValue, DataStatus dataStatusValue) {
		statusDevice.setOpaque(false);
		statusCard.setOpaque(false);
		statusData.setOpaque(false);
		JLabel currentLabel = statusDevice;
		if (deviceStatus.ordinal() < DeviceStatus.CONNECTED.ordinal()) {
			statusPing.setForeground(Color.MAGENTA);
			currentLabel = statusDevice;
		} else if (cardStatusValue.ordinal() < CardStatus.NOCARD.ordinal()) {
			statusPing.setForeground(Color.RED);
			currentLabel = statusCard;
		} else if (cardStatusValue.ordinal() < CardStatus.NOCARD.ordinal()) {
			statusPing.setForeground(Color.ORANGE);
			currentLabel = statusCard;
		} else if (dataStatusValue.ordinal() < DataStatus.EMPTY.ordinal()) {
			statusPing.setForeground(Color.YELLOW);
			currentLabel = statusData;
		} else if (dataStatusValue.ordinal() > DataStatus.VALID_DATA.ordinal()) {
			statusPing.setForeground(Color.BLUE);
			currentLabel = statusData;
		} else if (cardStatusValue.ordinal() >= CardStatus.FOUND.ordinal()) {
			statusPing.setForeground(Color.GREEN);
			currentLabel = statusCard;
		} else if (deviceStatus.ordinal() >= DeviceStatus.CONNECTED.ordinal()) {
			statusPing.setForeground(Color.YELLOW);
			currentLabel = statusDevice;
		}
		currentLabel.setOpaque(true);
		currentLabel.setBackground(Color.LIGHT_GRAY);
		updateDeviceHealthStatus(deviceStatus.ordinal() + cardStatusValue.ordinal() + dataStatusValue.ordinal());
	}

	public void foundDevice(DeviceStatus status) {
		statusDevice.setEnabled(status.ordinal() >= DeviceStatus.CONNECTED.ordinal());
		statusDevice.setText(String.format("%-20s : %s", "Device", status.toString()));
	}

	public void foundCard(CardStatus status) {
		statusCard.setEnabled(status.ordinal() >= CardStatus.FOUND.ordinal());
		statusCard.setText(String.format("%-20s : %s", "Card", status.toString()));
		statusData.setEnabled(status.ordinal() >= CardStatus.READING.ordinal());
	}

	public void foundData(DataStatus status) {
		statusData.setText(String.format("%-20s : %s", "Data", status.toString()));
	}

	public void log(String message) {
		labelDescription.setText(message);
	}

}