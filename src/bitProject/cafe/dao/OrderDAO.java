package bitProject.cafe.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import bitProject.cafe.dto.OrderDTO;

public class OrderDAO {
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String user = "java";
	private String password = "dkdlxl";

	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;

	private static OrderDAO instance;

	public static OrderDAO getInstance() {
		if (instance == null) {
			synchronized (OrderDAO.class) {
				instance = new OrderDAO();
			}
		}
		return instance;
	}

	public OrderDAO() {
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

	public void insert(ArrayList<OrderDTO> list) {
		getConnection();
		try {
			for (int i = 0; i < list.size(); i++) {
				String sql = "INSERT INTO CAFE_ORDER VALUES(SEQ_ORDER.NEXTVAL, ?, ?, ?, ?, ?)";

				OrderDTO order = list.get(i);
				pstmt = conn.prepareStatement(sql);

				pstmt.setString(1, order.getId());
				pstmt.setString(2, order.getMenuName());
				pstmt.setInt(3, order.getAmount());
				pstmt.setInt(4, order.getMenuPrice());
				pstmt.setInt(5, order.getAmount() * order.getMenuPrice());
				pstmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect(false);
		}
	}

	public void insert(OrderDTO orderDTO, int temp) {
		getConnection();
		String sql = "INSERT INTO CAFE_ORDER_TEMP VALUES(?, ?, ?, ?, ?)";
		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, orderDTO.getId());
			pstmt.setString(2, orderDTO.getMenuName());
			pstmt.setInt(3, orderDTO.getAmount());
			pstmt.setInt(4, orderDTO.getMenuPrice());
			pstmt.setInt(5, orderDTO.getAmount() * orderDTO.getMenuPrice());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect(false);
		}
	}

//    id varchar2(40),
//    menuName varchar2(40),
//    amount number, 
//    menuPrice number, 
//    totPrice number
	public ArrayList<OrderDTO> getFromDB(OrderDTO order) {
		ArrayList<OrderDTO> list = new ArrayList<OrderDTO>();
		getConnection();
		String sql = "SELECT * FROM CAFE_ORDER_TEMP";
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String id = rs.getString("id");
				String menuName = rs.getString("menuName");
				int amount = rs.getInt("amount");
				int menuPrice = rs.getInt("menuPrice");
				OrderDTO orderDTO = new OrderDTO(id, menuName, amount, menuPrice);
				list.add(orderDTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect(true);
		}
		return list;
	}

	public Vector<Vector<String>> getFromDB() { // 전체출력
		getConnection();
		Vector<Vector<String>> orderList = new Vector<Vector<String>>();
		Vector<String> order = null;
		String sql = "SELECT * FROM CAFE_ORDER ORDER BY seq ASC";
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				order = new Vector<String>();
				order.add(rs.getInt("seq") + "");
				order.add(rs.getString("id"));
				order.add(rs.getString("menuName"));
				order.add(rs.getInt("amount") + "");
				order.add(rs.getInt("menuPrice") + "");
				order.add((rs.getInt("amount") * rs.getInt("menuPrice")) + "");
				orderList.add(order);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect(true);
		}
		return orderList;
	}

	public void delete() {
		getConnection();
		String sql = "DELETE FROM CAFE_ORDER_TEMP";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect(false);
		}
	}

	public void delete(OrderDTO orderDTO) {
		getConnection();
		String sql = "DELETE FROM CAFE_ORDER_TEMP WHERE id = ? AND menuName = ? AND amount = ? AND menuPrice = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, orderDTO.getId());
			pstmt.setString(2, orderDTO.getMenuName());
			pstmt.setInt(3, orderDTO.getAmount());
			pstmt.setInt(4, orderDTO.getMenuPrice());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect(false);
		}

	}

	public void delete(int seq) { // 판매완료 -> 주문 내역 DB에서 삭제
		getConnection();
		String sql = "DELETE FROM CAFE_ORDER WHERE SEQ = ? ";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, seq);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect(false);
		}
	}

	public void disconnect(boolean isSelect) {
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
