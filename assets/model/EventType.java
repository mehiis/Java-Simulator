package assets.model;

import assets.framework.IEventType;

// TODO:
// Tapahtumien tyypit määritellään simulointimallin vaatimusten perusteella
public enum EventType implements IEventType {
	ARRIVE_TO_SHELTER,
	EXIT,
	CLEAR_GARBAGE_FROM_SHELTER
}
