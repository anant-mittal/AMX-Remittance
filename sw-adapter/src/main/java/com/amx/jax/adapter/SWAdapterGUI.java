package com.amx.jax.adapter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import org.springframework.stereotype.Component;

import com.amx.jax.AppContextUtil;
import com.amx.jax.adapter.ACardReaderService.CardStatus;
import com.amx.jax.adapter.ACardReaderService.DataStatus;
import com.amx.jax.adapter.ACardReaderService.DeviceStatus;
import com.amx.utils.Constants;
import com.amx.utils.StringUtils;

@Component
public class SWAdapterGUI extends JFrame {

	private static final long serialVersionUID = 2703873832309041808L;

	public SWAdapterGUI() {
		initUI();
	}

	public static final String DOT = "|";
	public static final String STAT_FORMAT_PRE = "%-10s";
	public static final String STAT_FORMAT_SUF = ":%30s";
	public static final String STAT_FORMAT_DEVICE = String.format(STAT_FORMAT_PRE, "Device") + STAT_FORMAT_SUF;
	public static final String STAT_FORMAT_READER = String.format(STAT_FORMAT_PRE, "Reader") + STAT_FORMAT_SUF;
	public static final String STAT_FORMAT_CARD = String.format(STAT_FORMAT_PRE, "Card") + STAT_FORMAT_SUF;
	public static final String STAT_FORMAT_DATA = String.format(STAT_FORMAT_PRE, "Data") + STAT_FORMAT_SUF;

	JLabel statusPing = new JLabel(DOT);
	JLabel statusReader = new JLabel(String.format(STAT_FORMAT_READER, Constants.BLANK));
	JLabel statusDevice = new JLabel(String.format(STAT_FORMAT_DEVICE, Constants.BLANK));
	JLabel statusCard = new JLabel(String.format(STAT_FORMAT_CARD, Constants.BLANK));
	JLabel statusData = new JLabel(String.format(STAT_FORMAT_DATA, Constants.BLANK));
	Font font = new Font("monospaced", Font.PLAIN, 12);

	JLabel labelDescription = new JLabel("....");
	JLabel labelDescriptionDetail = new JLabel("....");
	public static SWAdapterGUI CONTEXT = null;
	public static String LOG = "";
	public static String WIN_TITLE = "Al Mulla Exchange - BranchAdapter";

	private JTextArea textArea = null;

	private JScrollPane pane = null;

	private JScrollPane aboutTextPane = null;
	JTextArea aboutTextArea = null;
	private JButton updateButton;

