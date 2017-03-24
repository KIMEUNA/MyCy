package com.anonymity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class AnonymityDAO {
   private Connection conn = DBConn.getConnection();
   
   public int insertBoard(AnonymityDTO dto, String mode){
      int result = 0;
      PreparedStatement pstmt=null;
      StringBuffer sb=new StringBuffer();
      
      try {
         sb.append(" INSERT INTO anonymity_board(");
         sb.append("      seq, title, content, pass,");
         sb.append("       groupNum, depth, orderNum");
         sb.append(" ) VALUES (?, ?, ?, ?, ?, ?, ?)");

         pstmt=conn.prepareStatement(sb.toString());
         int maxNum = maxBoardNum()+1;
         
         pstmt.setInt(1,maxNum);
         pstmt.setString(2, dto.getTitle());      
         pstmt.setString(3, dto.getContent());
         pstmt.setString(4, dto.getPass());
         if(mode.equals("created")){
            // 게시물 등록인 경우
            pstmt.setInt(5, maxNum);
            pstmt.setInt(6, 0);
            pstmt.setInt(7, 0);
            
         }else if(mode.equals("reply")){
            //답변인 경우
            updateOrderNo(dto.getGroupNum(), dto.getOrderNum());
            
            dto.setDepth(dto.getDepth()+1);
            dto.setOrderNum(dto.getOrderNum()+1);
            
            pstmt.setInt(5, dto.getGroupNum());
            pstmt.setInt(6, dto.getDepth());
            pstmt.setInt(7, dto.getOrderNum());
         }
         
         result = pstmt.executeUpdate();
         pstmt.close();
         
      } catch (Exception e) {
         System.out.println(e.toString());
      }
      return result;
   }
   
   public int maxBoardNum(){
      int result=0;
      PreparedStatement pstmt=null;
      ResultSet rs=null;
      String sql;
      
      try {
         sql="SELECT NVL(MAX(seq),0) FROM anonymity_board";
         
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
   
   public int dataCount(){
      int result=0;
      PreparedStatement pstmt=null;
      ResultSet rs=null;
      String sql;
      
      try {
         sql="SELECT COUNT(*) FROM anonymity_board";
         pstmt=conn.prepareStatement(sql);
         
         rs=pstmt.executeQuery();
         
         if(rs.next())
            result=rs.getInt(1);
         pstmt.close();
         
      } catch (Exception e) {
         System.out.println(e.toString());
      }
      
      return result;
      
   }
   
   //검색
   public int dataCount(String searchKey, String searchValue){
      int result=0;
      PreparedStatement pstmt=null;
      ResultSet rs =null;
      String sql;
      
      try {
         sql="SELECT COUNT(*) FROM anonymity_board ";
         
         if(searchKey.equals("title"))
            sql+=" INSTR(title, ?) >=1"; // 일부내용이 같으면
         else if(searchKey.equals("content"))
            sql+=" INSTR(content, ?) >=1"; //일부내용이 같으면
         else if(searchKey.equals("regdate"))
            sql+=" TO_CHAR(regdate, 'YYYY-MM-DD')=?"; //날짜형식으로 바꿔주기.
      
         pstmt=conn.prepareStatement(sql);
         pstmt.setString(1, searchValue);
         
         rs=pstmt.executeQuery();
         
         if(rs.next())
            result=rs.getInt(1);
         pstmt.close();
      
      } catch (Exception e) {
         System.out.println(e.toString());
      }
      return result;
   }
   public List<AnonymityDTO> listBoard(int start, int end){
      List<AnonymityDTO> list=new ArrayList<>();
      PreparedStatement pstmt=null;
      ResultSet rs=null;
      StringBuffer sb = new StringBuffer();
      
      try {
         sb.append("SELECT * FROM (");
         sb.append("      SELECT ROWNUM rnum, tb.* FROM(");
         sb.append("         SELECT seq, title, content, pass, hit,");
         sb.append("               groupNum, depth, orderNum, ");
         sb.append("               TO_CHAR(regdate, 'YYYY-MM-DD') regdate ");
         sb.append("       FROM anonymity_board ");
         sb.append("       ORDER BY seq DESC, orderNum ASC");
         sb.append("      ) tb WHERE ROWNUM <= ?");
         sb.append(") WHERE rnum >= ?");
         
      
         pstmt=conn.prepareStatement(sb.toString());
         pstmt.setInt(1, end);
         pstmt.setInt(2, start);
         
         rs=pstmt.executeQuery();
         
         while(rs.next()){
            AnonymityDTO dto = new AnonymityDTO();
            
            dto.setSeq(rs.getInt("seq"));
            dto.setTitle(rs.getString("title"));
            dto.setContent(rs.getString("content"));
            dto.setPass(rs.getString("pass"));
            dto.setHit(rs.getInt("hit"));
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
   
   public List<AnonymityDTO> listBoard(int start, int end, String searchKey, String searchValue){
      List<AnonymityDTO> list=new ArrayList<>();
      PreparedStatement pstmt=null;
      ResultSet rs=null;
      StringBuffer sb=new StringBuffer();
      
      try {
         sb.append("SELELCT * FROM (");
         sb.append("  SELECT ROWNUM rnum, tb.* FROM(");
         sb.append("   SELECT seq, title, content, pass, hit,");
         sb.append("    groupNum, depth, orderNum");
         sb.append("    ,TO_CHAR(regdate, 'YYYY-MM-DD') regdate");
         sb.append("  FROM anonymity_board ");
         sb.append(" WHERE ");
         
         if(searchKey.equals("title"))
            sb.append(" INSTR(title, ?) >=1");
         else if(searchKey.equals("content"))
            sb.append(" INSTR(content, ?) >=1");
         else if(searchKey.equals("regdate"))
            sb.append(" TO_CHAR(regdate, 'YYYY-MM-DD') = ?");
         
         sb.append("  ORDER BY groupNum DESC, orderNum ASC");
         sb.append("  ) tb WHERE ROWNUM <= ?");
         sb.append(" ) WHERE rnum >= ?");
         
         pstmt = conn.prepareStatement(sb.toString());
         pstmt.setString(1, searchValue);
         pstmt.setInt(2, end);
         pstmt.setInt(3, start);
         
         rs=pstmt.executeQuery();
         
         while(rs.next()){
            AnonymityDTO dto = new AnonymityDTO();
            
            dto.setSeq(rs.getInt("seq"));
            dto.setTitle(rs.getString("title"));
            dto.setContent(rs.getString("content"));
            dto.setPass(rs.getString("pass"));
            dto.setHit(rs.getInt("hit"));
            dto.setGroupNum(rs.getInt("groupNum"));
            dto.setDepth(rs.getInt("depth"));
            dto.setOrderNum(rs.getInt("orderNum"));
            dto.setRegdate(rs.getString("regdate"));
            
            list.add(dto);
            
            rs.close();
            pstmt.close();
         }
         
      } catch (Exception e) {
         System.out.println(e.toString());
      }
      return list;
   }
   
   public int updateHit(int seq){
      int result=0;
      PreparedStatement pstmt=null;
      String sql;
      try {
         sql = "UPDATE anonymity_board SET hit=hit+1";
         sql +="   WHERE seq = ?";
         
         pstmt=conn.prepareStatement(sql);
         pstmt.setInt(1, seq);
         result=pstmt.executeUpdate();
         pstmt.close();
         
      } catch (Exception e) {
         System.out.println(e.toString());
      }
      
      return result;
      
   }
   //글보기
   public AnonymityDTO readBoard(int seq){
      AnonymityDTO dto=null;
      PreparedStatement pstmt=null;
      ResultSet rs=null;
      StringBuffer sb=new StringBuffer();
      
      try {
         sb.append("SELECT seq, title, content, pass," );
         sb.append(" hit, groupNum, depth, orderNum, regdate");
         sb.append(" FROM anonymity_board");
         sb.append(" WHERE seq =?");
         
         
         pstmt=conn.prepareStatement(sb.toString());
         pstmt.setInt(1, seq);
         rs=pstmt.executeQuery();
         
         if(rs.next()){
            dto = new AnonymityDTO();
            
            dto.setSeq(rs.getInt("seq"));
            dto.setTitle(rs.getString("title"));
            dto.setContent(rs.getString("content"));
            dto.setPass(rs.getString("pass"));
            dto.setHit(rs.getInt("hit"));
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
   // 이전글
    public AnonymityDTO preReadBoard(int groupNum, int orderNum, String searchKey, String searchValue) {
       AnonymityDTO dto=null;

        PreparedStatement pstmt=null;
        ResultSet rs=null;
        StringBuffer sb = new StringBuffer();

        try {
            if(searchValue!=null && searchValue.length() != 0) {
                sb.append("SELECT ROWNUM, tb.* FROM ( ");
                sb.append("  SELECT seq, title ");
             sb.append("       FROM anonymity_board");
             if(searchKey.equals("regdate"))
                sb.append("           WHERE (TO_CHAR(regdate, 'YYYY-MM-DD') = ? ) AND ");
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
                sb.append("     SELECT seq, title FROM anonymity_board");                
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
                dto=new AnonymityDTO();
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

    // 다음글
    public AnonymityDTO nextReadBoard(int groupNum, int orderNum, String searchKey, String searchValue) {
       AnonymityDTO dto=null;

        PreparedStatement pstmt=null;
        ResultSet rs=null;
        StringBuffer sb = new StringBuffer();

        try {
            if(searchValue!=null && searchValue.length() != 0) {
                sb.append("SELECT ROWNUM, tb.* FROM ( ");
                sb.append("  SELECT seq, title ");
             sb.append("          FROM anonymity_board ");
             if(searchKey.equals("regdate"))
                sb.append("           WHERE (TO_CHAR(regdate, 'YYYY-MM-DD') = ? ) AND ");
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
                sb.append("     SELECT seq, title FROM anonymity_board ");
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
                dto=new AnonymityDTO();
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
    
    // 답변인경우 orderNo 변경
    public int updateOrderNo(int groupNum, int orderNum) {
       int result=0;
       PreparedStatement pstmt=null;
       String sql;
       
       try {
         sql="UPDATE anonymity_board  SET  orderNum=orderNum+1 ";
         sql+="   WHERE  groupNum=? AND orderNum > ?";
         pstmt = conn.prepareStatement(sql);
         
         pstmt.setInt(1, groupNum);
         pstmt.setInt(2, orderNum);
         
         result=pstmt.executeUpdate();
         pstmt.close();
         
      } catch (Exception e) {
         System.out.println(e.toString());
      }
       
       return result;
    }
   
   public int deleteBoard(int seq){
      int result=0;
      PreparedStatement pstmt=null;
      String sql;
      
      try {
         sql="DELETE FROM anonymity_board WHERE seq IN";
         sql+=" (SELECT seq FROM anonymity_board START WITH seq=? CONNECT BY PRIOR seq = orderNum)";
         
         pstmt=conn.prepareStatement(sql);
         pstmt.setInt(1, seq);
         result=pstmt.executeUpdate();
         pstmt.close();
         
      } catch (Exception e) {
         System.out.println(e.toString());
      }
      return result;
   }
   
   public int updateBoard(AnonymityDTO dto){
      int result=0;
      PreparedStatement pstmt=null;
      String sql;
      
      try {
         sql="UPDATE anonymity_board SET title=?, content=?";
         sql+=" WHERE seq=?";
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
   
   
}
