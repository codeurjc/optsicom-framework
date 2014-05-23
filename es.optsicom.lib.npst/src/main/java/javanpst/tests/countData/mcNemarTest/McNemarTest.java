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

package javanpst.tests.countData.mcNemarTest;

import javanpst.data.structures.dataTable.DataTable;
import javanpst.distributions.tests.McNemarDistribution;
import javanpst.tests.StatisticalTest;

/**
 * The McNemar test.
 * 
 * This test can be applied to determine whether the row and column marginal frequencies 
 * of a 2 x 2 contingency table are equal.
 * 
 * Three approximations are provided:
 * 
 * - Exact p-value (through a binomial distribution)
 * - Asymptotic p-value (through a normal distribution)
 * - Asymptotic p-value (through a chi-square distribution)
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class McNemarTest extends StatisticalTest{

	/**
	 * Distribution of the test
	 */
	private McNemarDistribution distribution;
	
	/**
	 * Data to analyze
	 */
	private DataTable data;
	
	/**
	 * Contingency matrix
	 */
	private double contingencyTable [][];
	
	/**
	 * S statistic (Binomial)
	 */
	private double S;
	
	/**
	 * Exact p-value
	 */
	private double exactPValue;
	
	/**
	 * Z statistic (Normal)
	 */
	private double Z;
	
	/**
	 * Asymptotic p-value (Normal)
	 */
	private double asymptoticNormalPValue;
	
	/**
	 * T statistic (Chi-square)
	 */
	private double T;
	
	/**
	 * Asymptotic p-value (Chi-square)
	 */
	private double asymptoticChiPValue;
	
	/**
	 * Default builder
	 */
	public McNemarTest(){

		distribution=McNemarDistribution.getInstance();
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
		S=0.0;
		
		contingencyTable=null;
		
		asymptoticChiPValue=-1.0;
		asymptoticNormalPValue=-1.0;
		exactPValue=-1.0;
		
	}//end-method
	
	/**
	 * Builder. Load data to test.
	 * 
	 * @param newData data to test.
	 */
	public McNemarTest(DataTable newData){
		
		distribution=McNemarDistribution.getInstance();
		setReportFormat();
		
		data=DataTable.newInstance(newData);
		
		if((data.getColumns()>2)||(data.getColumns()<2)||(data.getRows()>2)||(data.getRows()<2)){
			System.out.println("Data must be represented in two rows and two columns");
			clearData();
			return;
		}
		
		for(int i=0;i<data.getColumns();i++){
			if(data.getColumnNulls(i)>0){
				System.out.println("No null values allowed in this test.");
				clearData();
				return;
			}
		}
		
		contingencyTable=new double[data.getRows()][data.getColumns()];
		
		for(int i=0;i<data.getRows();i++){
			for(int j=0;j<data.getColumns();j++){
				contingencyTable[i][j]=data.get(i, j);
			}
			
		}
		
		dataReady=true;
		performed=false;
		
	}//end-method

	/**
	 * Sets data to test
	 * 
	 * @param newData data to test.
	 */
	public void setData(DataTable newData){
		
		data=DataTable.newInstance(newData);
		
		if((data.getColumns()>2)||(data.getColumns()<2)||(data.getRows()>2)||(data.getRows()<2)){
			System.out.println("Data must be represented in two rows and two columns");
			clearData();
			return;
		}
		
		for(int i=0;i<newData.getColumns();i++){
			if(newData.getRowNulls(i)>0){
				System.out.println("No null values allowed in this test.");
				clearData();
				return;
			}
		}
		
		contingencyTable=new double[data.getRows()][data.getColumns()];
		
		for(int i=0;i<data.getRows();i++){
			for(int j=0;j<data.getColumns();j++){
				contingencyTable[i][j]=data.get(i, j);
			}
			
		}
		
		dataReady=true;
		performed=false;
		
	}//end-method

	/**
	 * Perform the test
	 */
	public void doTest(){
		
		if(!dataReady){		
			System.out.println("Data is not ready");
			return;	
		}
		
		//Binomial adjustment
		S=contingencyTable[0][1]+contingencyTable[1][0];
		
		exactPValue=distribution.computeBinomialAdjustment(S,contingencyTable[0][1]);
				
		//Normal aproximation
		Z=(contingencyTable[0][1]-contingencyTable[1][0]+0.5)/(Math.sqrt(contingencyTable[0][1]+contingencyTable[1][0]));
		
		asymptoticNormalPValue=distribution.computeNormalAdjustment(Z);
		
		//Chi-square distribution
		T=(contingencyTable[0][1]-contingencyTable[1][0])*(contingencyTable[0][1]-contingencyTable[1][0])/(contingencyTable[0][1]+contingencyTable[1][0]);
		
		asymptoticChiPValue=distribution.computeChiAdjustment(T);
		
		performed=true;
		
	}//end-method
	
	/**
	 * Get T statistic 
	 * 
	 * @return T statistic
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
	 * Get S statistic
	 * 
	 * @return S statistic
	 */
	public double getS(){
		
		return S;
		
	}//end-method
	
	/**
	 * Get exact p-value
	 * @return exact p-value
	 */
	public double getExactPValue(){
		
		return exactPValue;
		
	}//end-method
	
	/**
	 * Get asymptotic p-value (Normal approximation)
	 * @return asymptotic p-value
	 */
	public double getAsymptoticNormalPValue(){
		
		return asymptoticNormalPValue;
		
	}//end-method
	
	/**
	 * Get asymptotic p-value (Chi-square approximation)
	 * @return asymptotic p-value
	 */
	public double getAsymptoticChiPValue(){
		
		return asymptoticChiPValue;
		
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
		report+="McNemar Test\n";
		report+="*************\n\n";
		
		report+="S statistic (Binomial): "+nf6.format(S)+"\n";
		report+="Exact P-Value: "+nf6.format(exactPValue)+"\n\n";
		
		report+="Z statistic (Normal): "+nf6.format(Z)+"\n";
		report+="Asymptotic P-value: "+nf6.format(asymptoticNormalPValue)+"\n\n";
		
		report+="T statistic (Chi-square): "+nf6.format(T)+"\n";
		report+="Asymptotic P-value: "+nf6.format(asymptoticChiPValue)+"\n\n";
		
		report+="Contingency table\n\n"; 

		report+=contingencyTable[0][0]+"\t"+contingencyTable[0][1]+"\n";
		report+=contingencyTable[1][0]+"\t"+contingencyTable[1][1]+"\n";
		
		report+="\n";
		
		return report;
		
	}//end-method
	
}//end-class
