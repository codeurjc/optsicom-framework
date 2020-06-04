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

package javanpst.tests.goodness.K_STest;

import javanpst.data.structures.sequence.NumericSequence;
import javanpst.distributions.DistributionDefinitions;
import javanpst.distributions.common.continuous.ChiSquareDistribution;
import javanpst.distributions.common.continuous.ExponentialDistribution;
import javanpst.distributions.common.continuous.GammaDistribution;
import javanpst.distributions.common.continuous.LaplaceDistribution;
import javanpst.distributions.common.continuous.LogisticDistribution;
import javanpst.distributions.common.continuous.NormalDistribution;
import javanpst.distributions.common.continuous.UniformCDistribution;
import javanpst.distributions.common.continuous.WeibullDistribution;
import javanpst.distributions.tests.KolmogorovDistribution;
import javanpst.tests.StatisticalTest;

/**
 * The K_S test.
 * 
 * The Kolmogorov-Smirnov test for one sample can be used to adjust a given
 * sample to a continuous distribution
 * 
 * This version allows to adjust to several distributions ,
 * (Normal, Exponential, Uniform, Chi-Square, Laplace, Logistic, Gamma and Weibull)
 * but they have to be completely defined 
 * 
 * @author Joaquin
 * @version 1.0
 */
public class K_STest extends StatisticalTest{

	/**
	 * Distribution of the test
	 */
	private KolmogorovDistribution distribution;
	
	/**
	 * Sample to adjust
	 */
	private NumericSequence sequence;
	
	/**
	 * True distribution selected
	 */
	private double F0 [];
	
	/**
	 * Quantiles of the distribution
	 */
	private double Sn [];
	
	/**
	 * Type of distribution selected
	 */
	private int typeDistribution;
	
	/**
	 * Exponential distribution
	 */
	private ExponentialDistribution exponential;
	
	/**
	 * Normal distribution
	 */
	private NormalDistribution normal;
	
	/**
	 * Chi-square distribution
	 */
	private ChiSquareDistribution chisquare;

	/**
	 * Uniform distribution
	 */
	private UniformCDistribution uniform;
	
	/**
	 * Gamma distribution
	 */
	private GammaDistribution gamma;
	
	/**
	 * Laplace distribution
	 */
	private LaplaceDistribution laplace;
	
	/**
	 * Logistic distribution
	 */
	private LogisticDistribution logistic;
	
	/**
	 * Weibull distribution
	 */
	private WeibullDistribution weibull;
	
	/**
	 * Tests if the distribution to adjust is defined
	 */
	private boolean distributionReady;
	
	/**
	 * Dn statistic
	 */
	private double Dn;
	
	/**
	 * P-value of the adjustment
	 */
	private double pValue;
	
	/**
	 * Default builder
	 */
	public K_STest(){
		
		distribution=KolmogorovDistribution.getInstance();
		setReportFormat();
		clearData();
		
	}//end-method
	
	/**
	 * Clears the data stored in the test
	 */
	public void clearData(){
		
		sequence=new NumericSequence();
		
		performed=false;
		dataReady=false;
		distributionReady=false;
		
		typeDistribution=-1;
	
		F0=null;
		Sn=null;
		
		pValue=-1.0;
		
	}//end-method
	
	/**
	 * Builder. Load data to test.
	 * 
	 * @param newSequence data to test.
	 */
	public K_STest(NumericSequence newSequence){
		
		distribution=KolmogorovDistribution.getInstance();
		setReportFormat();

		sequence=new NumericSequence(newSequence);
		
		sequence.sort();
		
		F0=new double [sequence.size()];
		Sn=new double [sequence.size()];
		
		performed=false;
		dataReady=true;
		distributionReady=false;

	}//end-method
	
	/**
	 * Load data to test.
	 * 
	 * @param newSequence data to test.
	 */
	public void setData(NumericSequence newSequence){
		
		sequence=new NumericSequence(newSequence);
		
		sequence.sort();
		
		F0=new double [sequence.size()];
		Sn=new double [sequence.size()];
		
		performed=false;
		dataReady=true;
		distributionReady=false;

	}//end-method
		