	private void initUI() {

		setTitle(WIN_TITLE);
		setSize(400, 400);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		JTabbedPane tabs = new JTabbedPane();

		// create a new panel with GridBagLayout manager
		JPanel newPanel = new JPanel();
		newPanel.setLayout(new GridBagLayout());
		newPanel.setFont(font);

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(10, 10, 10, 10);
		constraints.gridwidth = 4;

		// add components to the panel
		constraints.gridx = 0;
		constraints.gridy = 0;
		newPanel.add(statusReader, constraints);
		constraints.gridx = 0;
		constraints.gridy = 1;
		newPanel.add(statusDevice, constraints);
		constraints.gridx = 0;
		constraints.gridy = 2;
		newPanel.add(statusCard, constraints);
		constraints.gridx = 0;
		constraints.gridy = 3;
		newPanel.add(statusData, constraints);

		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 4;
		statusPing.setForeground(Color.BLUE);
		newPanel.add(statusPing, constraints);

		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridwidth = 4;
		labelDescription.setFont(font);
		newPanel.add(labelDescription, constraints);

		constraints.gridx = 0;
		constraints.gridy = 6;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridwidth = 4;
		labelDescriptionDetail.setFont(font);
		newPanel.add(labelDescriptionDetail, constraints);

		// Button
		constraints.gridx = 0;
		constraints.gridy = 7;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.CENTER;
		JButton openButton = new JButton("Open");
		openButton.addActionListener((ActionEvent event) -> {
			SWAdapterLauncher.opnePage();
		});
		newPanel.add(openButton, constraints);

		constraints.gridx = 1;
		constraints.gridy = 7;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.CENTER;
		JButton refreshButton = new JButton("Refresh");
		refreshButton.addActionListener((ActionEvent event) -> {
			AppContextUtil.getTraceId();
			AppContextUtil.init();
			ACardReaderService.CONTEXT.reset();
		});
		newPanel.add(refreshButton, constraints);

		constraints.gridx = 2;
		constraints.gridy = 7;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.CENTER;
		JButton quitButton = new JButton("Quit");
		quitButton.addActionListener((ActionEvent event) -> {
			System.exit(0);
		});
		newPanel.add(quitButton, constraints);

		statusReader.setEnabled(false);
		statusReader.setBackground(Color.LIGHT_GRAY);
		statusReader.setFont(font);
		statusDevice.setEnabled(false);
		statusDevice.setBackground(Color.LIGHT_GRAY);
		statusDevice.setFont(font);
		statusCard.setEnabled(false);
		statusCard.setBackground(Color.LIGHT_GRAY);
		statusCard.setFont(font);
		statusData.setEnabled(false);
		statusData.setBackground(Color.LIGHT_GRAY);
		statusData.setFont(font);

		// newPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
		// "Card Reader Status"));

		textArea = new JTextArea();
		textArea.setFont(new Font("monospaced", Font.PLAIN, 8));
		textArea.setEditable(false);
		pane = new JScrollPane(textArea);

		// About Area
		JPanel aboutPaneWrapper = new JPanel();
		aboutPaneWrapper.setLayout(new BorderLayout());

		JPanel innerAboutPane = new JPanel();
		innerAboutPane.setLayout(new FlowLayout());

		aboutTextArea = new JTextArea();
		aboutTextArea.setFont(new Font("monospaced", Font.PLAIN, 8));
		aboutTextArea.setEditable(false);

		aboutTextPane = new JScrollPane();
		aboutTextPane.setViewportView(aboutTextArea);

		updateButton = new JButton("Check Update");
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkUpdate();
			}
		});
		innerAboutPane.add(updateButton);

		aboutPaneWrapper.add(aboutTextPane, BorderLayout.CENTER);
		aboutPaneWrapper.add(innerAboutPane, BorderLayout.SOUTH);

		tabs.addTab("Adapter", newPanel);
		tabs.addTab("Logs", pane);
		tabs.addTab("About", aboutPaneWrapper);
		add(tabs);

	}

	private String getDots(int count) {
		StringBuilder sb = new StringBuilder(count);
		for (int i = 0; i < count; i++) {
			sb.append(DOT);
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
			statusPing.setForeground(Color.RED);
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
		statusDevice.setText(String.format(STAT_FORMAT_DEVICE, status.toString()));
	}

	public void foundCard(CardStatus status) {
		statusCard.setEnabled(status.ordinal() >= CardStatus.FOUND.ordinal());
		statusCard.setText(String.format(STAT_FORMAT_CARD, status.toString()));
		statusData.setEnabled(status.ordinal() >= CardStatus.READING.ordinal());
	}

	public void foundData(DataStatus status) {
		statusData.setText(String.format(STAT_FORMAT_DATA, status.toString()));
	}

	public void readerName(String name) {
		statusReader.setText(String.format(STAT_FORMAT_READER, name));
	}

	private int logCount = 0;
	private static int MAX_LOG_COUNT = 2000;

	public void logWindow(String message) {
		if (textArea != null) {
			try {
				if (logCount >= MAX_LOG_COUNT) {
					logCount = MAX_LOG_COUNT;
					int end;

					end = textArea.getLineEndOffset(0);

					textArea.replaceRange("", 0, end);
					// textArea.remove(logCount - MAX_LOG_COUNT);
				}
				textArea.append(new Date().toString() + " : " + message + "\n");
				logCount++;
			} catch (Exception e) {
				textArea.append(logCount + " : " + message + " | " + e.getMessage() + "\n");
				logCount++;
			}
		}

	}

	public void log(String message) {
		LOG = message;
		logWindow(message);
		labelDescription.setText(StringUtils.substring(message, 42));
		labelDescriptionDetail.setText("");
	}

	public void log(String message, String detail) {
		LOG = message;
		logWindow(message + " - " + detail);
		labelDescription.setText(StringUtils.substring(message, 60));
		labelDescriptionDetail.setText(StringUtils.substring(detail, 100));
	}

	public static void updateTitle(String title) {
		WIN_TITLE = title;
		if (CONTEXT != null) {
			CONTEXT.setTitle(WIN_TITLE);
		}
	}

	public static void updateAbout(String text) {
		if (CONTEXT != null) {
			CONTEXT.getAboutTextArea().setText(text);
		}
	}

	public JTextArea getAboutTextArea() {
		return aboutTextArea;
	}

	public static String sourceServer = "https://docs.amxremit.com/dist-sw-adapter/";
	public static String updaterFile = "sw-updater.jar";
	public static File ADAPTER_FOLDER;
	private Thread worker;

	private void console(String msg) {
		aboutTextArea.setText(aboutTextArea.getText() + "\n" + msg);
	}

	private void downloadFile(String source, String target) throws MalformedURLException, IOException {

		File updater = new File(ADAPTER_FOLDER + "/" + target);

		if (!updater.exists()) {
			URL url = new URL(sourceServer + source);
			URLConnection conn = url.openConnection();
			InputStream is = conn.getInputStream();
			long max = conn.getContentLength();
			console("Downloding file " + source + " ...");
			console("Update Size(compressed): " + max + " Bytes");
			BufferedOutputStream fOut = new BufferedOutputStream(
					new FileOutputStream(updater));
			byte[] buffer = new byte[32 * 1024];
			int bytesRead = 0;
			int in = 0;
			while ((bytesRead = is.read(buffer)) != -1) {
				in += bytesRead;
				fOut.write(buffer, 0, bytesRead);
			}
			fOut.flush();
			fOut.close();
			is.close();
			console("Download Complete!");
		} else {
			console("Updater exists!");
		}

	}

	public void checkUpdate() {
		worker = new Thread(
				new Runnable() {
					public void run() {
						try {
							downloadFile(updaterFile, updaterFile);
							launchApp();
						} catch (Exception ex) {
							ex.printStackTrace();
							console(ex.toString());
							JOptionPane.showMessageDialog(null, "An error occured while preforming update!");
						}
					}
				});
		worker.start();
	}

	private void launchApp() {
		if (ACardReaderService.CONTEXT != null) {
			String adapterjarFile = String.format("sw-adapter-%s-$s-%s.jar", ACardReaderService.CONTEXT.getEnv(),
					ACardReaderService.CONTEXT.getTnt(), ACardReaderService.CONTEXT.getLane());
			console("Launching " + ADAPTER_FOLDER + "/" + updaterFile + " " + adapterjarFile);
			String[] run = { "java", "-jar", ADAPTER_FOLDER + "/" + updaterFile, adapterjarFile };
			try {
				Runtime.getRuntime().exec(run);
				System.exit(0);
			} catch (Exception ex) {
				ex.printStackTrace();
				console(ex.toString());
			}
		} else {
			console("Adapter is not ready please wait.");
		}
	}
}