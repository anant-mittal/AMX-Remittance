
package com.amx.jax.updater;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author Thomas Otero (H3R3T1C)
 */
public class Updater extends JFrame {

	private static final long serialVersionUID = 7655016354046052027L;
	private static File ADAPTER_FOLDER = null;
	private static String sourceServer = "https://docs.amxremit.com/dist-sw-adapter/";
	private static String adapterFile = "sw-adapter-appd-kwt-java8.jar";
	private static String adapterFileMd5 = adapterFile + ".md5";

	private static String adapterFileMd5() {
		return adapterFile + ".md5";
	}

	private Thread worker;
	private final String root = "update/";

	private JTextArea aboutTextArea;
	private JButton cancleUpdateButton;
	private JButton launchAppButton;
	private JButton updateButton;
	private JScrollPane sp;
	private JPanel pan1;
	private JPanel pan2;

	public Updater() {
		initComponents();
		console("Connecting to UpdateCenter...");
		checkForUpdate();
	}

	private void initComponents() {

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		pan1 = new JPanel();
		pan1.setLayout(new BorderLayout());

		pan2 = new JPanel();
		pan2.setLayout(new FlowLayout());

		aboutTextArea = new JTextArea();
		sp = new JScrollPane();
		sp.setViewportView(aboutTextArea);

		launchAppButton = new JButton("Launch App");
		launchAppButton.setEnabled(false);
		launchAppButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				launchApp();
			}
		});
		pan2.add(launchAppButton);

		updateButton = new JButton("Update");
		updateButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				updateAdapter();
			}
		});
		pan2.add(updateButton);

		cancleUpdateButton = new JButton("Exit");
		cancleUpdateButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		pan2.add(cancleUpdateButton);

		pan1.add(sp, BorderLayout.CENTER);
		pan1.add(pan2, BorderLayout.SOUTH);

		add(pan1);
		pack();
		this.setSize(500, 400);
	}

	private void checkForUpdate() {
		worker = new Thread(
				new Runnable() {
					public void run() {
						try {

							String fileLink = getDownloadLinkFromHost();
							if (fileLink != null) {
								console("New version is avaiable, click Update to get Latest Adapter");
								updateButton.setEnabled(true);
							} else {
								console("No Update Required");
								launchAppButton.setEnabled(true);
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							JOptionPane.showMessageDialog(null, "An error occured while preforming update!");
						}
					}
				});
		worker.start();
	}

	private void updateAdapter() {
		worker = new Thread(
				new Runnable() {
					public void run() {
						try {
							downloadFile(adapterFile, adapterFile);
							downloadFile(adapterFileMd5(), adapterFileMd5());
							// unzip();
							// copyFiles(new File(root), new File("").getAbsolutePath());
							// cleanup();
							console("Update Finished!");
							launchAppButton.setEnabled(true);
						} catch (Exception ex) {
							ex.printStackTrace();
							JOptionPane.showMessageDialog(null, "An error occured while preforming update!");
						}
					}
				});
		worker.start();
	}

	private void launchApp() {
		String[] run = { "java", "-jar", ADAPTER_FOLDER + "/" + adapterFile };
		try {
			Runtime.getRuntime().exec(run);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.exit(0);
	}

	private void remove(File f) {
		File[] files = f.listFiles();
		for (File ff : files) {
			if (ff.isDirectory()) {
				remove(ff);
				ff.delete();
			} else {
				ff.delete();
			}
		}
	}

	public void copy(String srFile, String dtFile) throws FileNotFoundException, IOException {

		File f1 = new File(srFile);
		File f2 = new File(dtFile);

		InputStream in = new FileInputStream(f1);

		OutputStream out = new FileOutputStream(f2);

		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
	}

	private void console(String msg) {
		aboutTextArea.setText(aboutTextArea.getText() + "\n" + msg);
	}

	private void downloadFile(String source, String target) throws MalformedURLException, IOException {
		URL url = new URL(sourceServer + source);
		URLConnection conn = url.openConnection();
		InputStream is = conn.getInputStream();
		long max = conn.getContentLength();
		console("Downloding file " + source + " ...");
		console("Update Size(compressed): " + max + " Bytes");
		BufferedOutputStream fOut = new BufferedOutputStream(
				new FileOutputStream(new File(ADAPTER_FOLDER + "/" + target)));
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
	}

	private String getDownloadLinkFromHost() throws MalformedURLException, IOException {
		String currentMD5String = null;
		BufferedReader brG = null;
		try (BufferedReader br = new BufferedReader(new FileReader(ADAPTER_FOLDER + "/" + adapterFileMd5()))) {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			currentMD5String = sb.toString().replaceAll("[^0-9a-zA-z.]", "");
			brG = br;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (brG != null) {
				brG.close();
			}
		}

		String newMD5String = null;
		try {
			URL url = new URL(sourceServer + "/" + adapterFileMd5());
			InputStream html = null;
			html = url.openStream();
			int c = 0;
			StringBuilder buffer = new StringBuilder("");

			while (c != -1) {
				c = html.read();
				buffer.append((char) c);
			}
			newMD5String = buffer.toString().replaceAll("[^0-9a-zA-z.]", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(currentMD5String + "=" + newMD5String);

		if (newMD5String == null || newMD5String.equals(currentMD5String)) {
			return null;
		}
		return sourceServer + "/" + adapterFile;

	}

	public static void main(String args[]) {
		final Class<?> referenceClass = Updater.class;
		final URL url = referenceClass.getProtectionDomain().getCodeSource().getLocation();
		try {
			ADAPTER_FOLDER = new File(url.toURI()).getParentFile();
			// this is the path you want
			String[] filenames = ADAPTER_FOLDER.list();

			for (String file : filenames) {
				if (file.endsWith(".jar")) {
					adapterFile = file;
				}
			}
			adapterFileMd5 = adapterFileMd5();

		} catch (final URISyntaxException e) {
			// etc.
		}

		if (args.length > 1) {
			sourceServer = args[1];
		}
		if (args.length > 0) {
			adapterFile = args[0];
		} else {

		}

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new Updater().setVisible(true);
			}
		});
	}

	private void unzip() throws IOException {
		int BUFFER = 2048;
		BufferedOutputStream dest = null;
		BufferedInputStream is = null;
		ZipEntry entry;
		ZipFile zipfile = new ZipFile("update.zip");
		Enumeration e = zipfile.entries();
		(new File(root)).mkdir();
		while (e.hasMoreElements()) {
			entry = (ZipEntry) e.nextElement();
			aboutTextArea.setText(aboutTextArea.getText() + "\nExtracting: " + entry);
			if (entry.isDirectory())
				(new File(root + entry.getName())).mkdir();
			else {
				(new File(root + entry.getName())).createNewFile();
				is = new BufferedInputStream(zipfile.getInputStream(entry));
				int count;
				byte data[] = new byte[BUFFER];
				FileOutputStream fos = new FileOutputStream(root + entry.getName());
				dest = new BufferedOutputStream(fos, BUFFER);
				while ((count = is.read(data, 0, BUFFER)) != -1) {
					dest.write(data, 0, count);
				}
				dest.flush();
				dest.close();
				is.close();
			}
		}
	}

	private void copyFiles(File f, String dir) throws IOException {
		File[] files = f.listFiles();
		for (File ff : files) {
			if (ff.isDirectory()) {
				new File(dir + "/" + ff.getName()).mkdir();
				copyFiles(ff, dir + "/" + ff.getName());
			} else {
				copy(ff.getAbsolutePath(), dir + "/" + ff.getName());
			}
		}
	}

	private void cleanup() {
		aboutTextArea.setText(aboutTextArea.getText() + "\nPreforming clean up...");
		File f = new File("update.zip");
		f.delete();
		remove(new File(root));
		new File(root).delete();
	}
}
