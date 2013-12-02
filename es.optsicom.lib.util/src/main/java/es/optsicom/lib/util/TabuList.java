/*******************************************************************************
 * Copyright (c) Gavab Research Group. Universidad Rey Juan Carlos.
 * http://www.gavab.es
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package es.optsicom.lib.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Patxi Gortázar
 * 
 */
public class TabuList<T extends Comparable<T>> {

	private int tabuTenure;
	List<T> tabuElements = new ArrayList<T>();
	Map<T, Integer> generationsInTabuList = new HashMap<T, Integer>();

	public TabuList(int tabuTenure) {
		this.tabuTenure = tabuTenure;
	}

	public void setTabuTenure(int tabuTenure) {
		this.tabuTenure = tabuTenure;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#add(java.lang.Object)
	 */
	public void add(T e) {
		int index = Collections.binarySearch(tabuElements, e);
		if (index >= 0) {
			// No se admiten duplicados
			return;
		}
		tabuElements.add(-(index + 1), e);
		// 0 to (tabuTenure - 1) iterations 
		generationsInTabuList.put(e, tabuTenure);
	}

	public boolean contains(T e) {
		return Collections.binarySearch(tabuElements, e) >= 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#size()
	 */
	public int size() {
		return tabuElements.size();
	}

	public void finishIteration() {
		List<T> toDelete = new ArrayList<T>();
		for (Entry<T, Integer> entry : generationsInTabuList.entrySet()) {
			if (entry.getValue() <= 0) {
				toDelete.add(entry.getKey());
			} else {
				entry.setValue(entry.getValue() - 1);
			}
		}

		for (T t : toDelete) {
			tabuElements.remove(Collections.binarySearch(tabuElements, t));
			generationsInTabuList.remove(t);
		}
	}

	/**
	 * @return
	 */
	public Collection<T> getAllElements() {
		return tabuElements;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((tabuElements == null) ? 0 : tabuElements.hashCode());
		result = prime * result + tabuTenure;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TabuList other = (TabuList) obj;
		if (tabuElements == null) {
			if (other.tabuElements != null)
				return false;
		} else if (!tabuElements.equals(other.tabuElements))
			return false;
		if (tabuTenure != other.tabuTenure)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TabuList [tabuElements=" + tabuElements + ", tabuTenure="
				+ tabuTenure + "]";
	}
	
	
}
