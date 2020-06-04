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
package es.optsicom.lib.analyzer.tablecreator;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import es.optsicom.lib.analyzer.report.table.NumericFormat;
import es.optsicom.lib.analyzer.tablecreator.atttable.Attribute;
import es.optsicom.lib.analyzer.tablecreator.atttable.AttributedTable;
import es.optsicom.lib.analyzer.tablecreator.atttable.TableValue;
import es.optsicom.lib.analyzer.tablecreator.group.InstanceGroupMaker;
import es.optsicom.lib.analyzer.tablecreator.group.InstancesGroup;
import es.optsicom.lib.analyzer.tablecreator.pr.RawProcessor;
import es.optsicom.lib.analyzer.tablecreator.statisticcalc.RelativeValueProvider;
import es.optsicom.lib.analyzer.tablecreator.statisticcalc.RelativizerStatisticCalc;
import es.optsicom.lib.analyzer.tablecreator.statisticcalc.StatisticCalc;
import es.optsicom.lib.expresults.manager.ExperimentManager;
import es.optsicom.lib.expresults.model.Execution;
import es.optsicom.lib.expresults.model.InstanceDescription;
import es.optsicom.lib.expresults.model.MethodDescription;
import es.optsicom.lib.util.ArraysUtil;
import es.optsicom.lib.util.BestMode;
import es.optsicom.lib.util.CollectionsUtil;
import es.optsicom.lib.util.MathUtil;
import es.optsicom.lib.util.RelativizeMode;
import es.optsicom.lib.util.Strings;
import es.optsicom.lib.util.description.Descriptive;

/**
 * Estadísticos (instancias de la clase Statistic) Filtros de instancias o
 * algoritmos. (Instancias de ElementFilter) Creador de grupos de instancias:
 * Los grupos deben tener nombre (puede generarse automáticamente) o Todas las
 * instancias (como lo hace ahora) o No fusionar o Grupos explícitos, en los que
 * se indica que instancias pertenecen a cada grupo. o Grupos por propiedades:
 * Similar al GROUP BY de SQL, indicas las propiedades y te agrupa las
 * instancias q tienen esas propiedades con el mismo valor. Resumen de
 * estadísticos en grupos de instancias: Valores del enumerado Summarize. Cada
 * estadístico tiene su "sumarizador" por defecto, pero es interesante que se
 * pueda sobreescribir ¿o no?
 */

public class AttributedTableCreator {

	private static final Color CELL_COLOR = new Color(197, 217, 241);

	private static final String INSTANCE_ATT_NAME = "instance";

	private static final String STATISTIC_ATT_NAME = "statistic";

	private static final String INSTANCEGROUP_ATT_NAME = "instancegroup";

	private static final String METHOD_ATT_NAME = "method";

	private ExperimentManager experimentResults;

	// Very bad logging system :(
	private boolean showProcessLog = false;

	protected List<StatisticGroup> statisticGroups = new ArrayList<StatisticGroup>();

	private InstanceGroupMaker instanceGroupMaker;

	// Optional Aliases
	private List<Alias> methodAliases;
	private List<Alias> statisticAliases;
	private List<Alias> instanceGroupAliases;
	private List<Alias> instanceAliases;

	// RelativiceMode
	private RelativizeMode relativizeMode = RelativizeMode.EXPERIMENT;

	// Experiment Method Names
	Map<MethodDescription, String> expMethodNames;

	private List<String> methodTitles;

	private List<String> statisticTitles;

	private List<String> instanceTitles;

	private List<String> instanceGroupTitles;

	protected BestMode bestMode = BestMode.MAX_IS_BEST;

	public void setBestMode(BestMode bestMode) {
		this.bestMode = bestMode;
	}

	public RelativizeMode getRelativezeMode() {
		return relativizeMode;
	}

	public void setRelativezeMode(RelativizeMode relativezeMode) {
		this.relativizeMode = relativezeMode;
	}

	public List<Alias> getMethodAliases() {
		return methodAliases;
	}

	public void setMethodAliases(List<Alias> methodAliases) {
		this.methodAliases = methodAliases;
	}

	public List<Alias> getStatisticAliases() {
		return statisticAliases;
	}

