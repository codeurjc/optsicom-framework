package es.optsicom.lib.approx.algorithm;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;
import es.optsicom.lib.util.description.Descriptive;

public interface Shaker<S extends Solution<I>, I extends Instance> extends Descriptive {

	public void shake(S solution);

}
