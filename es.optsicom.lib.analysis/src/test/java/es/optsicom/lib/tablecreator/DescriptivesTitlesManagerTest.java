package es.optsicom.lib.tablecreator;

import java.util.ArrayList;
import java.util.List;

import es.optsicom.lib.analyzer.tablecreator.DescriptiveTitlesManager;
import es.optsicom.lib.expresults.model.DBProperties;
import es.optsicom.lib.util.Id;
import es.optsicom.lib.util.description.Descriptive;
import es.optsicom.lib.util.description.DescriptiveHelper;

public class DescriptivesTitlesManagerTest {

	public static class DummyDescriptive implements Descriptive {

		private String name;
		private int n;
		private int m;

		public DummyDescriptive(String name, int n, int m) {
			this.name = name;
			this.n = n;
			this.m = m;
		}

		@Id
		public int getN() {
			return n;
		}

		@Id
		public String getName() {
			return name;
		}

		@Id
		public int getM() {
			return m;
		}

		public DBProperties getProperties() {
			return (DBProperties) DescriptiveHelper.createProperties(this);
		}
	}

	public static void main(String[] args) {

		List<DummyDescriptive> descs = new ArrayList<DummyDescriptive>();
		descs.add(new DummyDescriptive("title1", 10, 5));
		descs.add(new DummyDescriptive("title2", 15, 5));
		descs.add(new DummyDescriptive("title2", 20, 5));
		descs.add(new DummyDescriptive("title5", 25, 5));

		DescriptiveTitlesManager names = new DescriptiveTitlesManager(descs);

		for (Descriptive desc : descs) {
			System.out.println(names.getUniqueTitle(desc));
		}

	}

}
