package pl.polskieligi.dao;

import java.util.List;

import pl.polskieligi.dto.TableRow;

public interface TableDAO {
	public List<TableRow> getTableRows(Long projectId);
}
