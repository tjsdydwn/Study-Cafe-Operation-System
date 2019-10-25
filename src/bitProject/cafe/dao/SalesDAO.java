package bitProject.cafe.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import bitProject.cafe.dto.OrderDTO;
import bitProject.cafe.dto.SalesDTO;

public class SalesDAO {
	private static SalesDAO instance;

	public static SalesDAO getInstance() {
		if (instance == null) {
			synchronized (SalesDAO.class) {
				instance = new SalesDAO();
			}
		}
		return instance;
	}

	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";

	private String user = "java";
	private String password = "dkdlxl";
	private Connection conn;

	private PreparedStatement pstmt;

	private ResultSet rs;

	public SalesDAO() {
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void delete(int seq) {
		getConnection();
		String sql = "DELETE FROM CAFE_SALES WHERE SEQ = ?";
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

	public void delete(SalesDTO salesDTO) {
		getConnection();
		String sql = "DELETE FROM CAFE_SALES WHERE SUBSEQ = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, salesDTO.getSubseq());
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

	public void getConnection() {
		try {
			conn = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Vector<Vector<String>> getSalesList() { // 전체출력
		Vector<Vector<String>> salesList = new Vector<Vector<String>>();
		Vector<String> sales = null;

		getConnection();
		String sql = "SELECT seq, menuName, amount, menuPrice, amount * menuPrice as tot, orderDate FROM CAFE_SALES ORDER BY seq DESC";
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				sales = new Vector<String>();
				sales.add("" + rs.getInt("seq"));
				sales.add(rs.getString("menuName"));
				sales.add("" + rs.getInt("amount"));
				sales.add("" + rs.getInt("menuPrice"));
				sales.add("" + rs.getInt("tot"));
				sales.add(rs.getString("orderDate"));
				salesList.add(sales);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect(true);
		}
		return salesList;
	}

	public void insert(OrderDTO orderDTO) {
		getConnection();
		String sql = "INSERT INTO CAFE_SALES VALUES(SEQ_SALES.nextval, ?, ?, ?, sysdate, ?)";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, orderDTO.getMenuName());
			pstmt.setInt(2, orderDTO.getAmount());
			pstmt.setInt(3, orderDTO.getMenuPrice());
			pstmt.setInt(4, orderDTO.getSeq());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect(false);
		}
	}
	
}
