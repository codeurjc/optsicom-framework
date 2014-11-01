package es.optsicom.lib.util;

public abstract class RandomizedFor {

	public static class TrueRandomizedFor extends RandomizedFor {

		private int[] randomNumbers;
		
		public TrueRandomizedFor(int max) {
			randomNumbers = ArraysUtil.createNaturals(max);
			ArraysUtil.suffle(randomNumbers);
		}

		@Override
		public int get(int num) {
			return randomNumbers[num];
		}
	}

	public static class FakeRandomizedFor extends RandomizedFor {

		@Override
		public int get(int num) {
			return num;
		}
	}
	
	private static final FakeRandomizedFor FAKE_RANDOMIZED = new FakeRandomizedFor();

	public static RandomizedFor create(boolean randomized, int max){
		if(randomized){
			return new TrueRandomizedFor(max);
		} else {
			return FAKE_RANDOMIZED;
		}
	}
	
	public abstract int get(int num);
	
}
