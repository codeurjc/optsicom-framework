package es.optsicom.lib.util.outprocess;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OutprocessWithOperationMain {

	private static final Logger logger = Logger
			.getLogger(OutprocessWithOperationMain.class.getName());

	public static void main(String[] args) throws Exception {

		ServerSocket serverSocket = new ServerSocket();
		serverSocket.bind(null);
		int localPort = serverSocket.getLocalPort();

		System.out.println(Integer.toString(localPort));

		Socket socket = serverSocket.accept();

		ObjectOutputStream sendToStream = new ObjectOutputStream(
				socket.getOutputStream());	
		
		ObjectInputStream receiveFromStream = new ObjectInputStream(
				socket.getInputStream());

		Object remoteObject = receiveFromStream.readObject();

		while (true) {

			OperationPacket operationPacket = (OperationPacket) receiveFromStream
					.readObject();
			
			if (operationPacket != null) {
				Object result = execOperation(remoteObject,
						operationPacket.getOperationName(),
						operationPacket.getParams());
				
				sendToStream.writeObject(result);
				sendToStream.reset();
			} else {
				break;
			}
		}

		logger.log(Level.INFO, "Finish outprocess");
	}

	private static Object execOperation(Object remoteObject,
			String operationName, Object[] params)
			throws NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException {

		if (remoteObject instanceof OperationProcessor) {

			OperationProcessor opProcessor = (OperationProcessor) remoteObject;
			
			try {
				return new Result(opProcessor.execOperation(operationName, params));
			} catch (Exception e) {
				return new Result(e.getCause());
			}
			
		} else {

			Class[] paramClasses = new Class[params.length];
			for (int i = 0; i < params.length; i++) {
				paramClasses[i] = (params[i] != null) ? params[i].getClass()
						: null;
			}

			Method method = null;
			for (Method m : remoteObject.getClass().getMethods()) {
				if (m.getName().equals(operationName)) {
					method = m;
					break;
				}
			}

			if (method == null) {
				throw new NoSuchMethodException("Method " + operationName
						+ " not fount in class "
						+ remoteObject.getClass().getName());
			}

			try {
				return new Result(method.invoke(remoteObject, params));
			} catch (InvocationTargetException e) {
				return new Result(e.getCause());
			}
		}
	}

}
