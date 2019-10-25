package bitProject.cafe.dto;

import java.io.Serializable;
import java.util.Vector;

import bitProject.cafe.dao.Status;

//seq number primary key,
//menuName varchar2(40),
//amount number, 
//menuPrice number, 
//orderDate varchar2(40)

public class SalesDTO implements Serializable, CafeDTO {
	private static final long serialVersionUID = 8973548306773886664L;
	private int seq;
	private String menuName;
	private int amount;
	private int menuPrice;
	private int tot;
	private int subseq;
	private String orderDate;
	private Vector<Vector<String>> salesList;
	private Status status;

	public SalesDTO(int seq, String menuName, int amount, int menuPrice, int tot, String orderDate) {
		super();
		this.seq = seq;
		this.menuName = menuName;
		this.amount = amount;
		this.menuPrice = menuPrice;
		this.tot = tot;
		this.orderDate = orderDate;
	}
	
	public SalesDTO(Status status) {
		this.status = status;
	}

	public int getAmount() {
		return amount;
	}

	public String getMenuName() {
		return menuName;
	}

	public int getMenuPrice() {
		return menuPrice;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public Vector<Vector<String>> getSalesList() {
		return salesList;
	}

	public int getSeq() {
		return seq;
	}

	public Status getStatus() {
		return status;
	}

	public int getSubseq() {
		return subseq;
	}

	public int getTot() {
		return tot;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public void setMenuPrice(int menuPrice) {
		this.menuPrice = menuPrice;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public void setSalesList(Vector<Vector<String>> salesList) {
		this.salesList = salesList;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setSubseq(int subseq) {
		this.subseq = subseq;
	}

	public void setTot(int tot) {
		this.tot = tot;
	}

}