	/**
	 * Sets adjustment to a Normal distribution,
	 * specifying mean and sigma.
	 * 
	 * @param m mean of the distribution
	 * @param s sigma parameter of the distribution
	 */
	public void adjustNormal(double m, double s){
		
		typeDistribution=DistributionDefinitions.NORMAL;
		
		normal=new NormalDistribution();
		
		normal.setMean(m);
		normal.setS(s);
		
		distributionReady=true;
		
	}//end-method
	
	/**
	 * Sets adjustment to a Exponential distribution
	 * fully defined
	 * 
	 * @param m mean of the distribution
	 */
	public void adjustExponential(double m){
		
		typeDistribution=DistributionDefinitions.EXPONENTIAL;
		
		exponential=new ExponentialDistribution();

		exponential.setMean(m);
		
		distributionReady=true;
		
	}//end-method
	
	/**
	 * Sets adjustment to a Uniform distribution
	 * fully defined
	 *
	 * @param start lower limit of the distribution
	 * @param end upper limit of the distribution
	 */
	public void adjustUniform(double start,double end){
		
		typeDistribution=DistributionDefinitions.UNIFORMC;
		
		uniform=new UniformCDistribution();

		uniform.setStart(start);
		uniform.setEnd(end);
		
		distributionReady=true;
		
	}//end-method
	
	/**
	 * Sets adjustment to a Chi-square distribution
	 * fully defined
	 * 
	 * @param freedom number of degrees of freedom
	 */
	public void adjustChiSquare(int freedom){
		
		typeDistribution=DistributionDefinitions.CHI_SQUARE;
		
		chisquare=new ChiSquareDistribution();

		chisquare.setDegree(freedom);
		
		distributionReady=true;
		
	}//end-method
	
	/**
	 * Sets adjustment to a Gamma distribution
	 * fully defined
	 * 
	 * @param K K parameter of the distribution
	 * @param lambda lambda parameter of the distribution
	 */
	public void adjustGamma(double K, double lambda){
		
		typeDistribution=DistributionDefinitions.GAMMA;
		
		gamma=new GammaDistribution();

		gamma.setK(K);
		gamma.setLambda(lambda);
		
		distributionReady=true;
		
	}//end-method
	
	/**
	 * Sets adjustment to a Laplace distribution
	 * fully defined
	 * 
	 * @param mean mean of the distribution
	 * @param scale scale parameter of the distribution
	 */
	public void adjustLaplace(double mean, double scale){
		
		typeDistribution=DistributionDefinitions.LAPLACE;
		
		laplace=new LaplaceDistribution();

		laplace.setMean(mean);
		laplace.setScale(scale);
		
		distributionReady=true;
		
	}//end-method
	
	/**
	 * Sets adjustment to a Logistic distribution
	 * fully defined
	 * 
	 * @param mean mean of the distribution
	 * @param S S parameter of the distribution
	 */
	public void adjustLogistic(double mean, double S){
		
		typeDistribution=DistributionDefinitions.LOGISTIC;
		
		logistic=new LogisticDistribution();

		logistic.setMean(mean);
		logistic.setS(S);
		
		distributionReady=true;
		
	}//end-method
	
	/**
	 * Sets adjustment to a Weibull distribution
	 * fully defined
	 * 
	 * @param K K parameter of the distribution
	 * @param lambda lambda parameter of the distribution
	 */
	public void adjustWeibull(double K, double lambda){
		
		typeDistribution=DistributionDefinitions.WEIBULL;
		
		weibull=new WeibullDistribution();

		weibull.setK(K);
		weibull.setLambda(lambda);
		
		distributionReady=true;
		
	}//end-method
	
	/**
	 * Performs the test
	 */
	public void doTest(){

		double value,value2;

		if(!dataReady){		
			System.out.println("Data is not ready");
			return;	
		}
		
		if(!distributionReady){		
			System.out.println("Distribution to fit is not set");
			return;	
		}
		
		Dn=0.0;

		//compute Sn
		for(int i=0;i<Sn.length;i++){
			Sn[i]=((double)(i+1)/Sn.length);
		}
		
		//compute F0
		for(int i=0;i<F0.length;i++){
			F0[i]=computeExpected(sequence.getSequence().get(i));
		}
		
		//Compute Dn
		for(int i=0;i<F0.length;i++){
			
			//Sn - F0
			
			value=Math.abs(Sn[i]-F0[i]);
			Dn=Math.max(Dn,value);
			
			//Sn(X-e) - F0
			if(i==0){
				value2=0.0;
			}
			else{
				value2=Sn[i-1];
				
			}
			value=Math.abs(value2-F0[i]);
			Dn=Math.max(Dn,value);

		}
		
		pValue=distribution.computeProbability(sequence.size(), Dn);
		
		performed=true;
		
	}//end-method
	
