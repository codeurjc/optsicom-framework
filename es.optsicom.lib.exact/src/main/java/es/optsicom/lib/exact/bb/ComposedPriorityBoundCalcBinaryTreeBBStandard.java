package es.optsicom.lib.exact.bb;

import java.util.Arrays;
import java.util.List;

import es.optsicom.lib.Instance;
import es.optsicom.lib.util.ArraysUtil;
import es.optsicom.lib.util.Id;
import es.optsicom.lib.util.description.DescriptiveHelper;
import es.optsicom.lib.util.description.Properties;

/**
 * Calculador de cota que usa una lista ordenada de calculadores de cota.
 * Si el primer calculador de cota no poda, se usa el siguiente.
 * La cota superior se calcula como la menor de las cotas superiores.
 * @author Administrador
 *
 * @param <I>
 */
public class ComposedPriorityBoundCalcBinaryTreeBBStandard<I extends Instance>
		implements BoundCalcBinaryTreeBBStandard<I> {

	private List<BoundCalcBinaryTreeBBStandard<I>> boundCalcs;

	private int numPrones;
	private int[] numPronesByBound;
	private int numUpperBounds;
	private int[] numMinUpperBound;

	public ComposedPriorityBoundCalcBinaryTreeBBStandard(
			List<BoundCalcBinaryTreeBBStandard<I>> boundCalcs) {
		super();
		this.boundCalcs = boundCalcs;
	}
	
	public ComposedPriorityBoundCalcBinaryTreeBBStandard(
			BoundCalcBinaryTreeBBStandard<I>... boundCalcs) {
		this(Arrays.asList(boundCalcs));
	}

	@Override
	public void fixNextNode(boolean selected) {
		for (BoundCalcBinaryTreeBBStandard<I> boundCalc : boundCalcs) {
			boundCalc.fixNextNode(selected);
		}
	}

	@Override
	public void fixNode(int i, boolean selected) {
		for (BoundCalcBinaryTreeBBStandard<I> boundCalc : boundCalcs) {
			boundCalc.fixNode(i, selected);
		}
	}

	@Override
	public void freeLastFixedNode() {
		for (BoundCalcBinaryTreeBBStandard<I> boundCalc : boundCalcs) {
			boundCalc.freeLastFixedNode();
		}
	}

	@Override
	public boolean[] getFixedNodes() {
		return boundCalcs.get(0).getFixedNodes();
	}

	@Override
	public int getNumFixedNodes() {
		return boundCalcs.get(0).getNumFixedNodes();
	}

	@Override
	public int getNumSelectedNodes() {
		return boundCalcs.get(0).getNumSelectedNodes();
	}

	@Override
	public boolean[] getSelectedNodes() {
		return boundCalcs.get(0).getSelectedNodes();
	}

	@Override
	public double getUpperBound() {

		//System.out.println("UpperBound----------");
		//System.out.println(Arrays.toString(ArraysUtil.toIntArray(getFixedNodes())));
		
		numUpperBounds++;
		double[] upperBounds = new double[boundCalcs.size()];
		
		double minUpperBound = Double.MAX_VALUE;
		for (int i = 0; i < boundCalcs.size(); i++) {
			BoundCalcBinaryTreeBBStandard<I> boundCalc = boundCalcs.get(i);
			upperBounds[i] = boundCalc.getUpperBound();
			//System.out.println(boundCalc.getClass().getName()+" = "+upperBounds[i]);
			if (upperBounds[i] < minUpperBound) {
				minUpperBound = upperBounds[i];
			}	
		}

		for(int i=0; i<upperBounds.length; i++){
			if(upperBounds[i] == minUpperBound){
				numMinUpperBound[i]++;
			}
		}
		
		return minUpperBound;

	}

	@Override
	public boolean isRandomFixedAllowed() {
		return false;
	}

	@Override
	public boolean prone(double bestSolutionWeight) {

		for (int i = 0; i < boundCalcs.size(); i++) {
			BoundCalcBinaryTreeBBStandard<I> boundCalc = boundCalcs.get(i);
			boolean boundCalcProne = boundCalc.prone(bestSolutionWeight);
			if (boundCalcProne) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void setInstance(I instance) {

		// Esto es una chapuza para hacer una prueba rápida. Habría que idear un
		// mecanismo más adecuado para
		// guardar valores en un calculador de cota.
		printStatistics();

		for (BoundCalcBinaryTreeBBStandard<I> boundCalc : boundCalcs) {
			boundCalc.setInstance(instance);
		}

		this.numPrones = 0;
		this.numUpperBounds = 0;
		this.numPronesByBound = new int[boundCalcs.size()];
		this.numMinUpperBound = new int[boundCalcs.size()];
	}

	@Override
	public Properties getProperties() {
		return DescriptiveHelper.createProperties(this);
	}

	@Id
	public List<BoundCalcBinaryTreeBBStandard<I>> getBoundCalcs() {
		return boundCalcs;
	}

	public void printStatistics() {
		if (numPronesByBound != null) {
			System.out.println("NumPrones:" + numPrones);
			for (int i = 0; i < boundCalcs.size(); i++) {
				System.out.println("Prones of boundCalc ("+i+"):"
						+ boundCalcs.get(i).getClass().getSimpleName() + " = "
						+ numPronesByBound[i]);
			}
			System.out.println("NumUpperBounds:" + numUpperBounds);
			for (int i = 0; i < boundCalcs.size(); i++) {
				System.out.println("MinUpperBounds of boundCalc ("+i+"):"
						+ boundCalcs.get(i).getClass().getSimpleName() + " = "
						+ this.numMinUpperBound[i]);
			}
		}
	}
}
