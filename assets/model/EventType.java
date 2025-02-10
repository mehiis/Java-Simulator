package assets.model;

import assets.framework.IEventType;

// TODO:
// Tapahtumien tyypit määritellään simulointimallin vaatimusten perusteella
public enum EventType implements IEventType {
	ARRIVE_TO_SHELTER, THROW_TRASH, CAR_ARRIVE, CAR_COLLECT, CAR_MOVE;
}
