package com.visitor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class VisitorDAO {
	
	private Connection conn=DBConn.getConnection();
	
	// 가져오기
	public int insertVisitor(VisitorDTO dto, String mode) {
		int result=0;
		PreparedStatement pstmt=null;
		String sql;
		
		try {
			sql="INSERT INTO visitor_board ( seq,id,content,emoticon,groupNum,depth,orderNum)"
					+ "VALUES (?,?,?,?,?,?,?)";
			pstmt=conn.prepareStatement(sql);
			
			int maxNum= maxSeq()+1;
			
			pstmt.setInt(1, maxNum);
			pstmt.setString(2, dto.getId());
			pstmt.setString(3, dto.getContent());
			pstmt.setString(4, "_");
			
			// 댓글
			if(mode.equals("created")) {
				pstmt.setInt(5, maxNum);
				pstmt.setInt(6, 0);
				pstmt.setInt(7, 0);
			} else {
				
			}
			result=pstmt.executeUpdate();
			pstmt.close();
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return result;
	}

	// 댓글순번
	private int maxSeq() {
		int result=0;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		
		try {
			sql="SELECT NVL(MAX(seq),0) from visitor_board";
			pstmt=conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			if(rs.next()) 
				result=rs.getInt(1);
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		return result;
	}
	
	// 전체 데이터 개수
	public int dataCount() {
		int result=0;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		
		try {
			sql="SELECT COUNT(*) FROM visitor_board";
			pstmt=conn.prepareStatement(sql);
			
			rs=pstmt.executeQuery();
			if(rs.next()) 
				result=rs.getInt(1);
			rs.close();
			pstmt.close();
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		return result;
	}
	// 리스트
	public List<VisitorDTO> listVisitor(int start, int end) {
		List<VisitorDTO> list=new ArrayList<VisitorDTO>();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuffer sb=new StringBuffer();
		
		try {
			sb.append("SELECT * FROM (");
			sb.append("	SELECT ROWNUM rnum,tb.* FROM (");
			sb.append("		SELECT seq,id,content,emoticon,groupNum,depth,orderNum");
			sb.append("			,TO_CHAR(regdate,'YYYY-MM-DD') regdate");
			sb.append("			FROM visitor_board ");
			sb.append("		ORDER BY seq DESC ");
			sb.append("	)tb WHERE ROWNUM <= ? ");
			sb.append(") WHERE rnum >= ?  ");
			
			pstmt=conn.prepareStatement(sb.toString());
			pstmt.setInt(1, end);
			pstmt.setInt(2, start);
			
			rs=pstmt.executeQuery();
			while(rs.next()) {
				VisitorDTO dto=new VisitorDTO();
				
				dto.setSeq(rs.getInt("seq"));
				dto.setId(rs.getString("id"));
				dto.setContent(rs.getString("content"));
				dto.setEmoticon(rs.getString("emoticon"));
				dto.setGroupNum(rs.getInt("groupNum"));
				dto.setDepth(rs.getInt("depth"));
				dto.setOrderNum(rs.getInt("orderNum"));
				dto.setRegdate(rs.getString("regdate"));
				
				list.add(dto);
			}
			rs.close();
			pstmt.close();
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		return list;
	}
	
	// 수정
	public int updateVisitor(VisitorDTO dto) {
		int result=0;
		PreparedStatement pstmt=null;
		String sql;
		
		try {
			sql="UPDATE visitor_board SET content=? WHERE seq=?";
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getContent());
			pstmt.setInt(2, dto.getSeq());
			
			pstmt.executeQuery();
			pstmt.close();
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		return result;
	}
	
	// 삭제
	public int deleteVisitor(int seq) {
		int result=0;
		PreparedStatement pstmt=null;
		String sql;
		
		try {
			sql="DELETE FROM visitor_board WHERE seq=?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1,seq);
			
			result=pstmt.executeUpdate();
			pstmt.close();
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		return result;
	}
	
	public VisitorDTO readVisitor(int seq) {
		VisitorDTO dto=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuffer sb=new StringBuffer();
		
		try {
			 sb.append("SELECT seq, id, content, emoticon, groupNum, depth,orderNum, ");
	         sb.append(" TO_CHAR(regdate, 'YYYY-MM-DD') regdate ");
	         sb.append(" FROM visitor_board ");
	         sb.append("WHERE seq=?");

			pstmt=conn.prepareStatement(sb.toString());
			pstmt.setInt(1, seq);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				dto=new VisitorDTO();
				dto.setSeq(rs.getInt("seq"));
				dto.setId(rs.getString("id"));
				dto.setContent(rs.getString("content"));
				dto.setEmoticon(rs.getString("emoticon"));
				dto.setGroupNum(rs.getInt("groupNum"));
				dto.setDepth(rs.getInt("depth"));
				dto.setOrderNum(rs.getInt("orderNum"));
				dto.setRegdate(rs.getString("regdate"));
			}
			rs.close();
			pstmt.close();
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		return dto;
		
	}

}
