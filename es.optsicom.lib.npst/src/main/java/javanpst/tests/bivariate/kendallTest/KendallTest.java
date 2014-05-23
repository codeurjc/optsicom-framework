/***********************************************************************

	This file is part of JavaNPST, a Java library of NonParametric
	Statistical Tests.

	Copyright (C) 2011
	
	J. Derrac (jderrac@decsai.ugr.es)
	S. García (sglopez@ujaen.es)
	F. Herrera (herrera@decsai.ugr.es)

	This program is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with this program.  If not, see http://www.gnu.org/licenses/
  
**********************************************************************/

package javanpst.tests.bivariate.kendallTest;

import java.util.Arrays;

import javanpst.data.structures.dataTable.DataTable;
import javanpst.tests.StatisticalTest;
import javanpst.utils.Pair;

import javanpst.distributions.tests.KendallDistribution;

/**
 * The Kendall test.
 * 
 * This test computes Kendall's tau coefficient to
 * measure association between bivariate samples.
 * 
 * Two samples, with 3 o more values are needed. A exact p-value
 * is computed if the size of the sample is equal or lower than 10. If
 * the size is between 11 and 30, a quantile approximation is computed.
 * Ties are broken arbitrarily.
 * 
 * Furthermore, an asymptotic p-value, measuring positive, negative, and no dependence
 * is computed for any sample size.
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class KendallTest extends StatisticalTest{

	/**
	 * Distribution of the test
	 */
	private KendallDistribution distribution;
	
	/**
	 * Data to analyze
	 */
	private DataTable data;
	
	/**
	 * First sample
	 */
	private double sample1 [];
	
	/**
	 * Second sample
	 */
	private double sample2 [];
	
	/**
	 * Ranks of the first sample
	 */
	private double ranks1 [];
	
	/**
	 * Ranks of the second sample
	 */
	private double ranks2 [];
	
	/**
	 * C statistic array
	 */
	private double C[];
	
	/**
	 * Q statistic array
	 */
	private double Q[];
	
	/**
	 * T statistic (Kendall's tau coefficient)
	 */
	private double T;
	
	/**
	 * Z statistic
	 */
	private double Z;
	
	/**
	 * Number of elements
	 */
	private double n;
	
	/**
	 * Positive dependence p-value
	 */
	private double positiveDependencePvalue;
	
	/**
	 * Negative dependence p-value
	 */
	private double negativeDependencePvalue;
	
	/**
	 * No dependence p-value
	 */
	private double noDependencePvalue;
	
	/**
	 * Exact p-value
	 */
	private double exactPValue;

	/**
	 * Default builder
	 */
	public KendallTest(){

		distribution=KendallDistribution.getInstance();
		setReportFormat();
		clearData();
		
	}//end-method
	
	/**
	 * Clears the data stored in the test
	 */
	public void clearData(){
		
		data=new DataTable();
		
		performed=false;
		dataReady=false;
		
		T=0.0;
		Z=0.0;
		n=0;
		positiveDependencePvalue=-1.0;
		negativeDependencePvalue=-1.0;
		noDependencePvalue=-1.0;
		exactPValue=-1.0;
		
	}//end-method
	
	/**
	 * Builder. Load data to test.
	 * 
	 * @param newData data to test.
	 */
	public KendallTest(DataTable newData){
		
		distribution=KendallDistribution.getInstance();
		setReportFormat();
		
		if(newData.getColumns()!=2){
			System.out.println("This test only can be employed with two samples");
			clearData();
			return;
		}
		
		if(newData.getRows()<3){
			System.out.println("This test need samples of size 3 or more");
			clearData();
			return;
		}
		
		for(int i=0;i<newData.getColumns();i++){
			if(newData.getColumnNulls(i)>0){
				System.out.println("No null values allowed in this test.");
				clearData();
				return;
			}
		}
		
		data=DataTable.newInstance(newData);
		dataReady=true;
		performed=false;
		
		n=data.getRows();
		sample1=new double [(int)n];
		sample2=new double [(int)n];

		for(int i=0;i<data.getRows();i++){

			sample1[i]=data.get(i, 0);
			sample2[i]=data.get(i, 1);
		}
		
	}//end-method

	/**
	 * Sets data to test
	 * 
	 * @param newData data to test.
	 */
	public void setData(DataTable newData){
		
		if(newData.getColumns()!=2){
			System.out.println("This test only can be employed with two samples");
			return;
		}
		
		if(newData.getRows()<3){
			System.out.println("This test need samples of size 3 or more");
			clearData();
			return;
		}
		
		for(int i=0;i<newData.getColumns();i++){
			if(newData.getRowNulls(i)>0){
				System.out.println("No null values allowed in this test.");
				return;
			}
		}
		
		data=DataTable.newInstance(newData);
		dataReady=true;
		performed=false;
		
		n=data.getRows();
		sample1=new double [(int)n];
		sample2=new double [(int)n];

		for(int i=0;i<data.getRows();i++){

			sample1[i]=data.get(i, 0);
			sample2[i]=data.get(i, 1);
		}
		
	}//end-method
	
	/**
	 * Perform the test
	 */
	public void doTest(){
			
		if(!dataReady){		
			System.out.println("Data is not ready");
			return;		
		}
		
		computeRanks();
		
		//sort pairs of ranks according to sample1
		Pair samples []=new Pair[sample1.length];
		
		for(int i=0;i<sample1.length;i++){
			samples[i]=new Pair(ranks1[i],ranks2[i]);
		}
		
		Arrays.sort(samples);
		
		//Compute C and Q statistics
		C=new double[samples.length];
		Q=new double[samples.length];
		
		Arrays.fill(C, 0.0);
		Arrays.fill(Q, 0.0);
		
		//C array
		double value;
		for(int i=samples.length-1;i>0;i--){
			value=samples[i].b;
			for(int j=i-1;j>-1;j--){
				if(value>samples[j].b){
					C[j]+=1.0;
				}
			}
		}
		
		//Q array
		for(int i=samples.length-1;i>0;i--){
			value=samples[i].b;
			for(int j=i-1;j>-1;j--){
				if(value<samples[j].b){
					Q[j]+=1.0;
				}
			}
		}
		
		//Compute T
		
		double sumC,sumQ;
		
		sumC=0.0;
		sumQ=0.0;
		
		for(int i=0;i<samples.length;i++){
			sumC+=C[i];
			sumQ+=Q[i];
		}
		
		T=2*(sumC-sumQ)/(double)(n*(n-1));

		//compute exact p-value
		if((n>2)&&(n<11)){
			exactPValue=distribution.computeExactProbability((int)n,T);
		}
		else{
			if((n>10)&&(n<31)){
				exactPValue=distribution.computeApproximatedProbability((int)n,T);
			}	
		}
		
		//compute asymptotic p-value
		double numerator,stdDev;
		
		double var=(2.0*((2.0*n)+5.0));
			
		numerator=3.0*T*Math.sqrt(n*(n-1.0));
		
		stdDev=Math.sqrt(var);
		
		Z=numerator/stdDev;
		
		positiveDependencePvalue=distribution.computeAsymptoticProbability(Z,true);
		negativeDependencePvalue=distribution.computeAsymptoticProbability(Z,false);
		noDependencePvalue=2.0*Math.min(positiveDependencePvalue, negativeDependencePvalue);
		
		performed=true;
		
	}//end-method

	/**
	 * Compute ranks distribution of both samples
	 */
	private void computeRanks(){
		
		double DELETED = Double.MAX_VALUE;
		
		double copy1 [];
		double copy2 [];
		
		copy1=new double [sample1.length];
		copy2=new double [sample2.length];
		
		System.arraycopy(sample1, 0, copy1, 0, sample1.length);
		System.arraycopy(sample2, 0, copy2, 0, sample2.length);
		
		Arrays.sort(copy1);
		Arrays.sort(copy2);
		
		ranks1=new double [sample1.length];
		ranks2=new double [sample2.length];
		
		double value;
		boolean found;
		
		//assign ranks to sample 1
		for(int i=0;i<sample1.length;i++){
			
			value=sample1[i];
			
			found=false;
			for(int j=0;j<copy1.length&&!found;j++){
				if(copy1[j]==value){
					found=true;
					ranks1[i]=(j+1);
					copy1[j]=DELETED;
				}
			}
		}
		
		//assign ranks to sample 2
		for(int i=0;i<sample2.length;i++){
			
			value=sample2[i];
			
			found=false;
			for(int j=0;j<copy2.length&&!found;j++){
				if(copy2[j]==value){
					found=true;
					ranks2[i]=(j+1);
					copy2[j]=DELETED;
				}
			}
		}
		
	}//end-method
	
	/**
	 * Get R statistic (Spearman's correlation coefficient)
	 * 
	 * @return R statistic
	 */
	public double getT(){
		
		return T;
		
	}//end-method
	
	/**
	 * Get Z statistic
	 * 
	 * @return Z statistic
	 */
	public double getZ(){
		
		return Z;
		
	}//end-method
	
	/**
	 * Get exact p-value
	 * @return exact p-value
	 */
	public double getExactPValue(){
		
		return exactPValue;
		
	}//end-method
	
	/**
	 * Get positive dependence p-value
	 * @return positive dependence p-value
	 */
	public double getPositiveDependencePvalue(){
		
		return positiveDependencePvalue;
		
	}//end-method
	
	/**
	 * Get negative dependence p-value
	 * @return negative dependence p-value
	 */
	public double getNegativeDependencePvalue(){
		
		return negativeDependencePvalue;
		
	}//end-method
	
	/**
	 * Get no dependence p-value
	 * @return negative dependence p-value
	 */
	public double getNoDependencePvalue(){
		
		return noDependencePvalue;
		
	}//end-method

	/**
	 * Prints the data stored in the test
	 * 
	 * @return Data stored
	 */
	public String printData(){
		
		String text="";
		
		text+="\n"+data;
		
		return text;
		
	}//end-method

	/**
	 * Prints a report with the results of the test
	 * 
	 * @return Output report
	 */
	public String printReport(){
		
		String report="";
		
		if(!performed){
			report+="The test has not been performed.\n";
			return report;
		}
		
		report+="\n*************\n";
		report+="Kendall Test\n";
		report+="*************\n\n";
		report+="T statistic (Kendall's tau): "+nf6.format(T)+"\n";
		
		if((n>2)&&(n<11)&&(exactPValue>-1.0)){
			report+="Exact P-Value: "+nf6.format(exactPValue)+"\n";
		}
		else{
			if((n>10)&&(n<31)){
				report+="Exact P-Value: <= "+nf6.format(exactPValue)+"\n";
			}	
		}
		
		report+="\nAsymptotic P-Values"+"\n\n";
		report+="Z statistic: "+nf6.format(Z)+"\n";
		report+="Positive dependence (Right tail): "+nf6.format(positiveDependencePvalue)+"\n";
		report+="Negative dependence (Left Tail): "+nf6.format(negativeDependencePvalue)+"\n";
		report+="No dependence (Double tail): "+nf6.format(noDependencePvalue)+"\n\n";
		
		report+="Ranks computed:\n\n";
		report+="S1 \t S2\n\n";
		
		for(int i=0;i<ranks1.length;i++){
			report+=ranks1[i]+" \t "+ranks2[i]+"\n";
		}
		
		report+="\n";
		
		return report;
		
	}//end-method
	
}//end-class
