package es.optsicom.lib.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class Loader {
	
	private static Map<String, Map<String,InstanceExecution>> WITH_INF_300000    ;
	private static Map<String, Map<String,InstanceExecution>> WITH_INF_60000     ;
	private static Map<String, Map<String,InstanceExecution>> WITHOUT_INF_300000 ;
	private static Map<String, Map<String,InstanceExecution>> WITHOUT_INF_60000  ;
	private static Map<String, Map<String,InstanceExecution>> WITH_MEMORY_300000 ;
	private static Map<String, Map<String,InstanceExecution>> WITH_MEMORY_60000  ;

	public static void main(String[] args) throws IOException {

		String baseDir = "X:\\Docencia\\PFCs\\Desarrollo\\Metaheuristicas Paralelas para el MDP - Juan Manuel Barrio y Jorge Sancho\\RESULTADOS_Finales_2\\";
		
		convertFiles(baseDir+"resultados\\distribuido", baseDir+"resultados_procesados\\distribuido\\g", "MDG-c");
		convertFiles(baseDir+"resultados\\distribuido", baseDir+"resultados_procesados\\distribuido\\p", "MDG-b");
		convertFiles(baseDir+"resultados\\paralelo", baseDir+"resultados_procesados\\paralelo\\g", "MDG-c");
		convertFiles(baseDir+"resultados\\paralelo", baseDir+"resultados_procesados\\paralelo\\p", "MDG-b");		
		
	}

	private static void convertFiles(String src, String dst, String instanceType)
			throws IOException {
		initMaps();
		File resultsDir = new File(src);		
		processFiles(resultsDir);
		createResultsFiles(new File(dst),instanceType);
	}

	private static void initMaps() {
		WITH_INF_300000 = new HashMap<String, Map<String,InstanceExecution>>();
		WITH_INF_60000 = new HashMap<String, Map<String,InstanceExecution>>();
		WITHOUT_INF_300000 = new HashMap<String, Map<String,InstanceExecution>>();
		WITHOUT_INF_60000 = new HashMap<String, Map<String,InstanceExecution>>();
		WITH_MEMORY_300000 = new HashMap<String, Map<String,InstanceExecution>>();
		WITH_MEMORY_60000 = new HashMap<String, Map<String,InstanceExecution>>();		
	}

	private static void createResultsFiles(File dir, String instanceType) throws IOException {
		
		createResultsFiles(dir,"WITH_INF",300000,instanceType, WITH_INF_300000);
		createResultsFiles(dir,"WITH_INF",60000,instanceType,WITH_INF_60000);
		createResultsFiles(dir,"WITHOUT_INF",300000,instanceType,WITHOUT_INF_300000);
		createResultsFiles(dir,"WITHOUT_INF",60000,instanceType,WITHOUT_INF_60000);
		createResultsFiles(dir,"WITH_MEMORY",300000,instanceType,WITH_MEMORY_300000);
		createResultsFiles(dir,"WITH_MEMORY",60000,instanceType,WITH_MEMORY_60000);
		
	}

	private static void createResultsFiles(File dir, String algType, long timeLimit, String instanceType,
			Map<String, Map<String, InstanceExecution>> executions) throws IOException {
		
		File resultsDir = new File(dir, algType+"_"+timeLimit);
		if(!resultsDir.exists()){
			resultsDir.mkdirs();
		}
		
		System.out.println("AlgType: "+algType+" TimeLimit: "+timeLimit);
		
		for(Map.Entry<String, Map<String,InstanceExecution>> e : executions.entrySet()){
			
			File resultsFile = new File(resultsDir, e.getKey()+".txt");
			PrintWriter out = new PrintWriter(new FileWriter(resultsFile));
						
			System.out.println(e.getKey());
			out.println(e.getKey());
			
			List<String> sortedInstNames = new ArrayList<String>(e.getValue().keySet());
			Collections.sort(sortedInstNames);
			
			for(String instName : sortedInstNames){				
				InstanceExecution ie = e.getValue().get(instName);				
				System.out.println(ie);
				if(ie.getInstanceName().contains(instanceType)){
					out.println(ie.getInstanceName()+"\t"+ie.getValue()+"\t"+ie.getExecTime());
				}
			}			
			
			out.close();
		}
		
		System.out.println();
	}

	private static void processFiles(File resultsDir) {
		listFilesRecursive(new ArrayList<String>(), resultsDir);
	}

	private static void listFilesRecursive(List<String> prefix, File resultsDir) {

		// System.out.println(prefix);

		prefix.add(resultsDir.getName());

		File[] files = resultsDir.listFiles();

		if (files != null) {
			for (File file : files) {

				if (file.isDirectory()) {
					listFilesRecursive(prefix, file);
				} else {
					//System.out.println(prefix + ":" + file.getName());
					processFile(file);
				}
			}
		}

		prefix.remove(prefix.size() - 1);

	}

	private static void processFile(File file) {
		try {

			if (file.getName().endsWith(".log")) {

				BufferedReader br = new BufferedReader(new InputStreamReader(
						new FileInputStream(file)));
				String info = br.readLine();

				String algType = processAlgType(file.getName());
				String timeLimit = processTimeLimit(file.getName());
				String configuration = file.getParentFile().getParentFile().getName();

//				System.out.println("    AlgType:" + algType + "   TimeLimit:"
//						+ timeLimit+"   Configuration: "+configuration);

				Map<String, Map<String,InstanceExecution>> currentMap = getMap(algType, timeLimit);
				
				Map<String,InstanceExecution> execs = currentMap.get(configuration);
				if(execs == null){
					execs = new HashMap<String, InstanceExecution>();
					currentMap.put(configuration, execs);
				}
				
				String line;
				while ((line = br.readLine()) != null) {
					if (line.startsWith("Instance:")) {
						StringTokenizer st = new StringTokenizer(line, "\t");
						String instanceName = st.nextToken().substring(
								"Instance: ".length());
						double value = Double.parseDouble(st.nextToken());
						long time = (long) Double.parseDouble(st.nextToken());

						instanceName = processInstanceName(instanceName);

						InstanceExecution instanceExecution = new InstanceExecution(instanceName, value, time);
						//System.out.println(instanceExecution.toString());

						execs.put(instanceName, instanceExecution);
					}
				}

			}
		} catch (Exception e) {
			System.err.println("Exception processing file "
					+ file.getAbsolutePath() + ". " + e.getMessage());
		}
	}

	private static Map<String, Map<String, InstanceExecution>> getMap(
			String algType, String timeLimit) {
		
		if(algType.equals("WITH_INF") && timeLimit.equals("60000")){
			return WITH_INF_60000;
		} else if(algType.equals("WITH_INF") && timeLimit.equals("300000")){
			return WITH_INF_300000;
		} else if(algType.equals("WITHOUT_INF") && timeLimit.equals("60000")){
			return WITHOUT_INF_60000;
		} else if(algType.equals("WITHOUT_INF") && timeLimit.equals("300000")){
			return WITHOUT_INF_300000;
		} else if(algType.equals("WITH_MEMORY") && timeLimit.equals("60000")){
			return WITH_MEMORY_60000;
		} else if(algType.equals("WITH_MEMORY") && timeLimit.equals("300000")){
			return WITH_MEMORY_300000;
		} else {
			return null;
		}
	}

	private static String processTimeLimit(String info) {
		String[] timeLimits = { "300000", "60000" };
		for (String st : timeLimits) {
			if (info.contains(st)) {
				return st;
			}
		}
		System.err.println("Info \"" + info + "\" does not contain any of "
				+ Arrays.toString(timeLimits));
		return null;
	}

	private static String processAlgType(String info) {
		String[] algTypes = { "WITHOUT_INF", "WITH_INF", "WITH_MEMORY" };
		for (String st : algTypes) {
			if (info.contains(st)) {
				return st;
			}
		}
		System.err.println("Info \"" + info + "\" does not contain any of "
				+ Arrays.toString(algTypes));
		return null;
	}

	private static String processInstanceName(String instanceName) {

		String[] instanceNames = { "MDG-b_1_n500_m50", "MDG-b_2_n500_m50",
				"MDG-b_3_n500_m50", "MDG-b_4_n500_m50", "MDG-b_5_n500_m50",
				"MDG-c_1_n3000_m300", "MDG-c_2_n3000_m300",
				"MDG-c_3_n3000_m300", "MDG-c_4_n3000_m300",
				"MDG-c_5_n3000_m300" };

		for (String name : instanceNames) {
			if (instanceName.startsWith(name)) {
				return name;
			}
		}

		System.err
				.println("Nombre de instancia irreconocible: " + instanceName);
		return null;
	}

}
