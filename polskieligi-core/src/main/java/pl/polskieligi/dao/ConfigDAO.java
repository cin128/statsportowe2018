package pl.polskieligi.dao;

import pl.polskieligi.model.Config;

public interface ConfigDAO  extends AbstractDAO<Config>{

	Config findByName(String name);

}
