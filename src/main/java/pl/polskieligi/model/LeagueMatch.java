package pl.polskieligi.model;

import java.sql.Timestamp;

import javax.persistence.*;

@Entity
@Table(indexes = { @Index(name = "IDX_LM_PR_RO_MA", columnList = "project_id,round_id,matchpart1,matchpart2", unique = false) })
public class LeagueMatch {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long match_id;
/*	private Integer match_number;*/
	private Integer playground_id;
	private Timestamp match_date;
	private Long matchpart1;
	private Long matchpart2;
	private Float matchpart1_result;
	private Float matchpart2_result;
	/*private Integer matchpart1_bonus;
	private Integer matchpart2_bonus;
	private Float matchpart1_legs;
	private Float matchpart2_legs;
	private String matchpart1_result_split;
	private String matchpart2_result_split;
	private Boolean match_result_type;
	private Float matchpart1_result_ot;
	private Float matchpart2_result_ot;
	private Boolean alt_decision;
	private Float matchpart1_result_decision;
	private Float matchpart2_result_decision;
	private String decision_info;*/
	private Long project_id;
	private Long round_id;
	private Boolean count_result;
	private Boolean published;
	private Integer crowd;
/*	private Integer referee_id;
	private Integer checked_out;
	private Timestamp checked_out_time;*/
	private String summary;
/*	private Boolean show_report;
	private String match_result_detail;*/
	private Timestamp modified;
/*	private Integer modified_by;
	private String formation1;
	private String formation2;*/

	private Long af_id;

	public LeagueMatch() {
/*		match_number = 0;*/
		playground_id = 0;
		match_date = new Timestamp(0);
		matchpart1 = Long.valueOf(0);
		matchpart2 = Long.valueOf(0);
		matchpart1_result = new Float(0);
		matchpart2_result = new Float(0);
/*		matchpart1_bonus = 0;
		matchpart2_bonus = 0;
		matchpart1_legs = new Float(0);
		matchpart2_legs = new Float(0);
		matchpart1_result_split = ";";
		matchpart2_result_split = ";";
		match_result_type = false;
		matchpart1_result_ot = new Float(0);
		matchpart2_result_ot = new Float(0);
		alt_decision = false;
		matchpart1_result_decision = new Float(0);
		matchpart2_result_decision = new Float(0);
		decision_info = "";*/
		project_id = Long.valueOf(0);
		round_id = Long.valueOf(0);
		count_result = false;
		published = false;
		crowd = 0;
/*		referee_id = 0;
		checked_out = 0;
		checked_out_time = new Timestamp(0);*/
		summary = "";
/*		show_report = false;
		match_result_detail = "";*/
		modified = new Timestamp(0);
/*		modified_by = 0;
		formation1 = "";
		formation2 = "";*/
		af_id = Long.valueOf(0);
	}

	public Long getMatch_id() {
		return match_id;
	}

	public void setMatch_id(Long match_id) {
		this.match_id = match_id;
	}

/*	public Integer getMatch_number() {
		return match_number;
	}

	public void setMatch_number(Integer match_number) {
		this.match_number = match_number;
	}*/

	public Integer getPlayground_id() {
		return playground_id;
	}

	public void setPlayground_id(Integer playground_id) {
		this.playground_id = playground_id;
	}

	public Timestamp getMatch_date() {
		return match_date;
	}

	public void setMatch_date(Timestamp match_date) {
		this.match_date = match_date;
	}

	public Long getMatchpart1() {
		return matchpart1;
	}

	public void setMatchpart1(Long matchpart1) {
		this.matchpart1 = matchpart1;
	}

	public Long getMatchpart2() {
		return matchpart2;
	}

	public void setMatchpart2(Long matchpart2) {
		this.matchpart2 = matchpart2;
	}

	public Float getMatchpart1_result() {
		return matchpart1_result;
	}

	public void setMatchpart1_result(Float matchpart1_result) {
		this.matchpart1_result = matchpart1_result;
	}

	public Float getMatchpart2_result() {
		return matchpart2_result;
	}

	public void setMatchpart2_result(Float matchpart2_result) {
		this.matchpart2_result = matchpart2_result;
	}

