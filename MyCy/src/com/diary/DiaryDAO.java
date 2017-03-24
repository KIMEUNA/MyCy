package com.diary;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class DiaryDAO {
	private Connection conn=DBConn.getConnection();
	
	public int insertDiary(DiaryDTO dto, String mode) {
		int result=0;
		PreparedStatement pstmt=null;
		String sql;
		
		try {
			int maxNum=maxSeq();
			dto.setSeq(maxNum+1);
			
			if(mode.equals("regdate")) {
				dto.setGroupNum(dto.getSeq());
				dto.setOrderNum(0);
				dto.setDepth(0);
			} else if(mode.equals("reply")) {
				updateOrderNum(dto.getGroupNum(), dto.getOrderNum());
				
				dto.setDepth(dto.getDepth() + 1);
				dto.setOrderNum(dto.getOrderNum() + 1);
			}
			
			sql = "INSERT INTO diary_board(seq, id, title, content, groupNum, depth, orderNum) VALUES(?, ?, ?, ?, ?, ?, ?)";
			
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setInt(1, dto.getSeq());
			pstmt.setString(2, dto.getId());
			pstmt.setString(3, dto.getTitle());
			pstmt.setString(4, dto.getContent());
			pstmt.setInt(5, dto.getGroupNum());
			pstmt.setInt(6, dto.getDepth());
			pstmt.setInt(7, dto.getOrderNum());
			
			result=pstmt.executeUpdate();
			pstmt.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return result;
	}
	
	public int maxSeq() {
		int result=0;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		try {
			sql="SELECT NVL(MAX(seq), 0) FROM diary_board";
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

	public int dataCount() {
		int result=0;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		
		try {
			sql="SELECT COUNT(*) FROM diary_board";
			pstmt=conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			if(rs.next())
				result=rs.getInt(1);
			pstmt.close();
			pstmt.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return result;
	}
	
	public int dataCount(String searchKey, String searchValue) {
		int result=0;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		
		try {
			sql="SELECT COUNT(*) FROM diary_board WHERE ";
			
			if(searchKey.equals("id"))
				sql+="INSTR(id, ?) = 1";
			else if(searchKey.equals("title"))
				sql+="INSTR(title, ?) >= 1";
			else if(searchKey.equals("content"))
				sql+="INSTR(content, ?) >= 1";
			else if(searchKey.equals("regdate"))
				sql+="TO_CHAR(regdate, 'YYYY-MM-DD') = ?";
			
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, searchValue);		
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
	
	public List<DiaryDTO> listDiary(int start, int end) {
		List<DiaryDTO> list=new ArrayList<DiaryDTO>();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuffer sb=new StringBuffer();
		
		try {
			sb.append("SELECT * FROM (");
			sb.append("    SELECT ROWNUM rnum, tb.* FROM (");
			sb.append("        SELECT seq, id, title, content, groupNum, orderNum, depth, hit, ");
			sb.append("            TO_CHAR(regdate, 'YYYY-MM-DD') regdate");
			sb.append("        FROM diary_board");
			sb.append("	      ORDER BY groupNum DESC, orderNum ASC ");
			sb.append("    ) tb WHERE ROWNUM <= ? ");
			sb.append(") WHERE rnum >= ? ");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, end);
			pstmt.setInt(2, start);

			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				DiaryDTO dto=new DiaryDTO();
				
				dto.setSeq(rs.getInt("seq"));
				dto.setId(rs.getString("id"));
				dto.setTitle(rs.getString("title"));	
				dto.setContent(rs.getString("content"));
				dto.setGroupNum(rs.getInt("groupNum"));				
				dto.setOrderNum(rs.getInt("orderNum"));
				dto.setDepth(rs.getInt("depth"));
				dto.setHit(rs.getInt("hit"));
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
	
	public List<DiaryDTO> listDiary(int start, int end, String searchKey, String searchValue) {
		List<DiaryDTO> list = new ArrayList<DiaryDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();

		try {
			sb.append("SELECT * FROM (");
			sb.append("    SELECT ROWNUM rnum, tb.* FROM (");
			sb.append("        SELECT seq, id, title, content, groupNum, orderNum, depth, hit, ");
			sb.append("           TO_CHAR(regdate, 'YYYY-MM-DD') regdate");
			sb.append("        FROM diary_board");
			if (searchKey.equals("regdate"))
				sb.append("           WHERE TO_CHAR(regdate, 'YYYY-MM-DD') = ? ");
			else if (searchKey.equals("id"))
				sb.append("           WHERE INSTR(id, ?) = 1 ");
			else
				sb.append("           WHERE INSTR(" + searchKey + ", ?) >= 1 ");

			sb.append("               ORDER BY groupNum DESC, orderNum ASC ");
			sb.append("    ) tb WHERE ROWNUM <= ?");
			sb.append(") WHERE rnum >= ?");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, searchValue);
			pstmt.setInt(2, end);
			pstmt.setInt(3, start);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				DiaryDTO dto = new DiaryDTO();

				dto.setSeq(rs.getInt("seq"));
				dto.setId(rs.getString("id"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setGroupNum(rs.getInt("groupNum"));
				dto.setOrderNum(rs.getInt("orderNum"));
				dto.setDepth(rs.getInt("depth"));
				dto.setHit(rs.getInt("hit"));
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
	
	public int updateHit(int seq) {
		int result=0;
		PreparedStatement pstmt=null;
		String sql;
		
		sql="UPDATE diary_board SET hit=hit+1 WHERE seq=?";
		
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, seq);
			result=pstmt.executeUpdate();
			pstmt.close();
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return result;
	}
	
	public DiaryDTO readDiary(int seq) {
		DiaryDTO dto=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuffer sb=new StringBuffer();
		
		try {
			sb.append("SELECT seq, id, title, content, hit, regdate ");
			sb.append(", groupNum, depth, orderNum");
			sb.append("  FROM  diary_board ");
			sb.append("  WHERE seq=?");
			
			pstmt=conn.prepareStatement(sb.toString());
			pstmt.setInt(1, seq);
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				dto=new DiaryDTO();
				
				dto.setSeq(rs.getInt("seq"));
				dto.setId(rs.getString("id"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setHit(rs.getInt("hit"));
				dto.setRegdate(rs.getString("regdate"));
				dto.setGroupNum(rs.getInt("groupNum"));
				dto.setOrderNum(rs.getInt("orderNum"));
				dto.setDepth(rs.getInt("depth"));

			}
			rs.close();
			pstmt.close();
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return dto;
	}
	
    public DiaryDTO preReadDiary(int groupNum, int orderNum, String searchKey, String searchValue) {
    	DiaryDTO dto=null;
        PreparedStatement pstmt=null;
        ResultSet rs=null;
        StringBuffer sb = new StringBuffer();

        try {
            if(searchValue!=null && searchValue.length() != 0) {
                sb.append("SELECT ROWNUM, tb.* FROM ( ");
                sb.append("  SELECT seq, title  ");
    			sb.append("               FROM diary_board d");
    			sb.append("               JOIN member m ON d.id=m.id");
    			if(searchKey.equals("regdate"))
    				sb.append("           WHERE (TO_CHAR(regdate, 'YYYY-MM-DD') = ? ) AND ");
    			else if(searchKey.equals("id"))
    				sb.append("           WHERE (INSTR(id, ?) = 1 ) AND ");
    			else
    				sb.append("           WHERE (INSTR(" + searchKey + ", ?) >= 1 ) AND ");
    			
                sb.append("     (( groupNum = ? AND orderNum < ?) ");
                sb.append("         OR (groupNum > ? )) ");
                sb.append("         ORDER BY groupNum ASC, orderNum DESC) tb WHERE ROWNUM = 1 ");

                pstmt=conn.prepareStatement(sb.toString());
                
                pstmt.setString(1, searchValue);
                pstmt.setInt(2, groupNum);
                pstmt.setInt(3, orderNum);
                pstmt.setInt(4, groupNum);
			} else {
                sb.append("SELECT ROWNUM, tb.* FROM ( ");
                sb.append("     SELECT seq, title FROM diary_board d JOIN member m ON d.id=m.id ");                
                sb.append("  WHERE (groupNum = ? AND orderNum < ?) ");
                sb.append("         OR (groupNum > ? ) ");
                sb.append("         ORDER BY groupNum ASC, orderNum DESC) tb WHERE ROWNUM = 1 ");

                pstmt=conn.prepareStatement(sb.toString());
                pstmt.setInt(1, groupNum);
                pstmt.setInt(2, orderNum);
                pstmt.setInt(3, groupNum);
			}
            rs=pstmt.executeQuery();

            if(rs.next()) {
                dto=new DiaryDTO();
                dto.setSeq(rs.getInt("seq"));
                dto.setTitle(rs.getString("title"));
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return dto;
    }

    public DiaryDTO nextReadDiary(int groupNum, int orderNum, String searchKey, String searchValue) {
    	DiaryDTO dto=null;
        PreparedStatement pstmt=null;
        ResultSet rs=null;
        StringBuffer sb = new StringBuffer();

        try {
            if(searchValue!=null && searchValue.length() != 0) {
                sb.append("SELECT ROWNUM, tb.* FROM ( ");
                sb.append("  SELECT seq, title ");
    			sb.append("               FROM diary_board d");
    			sb.append("               JOIN member m ON d.id=m.id");
    			if(searchKey.equals("regdate"))
    				sb.append("           WHERE (TO_CHAR(regdate, 'YYYY-MM-DD') = ? ) AND ");
    			else if(searchKey.equals("id"))
    				sb.append("           WHERE (INSTR(id, ?) = 1) AND ");
    			else
    				sb.append("           WHERE (INSTR(" + searchKey + ", ?) >= 1) AND ");
    			
                sb.append("     (( groupNum = ? AND orderNum > ?) ");
                sb.append("         OR (groupNum < ? )) ");
                sb.append("         ORDER BY groupNum DESC, orderNum ASC) tb WHERE ROWNUM = 1 ");

                pstmt=conn.prepareStatement(sb.toString());
                pstmt.setString(1, searchValue);
                pstmt.setInt(2, groupNum);
                pstmt.setInt(3, orderNum);
                pstmt.setInt(4, groupNum);

			} else {
                sb.append("SELECT ROWNUM, tb.* FROM ( ");
                sb.append("     SELECT seq, title FROM diary_board d JOIN member m ON d.id=m.id ");
                sb.append("  WHERE (groupNum = ? AND orderNum > ?) ");
                sb.append("         OR (groupNum < ? ) ");
                sb.append("         ORDER BY groupNum DESC, orderNum ASC) tb WHERE ROWNUM = 1 ");

                pstmt=conn.prepareStatement(sb.toString());
                pstmt.setInt(1, groupNum);
                pstmt.setInt(2, orderNum);
                pstmt.setInt(3, groupNum);
            }
            rs=pstmt.executeQuery();

            if(rs.next()) {
                dto=new DiaryDTO();
                dto.setSeq(rs.getInt("seq"));
                dto.setTitle(rs.getString("title"));
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return dto;
    }
	
	public int updateOrderNum(int groupNum, int orderNum) {
		int result = 0;
		PreparedStatement pstmt=null;
		String sql;
		
		sql = "UPDATE diary_board SET orderNum=orderNum+1 WHERE groupNum = ? AND orderNum > ?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, groupNum);
			pstmt.setInt(2, orderNum);
			result = pstmt.executeUpdate();
			
			pstmt.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}	
		return result;
	}
	
	public int updateDiary(DiaryDTO dto) {
    	int result=0;
    	PreparedStatement pstmt=null;
    	String sql;
    	
    	sql="UPDATE diary_board SET title=?, content=?  WHERE seq=?";
    	
    	try {
			pstmt=conn.prepareStatement(sql);	
			pstmt.setString(1, dto.getTitle());
			pstmt.setString(2, dto.getContent());
			pstmt.setInt(3, dto.getSeq());
			result=pstmt.executeUpdate();
			pstmt.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
    	return result;
    }
	
	public int deleteDiary(int seq) {
	int result = 0;
	PreparedStatement pstmt = null;
	String sql;

	sql = "DELETE FROM diary_board WHERE seq=? ";
	
	try
	{
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, seq);
		result = pstmt.executeUpdate();
		pstmt.close();
	}catch(
	Exception e)
	{
		System.out.println(e.toString());
	}
	return result;
	}
}
