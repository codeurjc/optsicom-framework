package es.optsicom.lib.exact.bb;

import es.optsicom.lib.Instance;
import es.optsicom.lib.util.Id;
import es.optsicom.lib.util.description.DescriptiveHelper;
import es.optsicom.lib.util.description.Properties;

public class Composed2BoundCalcBinaryTreeBBStandard<I extends Instance> implements BoundCalcBinaryTreeBBStandard<I> {

	private BoundCalcBinaryTreeBBStandard<I> mainBoundCalc;
	private BoundCalcBinaryTreeBBStandard<I> secBoundCalc;
	
	private int numPrones;
	private int numBetterMainPrones;
	private int numBetterSecPrones;
	
	public Composed2BoundCalcBinaryTreeBBStandard(
			BoundCalcBinaryTreeBBStandard<I> mainBoundCalc,
			BoundCalcBinaryTreeBBStandard<I> secBoundCalc) {
		super();
		this.mainBoundCalc = mainBoundCalc;
		this.secBoundCalc = secBoundCalc;
	}

	@Override
	public void fixNextNode(boolean selected) {
		mainBoundCalc.fixNextNode(selected);
		secBoundCalc.fixNextNode(selected);
	}

	@Override
	public void fixNode(int i, boolean selected) {
		mainBoundCalc.fixNode(i,selected);
		secBoundCalc.fixNode(i,selected);
	}

	@Override
	public void freeLastFixedNode() {
		mainBoundCalc.freeLastFixedNode();
		secBoundCalc.freeLastFixedNode();
	}

	@Override
	public boolean[] getFixedNodes() {
		return mainBoundCalc.getFixedNodes();
	}

	@Override
	public int getNumFixedNodes() {
		return mainBoundCalc.getNumFixedNodes();
	}

	@Override
	public int getNumSelectedNodes() {
		return mainBoundCalc.getNumSelectedNodes();
	}

	@Override
	public boolean[] getSelectedNodes() {
		return mainBoundCalc.getSelectedNodes();
	}

	@Override
	public double getUpperBound() {
		double mainUpperBound = mainBoundCalc.getUpperBound();
		double secUpperBound = secBoundCalc.getUpperBound();
		
		double diff = mainUpperBound - secUpperBound;
		
		if(diff != 0){
			System.out.println(diff);
		}
		
		return mainUpperBound;
	}

	@Override
	public boolean isRandomFixedAllowed() {
		return mainBoundCalc.isRandomFixedAllowed() && secBoundCalc.isRandomFixedAllowed();
	}

	@Override
	public boolean prone(double bestSolutionWeight) {
		boolean mainProne = mainBoundCalc.prone(bestSolutionWeight);
		boolean secProne = secBoundCalc.prone(bestSolutionWeight);
		
		numPrones++;
		if(mainProne && !secProne){
			numBetterMainPrones++;
		} else if(!mainProne && secProne){
			numBetterSecPrones++;
		} 
		
		return mainProne;
	}

	@Override
	public void setInstance(I instance) {
		
		this.printStatistics();
		
		this.mainBoundCalc.setInstance(instance);
		this.secBoundCalc.setInstance(instance);
		
		this.numPrones = 0;
		this.numBetterMainPrones = 0;
		this.numBetterSecPrones = 0;		
	}

	@Override
	public Properties getProperties() {
		return DescriptiveHelper.createProperties(this);
	}

	@Id
	public BoundCalcBinaryTreeBBStandard<I> getMainBoundCalc() {
		return mainBoundCalc;
	}
	
	@Id
	public BoundCalcBinaryTreeBBStandard<I> getSecBoundCalc() {
		return secBoundCalc;
	}

	public void printStatistics() {
		System.out.println("NumPrones:"+numPrones);
		System.out.println("NumBetterMainPrones:"+numBetterMainPrones);
		System.out.println("NumBetterSecPrones:"+numBetterSecPrones);
	}
}
