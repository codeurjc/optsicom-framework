package es.optsicom.lib.exact.gurobi;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;
import es.optsicom.lib.exact.AbstractExactMethod;
import es.optsicom.lib.exact.ExactException;
import es.optsicom.lib.exact.ExactResult;
import gurobi.GRB;
import gurobi.GRBEnv;
import gurobi.GRBException;
import gurobi.GRBModel;
import gurobi.GRBVar;

/**
 * Gurobi MIP Solver
 *
 * Execute with -Djava.library.path=<path to gurobi lib folder>
 * @author patxi
 *
 * @param <S>
 * @param <I>
 */
public abstract class GurobiFormulation<S extends Solution<I>, I extends Instance> extends AbstractExactMethod<S, I> {

	@Override
	public ExactResult<S> execute(I instance, long timeLimit) {

		try {
			GRBEnv env = new GRBEnv(instance.getInstanceFile().getFileName() + ".log");

			GRBModel model = modelProblem(env, instance);

			if(timeLimit != -1) {
				// We set the timelimit in seconds
				model.getEnv().set(GRB.DoubleParam.TimeLimit, timeLimit / 1000.0);
			}

			long startTime = System.currentTimeMillis();
			model.optimize();
			long execMillis = System.currentTimeMillis() - startTime;

			int optimstatus = model.get(GRB.IntAttr.Status);

			ExactResult<S> result = null;

			if(optimstatus == GRB.OPTIMAL) {
				S solution = createSolutionFromVars(model.getVars(), instance);
				result = new ExactResult<S>(execMillis, solution, (long) model.get(GRB.DoubleAttr.NodeCount));
				System.out.println("Gurobi Error: " + (solution.getWeight() - model.get(GRB.DoubleAttr.ObjVal)));
			} else if(optimstatus == GRB.TIME_LIMIT) {
				S solution = createSolutionFromVars(model.getVars(), instance);
				result = new ExactResult<S>(execMillis, solution, model.get(GRB.DoubleAttr.ObjVal), solution.getWeight(), (long) model.get(GRB.DoubleAttr.NodeCount));
				System.out.println("Gurobi Error: " + (solution.getWeight() - model.get(GRB.DoubleAttr.ObjVal)));

			} else{
				System.out.println("El resultado de Gurobi del problema no se conoce: " + optimstatus);
			}

			return result;

		} catch (GRBException e) {
			throw new ExactException(e);
		}

	}

	public abstract GRBModel modelProblem(GRBEnv env, I instance) throws GRBException;

	public abstract S createSolutionFromVars(GRBVar[] vars, I instance) throws GRBException;
}
