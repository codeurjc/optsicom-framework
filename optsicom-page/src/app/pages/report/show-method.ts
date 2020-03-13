import { MethodName } from 'src/app/classes/experiment-clasess';

export class ShowMethod {
    public methodName: MethodName;
    public selected: boolean;

    constructor(methodName: MethodName, selected: boolean) {
        this.methodName = methodName;
        this.selected = selected;
    }

    public getMethodId() {
        return this.methodName.method.id;
    }

    public toString(): string {
        return this.methodName.name;
    }
}