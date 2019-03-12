package pl.polskieligi.log.comparator;

import pl.polskieligi.dto.TableRow;

import java.util.Comparator;

public class TableRowComparator implements Comparator<TableRow> {

	@Override
	public int compare(TableRow tr1, TableRow tr2) {
		if (tr1.getPoints() > tr2.getPoints()) {
			return -1;
		} else if (tr1.getPoints() < tr2.getPoints()) {
			return 1;
		} else if (tr1.getGoalsScored() - tr1.getGoalsAgainst() > tr2.getGoalsScored() - tr2.getGoalsAgainst()) {
			return -1;
		} else if (tr1.getGoalsScored() - tr1.getGoalsAgainst() < tr2.getGoalsScored() - tr2.getGoalsAgainst()) {
			return 1;
		} else if (tr1.getGoalsScored() > tr2.getGoalsScored()) {
			return -1;
		} else if (tr1.getGoalsScored() < tr2.getGoalsScored()) {
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
