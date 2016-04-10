package es.optsicom.lib.instancefile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import es.optsicom.lib.Instance;

public class FileInstancesRepositoryWithLoader extends FileInstancesRepository {

	private InstanceLoader instanceLoader;

	public FileInstancesRepositoryWithLoader(InstanceLoader instanceLoader) {
		super(false);
		this.instanceLoader = instanceLoader;
		populate();
	}

	public FileInstancesRepositoryWithLoader(boolean populate, InstanceLoader instanceLoader) {
		this(getDefaultInstancesDirOrZip(), FileInstancesRepository.DEFAULT_USE_CASE, populate, instanceLoader);
	}

	public FileInstancesRepositoryWithLoader(String useCase, InstanceLoader instanceLoader) {
		this(getDefaultInstancesDirOrZip(), useCase, true, instanceLoader);
	}

	public FileInstancesRepositoryWithLoader(File instancesDir, String useCase, InstanceLoader instanceLoader) {
		this(instancesDir, useCase, true, instanceLoader);
	}

	public FileInstancesRepositoryWithLoader(Path instancesDir, String useCase, InstanceLoader instanceLoader) {
		this(instancesDir, useCase, true, instanceLoader);
	}

	public FileInstancesRepositoryWithLoader(String useCase, boolean populate, InstanceLoader instanceLoader) {
		this(getDefaultInstancesDirOrZip(), useCase, populate, instanceLoader);
	}

	public FileInstancesRepositoryWithLoader(File instancesDir, String useCase, boolean populate,
			InstanceLoader instanceLoader) {
		this(instancesDir.toPath(), useCase, populate, instanceLoader);
	}

	public FileInstancesRepositoryWithLoader(Path instancesDir, String useCase, boolean populate,
			InstanceLoader instanceLoader) {
		super(instancesDir, useCase, false);
		this.instanceLoader = instanceLoader;
		if (populate) {
			populate();
		}
	}

	@Override
	public Instance loadInstance(InstanceFile instanceFile) throws IOException {
		return instanceLoader.loadInstance(instanceFile);
	}

}
