package pl.polskieligi.controller.view;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.polskieligi.dao.TeamDAO;
import pl.polskieligi.model.Team;

@Controller
public class AutocompleteController {

	@Autowired
	private TeamDAO teamDAO;
	
	@RequestMapping(value = "/teams", method = RequestMethod.GET)
	public @ResponseBody
	List<Entry> geTeams(String team) {
		List<Team> list = teamDAO.getMatchingTeams(team);
		
		List<Entry> res = new ArrayList<Entry>();
		for(Team t: list) {
			res.add(new Entry(t.getName(), t.getId().toString()));
		}
		return res;
	}
	
	
	private class Entry{
		String label;
		String value;
		Entry(String label, String value){
			this.label=label;
			this.value=value;
		}
		public String getLabel() {
			return label;
		}
		public void setLabel(String label) {
			this.label = label;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		
	}
}