	public void setStatisticAliases(List<Alias> statisticAliases) {
		this.statisticAliases = statisticAliases;
	}

	public List<Alias> getInstanceGroupAliases() {
		return instanceGroupAliases;
	}

	public List<Alias> getInstanceAliases() {
		return instanceAliases;
	}

	public void setInstanceAliases(List<Alias> instanceAliases) {
		this.instanceAliases = instanceAliases;
	}

	public InstanceGroupMaker getInstanceGroupMaker() {
		return instanceGroupMaker;
	}

	public void setInstanceGroupMaker(InstanceGroupMaker instanceGroupMaker) {
		this.instanceGroupMaker = instanceGroupMaker;
	}

	public boolean getShowProcessLog() {
		return this.showProcessLog;
	}

	public void setShowProcessLog(boolean showProcessLog) {
		this.showProcessLog = showProcessLog;
	}

	public void setStatisticGroups(List<StatisticGroup> statisticGroups) {
		this.statisticGroups = statisticGroups;
	}

	public void addStatisticGroup(StatisticGroup group) {
		this.statisticGroups.add(group);
	}

	public ExperimentManager getExperimentResults() {
		return experimentResults;
	}

	public void setExperimentResults(ExperimentManager experimentResults) {
		this.experimentResults = experimentResults;
	}

	public AttributedTable buildTable() {

		// checkResults();

		List<Statistic> statistics = new ArrayList<Statistic>();
		for (StatisticGroup statisticGroup : statisticGroups) {
			statistics.addAll(statisticGroup.getStatisticList());
		}

		// Reduce the number of decimals of the real numb
		List<NumericFormat> numberFormats = new ArrayList<NumericFormat>();
		for (Statistic statistic : statistics) {
			numberFormats.add(new NumericFormat(statistic.getNumberType(), 2));
		}

		// We create a new list to avoid modify the original instances List
		List<InstanceDescription> instances = new ArrayList<InstanceDescription>(experimentResults.getInstances());

		expMethodNames = new HashMap<MethodDescription, String>();
		List<MethodDescription> methods = new ArrayList<MethodDescription>();
		for (MethodDescription method : experimentResults.getMethods()) {

			methods.add(method);
			expMethodNames.put(method, experimentResults.getExperimentMethodName(method));

		}

		if (methods.size() == 0) {
			throw new RuntimeException("There is no methods in the experiment");
		}

		methodTitles = createTitles(methodAliases, methods, expMethodNames);
		statisticTitles = createTitles(statisticAliases, statistics, null);

		// Aquí es donde hay que ordenar por título.
		sortDescriptiveByTitles(methods, methodTitles);

		if (instanceGroupMaker == null) {
			instanceTitles = createTitles(instanceAliases, instances, null);
			sortDescriptiveByTitles(instances, instanceTitles);
		}

		// Creamos los valores de los estadí­sticos para cada método por cada
		// instancia
		List<List<List<TableValue>>> valuesByMethodByInstanceByStatistic = createValuesByMethodByInstanceByStatistic(
				methods, instances);

		AttributedTable table;

		if (instanceGroupMaker == null) {

			// Creamos la attributed table
			table = createAttributedTableInstances(methods, instances, statistics, numberFormats,
					valuesByMethodByInstanceByStatistic);

		} else {

			List<InstancesGroup> instanceGroups = instanceGroupMaker.createInstanceGroups(instances);

			instanceGroupTitles = createTitles(instanceGroupAliases, instanceGroups, null);

			sortDescriptiveByTitles(instanceGroups, instanceGroupTitles);

			// Resumimos los valores de los estadísticas de las instancias
			// de cada grupo por cada método
			List<List<List<TableValue>>> valuesByMethodByInstanceGroupByStatistic = summarizeInstanceGroupValues(
					methods, instanceGroups, instances, statistics, valuesByMethodByInstanceByStatistic);

			// Creamos la attributed table
			table = createAttributedTable(methods, instanceGroups, statistics, numberFormats,
					valuesByMethodByInstanceGroupByStatistic);
		}

		return table;

	}

