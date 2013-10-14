package es.optsicom.lib.tool;

import es.optsicom.lib.simpletool.SimpleTool;
import es.optsicom.lib.simpletool.SimpleTool2;

public class SimpleToolTest {

	public static void main(String[] args) {
		
		System.out.println("Distribuido Pequeñas");
		System.out.println();
		
//		simpleTool("X:\\Docencia\\PFCs\\Desarrollo\\Metaheuristicas Paralelas para el MDP - Juan Manuel Barrio y Jorge Sancho\\RESULTADOS_Finales_2\\resultados_procesados\\distribuido\\p\\WITH_INF_60000","max");
//		simpleTool("X:\\Docencia\\PFCs\\Desarrollo\\Metaheuristicas Paralelas para el MDP - Juan Manuel Barrio y Jorge Sancho\\RESULTADOS_Finales_2\\resultados_procesados\\distribuido\\p\\WITH_INF_300000","max");
//		simpleTool("X:\\Docencia\\PFCs\\Desarrollo\\Metaheuristicas Paralelas para el MDP - Juan Manuel Barrio y Jorge Sancho\\RESULTADOS_Finales_2\\resultados_procesados\\distribuido\\p\\WITH_MEMORY_60000","max");
//		simpleTool("X:\\Docencia\\PFCs\\Desarrollo\\Metaheuristicas Paralelas para el MDP - Juan Manuel Barrio y Jorge Sancho\\RESULTADOS_Finales_2\\resultados_procesados\\distribuido\\p\\WITH_MEMORY_300000","max");
//		simpleTool("X:\\Docencia\\PFCs\\Desarrollo\\Metaheuristicas Paralelas para el MDP - Juan Manuel Barrio y Jorge Sancho\\RESULTADOS_Finales_2\\resultados_procesados\\distribuido\\p\\WITHOUT_INF_60000","max");
		simpleTool("X:\\Docencia\\PFCs\\Desarrollo\\Metaheuristicas Paralelas para el MDP - Juan Manuel Barrio y Jorge Sancho\\RESULTADOS_Finales_2\\resultados_procesados\\distribuido\\p\\WITHOUT_INF_300000","max");
		
		System.out.println();
		System.out.println("Distribuido Grandes");
		System.out.println();
		
//		simpleTool("X:\\Docencia\\PFCs\\Desarrollo\\Metaheuristicas Paralelas para el MDP - Juan Manuel Barrio y Jorge Sancho\\RESULTADOS_Finales_2\\resultados_procesados\\distribuido\\g\\WITH_INF_60000","max");
//		simpleTool("X:\\Docencia\\PFCs\\Desarrollo\\Metaheuristicas Paralelas para el MDP - Juan Manuel Barrio y Jorge Sancho\\RESULTADOS_Finales_2\\resultados_procesados\\distribuido\\g\\WITH_INF_300000","max");
//		simpleTool("X:\\Docencia\\PFCs\\Desarrollo\\Metaheuristicas Paralelas para el MDP - Juan Manuel Barrio y Jorge Sancho\\RESULTADOS_Finales_2\\resultados_procesados\\distribuido\\g\\WITH_MEMORY_60000","max");
//		simpleTool("X:\\Docencia\\PFCs\\Desarrollo\\Metaheuristicas Paralelas para el MDP - Juan Manuel Barrio y Jorge Sancho\\RESULTADOS_Finales_2\\resultados_procesados\\distribuido\\g\\WITH_MEMORY_300000","max");
//		simpleTool("X:\\Docencia\\PFCs\\Desarrollo\\Metaheuristicas Paralelas para el MDP - Juan Manuel Barrio y Jorge Sancho\\RESULTADOS_Finales_2\\resultados_procesados\\distribuido\\g\\WITHOUT_INF_60000","max");
//		simpleTool("X:\\Docencia\\PFCs\\Desarrollo\\Metaheuristicas Paralelas para el MDP - Juan Manuel Barrio y Jorge Sancho\\RESULTADOS_Finales_2\\resultados_procesados\\distribuido\\g\\WITHOUT_INF_300000","max");
//		
		System.out.println();
		System.out.println("Paralelo Pequeñas");
		System.out.println();
		
//		simpleTool("X:\\Docencia\\PFCs\\Desarrollo\\Metaheuristicas Paralelas para el MDP - Juan Manuel Barrio y Jorge Sancho\\RESULTADOS_Finales_2\\resultados_procesados\\paralelo\\p\\WITH_INF_60000","max");
//		simpleTool("X:\\Docencia\\PFCs\\Desarrollo\\Metaheuristicas Paralelas para el MDP - Juan Manuel Barrio y Jorge Sancho\\RESULTADOS_Finales_2\\resultados_procesados\\paralelo\\p\\WITH_INF_300000","max");
//		simpleTool("X:\\Docencia\\PFCs\\Desarrollo\\Metaheuristicas Paralelas para el MDP - Juan Manuel Barrio y Jorge Sancho\\RESULTADOS_Finales_2\\resultados_procesados\\paralelo\\p\\WITH_MEMORY_60000","max");
		simpleTool("X:\\Docencia\\PFCs\\Desarrollo\\Metaheuristicas Paralelas para el MDP - Juan Manuel Barrio y Jorge Sancho\\RESULTADOS_Finales_2\\resultados_procesados\\paralelo\\p\\WITH_MEMORY_300000","max");
//		simpleTool("X:\\Docencia\\PFCs\\Desarrollo\\Metaheuristicas Paralelas para el MDP - Juan Manuel Barrio y Jorge Sancho\\RESULTADOS_Finales_2\\resultados_procesados\\paralelo\\p\\WITHOUT_INF_60000","max");
//		simpleTool("X:\\Docencia\\PFCs\\Desarrollo\\Metaheuristicas Paralelas para el MDP - Juan Manuel Barrio y Jorge Sancho\\RESULTADOS_Finales_2\\resultados_procesados\\paralelo\\p\\WITHOUT_INF_300000","max");
		
		System.out.println();
		System.out.println("Paralelo Grandes");
		System.out.println();
		
//		simpleTool("X:\\Docencia\\PFCs\\Desarrollo\\Metaheuristicas Paralelas para el MDP - Juan Manuel Barrio y Jorge Sancho\\RESULTADOS_Finales_2\\resultados_procesados\\paralelo\\g\\WITH_INF_60000","max");
//		simpleTool("X:\\Docencia\\PFCs\\Desarrollo\\Metaheuristicas Paralelas para el MDP - Juan Manuel Barrio y Jorge Sancho\\RESULTADOS_Finales_2\\resultados_procesados\\paralelo\\g\\WITH_INF_300000","max");
//		simpleTool("X:\\Docencia\\PFCs\\Desarrollo\\Metaheuristicas Paralelas para el MDP - Juan Manuel Barrio y Jorge Sancho\\RESULTADOS_Finales_2\\resultados_procesados\\paralelo\\g\\WITH_MEMORY_60000","max");
//		simpleTool("X:\\Docencia\\PFCs\\Desarrollo\\Metaheuristicas Paralelas para el MDP - Juan Manuel Barrio y Jorge Sancho\\RESULTADOS_Finales_2\\resultados_procesados\\paralelo\\g\\WITH_MEMORY_300000","max");
//		simpleTool("X:\\Docencia\\PFCs\\Desarrollo\\Metaheuristicas Paralelas para el MDP - Juan Manuel Barrio y Jorge Sancho\\RESULTADOS_Finales_2\\resultados_procesados\\paralelo\\g\\WITHOUT_INF_60000","max");
//		simpleTool("X:\\Docencia\\PFCs\\Desarrollo\\Metaheuristicas Paralelas para el MDP - Juan Manuel Barrio y Jorge Sancho\\RESULTADOS_Finales_2\\resultados_procesados\\paralelo\\g\\WITHOUT_INF_300000","max");
	}
	
	public static void simpleTool(String folder, String mode){
		System.out.println(folder);
		try {
			SimpleTool2.main(new String[]{folder,mode});
		} catch (Exception e){
			System.out.println("Exception:"+e);
			e.printStackTrace();
		}
	}
	
}
