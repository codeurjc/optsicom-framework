/* ******************************************************************************
 * 
 * This file is part of Optsicom
 * 
 * License:
 *   EPL: http://www.eclipse.org/legal/epl-v10.html
 *   LGPL 3.0: http://www.gnu.org/licenses/lgpl-3.0-standalone.html
 *   See the LICENSE file in the project's top-level directory for details.
 *
 * **************************************************************************** */
package es.optsicom.lib.util;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collections;

public class SortedAccumulator {

	public enum OrderMode {
		SMALLERS, BIGGERS
	}

	private static class ValueNode implements Comparable<ValueNode> {
		public int indexValue;
		boolean selected = true;
		public double value;

		public ValueNode(int indexValue, double value) {
			this.indexValue = indexValue;
			this.value = value;
		}

		public ValueNode(int indexValue, double value, boolean selected) {
			this.indexValue = indexValue;
			this.value = value;
			this.selected = selected;
		}

		public int compareTo(ValueNode valueNode) {
			return Double.compare(value, valueNode.value);
		}
	}

	private ValueNode[] values;
	private int[] indexes;
	private double sum = 0;
	private int lastSelectedValueIndex;
	private int selectedValues;
	private OrderMode orderMode;

	public SortedAccumulator(int initialSelectedValues, int numValues, OrderMode orderMode) {
		this.values = new ValueNode[numValues];
		this.indexes = new int[numValues];
		this.selectedValues = initialSelectedValues;
		this.lastSelectedValueIndex = initialSelectedValues - 1;
		this.orderMode = orderMode;
	}

	public void setValue(int indexValue, double value) {
		this.values[indexValue] = new ValueNode(indexValue, value);
	}

	public void setValue(int indexValue, double value, boolean selected) {
		this.values[indexValue] = new ValueNode(indexValue, value, selected);
	}

	/**
	 * Inicia la suma de los valores actualizada.
	 */
	public void initSumCalculation() {

		if (orderMode == OrderMode.BIGGERS) {
			Arrays.sort(values, Collections.reverseOrder());
		} else {
			Arrays.sort(values);
		}

		for (int i = 0; i < this.values.length; i++) {
			ValueNode valueNode = values[i];
			indexes[valueNode.indexValue] = i;
			if (i < selectedValues) {
				sum += valueNode.value;
			}
		}
	}

	public double getSum() {
		return sum;
	}

	/**
	 * Marca el �ndice como no seleccionado para la suma y reduce el n�mero de
	 * elementos en la suma en 1. Si el �ndice estaba participando en la suma,
	 * se resta su valor. Si no estaba participando en la suma, se resta el
	 * valor del menor valor.
	 * 
	 * @param index
	 */
	public void deselectAndDec(int index) {

		int valueIndex = this.indexes[index];

		ValueNode valueNode = this.values[valueIndex];
		valueNode.selected = false;

		if (lastSelectedValueIndex > valueIndex) {
			this.selectedValues--;
			sum -= valueNode.value;
		} else {
			decSelectedNodes();
		}
	}

	/**
	 * Marca el �ndice como no seleccionado para la suma pero no altera el
	 * n�mero de elementos de la suma. Si el �ndice estaba participando en la
	 * suma, se resta su valor y se suma el valor del siguiente para mantener
	 * igual el n�mero de elementos seleccionados. Si no estaba participando en
	 * la suma, entonces simplemente se marca como no seleccionado, sin afectar
	 * al valor de la suma. Si se da la situaci�n de que no hay m�s candidatos
	 * para participar en la selecci�n, se decrementa el n�mero de elementos
	 * seleccionados.
	 * 
	 * @param index
	 */
	public void deselect(int index) {
		int valueIndex = this.indexes[index];

		ValueNode valueNode = this.values[valueIndex];
		valueNode.selected = false;

		if (lastSelectedValueIndex >= valueIndex) {
			sum -= valueNode.value;

			// Hay que insertar uno m�s al final, para mantener
			// constante
			// el n�mero de elementos seleccionados
			insertaNuevoNodo();
		}
	}

	/**
	 * Marca el �ndice como seleccionado para la suma pero no altera el n�mero
	 * de elementos de la suma. Si el �ndice pasa a participar en la suma, se
	 * suma su valor y se resta el valor del siguiente para mantener igual el
	 * n�mero de elementos seleccionados. Si no participa en la suma, entonces
	 * simplemente se marca como seleccionado, sin afectar al valor de la suma.
	 * 
	 * @param index
	 */
	public void select(int index) {

		int valueIndex = this.indexes[index];

		ValueNode valueNode = this.values[valueIndex];
		valueNode.selected = true;

		if (lastSelectedValueIndex >= valueIndex) {
			sum += valueNode.value;

			// Hay que quitar uno al final, para mantener constante
			// el n�mero de elementos seleccionados
			eliminaNodo();
		}
	}