	// Checks that results seem ok
	private void checkResults() {

		System.out.println("Checking results...");

		List<InstanceDescription> instances = experimentResults.getInstances();
		List<MethodDescription> methods = experimentResults.getMethods();
		for (InstanceDescription id : instances) {
			for (MethodDescription md : methods) {
				List<Execution> executions = experimentResults.getExecutions(id, md);
				if (executions.isEmpty()) {
					throw new Error("No executions for instance and method:\n\t" + id + "\n\t" + md);
				}
				for (Execution exec : executions) {
					if (exec.getInstance() == null) {
						throw new Error("Null instance in execution for method " + md
								+ " and experiment declared instance " + id);
					}
					if (!exec.getInstance().equals(id)) {
						throw new Error("Instance " + id + " in experiment is different from instance in execution ("
								+ exec.getInstance() + " for method " + md);
					}
					if (exec.getMethod() == null) {
						throw new Error("Null method in execution for declared method " + md
								+ " and experiment declared instance " + id);
					}
					if (!exec.getMethod().equals(md)) {
						throw new Error("Method " + md + " in experiment is different from method in execution ("
								+ exec.getMethod() + " for instance " + id);
					}
					if (exec.getEvents().isEmpty()) {
						throw new Error("No events for execution #" + exec.getNumExecution() + " for instance " + id
								+ " and method " + md);
					}
					// if(exec.getLastEvent(Event.FINISH_TIME_EVENT) == null) {
					// throw new Error("There is no FINISH_TIME_EVENT");
					// }
					// if(exec.getExecutionTime() <= 0) {
					// throw new Error("Impossible execution time: " +
					// exec.getExecutionTime());
					// }
				}
			}
		}

		System.out.println("Check ok");

	}

	private void sortDescriptiveByTitles(List<? extends Descriptive> descriptive, List<String> names) {

		CollectionsUtil.sortListsByListA(names, Strings.getNaturalComparator(), descriptive);

	}

	private List<List<List<TableValue>>> createValuesByMethodByInstanceByStatistic(List<MethodDescription> methods,
			List<InstanceDescription> instances) {

		// Method x Instance x Statistic
		List<List<List<TableValue>>> valuesByMethodByInstanceByStatistic = new ArrayList<List<List<TableValue>>>();

		// Instance x Statistic x Method (to automatic processing the results)
		List<List<List<TableValue>>> valuesByInstanceByStatisticByMethod = new ArrayList<List<List<TableValue>>>();

		// For each instance...
		Iterator<InstanceDescription> it = instances.iterator();
		while (it.hasNext()) {

			InstanceDescription instance = it.next();

			// for (InstanceDescription instance : instances) {

			List<List<TableValue>> valuesByStatisticByMethod = new ArrayList<List<TableValue>>();

			valuesByInstanceByStatisticByMethod.add(valuesByStatisticByMethod);

			// Very bad logging system
			log("Instance: " + instance.getName());

			// Method x RawProcessor x CookedEvent
			List<List<Double[]>> byMethodCookedEvents = new ArrayList<List<Double[]>>();

			for (MethodDescription method : methods) {
				log("  Method:" + method);

				List<Execution> execs = experimentResults.getExecutions(instance, method);

				if (execs == null || execs.size() == 0) {
					log("Method '" + method + "' doesn't contain execution information for instance '" + instance
							+ "'");

					it.remove();
					valuesByInstanceByStatisticByMethod.remove(valuesByInstanceByStatisticByMethod.size() - 1);
					break;

				} else {
					proccessMethod(execs, byMethodCookedEvents);
				}
			}

			if (byMethodCookedEvents.isEmpty()) {
				System.out.println("WARNING: byMyehtodCookedEvents is empty");
			} else {

				List<List<TableValue>> valuesByMethodByStatistic = fromCookedEventsToStatisticValues(methods,
						byMethodCookedEvents, this.relativizeMode, instance, experimentResults);

				for (int numStatistic = 0; numStatistic < valuesByMethodByStatistic.get(0).size(); numStatistic++) {
					List<TableValue> valuesByStatistic = new ArrayList<TableValue>();
					valuesByStatisticByMethod.add(valuesByStatistic);
					// Add valuesByMethodByStatistic to
					// valuesByStatisticByMethod
					for (int numMethod = 0; numMethod < valuesByMethodByStatistic.size(); numMethod++) {
						valuesByStatistic.add(valuesByMethodByStatistic.get(numMethod).get(numStatistic));
					}
				}

				addToInstanceValues(valuesByMethodByInstanceByStatistic, valuesByMethodByStatistic);
			}

		}

		List<Statistic> statistics = new ArrayList<Statistic>();
		for (StatisticGroup statGroup : statisticGroups) {
			statistics.addAll(statGroup.getStatisticList());
		}

		processValuesByInstanceGroupByStatisticByMethod(statistics, valuesByInstanceByStatisticByMethod);

		return valuesByMethodByInstanceByStatistic;
	}

