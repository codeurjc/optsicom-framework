package es.optsicom.lib.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class RandomFile extends Random {

	private BufferedReader br;
	private int count;
	
	public RandomFile(String fileName) {
		try {
			br = new BufferedReader(new FileReader(fileName));
			br.readLine();
			
			count = 0;
		} catch (Exception e) {
			throw new Error(e.getMessage());
		}
	}
	
	@Override
	public int nextInt(int n) {
		try {
			String line = br.readLine();
			count++;
			return Integer.parseInt(line) % n;
		} catch(NumberFormatException e) {
			throw new Error(e.getMessage());
		} catch (IOException e) {
			throw new Error(e.getMessage());
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		br.close();
	}
	
}