	/**
	 * Marca el �ndice como seleccionado para la suma y aumenta el n�mero de
	 * elementos en la suma en 1. Si el �ndice deber�a participar en la suma, se
	 * suma su valor. Si no debe participar en la suma, se suma el valor del
	 * siguiente valor.
	 * 
	 * @param index
	 */
	public void selectAndInc(int index) {

		int valueIndex = this.indexes[index];

		ValueNode valueNode = this.values[valueIndex];
		valueNode.selected = true;

		if (lastSelectedValueIndex > valueIndex) {
			this.selectedValues++;
			sum += valueNode.value;
		} else {
			incSelectedNodes();
		}
	}

	/**
	 * Incrementa el n�mero de nodos seleccionados en lo que indique el
	 * par�metro.
	 * 
	 * @param selectedNodes
	 */
	public void incSelectedNodes(int selectedNodes) {

		// System.out.println("incSelectedNodes(" + selectedNodes + ")");

		this.selectedValues += selectedNodes;
		int numNewNodes = 0;
		for (int i = lastSelectedValueIndex + 1; i < this.indexes.length; i++) {
			ValueNode aux = this.values[i];
			if (aux.selected) {
				sum += aux.value;
				numNewNodes++;
				if (numNewNodes == selectedNodes) {
					lastSelectedValueIndex = i;
					break;
				}
			}
		}

		// if (lastSelectedValueIndex == -1) {
		// throw new Error();
		// }
	}

	/**
	 * Decrementa el n�mero de nodos seleccionados en lo que indique el
	 * par�metro.
	 * 
	 * @param selectedNodes
	 */
	public void decSelectedNodes(int selectedNodes) {

		// System.out.println("decSelectedNodes(" + selectedNodes + ")");

		this.selectedValues -= selectedNodes;
		int numNewNodes = 0;

		boolean lastSelectedValueRefreshed = false;
		for (int i = lastSelectedValueIndex; i >= 0; i--) {
			ValueNode aux = this.values[i];
			if (aux.selected) {
				if (numNewNodes == selectedNodes) {
					lastSelectedValueRefreshed = true;
					lastSelectedValueIndex = i;
					break;
				} else {
					sum -= aux.value;
					numNewNodes++;
				}
			}
		}

		if (!lastSelectedValueRefreshed) {
			lastSelectedValueIndex = -1;
		}
	}

	/**
	 * Incrementa el uno el n�mero de nodos seleccionados para hacer la suma y
	 * actualiza la suma en consecuencia.
	 */
	public void incSelectedNodes() {
		this.selectedValues++;
		insertaNuevoNodo();
	}

	private void insertaNuevoNodo() {
		boolean addedNode = false;
		for (int i = lastSelectedValueIndex + 1; i < this.indexes.length; i++) {
			ValueNode aux = this.values[i];
			if (aux.selected) {
				addedNode = true;
				lastSelectedValueIndex = i;
				sum += aux.value;
				break;
			}
		}

		if (!addedNode) {
			this.selectedValues--;
		}
		// if(lastSelectedValueIndex == 29){
		// System.out.println("HOLA");
		// }
	}

	/**
	 * Decrementa en uno el n�mero de nodos seleccionados para hacer la suma y
	 * actualiza la suma en consecuencia.
	 */
	public void decSelectedNodes() {
		this.selectedValues--;
		eliminaNodo();
	}

	private void eliminaNodo() {
		ValueNode lastValueNode = this.values[lastSelectedValueIndex];
		sum -= lastValueNode.value;

		boolean lastSelectedValueRefreshed = false;
		for (int j = lastSelectedValueIndex - 1; j >= 0; j--) {
			ValueNode aux = this.values[j];
			if (aux.selected) {
				lastSelectedValueRefreshed = true;
				lastSelectedValueIndex = j;
				break;
			}
		}
		if (!lastSelectedValueRefreshed) {
			lastSelectedValueIndex = -1;
		}

	}

	public int getSelectedValues() {
		return selectedValues;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int numValue = 0; numValue < values.length; numValue++) {
			if (values[numValue].selected) {
				sb.append(NumberFormat.getInstance().format(values[numValue].value));
			} else {
				sb.append("*");
				sb.append(NumberFormat.getInstance().format(values[numValue].value));
				sb.append("*");
			}
			sb.append(":" + values[numValue].indexValue);
			if (this.lastSelectedValueIndex == numValue) {
				sb.append(">>>>>>>>");
			} else {
				sb.append("|");
			}
		}
		sb.append("]");

		sb.append(" selectedValues=").append(this.selectedValues);
		sb.append(" sum=").append(sum);
		sb.append(" lastSelectedValueIndex = " + lastSelectedValueIndex);
		return sb.toString();
	}

	public boolean isSelected(int index) {
		return values[this.indexes[index]].selected;
	}

	/**
	 * Establece el n�mero de elementos seleccionados que tienen que estar
	 * activos para la suma.
	 * 
	 * @param numSelected
	 */
	public void setSelected(int numSelected) {
		if (this.selectedValues == numSelected) {
			return;
		} else if (this.selectedValues > numSelected) {
			// System.out.println("DecSelectedNodes: "+(this.selectedValues -
			// numSelected));
			decSelectedNodes(this.selectedValues - numSelected);
		} else {
			// System.out.println("IncSelectedNodes: "+(numSelected -
			// this.selectedValues));
			incSelectedNodes(numSelected - this.selectedValues);
		}
	}

}
