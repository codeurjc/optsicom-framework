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
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;
import ilog.cplex.IloCplex.DoubleParam;
import ilog.cplex.IloCplex.IntParam;

import java.util.ArrayList;
import java.util.List;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;
import es.optsicom.lib.exact.ExactException;
import es.optsicom.lib.exact.RelaxationResult;
import es.optsicom.lib.exact.RelaxationResult.Mode;
import es.optsicom.lib.util.description.Descriptive;
import es.optsicom.lib.util.description.DescriptiveHelper;
import es.optsicom.lib.util.description.Properties;

public abstract class CplexFormRelaxation<S extends Solution<I>, I extends Instance> implements Descriptive {

	protected IloCplex cplex;

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

	public RelaxationResult<S> execute(I instance, long timeLimit) {

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
				// Limita el tiempo de ejecución, en segundos. No obstante, es poco probable que
				// se llegue al límite ya que se trata de la resolución de una relajación lineal
				// del problema que suele ser bastante rápida.
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

			List<IloNumVar> vars = modelProblem(instance);

//				CurrentExperiment.addEventPack(new Event(Event.FINISH_TIME_APPROX_METHOD), 
//						new Event(Event.OBJ_VALUE_EVENT, heuristicSolution.getWeight()),
//						new Event(Event.SOLUTION_EVENT, heuristicSolution.getInfoToSave()));
		
			
			long startTime = System.currentTimeMillis();
			// Iniciar la resolución del problema
			boolean solutionFound = cplex.solve();
			long execMillis = System.currentTimeMillis() - startTime;

			List<String> varNames = getVarNames(vars);
			
			RelaxationResult<S> result = null;
			Mode mode = null;
			boolean abortedByTime = false;
			
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

				if (cplexStatus == IloCplex.CplexStatus.Optimal) {

					mode = RelaxationResult.Mode.OPTIMAL;
					
				} else if (cplexStatus == IloCplex.CplexStatus.AbortTimeLim) {

					mode = RelaxationResult.Mode.ABORTED_BY_TIME;
					abortedByTime = true;

				} else {
					
					mode = RelaxationResult.Mode.UNKNOWN;
					
				}

			} else {
				
				mode = RelaxationResult.Mode.SOLUTION_NOT_FOUND;
								
			}
			
			result = new RelaxationResult<S>(mode, execMillis, abortedByTime, cplex.getBestObjValue(), cplex.getObjValue(), varNames, cplex.getValues(vars.toArray(new IloNumVar[0])));
			
			cplex.end();

			return result;

		} catch (Exception e) {
			throw new ExactException(e);
		}

	}

	private List<String> getVarNames(List<IloNumVar> vars) {
		List<String> varNames = new ArrayList<String>();
		for(IloNumVar var : vars){
			varNames.add(var.getName());
		}
		return varNames;
	}

	protected abstract List<IloNumVar> modelProblem(I instance) throws IloException;	

	@Override
	public Properties getProperties() {
		return DescriptiveHelper.createProperties(this);
	}
}
