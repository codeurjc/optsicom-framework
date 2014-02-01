package es.optsicom.lib.intances;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class InstanceGenerator {

	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		InstanceWriter writer = new InstanceWriter();

		EDPInstanceBuilder builder = new EDPInstanceBuilder();
		Range range = new Range(-10, 10);
		builder.addRange(range);

		EDPInstanceBuilder builder2 = new EDPInstanceBuilder();
		Range range1 = new Range(-10, -5);
		Range range2 = new Range(5, 10);
		builder2.addRange(range1);
		builder2.addRange(range2);

		int[] dimensions = { 20, 25, 30, 35, 150, 500 };
		for (int dimension : dimensions) {
			for (int i = 0; i < 10; i++) {
				writer.write("typeI", "typeI_" + dimension + "_" + i, builder.build(dimension));
				writer.write("typeII", "typeII_" + dimension + "_" + i, builder2.build(dimension));
			}
		}

		System.out.println("Done!");
	}
}
