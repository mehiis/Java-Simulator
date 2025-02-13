package assets.framework;

public interface EngineInterface { // UUSI
		
	// Kontrolleri käyttää tätä rajapintaa
	
	public void setSimulointiaika(double aika);
	public void setViive(long aika);
	public long getViive();
}
