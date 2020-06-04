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
package es.optsicom.lib.exact.cplex;

import ilog.concert.IloException;


import ilog.concert.IloIntVar;
import ilog.concert.IloNumExpr;
import ilog.cplex.IloCplex;
import ilog.cplex.IloCplex.DoubleParam;
import ilog.cplex.IloCplex.IntParam;

import java.util.ArrayList;
import java.util.List;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;
import es.optsicom.lib.approx.ApproxMethod;
import es.optsicom.lib.exact.AbstractExactMethod;
import es.optsicom.lib.exact.ExactException;
import es.optsicom.lib.exact.ExactResult;
import es.optsicom.lib.experiment.CurrentExperiment;
import es.optsicom.lib.expresults.model.Event;
import es.optsicom.lib.util.Id;
import es.optsicom.lib.util.Log;

public abstract class CplexFormulation<S extends Solution<I>, I extends Instance> extends AbstractExactMethod<S, I> {

	protected IloCplex cplex;
	private ApproxMethod<S, I> approxMethod;

	protected IloNumExpr createSum(List<IloNumExpr> prods) throws IloException {
		List<IloNumExpr> result = prods;
		do {
			result = createSumList(result);
		} while (result.size() != 1);
		return result.get(0);
	}

	private List<IloNumExpr> createSumList(List<IloNumExpr> prods) throws IloException {
		List<IloNumExpr> result = new ArrayList<IloNumExpr>();
		for (int i = 0; i < prods.size(); i = i + 8) {
			int numElems = Math.min(i + 8, prods.size());
			result.add(cplexSum(prods.subList(i, numElems)));
		}
		return result;
	}

	private IloNumExpr cplexSum(List<IloNumExpr> prods) throws IloException {
		switch (prods.size()) {
		case 1:
			return prods.get(0);
		case 2:
			return cplex.sum(prods.get(0), prods.get(1));
		case 3:
			return cplex.sum(prods.get(0), prods.get(1), prods.get(2));
		case 4:
			return cplex.sum(prods.get(0), prods.get(1), prods.get(2), prods.get(3));
		case 5:
			return cplex.sum(prods.get(0), prods.get(1), prods.get(2), prods.get(3), prods.get(4));
		case 6:
			return cplex.sum(prods.get(0), prods.get(1), prods.get(2), prods.get(3), prods.get(4), prods.get(5));
		case 7:
			return cplex.sum(prods.get(0), prods.get(1), prods.get(2), prods.get(3), prods.get(4), prods.get(5), prods
			        .get(6));
		case 8:
			return cplex.sum(prods.get(0), prods.get(1), prods.get(2), prods.get(3), prods.get(4), prods.get(5), prods
			        .get(6), prods.get(7));
		}
		throw new Error("Assert Error: prods can't have more than 8 elements");
	}

