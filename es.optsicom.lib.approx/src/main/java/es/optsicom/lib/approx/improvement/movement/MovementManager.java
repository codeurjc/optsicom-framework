package es.optsicom.lib.approx.improvement.movement;

public interface MovementManager {

	void testMovement(double weight, Object movementAttributes);

	void finishMovementGroup();

	boolean canTestMovement(Object movementAttributes);

}
