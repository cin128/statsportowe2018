package pl.polskieligi.log.comparator;

import pl.polskieligi.dto.TableRow;

import java.util.Comparator;

public class TableRowHomeComparator implements Comparator<TableRow> {

	@Override
	public int compare(TableRow tr1, TableRow tr2) {
		if (tr1.getPointsHome() > tr2.getPointsHome()) {
			return -1;
		} else if (tr1.getPointsHome() < tr2.getPointsHome()) {
			return 1;
		} else if (tr1.getGoalsScoredHome() - tr1.getGoalsAgainstHome() > tr2.getGoalsScoredHome() - tr2.getGoalsAgainstHome()) {
			return -1;
		} else if (tr1.getGoalsScoredHome() - tr1.getGoalsAgainstHome() < tr2.getGoalsScoredHome() - tr2.getGoalsAgainstHome()) {
			return 1;
		} else if (tr1.getGoalsScoredHome() > tr2.getGoalsScoredHome()) {
			return -1;
		} else if (tr1.getGoalsScoredHome() < tr2.getGoalsScoredHome()) {
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
