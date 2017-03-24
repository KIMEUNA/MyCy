package com.survey;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class SurveyDAO {
	private Connection conn = DBConn.getConnection();

	public void insertSurvey(SurveyDTO dto) {
		PreparedStatement pstmt = null;
		String sql;

		try {

			sql = "insert into survey_board(seq, id, title, question1, question2, question3, question4, question5, choice, enddate) "
					+ "values(survey_seq.nextval, ?, ?, ?, ?, ?, ?, ?, ?, to_date(?, 'yyyy-mm-dd hh:mi'))";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, dto.getId());
			pstmt.setString(2, dto.getTitle());
			for (int i = 0; i < dto.getQuestLength(); i++) {
				pstmt.setString(i + 3, dto.getQuest(i));
			}
			pstmt.setInt(8, dto.getChoice());
			pstmt.setString(9, dto.getEnddate());

			pstmt.executeUpdate();

		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			try {
				pstmt.close();
			} catch (Exception e2) {	}
		}
	}

	public int dataCount() {
		int res = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT COUNT(*) FROM survey_board";
			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();

			if (rs.next())
				res = rs.getInt(1);

		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			try {
				rs.close();
				pstmt.close();
			} catch (Exception e2) {	}
		}

		return res;

	}

	public List<SurveyDTO> listBoard(int start, int end) {
		List<SurveyDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();

		try {
			sb.append("SELECT * FROM (");
			sb.append("    SELECT ROWNUM rnum, tb.* FROM (");
			sb.append("        SELECT seq, id, title, hit, ");
			sb.append("            TO_CHAR(enddate, 'yy-mm-dd hh:mi') enddate, ");
			sb.append("            TO_CHAR(regdate, 'yy-mm-dd hh:mi') regdate ");
			sb.append("        	 FROM survey_board ");
			sb.append("        	 ORDER BY seq DESC ");
			sb.append("    ) tb WHERE ROWNUM <= ? ");
			sb.append(") WHERE rnum >= ? ");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, end);
			pstmt.setInt(2, start);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				SurveyDTO dto = new SurveyDTO();

				dto.setSeq(rs.getInt("seq"));
				dto.setId(rs.getString("id"));
				dto.setTitle(rs.getString("title"));
				dto.setHit(rs.getInt("hit"));
				dto.setEnddate(rs.getString("enddate"));
				dto.setRegdate(rs.getString("regdate"));

				list.add(dto);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			try {
				rs.close();
				pstmt.close();
			} catch (Exception e2) {	}
		}

		return list;
	}
	
	public void updateHit(int seq) {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "update survey_board set hit = hit + 1 "
				 + "where seq = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, seq);
			pstmt.executeQuery();
			
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			try {
				pstmt.close();
			} catch (Exception e2) {	}
		}
	}
	
	public SurveyDTO getSurvey(int seq) {
		SurveyDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			sb.append("select id, title, hit, question1, question2, question3, question4, question5, choice, ");
			sb.append("to_char(enddate, 'yy/mm/dd hh:mi') enddate, to_char(regdate, 'yy/mm/dd') regdate ");
			sb.append("from survey_board ");
			sb.append("where seq = ?");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, seq);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new SurveyDTO();
				
				dto.setSeq(seq);
				dto.setId(rs.getString("id"));
				dto.setTitle(rs.getString("title"));
				dto.setHit(rs.getInt("hit"));
				
				for (int i = 0; i < dto.getQuestLength(); i++) {
					dto.setQuest(rs.getString(i+4), i);
				}
				
				dto.setChoice(rs.getInt("choice"));
				dto.setEnddate(rs.getString("enddate"));
				dto.setRegdate(rs.getString("regdate"));
			}
			
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			try {
				rs.close();
				pstmt.close();
			} catch (Exception e2) {	}
		}
		
		return dto;
	}
	
	public int Chk(int seq, String id) {
		int res = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "select count(*) from survey_sub "
				 + "where seq = ? and id = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, seq);
			pstmt.setString(2, id);
			
			rs = pstmt.executeQuery();
			if(rs.next())
				res = rs.getInt(1);
			
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			try {
				rs.close();
				pstmt.close();
			} catch (Exception e2) {}
		}
		
		return res;
	}
	
	public void insertAnswer(Survey_Sub dto) {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "insert into survey_sub(seq, id, answer1, answer2, answer3, answer4, answer5) "
				 + "values(?, ?, ?, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, dto.getSeq());
			pstmt.setString(2, dto.getId());
			
			for (int i = 0; i < dto.getAnswerLength(); i++) 
				pstmt.setInt(i+3, dto.getAnswer(i));
			
			pstmt.executeQuery();
			
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			try {
				pstmt.close();
			} catch (Exception e2) {	}
			
		}
	}
	
	public int[] countAnswer(int seq) {
		int[] res = new int[5];
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			sb.append("select count(decode(answer1, 1, answer1)), ");
			sb.append("       count(decode(answer2, 1, answer2)),");
			sb.append("       count(decode(answer3, 1, answer3)),");
			sb.append("       count(decode(answer4, 1, answer4)),");
			sb.append("       count(decode(answer5, 1, answer5))");
			sb.append("from survey_sub where seq = ?");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, seq);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				for (int i = 0; i < res.length; i++) {
					res[i] = rs.getInt(i+1);
				}
			}
			
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			try {
				rs.close();
				pstmt.close();
			} catch (Exception e2) {}
		}
		
		return res;
	}
	
	public int totalAnswer(int seq) {
		int res = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "select count(*) from survey_sub where seq = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, seq);
			rs = pstmt.executeQuery();
			if(rs.next())
				res = rs.getInt(1);
			
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			try {
				rs.close();
				pstmt.close();
			} catch (Exception e2) {}
		}
		
		return res;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
