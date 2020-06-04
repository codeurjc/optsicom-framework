package es.optsicom.lib.approx.improvement.movement.info;

import java.util.List;

import es.optsicom.lib.Solution;

public interface MovementSelectorByInfo<S extends Solution<?>, IN> {

	// public abstract int compare(IN info1, Object movement1, IN info2,
	// Object movement2);

	public abstract IN selectMovementToApply(List<IN> infos);

	public void startNewIteration(S solution);

	// This is used in Mode.FIRST to determine if the movement have to be
	// applied.
	public boolean isImprovement(IN info);

	// This is used in TabuSearch to know if movement met the aspiration
	// criteria
	public boolean isBetterThan(IN info, S otherSolution);

	// Tenemos que cambiar la forma en la que se gestionan los movimientos en
	// las siguientes cuestiones:
	// * Estudiar isImprovement(...) para ver si necesitamos pasar la solución
	// original. Esto es necesario para saber si realmente la info supone una
	// mejora o no. Esto depende del MovementSelector porque es el único que
	// conoce la info condificada.
	// * Hay que cambiar los algoritmos para no se haga el cálculo del MEJOR de
	// forma incremental. Ahora hay que hacerlo con el método
	// selectMovementToApply
	// de esta misma clase. Eso permitirá aplicar criterios globales a la
	// selección,
	// no sólo comparar dos movimientos entre sí.
	// * Hay que hacer una prueba haciendo que Info sea Double como se estaba
	// usando ahora. Así podemos hacer pruebas y ver si estas nuevas
	// implementaciones
	// son correctas.
}