	private AttributedTable createAttributedTableInstances(List<MethodDescription> methods,
			List<InstanceDescription> instances, List<Statistic> statistics, List<NumericFormat> numberFormats,
			List<List<List<TableValue>>> valuesByMethodByInstanceByStatistic) {

		List<Attribute> methodsAsAttributes = createAttributesFromDescriptives(METHOD_ATT_NAME, methods, methodTitles);
		List<Attribute> instancesAsAttributes = createAttributesFromDescriptives(INSTANCE_ATT_NAME, instances,
				instanceTitles);
		List<Attribute> statisticsAsAttributes = createAttributesFromDescriptives(STATISTIC_ATT_NAME, statistics,
				statisticTitles);

		AttributedTable table = new AttributedTable();
		table.addSortedAttributes(METHOD_ATT_NAME, methodsAsAttributes);
		table.addSortedAttributes(INSTANCE_ATT_NAME, instancesAsAttributes);
		table.addSortedAttributes(STATISTIC_ATT_NAME, statisticsAsAttributes);

		for (int i = 0; i < methods.size(); i++) {
			for (int j = 0; j < instances.size(); j++) {
				for (int k = 0; k < statistics.size(); k++) {
					table.addValue(valuesByMethodByInstanceByStatistic.get(i).get(j).get(k), numberFormats.get(k),
							methodsAsAttributes.get(i), instancesAsAttributes.get(j), statisticsAsAttributes.get(k));
				}
			}
		}

		return table;

	}

	// private AttributedTable createAttributedTable(
	// List<MethodDescription> methods,
	// List<InstanceGroup> instanceGroups, List<Statistic> statistics,
	// List<List<List<Double>>> valuesByMethodByInstanceGroupByStatistic) {
	//
	// List<NumberFormat> numberFormats = new ArrayList<NumberFormat>();
	// for (Statistic statistic : statistics) {
	// numberFormats.add(new NumberFormat(statistic.getNumberType(), 2));
	// }
	//
	// List<Attribute> methodsAsAttributes = createAttributesFromDescriptives(
	// METHOD_ATT_NAME, methods, methodAliases, expMethodNames);
	//
	// List<List<Descriptive>> propValuesAsDescriptives =
	// createPropValuesAsDescriptive(instanceGroups);
	//
	// List<Attribute> instanceGroupsAsAttributes =
	// createAttributesFromDescriptives(
	// INSTANCEGROUP_ATT_NAME, instanceGroups, instanceGroupAliases,
	// null);
	//
	// List<Attribute> statisticsAsAttributes =
	// createAttributesFromDescriptives(
	// STATISTIC_ATT_NAME, statistics, statisticAliases, null);
	//
	// AttributedTable table = new AttributedTable();
	// table.addSortedAttributes(METHOD_ATT_NAME, methodsAsAttributes);
	//
	// table.addSortedAttributes(INSTANCEGROUP_ATT_NAME,
	// instanceGroupsAsAttributes);
	//
	// table.addSortedAttributes(STATISTIC_ATT_NAME, statisticsAsAttributes);
	//
	// for (int i = 0; i < methods.size(); i++) {
	// for (int j = 0; j < instanceGroups.size(); j++) {
	// for (int k = 0; k < statistics.size(); k++) {
	// table.addValue(valuesByMethodByInstanceGroupByStatistic
	// .get(i).get(j).get(k), numberFormats.get(k),
	// methodsAsAttributes.get(i),
	// instanceGroupsAsAttributes.get(j),
	// statisticsAsAttributes.get(k));
	// }
	// }
	// }
	//
	// return table;
	// }
	//
	// private List<List<Descriptive>> createPropValuesAsDescriptive(
	// List<Descriptive> fullDescriptives) {
	//
	// //This method can only be called with objects with properties
	// (descriptives)
	// //that have the following characteristics. All values must have the same
	// properties.
	// //Also, all objets must be the catesian product of the values of the
	// properties.
	// //For example, if there is two props "propA" and "propB" with values
	// "pAv1","pAv2" for
	// //"propA" and "pBv1","pBv2" for "propB", then, the number of objects must
	// be
	// //exactly 4:
	// {propA=pAv1,propB=pBv1},{propA=pAv1,propB=pBv2},{propA=pAv2,propB=pBv1}
	// //{propA=pAv2,propB=pBv2}. Only with this requisites, the props can be
	// considered as
	// //table attributes.
	//
	// Set<String> propNames = fullDescriptives.get(0).getProperties().keySet();
	// Map<String,List<Object>> propValues = new HashMap<String,List<Object>>();
	// for(String propName : propNames){
	// propValues.put(propName, new ArrayList<Object>());
	// }
	//
	// for(int i=0; i<fullDescriptives.size(); i++){
	//
	// Descriptive descriptive = fullDescriptives.get(i);
	//
	// if(!descriptive.getProperties().keySet().equals(propNames)){
	// throw new NotConvertibleException("Object "+descriptive+" hasn't the " +
	// "same properties as the first object: "+propNames);
	// }
	//
	// for(Entry<String,Object> e : descriptive.getProperties().entrySet()){
	// propValues.get(e.getValue()).add(e.getValue());
	// }
	// }
	//
	//
	//
	//
	// return null;
	// }

