package pl.polskieligi.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pl.polskieligi.dao.LeagueDAO;
import pl.polskieligi.model.League;
import pl.polskieligi.model.LeagueType;
import pl.polskieligi.model.Region;

import javax.persistence.TypedQuery;

@Repository
@Transactional
public class LeagueDAOImpl extends AbstractDAOImpl<League> implements LeagueDAO {

	public LeagueDAOImpl() {
		super(League.class);
	}

	@Override
	protected TypedQuery<League> getRetrieveQuery(League league) {
		TypedQuery<League> query = getEntityManager().createQuery("SELECT l from League l where name = :name", League.class);
		query.setParameter("name", league.getName());
		return query;
	}
	
	public void updateRegions() {
		for(League l: findAll()) {
			l.setRegion(Region.getRegionByProjectName(l.getName()).getId());
			update(l);
		}
	}
	public void updateLeagueTypes() {
		for(League l: findAll()) {
			l.setLeagueType(LeagueType.getByLeagueName(l.getName()).getId());
			update(l);
		}
	}
	
	public void updateGroups() {
		for(League l: findAll()) {
			if(l.getName().contains("grupa: ")){
				l.setGroupName(l.getName().split("grupa: ")[1]);
				update(l);
			}
		}
	}
	
}
