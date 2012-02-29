package com.jakemadethis.pinballeditor;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class FileMonitor {
	private final Timer timer;
	private FileChangedListener listener;
	private final LinkedList<File> files;
	private final int period;

	public FileMonitor(int period) {
		this.period = period;
		timer = new Timer(true);
		files = new LinkedList<File>();
	}
	
	public void setFileChangedListener(FileChangedListener listener) {
		this.listener = listener;
	}
	
	public void addFile(File file) {
		files.add(file);
		FileMonitorTask task;
		try {
			task = new FileMonitorTask(file);
			timer.schedule(task, period, period);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void fireFileChangeEvent(File file) {
		listener.fileChanged(file);
	}
	
	public class FileMonitorTask extends TimerTask {

		File monitoredFile;

		long lastModified;

		public FileMonitorTask(File file) throws FileNotFoundException {
			this.lastModified = 0;
			monitoredFile = file;
			if (!monitoredFile.exists()) { // but is it on CLASSPATH?
				URL fileURL = listener.getClass().getClassLoader().getResource(file.toString());
				if (fileURL != null) {
					monitoredFile = new File(fileURL.getFile());
				} else {
					throw new FileNotFoundException("File Not Found: " + file);
				}
			}
			this.lastModified = monitoredFile.lastModified();
		}

		@Override
		public void run() {
			long lastModified = monitoredFile.lastModified();
			if (lastModified != this.lastModified) {
				this.lastModified = lastModified;
				fireFileChangeEvent(monitoredFile);
			}
		}
	}
	
	public interface FileChangedListener {
		void fileChanged(File file);
	}
}