	@Override
	public ExactResult<S> execute(I instance, long timeLimit) {

		try {
			
			// Create the modeler/solver object
			cplex = new IloCplex();

			// Parámetros del CPLEX
			// Configura a CPLEX para que utilice el disco en caso de no tener
			// suficiente memoria para resolver el problema
			cplex.setParam(IntParam.NodeFileInd, 2);

			// Limita el tamaño de disco que puede utilizar, en megabytes.
			cplex.setParam(DoubleParam.TreLim, 8000);

			if (timeLimit != -1) {
				// Limita el tiempo de ejecución, en segundos.
				cplex.setParam(DoubleParam.TiLim, ((double) timeLimit) / 1000);
			}

			// Establece la tolerancia en el gap relativo. Si este valor
			// es mayor que 0, la resolución se detendrá cuando se haya obtenido
			// una solución cuyo gap relativo sea menor o igual que este valor.
			cplex.setParam(DoubleParam.EpGap, 0);

			// Establece la tolerancia en el gap absoluto. Si este valor
			// es mayor que 0, la resolución se detendrá cuando se haya obtenido
			// una solución cuyo gap absoluto sea menor o igual que este valor.
			cplex.setParam(DoubleParam.EpAGap, 0);

			// Inactiva la salida estándar para envío de mensajes
			cplex.setOut(null);

			IloIntVar[] x = modelProblem(instance);

			S heuristicSolution = null;
//			if(approxMethod != null){
//				approxMethod.setInstance(instance);
//				heuristicSolution = approxMethod.calculateSolution();
//				System.out.println("Heuristic solution: "+heuristicSolution);
//				cplex.setParam(IloCplex.DoubleParam.CutLo, heuristicSolution.getWeight());
//			}
			
			if(approxMethod != null){
				CurrentExperiment.addEvent(Event.START_TIME_APPROX_METHOD);
				approxMethod.setInstance(instance);
				heuristicSolution = approxMethod.calculateSolution();
				CurrentExperiment.addEventPack(new Event(Event.FINISH_TIME_APPROX_METHOD), 
						new Event(Event.OBJ_VALUE_EVENT, heuristicSolution.getWeight()),
						new Event(Event.SOLUTION_EVENT, heuristicSolution.getInfoToSave()));
				System.out.println("Heuristic solution: "+heuristicSolution);
				cplex.setParam(IloCplex.DoubleParam.CutLo, heuristicSolution.getWeight());
			}
			
			
			long startTime = System.currentTimeMillis();
			// Iniciar la resolución del problema
			boolean solutionFound = cplex.solve();
			long execMillis = System.currentTimeMillis() - startTime;

			ExactResult<S> result = null;

			if (solutionFound) {

				// Devuelve el estado de CPLEX al finalizar la resolución del
				// problema. Una lista
				// detallada de los posibles valores, se puede consultar en:
				// http://web.njit.edu/all_topics/Prog_Lang_Docs/cplex80/doc/refman/html/appendixB.html
				// Los valores interesantes son:
				// * IloCplex.CplexStatus.Optimal: Finalizado con solución es óptima
				// * IloCplex.CplexStatus.OptimalTol: Finalizado con solución óptima dentro del umbral
				//   especificado con las opciones EpGap y EpAGap.
				// * IloCplex.CplexStatus.TimeLimFeas: Finalizado con límite de
				//   tiempo. Se ha encontrado una solución al problema
				// * IloCplex.CplexStatus.TimeLimInfeas: Finalizado con límite de tiempo. No se ha
				//   encontrado una solución al problema
				IloCplex.CplexStatus cplexStatus = cplex.getCplexStatus();

				long numNodos = cplex.getNnodes();

				if (cplexStatus == IloCplex.CplexStatus.Optimal) {

					S s = createSolutionFromVars(cplex.getValues(x), instance);

					result = new ExactResult<S>(execMillis, s, numNodos);

					System.out.println("CPLEX Error: " + (s.getWeight() - cplex.getObjValue()));

				} else if (cplexStatus == IloCplex.CplexStatus.AbortTimeLim) {

					S s = createSolutionFromVars(cplex.getValues(x), instance);

					result = new ExactResult<S>(execMillis, s, cplex.getBestObjValue(), s.getWeight(), numNodos);

					System.out.println("CPLEX Error: " + (s.getWeight() - cplex.getObjValue()));

				} else {
					Log.debug("Resultado de Cplex del problema " + instance.getId() + " es " + cplexStatus
					        + ". Es irreconocible");
				}

			} else {
				if(heuristicSolution != null){
					System.out.println("Cplex solution not found. Using heuristic solution.");
					//Antes se hacía esta verificación porque se suponía que si no se encuentra 
					//una solución, no tenía sentido hablar de cplex.getBestObjValue(), ya que es
					//el peso de la mejor solución encontrada. Pero parece que en ciertas ocasiones,
					//aquí puede haber un valor, entendemos que quizás de la solución a la relajación
					//del problema.
					//if(cplex.getBestObjValue() == -1.0E75){
					
					System.out.println("cplex.getBestObjValue()="+cplex.getBestObjValue());
					
					if(cplex.getCplexStatus() == IloCplex.CplexStatus.AbortTimeLim){
						System.out.println("Aborted by time and no solution has been found by Cplex");
						result = new ExactResult<S>(ExactResult.ExactResultMode.ABORTED_BY_TIME_WITH_HEURISTIC_SOLUTION_WITHOUT_CPLEX_SOLUTION,execMillis, heuristicSolution, cplex.getBestObjValue(), heuristicSolution.getWeight(), cplex.getNnodes());
					} else {
						System.out.println("Using heuristic solution as optimal solution");
						result = new ExactResult<S>(execMillis, heuristicSolution, -1);
					}
					
				} else {
					System.out.println("Cplex solution not found.");
					result = new ExactResult<S>(execMillis);	
				}
			}
			cplex.end();

			return result;

		} catch (Exception e) {
			throw new ExactException(e);
		}

	}

	protected abstract S createSolutionFromVars(double[] vars, I instance);

	protected int[] createTrueVarsArray(double[] vars) {

		int numTrue = 0;
		for (double element : vars) {
			if (element > 0.5) {
				numTrue++;
			}
		}

		// Se obtiene el valor de las variables como un array de double
		int[] trueVars = new int[numTrue];
		int index = 0;
		for (int i = 0; i < vars.length; i++) {
			if (vars[i] > 0.5) {
				trueVars[index] = i;
				index++;
			}
		}

		return trueVars;
	}

	protected boolean[] createBooleanArray(double[] vars) {

		boolean[] booleanArray = new boolean[vars.length];
		for (int i = 0; i < vars.length; i++) {
			booleanArray[i] = (vars[i] > 0.5);
		}

		return booleanArray;
	}

	protected abstract IloIntVar[] modelProblem(I instance) throws IloException;
	
	public void setApproxMethod(ApproxMethod<S,I> approxMethod){
		this.approxMethod = approxMethod;
	}
	
	@Id
	public ApproxMethod<S, I> getApproxMethod() {
		return approxMethod;
	}

}
