package es.optsicom.lib.util.outprocess;

import java.io.File;
import java.net.URL;

public class CodePathDetector {

	private static File CODE_PATH;

	public static File get() {

		String resource = "/"+CodePathDetector.class.getName().replace(".", "/") + ".class";
		
		if (CODE_PATH == null) {
			try {
				URL url = CodePathDetector.class.getResource(resource);
				
				File f = null;
				
				if (url.getProtocol().equals("file")) {
					f = new File(url.toURI());
					
					int numPackageLevels = CodePathDetector.class.getName().split("\\.").length;
					
					for(int i=0; i<numPackageLevels;i++) {
	                    f = f.getParentFile();
	                }				
	                CODE_PATH = f;
	            } else if (url.getProtocol().equals("jar")) {					
					String expected = "!/" + resource;					
					String s = url.toString();					
					s = s.substring(4);					
					s = s.substring(0, s.length() - expected.length()+1);					
					f = new File(new URL(s).toURI());					
				}				
				CODE_PATH = f;				
			} catch (Exception e) {
				throw new RuntimeException("Exception while obtaining jar path",e);
			}
		}
		
		return CODE_PATH;
	}

	public static void main(String[] args) {		
		System.out.println("Jar detector test: "+CodePathDetector.get());				
	}

}
