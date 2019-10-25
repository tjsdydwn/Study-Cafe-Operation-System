package bitProject.cafe.dto;

import java.io.Serializable;

import bitProject.cafe.dao.Status;

public class LoginDTO implements Serializable, CafeDTO {

	private static final long serialVersionUID = -3398789852219044576L;
	private String id;
	private String pw;
	private String emailAccount;
	private String emailDomain;
	private Status status;
	private int staff;

	public LoginDTO(String id, Status status) {
		this.id = id;
		this.status = status;
	}

	public LoginDTO(String id, String pw, Status status) {
		super();
		this.id = id;
		this.pw = pw;
		this.status = status;
	}

	public LoginDTO(String emailAccount, String emailDomain) {
		this.emailAccount = emailAccount;
		this.emailDomain = emailDomain;
		status = Status.SEND_EMAIL;
	}

	public LoginDTO(String id, Status status, int staff) {
		super();
		this.id = id;
		this.status = status;
		this.staff = staff;
	}

	public int getStaff() {
		return staff;
	}

	public void setStaff(int staff) {
		this.staff = staff;
	}

	public String getEmailAccount() {
		return emailAccount;
	}

	public String getEmailDomain() {
		return emailDomain;
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