	private AttributedTable createAttributedTable(List<MethodDescription> methods, List<InstancesGroup> instanceGroups,
			List<Statistic> statistics, List<NumericFormat> numberFormats,
			List<List<List<TableValue>>> valuesByMethodByInstanceGroupByStatistic) {

		List<Attribute> methodsAsAttributes = createAttributesFromDescriptives(METHOD_ATT_NAME, methods, methodTitles);
		List<Attribute> instanceGroupsAsAttributes = createAttributesFromDescriptives(INSTANCEGROUP_ATT_NAME,
				instanceGroups, instanceGroupTitles);
		List<Attribute> statisticsAsAttributes = createAttributesFromDescriptives(STATISTIC_ATT_NAME, statistics,
				statisticTitles);

		AttributedTable table = new AttributedTable();
		table.addSortedAttributes(METHOD_ATT_NAME, methodsAsAttributes);
		table.addSortedAttributes(INSTANCEGROUP_ATT_NAME, instanceGroupsAsAttributes);
		table.addSortedAttributes(STATISTIC_ATT_NAME, statisticsAsAttributes);

		for (int i = 0; i < methods.size(); i++) {
			for (int j = 0; j < instanceGroups.size(); j++) {
				for (int k = 0; k < statistics.size(); k++) {
					table.addValue(valuesByMethodByInstanceGroupByStatistic.get(i).get(j).get(k), numberFormats.get(k),
							methodsAsAttributes.get(i), instanceGroupsAsAttributes.get(j),
							statisticsAsAttributes.get(k));
				}
			}
		}

		return table;
	}

	private List<String> createTitles(List<Alias> aliases, List<? extends Descriptive> descriptions,
			Map<? extends Descriptive, String> expNames) {

		List<String> titles = new ArrayList<String>();

		if (expNames != null) {
			for (int i = 0; i < descriptions.size(); i++) {
				Descriptive desc = descriptions.get(i);
				String expName = expNames.get(desc);
				titles.add(expName);
			}
		}

		if (aliases != null) {
			Map<String, String> aliasesMap = new HashMap<String, String>();
			for (Alias alias : aliases) {
				aliasesMap.put(alias.getFrom(), alias.getTo());
			}

			List<String> newTitles = new ArrayList<String>();
			for (int i = 0; i < descriptions.size(); i++) {
				Descriptive description = descriptions.get(i);
				String alias = aliasesMap.get(description.getProperties().toString());
				if (alias != null) {
					newTitles.add(alias);
				} else {
					newTitles.add(titles.get(i));
				}
			}

			titles = newTitles;
		}

		DescriptiveTitlesManager titlesManager = new DescriptiveTitlesManager(titles, descriptions);

		return titlesManager.getUniqueTitles();

	}

