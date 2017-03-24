package com.gallery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class GalleryDAO {
	private Connection conn = DBConn.getConnection();

	public int insertPhoto(GalleryDTO dto) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		try {
			sql = "INSERT INTO gallery_board(seq, id, title, content, filepath) ";
			sql += "VALUES ( ";
			sql += " gallery_seq.NEXTVAL, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, dto.getId());
			pstmt.setString(2, dto.getTitle());
			pstmt.setString(3, dto.getContent());
			pstmt.setString(4, dto.getFilepath());

			result = pstmt.executeUpdate();

			pstmt.close();

		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return result;
	}

	public int dataCount() {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT COUNT(*) FROM gallery_board";
			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();

			if (rs.next())
				result = rs.getInt(1);
			pstmt.close();

		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return result;
	}

	public List<GalleryDTO> listBoard(int start, int end) {
		List<GalleryDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();

		try {
			sb.append("SELECT * FROM ( ");
			sb.append(" SELECT ROWNUM rnum, tb.* FROM ( ");
			sb.append("  SELECT seq, id, title, filepath, hit, ");
			sb.append("   TO_CHAR(regdate, 'YYYY-MM-DD') regdate ");
			sb.append("  FROM gallery_board ");
			sb.append("	 ORDER BY seq DESC ");
			sb.append(" ) tb WHERE ROWNUM <= ? ");
			sb.append(") WHERE rnum >= ? ");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, end);
			pstmt.setInt(2, start);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				GalleryDTO dto = new GalleryDTO();

				dto.setSeq(rs.getInt("seq"));
				dto.setId(rs.getString("id"));
				dto.setTitle(rs.getString("title"));
				dto.setHit(rs.getInt("hit"));
				dto.setFilepath(rs.getString("filepath"));
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

	public int updateHitCount(int seq) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		try {
			sql = "UPDATE gallery_board SET hit = hit + 1 WHERE seq = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, seq);
			result = pstmt.executeUpdate();
			pstmt.close();

		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return result;
	}

	public GalleryDTO readPhoto(int seq) {
		GalleryDTO dto = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();

		try {
			sb.append("SELECT seq, id, title, content, hit, filepath, ");
			sb.append(" TO_CHAR(regdate, 'YYYY-MM-DD') regdate ");
			sb.append(" FROM gallery_board ");
			sb.append("WHERE seq=?");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, seq);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new GalleryDTO();
				dto.setSeq(rs.getInt("seq"));
				dto.setId(rs.getString("id"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setHit(rs.getInt("hit"));
				dto.setFilepath(rs.getString("filepath"));
				dto.setRegdate(rs.getString("regdate"));
			}

			rs.close();
			pstmt.close();

		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return dto;
	}

	public int updatePhoto(GalleryDTO dto) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "UPDATE gallery_board SET title=?, content=?, filepath=? WHERE seq=?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, dto.getTitle());
			pstmt.setString(2, dto.getContent());
			pstmt.setString(3, dto.getFilepath());
			pstmt.setInt(4, dto.getSeq());

			result = pstmt.executeUpdate();
			pstmt.close();

		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return result;
	}

	public int deletePhoto(int seq) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "DELETE FROM gallery_board WHERE seq = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, seq);
			result = pstmt.executeUpdate();
			pstmt.close();

		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return result;
	}

	public int maxHit() {
		int result=0;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		
		try {
			sql="SELECT NVL(MAX(hit), 0) FROM gallery_board";
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
	
	public GalleryDTO maxHitPhoto(int hit) {
		GalleryDTO dto = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();

		try {
			sb.append("SELECT seq, id, title, content, hit, filepath, ");
			sb.append(" TO_CHAR(regdate, 'YYYY-MM-DD') regdate ");
			sb.append(" FROM gallery_board ");
			sb.append("WHERE hit=?");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, hit);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new GalleryDTO();
				dto.setSeq(rs.getInt("seq"));
				dto.setId(rs.getString("id"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setHit(rs.getInt("hit"));
				dto.setFilepath(rs.getString("filepath"));
				dto.setRegdate(rs.getString("regdate"));
			}

			rs.close();
			pstmt.close();

		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return dto;
	}
	
//	public int maxSeq() {
//		int result=0;
//		PreparedStatement pstmt=null;
//		ResultSet rs=null;
//		String sql;
//		
//		try {
//			sql="SELECT NVL(MAX(seq), 0) FROM gallery_board";
//			pstmt=conn.prepareStatement(sql);
//			rs=pstmt.executeQuery();
//			if(rs.next())
//				result=rs.getInt(1);
//			rs.close();
//			pstmt.close();
//		} catch (Exception e) {
//			System.out.println(e.toString());
//		}
//		
//		return result;
//	}
	
	public List<GalleryDTO> loadPhotoList(int seq) {
		List<GalleryDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT * FROM gallery_board WHERE seq < ? ORDER BY seq DESC";		

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, seq);		

			rs = pstmt.executeQuery();

			while (rs.next()) {
				GalleryDTO dto = new GalleryDTO();

				dto.setSeq(rs.getInt("seq"));
				dto.setId(rs.getString("id"));
				dto.setTitle(rs.getString("title"));
				dto.setHit(rs.getInt("hit"));
				dto.setFilepath(rs.getString("filepath"));
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
}
