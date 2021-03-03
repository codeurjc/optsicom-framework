import { Pipe, PipeTransform } from '@angular/core';
@Pipe({ name: 'showExperimentMode' })
export class ShowExperimentMode implements PipeTransform {
    transform(value: string) {
        switch (value) {
            case "max":
                return "Maximization";
            case "min":
                return "Minimization"
        }
    }
}