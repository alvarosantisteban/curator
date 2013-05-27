package com.alvarosantisteban.berlincurator;

public class EventLoaderFactory {

	public static EventLoader newArtParasitesEventLoader() {
		return new ArtParasitesEventLoader();
	}

	public static EventLoader newIHeartBerlinEventLoader() {
		return new IHeartBerlinEventLoader();
	}
	
	public static EventLoader newMetalConcertsEventLoader() {
		return new MetalConcertsEventLoader();
	}

	public static EventLoader newWhiteTrashEventLoader() {
		return new WhiteTrashEventLoader();
	}
	
	public static EventLoader newKoepiEventLoader() {
		return new KoepiEventLoader();
	}
}
