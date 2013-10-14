package es.optsicom.lib.util.outprocess;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;

public class OutprocessWithOperations {

	private static final int CONNECTION_TIMEOUT = 10000;

	private Process process;
	private int communicationPort;
	private Socket socket;
	private ObjectOutputStream sendToStream;
	private ObjectInputStream receiveFromStream;

	public void startOutprocess(Object remoteObject)
			throws OutprocessException {

		if(!(remoteObject instanceof Serializable)){
			throw new OutprocessException("remoteObject has to implement Serializable interface");
		}
		
		try {

			this.process = createJavaProcess();
			this.socket = new Socket();
			this.socket.connect(new InetSocketAddress("127.0.0.1",
					communicationPort), CONNECTION_TIMEOUT);

			sendToStream = new ObjectOutputStream(socket.getOutputStream());
			receiveFromStream = new ObjectInputStream(socket.getInputStream());

		} catch (IOException e) {
			throw new OutprocessException("Exception creating Java process", e);
		}

		try {
			sendToStream.writeObject(remoteObject);
		} catch (IOException e) {
			throw new OutprocessException("Exception sending the remoteObject to new Java process", e);
		}
	}

	public Object execOperation(String operationName, Object... params)
			throws OutprocessException {

		OperationPacket packet = new OperationPacket(operationName, params);

		try {
			sendToStream.writeObject(packet);
			sendToStream.reset();
		} catch (IOException e) {
			throw new OutprocessException(
					"Exception while sending operation to outprocess", e);
		}

		try {
			Result result = (Result) receiveFromStream.readObject();
			
			if(result.isCorrectExecution()){
				return result.getResult();
			} else {
				throw new OutprocessException("Exception in the execution of operation \""+operationName+"\"",result.getException());
			}
			
		} catch (IOException e) {
			throw new OutprocessException(
					"Exception while receiving result from outprocess", e);
		} catch (ClassNotFoundException e) {
			throw new OutprocessException(
					"Exception while converting to object the operation result",
					e);
		}
	}

	private Process createJavaProcess() throws IOException {

		String javaPath = System.getProperty("outprocess.java.exec");
		if (javaPath == null) {
			javaPath = System.getProperty("java.home") + "/bin/java";
		}

		String classpath = System.getProperty("java.class.path");

		// System.out.println("classpath:"+classpath);
		// System.out.println("pathdetector:"+CodePathDetector.get());

		classpath += File.pathSeparator + CodePathDetector.get();
		// System.out.println("finalclasspath:"+classpath);

		String[] cmd = null;
		if (System.getProperty("os.name").toLowerCase().contains("win")) {
			cmd = new String[] { "\"" + javaPath + "\"", "-cp", "\"" + classpath + "\"",
					OutprocessWithOperationMain.class.getName() };
		} else {
			cmd = new String[] {
					"/bin/sh",
					"-c",
					javaPath + " -cp " + classpath
							+ " outprocess.OutprocessWithOperationMain" };
		}

		System.out.println("Outprocess mode: " + Arrays.toString(cmd));
		final Process process = Runtime.getRuntime().exec(cmd);

		BufferedReader r = new BufferedReader(new InputStreamReader(
				process.getInputStream()));
		String portAsString = r.readLine();
		communicationPort = Integer.parseInt(portAsString);

		Thread t = new Thread() {
			public void run() {
				try {
					redirectToStdout(System.err, process.getErrorStream());
				} catch (IOException e) {
				}
			}
		};

		t.setPriority(Thread.MIN_PRIORITY);
		t.start();

		Thread t2 = new Thread() {
			public void run() {
				try {
					redirectToStdout(System.out, process.getInputStream());
				} catch (IOException e) {
				}
			}
		};
		t2.setPriority(Thread.MIN_PRIORITY);
		t2.start();
		return process;
	}

	public int waitFor() throws InterruptedException {
		return process.waitFor();
	}

	private static void redirectToStdout(OutputStream out, InputStream is)
			throws IOException {
		byte[] buffer = new byte[512];
		int read = 0;
		while ((read = is.read(buffer)) != -1) {
			out.write(buffer, 0, read);
			out.flush();
		}
	}

	public void dispose() throws OutprocessException {
		try {
			sendToStream.writeObject(null);
		} catch (IOException e) {
			throw new OutprocessException("Exception while disposing outprocess", e);
		}		
	}

}
