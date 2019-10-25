package bitProject.cafe.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import bitProject.cafe.dto.LoginDTO;
import bitProject.cafe.dto.MemberDTO;

public class MemberDAO {
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String user = "java";
	private String password = "dkdlxl";

	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;

	private static MemberDAO instance;

	public static MemberDAO getInstance() {
		if (instance == null) {
			synchronized (MemberDAO.class) {
				instance = new MemberDAO();
			}
		}
		return instance;
	}

	public MemberDAO() {
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

	public boolean delete(String id) {
		boolean result = false;
		getConnection();
		String sql = "DELETE FROM CAFE_MEMBER WHERE ID = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			int cnt = pstmt.executeUpdate();
			if (cnt != 0) {
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect(false);
		}
		return result;
	}

	public int update(MemberDTO member) {
		int cnt = 0;
		getConnection();
		String sql = "UPDATE CAFE_MEMBER SET pw = ?, email1 = ?, email2 = ?, tel1 = ? , tel2 = ?, tel3 = ? WHERE ID = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getPw());
			pstmt.setString(2, member.getEmailAccount());
			pstmt.setString(3, member.getEmailDomain());
			pstmt.setString(4, member.getTel1());
			pstmt.setString(5, member.getTel2());
			pstmt.setString(6, member.getTel3());
			pstmt.setString(7, member.getId());

			cnt = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect(false);
		}
		return cnt;
	}

	public void update(String id, int authority) {
		getConnection();
		String sql = "UPDATE CAFE_MEMBER SET staff = ? WHERE id = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, authority);
			pstmt.setString(2, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect(false);
		}
	}

	public MemberDTO tryLogin(String inputId, String inputPw) {
		MemberDTO member = null;
		getConnection();
		String sql = "SELECT * FROM CAFE_MEMBER WHERE ID = ? AND PW = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, inputId);
			pstmt.setString(2, inputPw);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				String id = rs.getString("id");
				String pw = rs.getString("pw");
				String name = rs.getString("name");
				String emailAccount = rs.getString("email1");
				String emailDomain = rs.getString("email2");
				String tel1 = rs.getString("tel1");
				String tel2 = rs.getString("tel2");
				String tel3 = rs.getString("tel3");
				int birthYear = rs.getInt("birthYear");
				int birthMonth = rs.getInt("birthMonth");
				int birthDate = rs.getInt("birthDate");
				member = new MemberDTO(id, pw, name, emailAccount, emailDomain, tel1, tel2, tel3, birthYear, birthMonth,
						birthDate);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect(true);
		}
		return member;
	}

	public int insert(MemberDTO member) {
		int cnt = 0;
		String id = member.getId();
		String pw = member.getPw();
		String name = member.getName();
		String emailAccount = member.getEmailAccount();
		String emailDomain = member.getEmailDomain();
		String tel1 = member.getTel1();
		String tel2 = member.getTel2();
		String tel3 = member.getTel3();
		int birthYear = member.getBirthYear();
		int birthMonth = member.getBirthMonth();
		int birthDate = member.getBirthDate();
		int staff = member.isStaff() ? 1 : 0;

		getConnection();
		String sql = "INSERT INTO CAFE_MEMBER VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			pstmt.setString(3, name);
			pstmt.setString(4, emailAccount);
			pstmt.setString(5, emailDomain);
			pstmt.setString(6, tel1);
			pstmt.setString(7, tel2);
			pstmt.setString(8, tel3);
			pstmt.setInt(9, birthYear);
			pstmt.setInt(10, birthMonth);
			pstmt.setInt(11, birthDate);
			pstmt.setInt(12, staff);

			cnt = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect(false);
		}
		return cnt;
	}

	public boolean isStaff(LoginDTO login) {
		boolean result = false;

		getConnection();
		String sql = "SELECT staff FROM CAFE_MEMBER WHERE id = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, login.getId());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				int staff = rs.getInt("staff");
				if (staff == 1) {
					result = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect(true);
		}
		return result;
	}

	public boolean hasSameId(LoginDTO login) {
		boolean result = true;
		int cnt = 0;
		getConnection();
		String sql = "SELECT * FROM CAFE_MEMBER WHERE ID = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, login.getId());

			rs = pstmt.executeQuery();
			if (rs.next()) {
				cnt++;
			}
			if (cnt == 0)
				result = false;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect(true);
		}
		return result;
	}
	
	public boolean hasSameMail(LoginDTO login) {
		boolean result = true;
		int cnt = 0;
		getConnection();	
		String sql = "SELECT * FROM CAFE_MEMBER WHERE EMAIL1 = ? AND EMAIL2 = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, login.getEmailAccount());
			pstmt.setString(2, login.getEmailDomain());

			rs = pstmt.executeQuery();
			if (rs.next()) {
				cnt++;
			}
			if (cnt == 0)
				result = false;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect(true);
		}
		return result;
	}
	
	public String findId(LoginDTO login) {
		String result = null;
		getConnection();
		String sql = "SELECT ID FROM CAFE_MEMBER WHERE EMAIL1 = ? AND EMAIL2 = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, login.getEmailAccount());
			pstmt.setString(2, login.getEmailDomain());

			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = rs.getString("id");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect(true);
		}
		return result;
	}

	public String findPw(LoginDTO login) {
		String result = null;
		int cnt = 0;
		getConnection();
		String sql = "UPDATE CAFE_MEMBER SET PW = ? WHERE EMAIL1 = ? AND EMAIL2 = ? AND ID = ?";

		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, login.getPw());
			pstmt.setString(2, login.getEmailAccount());
			pstmt.setString(3, login.getEmailDomain());
			pstmt.setString(4, login.getId());

			cnt = pstmt.executeUpdate();
			if (cnt == 1) {
				result = login.getPw();
			} else {
				result = "";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect(true);
		}
		return result;
	}

	public Vector<Vector<String>> getMemberList() {
		Vector<Vector<String>> memberLIst = new Vector<Vector<String>>();
		Vector<String> member = null;

		getConnection();
		String sql = "SELECT id, name, email1 || '@' || email2 as email, tel1 || '-' || tel2 || '-' || tel3 as tel, birthYear || '년' || birthMonth || '월' || birthDate || '일' as birthday, staff FROM CAFE_MEMBER";
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				member = new Vector<String>();
				member.add(rs.getString("id"));
				member.add(rs.getString("name"));
				member.add(rs.getString("email"));
				member.add(rs.getString("tel"));
				member.add(rs.getString("birthday"));
				member.add(rs.getInt("staff") == 1 ? "스태프" : "");
				memberLIst.add(member);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect(true);
		}
		return memberLIst;
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
