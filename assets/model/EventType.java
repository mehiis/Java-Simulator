package assets.model;

import assets.framework.IEventType;

// TODO:
// Tapahtumien tyypit määritellään simulointimallin vaatimusten perusteella
public enum EventType implements IEventType {
	ARRIVE_TO_SHELTER, THROW_TRASH, DEP2, DEP3;

}
