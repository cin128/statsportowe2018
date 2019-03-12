package pl.polskieligi.log.comparator;

import pl.polskieligi.dto.TableRow;

import java.util.Comparator;

public class TableRowAwayComparator implements Comparator<TableRow> {

	@Override
	public int compare(TableRow tr1, TableRow tr2) {
		if (tr1.getPointsAway() > tr2.getPointsAway()) {
			return -1;
		} else if (tr1.getPointsAway() < tr2.getPointsAway()) {
			return 1;
		} else if (tr1.getGoalsScoredAway() - tr1.getGoalsAgainstAway() > tr2.getGoalsScoredAway() - tr2.getGoalsAgainstAway()) {
			return -1;
		} else if (tr1.getGoalsScoredAway() - tr1.getGoalsAgainstAway() < tr2.getGoalsScoredAway() - tr2.getGoalsAgainstAway()) {
			return 1;
		} else if (tr1.getGoalsScoredAway() > tr2.getGoalsScoredAway()) {
			return -1;
		} else if (tr1.getGoalsScoredAway() < tr2.getGoalsScoredAway()) {
			return 1;
		} else if (tr1.getSequence() > tr2.getSequence()) {
			return 1;
		} else if (tr1.getSequence() < tr2.getSequence()) {
			return -1;
		} else {
			return 0;
		}
	}
}
