package application.assets.model;

import application.assets.framework.IEventType;

// TODO:
// Tapahtumien tyypit määritellään simulointimallin vaatimusten perusteella
public enum EventType implements IEventType {
	YKSIO_ARRIVE_TO_SHELTER,
	KAKSIO_ARRIVE_TO_SHELTER,
	KOLMIO_ARRIVE_TO_SHELTER,
	NELIO_ARRIVE_TO_SHELTER,
	EXIT,
	CLEAR_GARBAGE_FROM_SHELTER
}
