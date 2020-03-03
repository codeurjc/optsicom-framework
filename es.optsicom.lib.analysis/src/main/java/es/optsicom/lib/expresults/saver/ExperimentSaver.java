package es.optsicom.lib.expresults.saver;

import es.optsicom.lib.expresults.model.Experiment;
import es.optsicom.lib.expresults.model.InstanceDescription;
import es.optsicom.lib.expresults.model.MethodDescription;

public abstract class ExperimentSaver {

	public abstract long getExperimentId();

	public abstract DBExecutionSaver startExecution(MethodDescription createMethodDescription,
			InstanceDescription instanceDesc, long timeLimit);

	public abstract void setExperiment(Experiment expdb);

	public abstract MethodDescription findMethodDescription(String string);

	public abstract Experiment getExperiment();

	public abstract InstanceDescription findInstanceDescription(String string);

	public abstract void persist(Object entity);

	public abstract void persistExperiment();

	public abstract void commitTx();

}