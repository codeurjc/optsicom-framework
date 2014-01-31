package es.optsicom.lib.intances;

import java.util.ArrayList;
import java.util.List;

public class EDPInstanceBuilder implements InstanceBuilder {

	private final List<Range> ranges = new ArrayList<Range>();

	public EDPInstanceBuilder() {
	}

	@Override
	public double[][] build(int dimension) {

		if (dimension < 0) {
			throw new IllegalArgumentException("Dimension must be positive");
		}

		if (ranges.isEmpty()) {
			throw new UnsupportedOperationException("Cannot generate a EDP intance without a range");
		}

		double[][] result = new double[dimension][dimension];

		for (int i = 0; i < dimension - 1; i++) {
			result[i][i] = 0;
			for (int j = i + 1; j < dimension; j++) {
				Range range = ranges.get((int) (Math.random() * (ranges.size())));
				result[i][j] = range.getMin() + (int) (Math.random() * ((range.getMax() - range.getMin()) + 1));
				result[j][i] = result[i][j];
			}
		}

		return result;
	}

	public void addRange(Range range) {
		ranges.add(range);
	}

	public void removeRange(Range range) {
		ranges.remove(range);
	}

}
