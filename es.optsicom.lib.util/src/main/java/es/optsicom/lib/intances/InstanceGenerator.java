package es.optsicom.lib.intances;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class InstanceGenerator {

	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		EDPInstanceBuilder builder = new EDPInstanceBuilder();

		Range range = new Range(-10, 10);
		builder.addRange(range);
		InstanceWriter.write("testA.txt", builder.build(25));

		builder.removeRange(range);
		range = new Range(-10, -5);
		builder.addRange(range);
		range = new Range(5, 10);
		builder.addRange(range);
		InstanceWriter.write("testB.txt", builder.build(25));

		System.out.println("Done!");
	}
}