	/**
	 * Compute expected cumulative value for the distribution to adjust
	 * 
	 * @param value value of the distribution
	 * @return value computed
	 */
	private double computeExpected(double value){
		
		double prob=0.0;
		
		switch(typeDistribution){
		
			case DistributionDefinitions.NORMAL:
					prob=normal.computeCumulativeProbability(value);
				break;
				
			case DistributionDefinitions.UNIFORMC:
					prob=uniform.computeCumulativeProbability(value);
				break;
				
			case DistributionDefinitions.CHI_SQUARE:
					prob=chisquare.computeCumulativeProbability(value);
				break;
				
			case DistributionDefinitions.EXPONENTIAL:
					prob=exponential.computeCumulativeProbability(value);
				break;
				
			case DistributionDefinitions.GAMMA:
					prob=gamma.computeCumulativeProbability(value);
				break;
				
			case DistributionDefinitions.LAPLACE:
					prob=laplace.computeCumulativeProbability(value);
				break;
				
			case DistributionDefinitions.LOGISTIC:
					prob=logistic.computeCumulativeProbability(value);
				break;
				
			case DistributionDefinitions.WEIBULL:
					prob=weibull.computeCumulativeProbability(value);
				break;

		}
		
		return prob;
		
	}//end-method

	/**
	 * Get Dn statistic 
	 * 
	 * @return Dn Statistic
	 */
	public double getDn(){
		
		return Dn;
		
	}//end-method
	
	/**
	 * Get p-value of the test
	 * 
	 * @return p-value computed
	 */
	public double getPValue(){
		
		return pValue;
		
	}//end-method
	
	/**
	 * Prints the data stored in the test
	 * 
	 * @return Data stored
	 */
	public String printData(){
		
		String text="";
		
		text+="\n"+sequence;
		
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
		
		report+="\n************\n";
		report+="Kolmogorov-Smirnov goodness of fit test\n";
		report+="**************\n\n";
		
		switch(typeDistribution){
		
			case DistributionDefinitions.NORMAL:
				report+="Fitting data to Normal distribution with Mean = "+nf6.format(normal.getMean())+" and Sigma = "+nf6.format(normal.getSigma())+"\n\n";
				break;
			
			case DistributionDefinitions.EXPONENTIAL:
				report+="Fitting data to Exponential distribution with Mean = "+nf6.format(exponential.getMean())+"\n\n";
				break;
			
			case DistributionDefinitions.UNIFORMC:
				report+="Fitting data to Uniform distribution between ["+nf6.format(uniform.getStart())+","+nf6.format(uniform.getEnd())+"]\n\n";
				break;
			
			case DistributionDefinitions.CHI_SQUARE:
				report+="Fitting data to Chi-square distribution with "+nf6.format(chisquare.getDegree())+" degrees of freedom\n\n";
				break;
			
			case DistributionDefinitions.GAMMA:
				report+="Fitting data to Gamma distribution with K = "+nf6.format(gamma.getK())+" and Lambda = "+nf6.format(gamma.getLambda())+"\n\n";
				break;
				
			case DistributionDefinitions.WEIBULL:
				report+="Fitting data to Weibull distribution with K = "+nf6.format(weibull.getK())+" and Lambda = "+nf6.format(weibull.getLambda())+"\n\n";
				break;
				
			case DistributionDefinitions.LAPLACE:
				report+="Fitting data to Laplace distribution with Mean = "+nf6.format(laplace.getMean())+" and Scale = "+nf6.format(laplace.getScale())+"\n\n";
				break;

			case DistributionDefinitions.LOGISTIC:
				report+="Fitting data to Logistic distribution with Mean = "+nf6.format(logistic.getMean())+" and S = "+nf6.format(logistic.getS())+"\n\n";
				break;
		}

		report+="Dn Statistic: "+nf6.format(Dn)+"\n\n";
		
		report+="Exact p-value : <= "+nf6.format(pValue)+"\n";
	
		return report;
		
	}//end-method
	
}//end-class
