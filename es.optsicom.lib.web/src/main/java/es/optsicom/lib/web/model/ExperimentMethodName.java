package es.optsicom.lib.web.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ExperimentMethodName implements Serializable {
	private long expId;
	private String expName;

	public ExperimentMethodName(long expId, String expName) {
		this.expId = expId;
		this.expName = expName;
	}

	public long getExpId() {
		return expId;
	}

	public String getExpName() {
		return expName;
	}

	public void setExpId(long expId) {
		this.expId = expId;
	}

	public void setExpName(String expName) {
		this.expName = expName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (expId ^ (expId >>> 32));
		result = prime * result + ((expName == null) ? 0 : expName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ExperimentMethodName))
			return false;
		ExperimentMethodName other = (ExperimentMethodName) obj;
		if (expId != other.expId)
			return false;
		if (expName == null) {
			if (other.expName != null)
				return false;
		} else if (!expName.equals(other.expName))
			return false;
		return true;
	}

}
