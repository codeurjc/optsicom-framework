package es.optsicom.lib.instancefile;

import java.io.IOException;

import es.optsicom.lib.Instance;

public interface InstanceLoader {

	Instance loadInstance(InstanceFile instanceFile) throws IOException;

	void loadProperties(InstanceFile instanceFile) throws IOException;

}
