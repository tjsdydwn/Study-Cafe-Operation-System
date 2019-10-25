package bitProject.cafe.dao;

// Board 클래스 DB 연동용 DAO 클래스

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import bitProject.cafe.dto.BoardDTO;

public class BoardDAO {
	private static BoardDAO instance;
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String user = "java";
	private String password = "dkdlxl";
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;

	public static BoardDAO getInstance() {
		if (instance == null) {
			synchronized (BoardDAO.class) {
				instance = new BoardDAO();
			}
		}
		return instance;
	}

	public BoardDAO() {
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void getConnection() {
		try {
			conn = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public BoardDTO writeBoard(BoardDTO board) {
		getConnection();
		String sql = "INSERT INTO CAFE_BOARD VALUES(SEQ_CAFE_BOARD.nextVal, ?,?,?)";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, board.getId());
			pstmt.setString(2, board.getText());
			pstmt.setString(3, board.getWriteTime());

			int cnt = pstmt.executeUpdate();
			if (cnt != 0) {
				sql = "SELECT SEQ_CAFE_BOARD.CURRVAL AS SEQ FROM DUAL";
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					board.setSeq(rs.getInt("SEQ"));
				}
			} else {
				board = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect(false);
		}
		return board;
	}

	public Vector<Vector<String>> getAllBoardContents() {
		Vector<Vector<String>> boardList = new Vector<Vector<String>>();
		Vector<String> board = null;
		getConnection();
		String sql = "SELECT * FROM CAFE_BOARD ORDER BY BOARD_SEQ DESC, BOARD_DATE DESC";
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				board = new Vector<String>();
				board.add("" + rs.getInt("BOARD_SEQ"));
				board.add(rs.getString("id"));
				board.add(rs.getString("BOARD_TEXT"));
				board.add(rs.getString("BOARD_DATE"));
				boardList.add(board);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect(true);
		}
		return boardList;
	}

	public int delete(BoardDTO board) {
		int cnt = 0;
		getConnection();
		String sql = "DELETE FROM CAFE_BOARD WHERE BOARD_SEQ = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, board.getSeq());
			cnt = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect(false);
		}
		return cnt;
	}

	public void disconnect(boolean isSelect) {
		try {
			if (isSelect) {
				rs.close();
				pstmt.close();
				conn.close();
			} else {
				pstmt.close();
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