	private List<Attribute> createAttributesFromDescriptives(String attributeName,
			List<? extends Descriptive> descriptives, List<String> titles) {

		List<Attribute> attributes = new ArrayList<Attribute>();
		for (int i = 0; i < descriptives.size(); i++) {
			attributes.add(new Attribute(attributeName, descriptives.get(i), titles.get(i)));
		}

		return attributes;
	}

	private void log(String message) {
		if (showProcessLog) {
			System.out.println(message);
		}
	}

	private void proccessMethod(List<Execution> execs, List<List<Double[]>> byMethodCookedEvents) throws Error {

		List<Double[]> cookedEventsByRP = new ArrayList<Double[]>();

		// For each RawProcessor...
		for (StatisticGroup rawToStatistic : statisticGroups) {
			RawProcessor rawProcessor = rawToStatistic.getRawProcessor();

			try {
				Double[] cookedEvents = rawProcessor.cookEvents(execs);

				// System.out.println(" RawProcessor: "
				// + rawProcessor.getClass().getSimpleName() + " -> "
				// + Arrays.toString(cookedEvents));

				cookedEventsByRP.add(cookedEvents);

				// if (!cookedNamesTest) {
				if (rawProcessor.getCookedEventsNames().size() != cookedEvents.length) {
					throw new Error("RawProcessor " + rawProcessor.getClass().getName()
							+ " isn't coherent in cookedEvents and its names");
				}
				// }
			} catch (AnalysisException e) {
				throw new AnalysisException("Error processing events of method: " + execs.get(0).getMethod().getName(),
						e);
			}

		}

		// cookedNamesTest = true;

		byMethodCookedEvents.add(cookedEventsByRP);
	}

	private void addToInstanceValues(List<List<List<TableValue>>> methodsByInstanceColumnValues,
			List<List<TableValue>> methodColumnValues) {
		// For each method are set relativized column values in a data structure
		for (int k = 0; k < methodColumnValues.size(); k++) {
			List<List<TableValue>> instanceColumnValues;
			if (methodsByInstanceColumnValues.size() == k) {
				instanceColumnValues = new ArrayList<List<TableValue>>();
				methodsByInstanceColumnValues.add(instanceColumnValues);
			} else {
				instanceColumnValues = methodsByInstanceColumnValues.get(k);
			}

			instanceColumnValues.add(methodColumnValues.get(k));
		}
	}

	private List<List<TableValue>> fromCookedEventsToStatisticValues(List<MethodDescription> methods,
			List<List<Double[]>> cookedEventsByMethod, RelativizeMode relativizeMode, InstanceDescription instance,
			ExperimentManager experimentResults) {

		log("Relativize and transform cookedEvent in statistic values");

		// Method x Statistic
		List<List<TableValue>> statisticValuesByMethod = new ArrayList<List<TableValue>>();

		int numMethods = cookedEventsByMethod.size();

		// For each StatisticGroup
		for (int numStatGroup = 0; numStatGroup < cookedEventsByMethod.get(0).size(); numStatGroup++) {

			log("Raw Processor: " + statisticGroups.get(numStatGroup).getRawProcessor().getClass().getSimpleName());

			// For each statistic of each statisticGroup
			for (int numStatistic = 0; numStatistic < cookedEventsByMethod.get(0)
					.get(numStatGroup).length; numStatistic++) {

				Double[] cookedEvents = ArraysUtil.createDoubleArray(numMethods);

				// For each method
				for (int numMethod = 0; numMethod < numMethods; numMethod++) {
					cookedEvents[numMethod] = cookedEventsByMethod.get(numMethod).get(numStatGroup)[numStatistic];
				}

				Statistic[] statistics = statisticGroups.get(numStatGroup).getStatistics()[numStatistic];

				for (Statistic statistic : statistics) {

					log("    Statistic:" + statistic.getName());

					Double[] relativizedValues;

					StatisticCalc statisticCalc = statistic.getStatisticCalc();

					if (statisticCalc instanceof RelativizerStatisticCalc) {

						RelativizerStatisticCalc rsc = (RelativizerStatisticCalc) statisticCalc;
						RelativeValueProvider rvp = rsc.getRelativeValueProvider();
						if (rvp != null) {

							relativizedValues = statisticCalc.relativize(cookedEvents.clone(),
									rvp.getValue(instance, experimentResults));

						} else {

							relativizedValues = statisticCalc.relativize(cookedEvents.clone(), null);

						}
					} else {
						relativizedValues = cookedEvents;
					}

					for (int numMethod = 0; numMethod < numMethods; numMethod++) {
						log("    Method: " + methods.get(numMethod) + ": " + cookedEvents[numMethod] + " -> "
								+ relativizedValues[numMethod]);
					}

					for (int numMethod = 0; numMethod < numMethods; numMethod++) {

						List<TableValue> statisticValues;

						if (statisticValuesByMethod.size() == numMethod) {
							statisticValues = new ArrayList<TableValue>();
							statisticValuesByMethod.add(statisticValues);
						} else {
							statisticValues = statisticValuesByMethod.get(numMethod);
						}

						TableValue tableValue = new TableValue(relativizedValues[numMethod]);

						statisticValues.add(tableValue);
					}
				}
			}
		}
		return statisticValuesByMethod;
	}