	/*public Integer getMatchpart1_bonus() {
		return matchpart1_bonus;
	}

	public void setMatchpart1_bonus(Integer matchpart1_bonus) {
		this.matchpart1_bonus = matchpart1_bonus;
	}

	public Integer getMatchpart2_bonus() {
		return matchpart2_bonus;
	}

	public void setMatchpart2_bonus(Integer matchpart2_bonus) {
		this.matchpart2_bonus = matchpart2_bonus;
	}

	public Float getMatchpart1_legs() {
		return matchpart1_legs;
	}

	public void setMatchpart1_legs(Float matchpart1_legs) {
		this.matchpart1_legs = matchpart1_legs;
	}

	public Float getMatchpart2_legs() {
		return matchpart2_legs;
	}

	public void setMatchpart2_legs(Float matchpart2_legs) {
		this.matchpart2_legs = matchpart2_legs;
	}

	public String getMatchpart1_result_split() {
		return matchpart1_result_split;
	}

	public void setMatchpart1_result_split(String matchpart1_result_split) {
		this.matchpart1_result_split = matchpart1_result_split;
	}

	public String getMatchpart2_result_split() {
		return matchpart2_result_split;
	}

	public void setMatchpart2_result_split(String matchpart2_result_split) {
		this.matchpart2_result_split = matchpart2_result_split;
	}

	public Boolean getMatch_result_type() {
		return match_result_type;
	}

	public void setMatch_result_type(Boolean match_result_type) {
		this.match_result_type = match_result_type;
	}

	public Float getMatchpart1_result_ot() {
		return matchpart1_result_ot;
	}

	public void setMatchpart1_result_ot(Float matchpart1_result_ot) {
		this.matchpart1_result_ot = matchpart1_result_ot;
	}

	public Float getMatchpart2_result_ot() {
		return matchpart2_result_ot;
	}

	public void setMatchpart2_result_ot(Float matchpart2_result_ot) {
		this.matchpart2_result_ot = matchpart2_result_ot;
	}

	public Boolean getAlt_decision() {
		return alt_decision;
	}

	public void setAlt_decision(Boolean alt_decision) {
		this.alt_decision = alt_decision;
	}

	public Float getMatchpart1_result_decision() {
		return matchpart1_result_decision;
	}

	public void setMatchpart1_result_decision(Float matchpart1_result_decision) {
		this.matchpart1_result_decision = matchpart1_result_decision;
	}

	public Float getMatchpart2_result_decision() {
		return matchpart2_result_decision;
	}

	public void setMatchpart2_result_decision(Float matchpart2_result_decision) {
		this.matchpart2_result_decision = matchpart2_result_decision;
	}

	public String getDecision_info() {
		return decision_info;
	}

	public void setDecision_info(String decision_info) {
		this.decision_info = decision_info;
	}*/

	public Long getProject_id() {
		return project_id;
	}

	public void setProject_id(Long project_id) {
		this.project_id = project_id;
	}

	public Long getRound_id() {
		return round_id;
	}

	public void setRound_id(Long round_id) {
		this.round_id = round_id;
	}

	public Boolean getCount_result() {
		return count_result;
	}

	public void setCount_result(Boolean count_result) {
		this.count_result = count_result;
	}

	public Boolean getPublished() {
		return published;
	}

	public void setPublished(Boolean published) {
		this.published = published;
	}

	public Integer getCrowd() {
		return crowd;
	}

	public void setCrowd(Integer crowd) {
		this.crowd = crowd;
	}

	/*public Integer getReferee_id() {
		return referee_id;
	}

	public void setReferee_id(Integer referee_id) {
		this.referee_id = referee_id;
	}

	public Integer getChecked_out() {
		return checked_out;
	}

	public void setChecked_out(Integer checked_out) {
		this.checked_out = checked_out;
	}

	public Timestamp getChecked_out_time() {
		return checked_out_time;
	}

	public void setChecked_out_time(Timestamp checked_out_time) {
		this.checked_out_time = checked_out_time;
	}*/

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	/*public Boolean getShow_report() {
		return show_report;
	}

	public void setShow_report(Boolean show_report) {
		this.show_report = show_report;
	}

	public String getMatch_result_detail() {
		return match_result_detail;
	}

	public void setMatch_result_detail(String match_result_detail) {
		this.match_result_detail = match_result_detail;
	}*/

	public Timestamp getModified() {
		return modified;
	}

	public void setModified(Timestamp modified) {
		this.modified = modified;
	}

	/*public Integer getModified_by() {
		return modified_by;
	}

	public void setModified_by(Integer modified_by) {
		this.modified_by = modified_by;
	}

	public String getFormation1() {
		return formation1;
	}

	public void setFormation1(String formation1) {
		this.formation1 = formation1;
	}

	public String getFormation2() {
		return formation2;
	}

	public void setFormation2(String formation2) {
		this.formation2 = formation2;
	}*/

	public Long getAf_id() {
		return af_id;
	}

	public void setAf_id(Long af_id) {
		this.af_id = af_id;
	}

	@Override public String toString() {
		return match_id+" " + match_date + " " + matchpart1 + " " + matchpart1_result + " - " + matchpart2_result + " " + matchpart2;
	}
}
