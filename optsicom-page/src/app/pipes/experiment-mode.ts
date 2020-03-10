import { Pipe, PipeTransform } from '@angular/core';
import { BestMode } from '../classes/experiment-clasess';

@Pipe({ name: 'showExperimentMode' })
export class ShowExperimentMode implements PipeTransform {
    transform(value) {
        switch(value) {
            case BestMode.MAX_IS_BEST:
                return "Maximization";
            case BestMode.MIN_IS_BEST: 
                return "Minimization"
        }
    }
}