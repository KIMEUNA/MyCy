package com.survey;

import java.util.Arrays;

public class SurveyDTO {
	
	private int seq;
	private String id;
	private String title;
	private int hit;
	private String[] quest = new String[5];
	private int choice;
	private String enddate, regdate;
	
	
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getHit() {
		return hit;
	}
	public void setHit(int hit) {
		this.hit = hit;
	}
	public String getQuest(int i) {
		return quest[i];
	}
	public void setQuest(String quest, int i) {
		this.quest[i] = quest;
	}
	public int getQuestLength() {
		return quest.length;
	}
	public int getChoice() {
		return choice;
	}
	public void setChoice(int choice) {
		this.choice = choice;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public String getRegdate() {
		return regdate;
	}
	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}
	
	@Override
	public String toString() {
		return "SurveyDTO [seq=" + seq + ", id=" + id + ", title=" + title + ", hit=" + hit + ", quest="
				+ Arrays.toString(quest) + ", choice=" + choice + ", enddate=" + enddate + ", regdate=" + regdate + "]";
	}
	
	
	
	
	

}
