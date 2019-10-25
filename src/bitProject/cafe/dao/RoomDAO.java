package bitProject.cafe.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import bitProject.cafe.dto.RoomDTO;

public class RoomDAO {
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String user = "java";
	private String password = "dkdlxl";

	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;

	private static RoomDAO instance;

	public static RoomDAO getInstance() {
		if (instance == null) {
			synchronized (RoomDAO.class) {
				instance = new RoomDAO();
			}
		}
		return instance;
	}

	public RoomDAO() {
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

	public int insert(RoomDTO room) {
		int subseq = 0;

		String id = room.getId();
		int roomNum = room.getRoomNum();
		int year = room.getYear();
		int month = room.getMonth();
		int date = room.getDate();
		int inHour = room.getInHour();
		int outHour = room.getOutHour();
		int price = room.getPrice();

		getConnection();
		String sql = "INSERT INTO CAFE_ROOMRESERVATION VALUES(SEQ_ROOM.nextVAL,?,?,?,?,?,?,?,?,SUBSEQ_ROOM.nextVAL)";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setInt(2, roomNum);
			pstmt.setInt(3, year);
			pstmt.setInt(4, month);
			pstmt.setInt(5, date);
			pstmt.setInt(6, inHour);
			pstmt.setInt(7, outHour);
			pstmt.setInt(8, price);

			subseq = pstmt.executeUpdate();

			sql = "SELECT SUBSEQ_ROOM.CURRVAL AS subseq FROM DUAL";
			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				subseq = rs.getInt("subseq");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnection(false);
		}
		return subseq;
	}

	public boolean hasSameReservation(RoomDTO room) {
		boolean result = false;
		getConnection();
		String sql = "SELECT * FROM CAFE_ROOMRESERVATION WHERE ROOM_NUM = ? AND ROOM_YEAR = ? AND ROOM_MONTH = ? AND ROOM_DATE = ? AND INHOUR = ? AND OUTHOUR = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, room.getRoomNum());
			pstmt.setInt(2, room.getYear());
			pstmt.setInt(3, room.getMonth());
			pstmt.setInt(4, room.getDate());
			pstmt.setInt(5, room.getInHour());
			pstmt.setInt(6, room.getOutHour());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnection(true);
		}
		return result;
	}

	public ArrayList<RoomDTO> check(RoomDTO room) {
		ArrayList<RoomDTO> roomList = new ArrayList<RoomDTO>();

		int room_year = room.getYear();
		int room_month = room.getMonth();
		int room_date = room.getDate();

		getConnection();
		String sql = "SELECT * FROM CAFE_ROOMRESERVATION WHERE ROOM_YEAR = ? AND ROOM_MONTH = ? AND ROOM_DATE = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, room_year);
			pstmt.setInt(2, room_month);
			pstmt.setInt(3, room_date);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				int roomNum = rs.getInt("room_num");
				String id = rs.getString("id");
				int year = rs.getInt("room_year");
				int month = rs.getInt("room_month");
				int date = rs.getInt("room_date");
				int inHour = rs.getInt("inhour");
				int outHour = rs.getInt("outHour");

				roomList.add(new RoomDTO(roomNum, id, year, month, date, inHour, outHour, 0));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnection(true);
		}
		return roomList;
	}

	public Vector<Vector<String>> getMyList(String id) {
		Vector<Vector<String>> roomList = new Vector<Vector<String>>();
		Vector<String> room = null;
		getConnection();
		String sql = "SELECT * FROM CAFE_ROOMRESERVATION WHERE ID = ? ORDER BY room_year, room_month, room_date, inhour, outHour";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				room = new Vector<String>();
				room.add("" + rs.getInt("seq"));
				room.add("" + rs.getInt("room_year"));
				room.add("" + rs.getInt("room_month"));
				room.add("" + rs.getInt("room_date"));
				room.add("" + rs.getInt("inhour"));
				room.add("" + rs.getInt("outHour"));
				room.add("" + rs.getInt("room_num"));
				room.add("" + rs.getInt("subseq"));
				roomList.add(room);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnection(true);
		}
		return roomList;
	}

	public int delete(RoomDTO room) {
		int cnt = 0;
		getConnection();
		String sql = "DELETE FROM CAFE_ROOMRESERVATION WHERE seq = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, room.getSeq());
			cnt = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnection(false);
		}
		return cnt;
	}

	public Vector<Vector<String>> getAllList() {
		Vector<Vector<String>> roomList = new Vector<Vector<String>>();
		Vector<String> room = null;
		getConnection();
		String sql = "SELECT seq, id, room_num, room_year||'년 '||room_month||'월 '||room_date||'일' as day, inhour, outhour \r\n"
				+ "FROM CAFE_ROOMRESERVATION\r\n" + "ORDER BY day desc, inhour desc, outhour desc, room_num";
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				room = new Vector<String>();
				room.add("" + rs.getInt("seq"));
				room.add(rs.getString("id"));
				room.add("" + rs.getInt("room_num"));
				room.add(rs.getString("day"));
				room.add("" + rs.getInt("inhour"));
				room.add("" + rs.getInt("outhour"));
				roomList.add(room);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnection(true);
		}
		return roomList;
	}

	public void disconnection(boolean isSelect) {
		try {
			if (isSelect) {
				rs.close();
			}
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
