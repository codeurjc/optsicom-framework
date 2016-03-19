package es.optsicom.lib.util.outprocess;

public interface OperationProcessor {

	public Object execOperation(String operationName, Object[] params) throws Exception;

}
