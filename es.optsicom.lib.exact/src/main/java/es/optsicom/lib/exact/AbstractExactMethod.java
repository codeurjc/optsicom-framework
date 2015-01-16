/* ******************************************************************************
 *
 * This file is part of Optsicom
 *
 * License:
 *   EPL: http://www.eclipse.org/legal/epl-v10.html
 *   LGPL 3.0: http://www.gnu.org/licenses/lgpl-3.0-standalone.html
 *   See the LICENSE file in the project's top-level directory for details.
 *
 * **************************************************************************** */
package es.optsicom.lib.exact;

import es.optsicom.lib.AbstractMethod;
import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;
import es.optsicom.lib.approx.experiment.ApproxExecResult;
import es.optsicom.lib.experiment.ExecutionResult;
import es.optsicom.lib.expresults.model.DBProperties;
import es.optsicom.lib.expresults.model.MethodDescription;

public abstract class AbstractExactMethod<S extends Solution<I>, I extends Instance> extends AbstractMethod<S,I> implements ExactMethod<S,I> {

	private I instance;

	public ExactResult<S> execute(I instance) throws ExactException {
		return execute(instance, -1);
	}

	public abstract ExactResult<S> execute(I instance, long timeLimit);

	@Override
	public MethodDescription createMethodDescription() {
		return new MethodDescription(new DBProperties(this.getProperties()
				.getMap()));
	}

	@Override
	public void setInstance(I instance) {
		this.instance = instance;
	}

	@Override
	public I getInstance() {
		return instance;
	}

	@Override
	public void removeInstance() {
		instance = null;
	}

	@Override
	public ExecutionResult execute(long timeLimit) {
		return new ApproxExecResult(execute(instance, timeLimit).getSolution());
	}

	@Override
	public ExecutionResult execute() {
		return new ApproxExecResult(execute(instance).getSolution());
	}

}
