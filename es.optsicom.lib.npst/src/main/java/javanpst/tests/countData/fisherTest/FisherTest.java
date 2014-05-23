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

package javanpst.tests.countData.fisherTest;

import javanpst.data.structures.dataTable.DataTable;
import javanpst.distributions.tests.FisherDistribution;
import javanpst.tests.StatisticalTest;

/**
 * The Fisher test.
 * 
 * Fisher's exact test can be used to test the significance of the association (contingency) 
 * between two kinds of classifications (in 2 x 2 contingency matrixes).
 * 
 * A chi-square based approximation is also provided.
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class FisherTest extends StatisticalTest{

	/**
	 * Distribution of the test
	 */
	private FisherDistribution distribution;
	
	/**
	 * Data to analyze
	 */
	private DataTable data;
	
	/**
	 * Contingency matrix
	 */
	private double contingencyTable [][];
	
	/**
	 * Number of samples
	 */
	private int N;
	 
	/**
	 * Sum of the samples of the first column
	 */
	private int Y;
	
	/**
	 * Samples in first column, first row
	 */
	private int n00;
	
	/**
	 * Sum of the samples of the first row
	 */
	private int n1;
	
	/**
	 * Sum of the samples of the second row
	 */
	private int n2;
	
	/**
	 * Exact left tail p-value
	 */
	private double exactLeftPValue;
	
	/**
	 * Exact right tail p-value
	 */
	private double exactRightPValue;
	
	/**
	 * Q statistic
	 */
	private double Q;
	
	/**
	 * Asymptotic p-value
	 */
	private double asymptoticPValue;
	
	/**
	 * Default builder
	 */
	public FisherTest(){

		distribution=FisherDistribution.getInstance();
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
		
		Q=0.0;
		
		contingencyTable=null;
		
		asymptoticPValue=-1.0;
		exactLeftPValue=-1.0;
		exactRightPValue=-1.0;
		
	}//end-method
	
	/**
	 * Builder. Load data to test.
	 * 
	 * @param newData data to test.
	 */
	public FisherTest(DataTable newData){
		
		distribution=FisherDistribution.getInstance();
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
		
		//Exact P-Value
		N=(int)contingencyTable[0][0]+(int)contingencyTable[0][1]+(int)contingencyTable[1][0]+(int)contingencyTable[1][1];
		n1=(int)contingencyTable[0][0]+(int)contingencyTable[0][1];
		n2=(int)contingencyTable[1][0]+(int)contingencyTable[1][1];
		Y=(int)contingencyTable[0][0]+(int)contingencyTable[1][0];
		n00=(int)contingencyTable[0][0];
		
		exactLeftPValue=distribution.computeLeftExactProbability(N, n1, n2, Y, n00);
		exactRightPValue=distribution.computeRightExactProbability(N, n1, n2, Y, n00);
		
		//Asymptotic P-Value
		Q=0;
		
		double term;
		double sumRow []={n1,n2};
		double sumColumn []={Y,N-Y};
		
		for(int i=0;i<contingencyTable.length;i++){
			for(int j=0;j<contingencyTable[0].length;j++){
				term=(N*contingencyTable[i][j])-(sumRow[i]*sumColumn[j]);
				term=term*term;
				term/=N*sumRow[i]*sumColumn[j];
				Q+=term;
			}
		}

		int dF=(sumRow.length-1)*(sumColumn.length-1);
		asymptoticPValue=distribution.computeAsymptoticProbability(Q,dF);
		
		performed=true;

	}//end-method
	
	/**
	 * Get Q statistic 
	 * 
	 * @return Q statistic
	 */
	public double getQ(){
		
		return Q;
		
	}//end-method
	
	/**
	 * Get exact left tail p-value
	 * @return exact p-value
	 */
	public double getExactLeftPValue(){
		
		return exactLeftPValue;
		
	}//end-method
	
	/**
	 * Get exact right tail p-value
	 * @return exact p-value
	 */
	public double getExactRightPValue(){
		
		return exactRightPValue;
		
	}//end-method
	
	/**
	 * Get asymptotic p-value 
	 * @return asymptotic p-value
	 */
	public double getAsymptoticPValue(){
		
		return asymptoticPValue;
		
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
		report+="Fisher's exact Test\n";
		report+="*************\n\n";
		
		report+="Exact Left P-Value: "+nf6.format(exactLeftPValue)+"\n";
		report+="Exact Right P-Value: "+nf6.format(exactRightPValue)+"\n\n";
		
		report+="Q statistic: "+nf6.format(Q)+"\n";
		report+="Asymptotic P-Value: "+nf6.format(asymptoticPValue)+"\n\n";
		
		report+="Contingency table\n\n"; 
		report+=contingencyTable[0][0]+"\t"+contingencyTable[0][1]+"\n";
		report+=contingencyTable[1][0]+"\t"+contingencyTable[1][1]+"\n";
		
		return report;
		
	}//end-method
	
}//end-class
