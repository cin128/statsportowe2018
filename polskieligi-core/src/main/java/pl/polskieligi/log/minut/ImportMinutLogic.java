package pl.polskieligi.log.minut;

import pl.polskieligi.model.MinutObject;

public interface ImportMinutLogic<T extends MinutObject> {
	T doImport(Integer playerMinutId);
}
