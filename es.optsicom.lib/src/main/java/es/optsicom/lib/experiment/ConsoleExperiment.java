package es.optsicom.lib.experiment;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Method;
import es.optsicom.lib.instancefile.InstanceFile;
import es.optsicom.lib.instancefile.InstanceLoader;
import es.optsicom.lib.util.RandomManager;

public class ConsoleExperiment {

	private Map<String, Method<?, ?>> methods = new HashMap<String, Method<?, ?>>();

	private String jarName;
	private String instanceFormatURL;
	private String webURL;
	private String problemName;
	private String solutionFormat;

	private InstanceLoader instanceLoader;

	public ConsoleExperiment(String jarName, String instanceFormatURL,
			String webURL, String problemName, InstanceLoader instanceLoader, String solutionFormat) {
		super();
		this.jarName = jarName;
		this.instanceFormatURL = instanceFormatURL;
		this.webURL = webURL;
		this.problemName = problemName;
		this.instanceLoader = instanceLoader;
		this.solutionFormat = solutionFormat;
	}

	public void putMethod(String name, Method<?, ?> method) {
		if (name.contains(" ")) {
			throw new InvalidParameterException("The name of the method \""
					+ name + "\" musn't contain spaces");
		}
		this.methods.put(name, method);
	}

	public void setJarName(String jarName) {
		this.jarName = jarName;
	}

	public void execMethodWithArgs(String[] args) {

		if (args.length == 0) {

			showUsage();

		} else if (args.length != 3 && args.length != 4) {

			System.out.println("Incorrect number of command line parameters.");
			System.out.println("Found " + args.length + " paramters: "
					+ Arrays.toString(args));
			System.out.println();

			showUsage();

		} else {

			String methodName = args[0];
			if (!methods.containsKey(methodName)) {
				System.out
						.println("COMMAND LINE PARAMETER ERROR\nThe first parameter \""
								+ args[0]
								+ "\" should be one of the methods: "
								+ getMethodNamesAsString() + ".");
				System.out.println();
				showUsage();
				System.exit(1);
			}

			File instanceFile = new File(args[1]);
			if (!instanceFile.exists()) {
				System.out
						.println("COMMAND LINE PARAMETER ERROR\nThe second parameter should be an instance file. The file \""
								+ args[1] + "\" does not exist.");
				System.out.println();
				showUsage();
				System.exit(1);
			}

			long timeLimitMillis = 0;
			try {
				timeLimitMillis = Long.parseLong(args[2]);
			} catch (NumberFormatException e) {
				System.out
						.println("COMMAND LINE PARAMETER ERROR\nThe third parameter \""
								+ args[2]
								+ "\" should be a number indicating the time limit in millis.");
				System.out.println();
				showUsage();
				System.exit(1);
			}

			boolean seedSpecified = false;
			long seed = 0;

			if (args.length == 4) {
				seedSpecified = true;
				try {
					seed = Long.parseLong(args[3]);
				} catch (NumberFormatException e) {
					System.out
							.println("COMMAND LINE PARAMETER ERROR\nThe fourth parameter \""
									+ args[2]
									+ "\" should be a number indicating the seed of the random number generator.");
					System.out.println();
					showUsage();
					System.exit(1);
				}
			}

			execMethod(methodName, instanceFile, timeLimitMillis,
					seedSpecified, seed);
		}
	}

	private void showUsage() {

		System.out
				.println("This program includes several algorithms to solve the problem \""
						+ problemName + "\"");
		System.out
				.println("You can find more information about this problem and the algorithms in "
						+ webURL + ".");
		System.out.println();
		System.out
				.println("Usage: java -jar "
						+ jarName
						+ " <method_name> <instance_file> <cpu_time_millis> [ <random_seed> ]");
		System.out.println();

		System.out.println("Where:");

		System.out
				.println("   <method_name> is one of the following method names: "
						+ getMethodNamesAsString());
		System.out.println();

		System.out
				.println("   <instance_file> is the name of a file containing instance data. The data must "
						+ "follow the format described in "
						+ instanceFormatURL
						+ ".");
		System.out.println();

		System.out
				.println("   <cpu_time_millis> is the execution CPU time in milliseconds.");
		System.out.println();

		System.out
				.println("   <random_seed> is an optional parameter to specify the seed used to initialize the number generator (class java.util.Random). "
						+ " If not specified, the program will be executed with the default seed offered by Random class.");
		System.out.println();
		System.out.println("The output of the program will be similar to:");
		System.out.println();
		System.out
				.println("Loading instance file \"Geo_n010_ds_01.txt\"...\n"
						+ "Instance file \"Geo_n010_ds_01.txt\" successfully loaded.\n"
						+ "Executing method \"SO\" with instance file \"Geo_n010_ds_01.txt\"\n"
						+ "Execution CPU time: 1000 millis\n"
						+ "Seed value: 1317916971787\n"
						+ "Method \"SO\" successfully executed\n"
						+ "Execution Time: 1002 millis\n"
						+ "Solution value: 3864.6891326904297\n"
						+ "Solution: [0, 1, 1, 1, 0, 0, 0, 1, 0, 0]");

		System.out.println();
		System.out
				.println("The solution is represented with the following format. "
						+ solutionFormat);
	}

	private String getMethodNamesAsString() {

		List<String> methodNames = new ArrayList<String>(methods.keySet());
		Collections.sort(methodNames);
		StringBuilder sb = new StringBuilder();
		for (String methodName : methodNames) {
			sb.append(methodName + " ");
		}
		return sb.toString();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void execMethod(String methodName, File instanceFile,
			long cpuTimeMillis, boolean seedSpecified, long seed) {

		Method method = methods.get(methodName);

		Instance instance = null;
		try {
			System.out.println("Loading instance file \""
					+ instanceFile.getName() + "\"...");
			instance = instanceLoader.loadInstance(new InstanceFile(null,
					instanceFile, null, null, null));
			System.out.println("Instance file \"" + instanceFile.getName()
					+ "\" successfully loaded.");
		} catch (IOException e) {
			System.out.println("INSTANCE LOADING ERROR");
			System.out.println("Error while loading instance file \""
					+ instanceFile + "\".");
			e.printStackTrace(System.out);
			System.exit(2);
		}

		method.setInstance(instance);

		System.out.println("Executing method \"" + methodName
				+ "\" with instance file \"" + instanceFile.getName() + "\"");

		System.out.println("Execution CPU time: " + cpuTimeMillis + " millis");

		if (seedSpecified) {
			RandomManager.setSeed(seed);
		} else {
			seed = System.currentTimeMillis();
		}
		System.out.println("Seed value: " + seed);

		long start = System.currentTimeMillis();
		try {
			ExecutionResult execResult = method.execute(cpuTimeMillis);

			long executionTime = System.currentTimeMillis() - start;

			System.out.println("Method \"" + methodName
					+ "\" successfully executed");

			System.out.format("Execution Time: %d millis\r\n", executionTime);
			System.out.println("Solution value: "
					+ execResult.getBestSolution().getWeight());
			System.out.println("Solution: "
					+ execResult.getBestSolution().toStringConsoleExperiment());
			
		} catch (Exception e) {
			System.out.println("EXECUTION ERROR");
			System.out.println("An error has ocurred when executing the method\n");
			e.printStackTrace(System.out);
			System.exit(3);
		}
	}
}
