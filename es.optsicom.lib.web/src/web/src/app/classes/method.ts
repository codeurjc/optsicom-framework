export class MethodBasicResponseDTO {
    public id: number;
	public methodNameExp: string;
	public methodName: string;
}

export class MethodExtendResponseDTO {
	public id: number;
	public methodNameExp: string;
	public methodName: string;
	public properties: Map<string, string>;;
}
