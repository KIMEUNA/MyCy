package com.survey;

public class Survey_Sub {

	private int seq;
	private String id;
	private int[] answer = new int[5];
	
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
	public int getAnswer(int i) {
		return answer[i];
	}
	public void setAnswer(int answer, int i) {
		this.answer[i] = answer;
	}
	public int getAnswerLength() {
		return answer.length;
	}
	
	
}
