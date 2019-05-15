package pl.polskieligi.log.distance;

import org.apache.commons.text.similarity.LevenshteinDistance;

import java.text.Normalizer;
import java.util.*;

public abstract class DistanceLogic<W, P> {
	
	private static final LevenshteinDistance levenshteinDistance = LevenshteinDistance.getDefaultInstance();
	
	protected abstract Integer getWebId(W w);
	protected abstract Long getPersId(P p);
	
	public List<Distance<W, P>> findMatchings(Set<W> webObjects, Set<P> persObjects) {
		List<Distance<W, P>> distances = calculateDistances(webObjects, persObjects);
		Set<Long> processedTeams = new HashSet<Long>();
		Set<Integer> processedLnpTeams = new HashSet<Integer>();

		List<Distance<W, P>> result = new ArrayList<Distance<W, P>>();
		Double currentDistance = Double.MIN_VALUE;
		List<Distance<W, P>> currentDistances = new ArrayList<Distance<W, P>>();
		for(Distance<W, P> d: distances) {
			if(currentDistance<d.getDistance()) {
				for(Distance<W, P> cd: currentDistances) {
					processedTeams.add(getPersId(cd.getPersObject()));
					processedLnpTeams.add(getWebId(cd.getWebObject()));
					long countLnpTeam = currentDistances.stream().filter(a->getWebId(a.getWebObject()).equals(getWebId(cd.getWebObject()))).count();
					long countTeam = currentDistances.stream().filter(a->getPersId(a.getPersObject()).equals(getPersId(cd.getPersObject()))).count();
					if(countLnpTeam==1 && countTeam==1) {
						result.add(cd);
					}
				}

				currentDistance = d.getDistance();
				currentDistances.clear();
			}
			if(!processedTeams.contains(getPersId(d.getPersObject())) && !processedLnpTeams.contains(getWebId(d.getWebObject()))) {
				currentDistances.add(d);
			}
		}
		if(currentDistances.size()>0) {
			for(Distance<W, P> cd: currentDistances) {
				long countLnpTeam = currentDistances.stream().filter(a->getWebId(a.getWebObject()).equals(getWebId(cd.getWebObject()))).count();
				long countTeam = currentDistances.stream().filter(a->getPersId(a.getPersObject()).equals(getPersId(cd.getPersObject()))).count();
				if(countLnpTeam==1 && countTeam==1) {
					result.add(cd);
				}
			}
		}
		return result;
	}

	private List<Distance<W, P>> calculateDistances(Set<W> lnpTeams, Set<P> teamLeagueList){
		List<Distance<W, P>> distances = new ArrayList<Distance<W, P>>();
		for(P tl: teamLeagueList) {
			for(W lt: lnpTeams) {
				Double distance = getDistance(lt, tl);
				distances.add(new Distance<W, P>(distance, lt, tl));
				if (distance == 0) {
					continue;
				}
			}
		}
		distances.sort(Comparator.comparing(a -> a.getDistance()));
		return distances;
	}


	protected abstract Double getDistance(W w, P p);

	protected Double getDistance(String t1, String t2) {
		Double min = new Double(Math.min(t1.length(), t2.length()));
		Double max = new Double(Math.max(t1.length(), t2.length()));
		Integer distance = levenshteinDistance.apply(normalize(t1), normalize(t2));
		Double result = (distance - (max - min)) / min;
		if (result == 0) {
			result = distance / max;
		} else {
			result = result + 1;
		}
		return result;
	}
	
	private String normalize(String s) {
		return  Normalizer
		           .normalize(s.toLowerCase(), Normalizer.Form.NFD)
		           .replaceAll("[^\\p{ASCII}]", "");
	}
}