	private List<List<List<TableValue>>> summarizeInstanceGroupValues(List<MethodDescription> methods,
			List<InstancesGroup> instanceGroups, List<InstanceDescription> instances, List<Statistic> statistics,
			List<List<List<TableValue>>> valuesByMethodByInstanceByStatistic) {

		// Calculamos el número de grupo al que pertenece cada instancia.
		Map<InstanceDescription, Integer> instancesGroupIndex = new HashMap<InstanceDescription, Integer>();
		int instanceGroupIndex = 0;
		for (InstancesGroup instanceGroup : instanceGroups) {
			for (InstanceDescription instance : instanceGroup.getInstances()) {
				instancesGroupIndex.put(instance, instanceGroupIndex);
			}
			instanceGroupIndex++;
		}

		List<List<List<TableValue>>> valuesByMethodByInstanceGroupByStatistic = new ArrayList<List<List<TableValue>>>();

		for (int numMethod = 0; numMethod < methods.size(); numMethod++) {

			List<List<TableValue>> valuesByInstanceGroupByStatistic = new ArrayList<List<TableValue>>();
			valuesByMethodByInstanceGroupByStatistic.add(valuesByInstanceGroupByStatistic);

			// Creamos una lista vací­a para cada InstanceGroup
			for (int j = 0; j < instanceGroups.size(); j++) {
				valuesByInstanceGroupByStatistic.add(new ArrayList<TableValue>());
			}

			List<List<List<TableValue>>> valuesByInstanceGroupByInstanceByStatistic = new ArrayList<List<List<TableValue>>>();
			// Creamos una lista vacÃ­a para cada InstanceGroup
			for (int j = 0; j < instanceGroups.size(); j++) {
				valuesByInstanceGroupByInstanceByStatistic.add(new ArrayList<List<TableValue>>());
			}

			// Recolectamos las instancias de cada grupo en
			// valuesByInstanceGroupByInstanceByStatistic
			for (int j = 0; j < instances.size(); j++) {

				List<TableValue> valuesByInstanceByStatistics = valuesByMethodByInstanceByStatistic.get(numMethod)
						.get(j);
				int instanceGroupNum = instancesGroupIndex.get(instances.get(j));

				valuesByInstanceGroupByInstanceByStatistic.get(instanceGroupNum).add(valuesByInstanceByStatistics);

			}

			// Tenemos que fusionar cada uno de los estadí­sticos...
			for (int numStatistic = 0; numStatistic < statistics.size(); numStatistic++) {

				// En cada uno de los grupos de instancias
				for (int numInstanceGroup = 0; numInstanceGroup < instanceGroups.size(); numInstanceGroup++) {

					List<List<TableValue>> valuesByInstanceByStatistic = valuesByInstanceGroupByInstanceByStatistic
							.get(numInstanceGroup);

					Double[] groupInstanceValues = ArraysUtil.createDoubleArray(valuesByInstanceByStatistic.size());

					for (int l = 0; l < valuesByInstanceByStatistic.size(); l++) {
						groupInstanceValues[l] = valuesByInstanceByStatistic.get(l).get(numStatistic).getValue();
					}

					Statistic statistic = statistics.get(numStatistic);
					valuesByInstanceGroupByStatistic.get(numInstanceGroup)
							.add(new TableValue(statistic.getStatisticCalc().summarize(groupInstanceValues)));
				}

			}

		}

		processValuesByInstanceGroupByStatisticByMethod(statistics,
				transformToValuesByInstanceGroupByStatisticByMethod(valuesByMethodByInstanceGroupByStatistic));

		return valuesByMethodByInstanceGroupByStatistic;

	}

