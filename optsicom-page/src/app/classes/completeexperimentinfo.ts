import { ExperimentMethodName } from './experimentmethodname';
import { Experiment } from './experiment';

export class CompleteExperimentInfo {
    public experiment: Experiment;
    public methodNames: Array<ExperimentMethodName>
}