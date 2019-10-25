package bitProject.cafe.dto;

import java.io.Serializable;
import java.util.Vector;

import bitProject.cafe.dao.Status;

public class MemberDTO implements Serializable, CafeDTO {
	private static final long serialVersionUID = 5357461459330133901L;
	private String id;
	private String pw;
	private String name;
	private String emailAccount;
	private String emailDomain;
	private String tel1;
	private String tel2;
	private String tel3;
	private int birthYear;
	private int birthMonth;
	private int birthDate;
	private boolean isStaff;
	private Vector<Vector<String>> memberList;
	private Status status;

	public MemberDTO() {
		super();
	}

	public MemberDTO(String id, String pw, String name, String emailAccount, String emailDomain, String tel1,
			String tel2, String tel3, int birthYear, int birthMonth, int birthDate) {
		super();
		this.id = id;
		this.pw = pw;
		this.name = name;
		this.emailAccount = emailAccount;
		this.emailDomain = emailDomain;
		this.tel1 = tel1;
		this.tel2 = tel2;
		this.tel3 = tel3;
		this.birthYear = birthYear;
		this.birthMonth = birthMonth;
		this.birthDate = birthDate;
		isStaff = false;
		status = Status.WELCOME;
	}

	public Vector<Vector<String>> getMemberList() {
		return memberList;
	}

	public void setMemberList(Vector<Vector<String>> memberList) {
		this.memberList = memberList;
	}

	public MemberDTO(String name, Status status) {
		this.name = name;
		this.status = status;
	}

	public MemberDTO(String id, String pw, String emailAccount, String emailDomain, String tel1, String tel2,
			String tel3) {
		this.id = id;
		this.pw = pw;
		this.emailAccount = emailAccount;
		this.emailDomain = emailDomain;
		this.tel1 = tel1;
		this.tel2 = tel2;
		this.tel3 = tel3;
		isStaff = false;
		status = Status.CHANGE_MY_INFO;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmailAccount() {
		return emailAccount;
	}

	public void setEmailAccount(String emailAccount) {
		this.emailAccount = emailAccount;
	}

	public String getEmailDomain() {
		return emailDomain;
	}

	public void setEmailDomain(String emailDomain) {
		this.emailDomain = emailDomain;
	}

	public String getTel1() {
		return tel1;
	}

	public void setTel1(String tel1) {
		this.tel1 = tel1;
	}

	public String getTel2() {
		return tel2;
	}

	public void setTel2(String tel2) {
		this.tel2 = tel2;
	}

	public String getTel3() {
		return tel3;
	}

	public void setTel3(String tel3) {
		this.tel3 = tel3;
	}

	public int getBirthYear() {
		return birthYear;
	}

	public void setBirthYear(int birthYear) {
		this.birthYear = birthYear;
	}

	public int getBirthMonth() {
		return birthMonth;
	}

	public void setBirthMonth(int birthMonth) {
		this.birthMonth = birthMonth;
	}

	public int getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(int birthDate) {
		this.birthDate = birthDate;
	}

	public boolean isStaff() {
		return isStaff;
	}

	public void setStaff(boolean isStaff) {
		this.isStaff = isStaff;
	}
}