	private List<List<List<TableValue>>> transformToValuesByInstanceGroupByStatisticByMethod(
			List<List<List<TableValue>>> valuesByMethodByInstanceGroupByStatistic) {

		List<List<List<TableValue>>> valuesByInstanceGroupByStatisticByMethod = new ArrayList<List<List<TableValue>>>();

		for (int numInstanceGroup = 0; numInstanceGroup < valuesByMethodByInstanceGroupByStatistic.get(0)
				.size(); numInstanceGroup++) {

			ArrayList<List<TableValue>> valuesByStatisticByMethod = new ArrayList<List<TableValue>>();

			valuesByInstanceGroupByStatisticByMethod.add(valuesByStatisticByMethod);

			for (int numStatistic = 0; numStatistic < valuesByMethodByInstanceGroupByStatistic.get(0).get(0)
					.size(); numStatistic++) {

				List<TableValue> valuesByMethod = new ArrayList<TableValue>();
				valuesByStatisticByMethod.add(valuesByMethod);

				for (int numMethod = 0; numMethod < valuesByMethodByInstanceGroupByStatistic.size(); numMethod++) {

					valuesByMethod.add(valuesByMethodByInstanceGroupByStatistic.get(numMethod).get(numInstanceGroup)
							.get(numStatistic));

				}

			}
		}

		return valuesByInstanceGroupByStatisticByMethod;

	}

	/**
	 * Makes a naive analysis of statistics calculated by each method in each
	 * instanceGroup. The analysis here is marking the value containing the best
	 * values in this statistic among others methods. This visual mark allows
	 * the researcher to identify quickly the best method. However, we need a
	 * best way to do automatic analysis of the values in the tables, at least,
	 * in the cases where the best method is clear.
	 * 
	 * @param statistics
	 * @param valuesByMethodByInstanceGroupByStatistic
	 */
	private void processValuesByInstanceGroupByStatisticByMethod(List<Statistic> statistics,
			List<List<List<TableValue>>> valuesByInstanceGroupByStatisticByMethod) {

		for (int numInstanceGroup = 0; numInstanceGroup < valuesByInstanceGroupByStatisticByMethod
				.size(); numInstanceGroup++) {

			for (int numStatistic = 0; numStatistic < valuesByInstanceGroupByStatisticByMethod.get(0)
					.size(); numStatistic++) {

				List<TableValue> valuesByMethod = valuesByInstanceGroupByStatisticByMethod.get(numInstanceGroup)
						.get(numStatistic);

				Statistic statistic = statistics.get(numStatistic);
				BestMode resultsBestMode = statistic.getStatisticCalc().getResultsBestMode();

				if (resultsBestMode != null) {

					if (resultsBestMode == BestMode.MAX_IS_BEST) {
						Collections.sort(valuesByMethod, Collections.reverseOrder());
					} else {
						Collections.sort(valuesByMethod);
					}

					Double firstValue = null;
					boolean firstValueSet = false;

					for (TableValue value : valuesByMethod) {
						if (!firstValueSet) {
							value.setColor(CELL_COLOR);
							firstValue = value.getValue();
							firstValueSet = true;
						} else {
							if (MathUtil.efectiveEqualsDouble(value.getValue(), firstValue, 0.0001)) {
								value.setColor(CELL_COLOR);
							} else {
								break;
							}
						}
					}
				}

			}

		}
	}

}
