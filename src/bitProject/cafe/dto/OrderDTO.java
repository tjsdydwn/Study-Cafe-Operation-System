package bitProject.cafe.dto;

import java.io.Serializable;
import java.util.Vector;

import bitProject.cafe.dao.Status;

public class OrderDTO implements Serializable, CafeDTO {

	private static final long serialVersionUID = -7629140918119950134L;
	private int seq;
	private String id;
	private String menuName;
	private int amount;
	private int menuPrice;
	private String orderDate;
	private Vector<Vector<String>> orderList;
	private Vector<OrderDTO> orderArr;
	private Status status;

	public OrderDTO() {

	}

	public OrderDTO(int seq, String id, String menuName, int amount, int menuPrice) {
		this.seq = seq;
		this.id = id;
		this.menuName = menuName;
		this.amount = amount;
		this.menuPrice = menuPrice;
	}

	public OrderDTO(String id, String menuName, int amount, int menuPrice) {
		super();
		this.id = id;
		this.menuName = menuName;
		this.amount = amount;
		this.menuPrice = menuPrice;
	}

	public OrderDTO(Vector<OrderDTO> orderArr, Status status) {
		this.orderArr = orderArr;
		this.status = status;
	}

	public Vector<OrderDTO> getOrderArr() {
		return orderArr;
	}

	public void setOrderArr(Vector<OrderDTO> orderArr) {
		this.orderArr = orderArr;
	}

	public Vector<Vector<String>> getOrderList() {
		return orderList;
	}

	public void setOrderList(Vector<Vector<String>> orderList) {
		this.orderList = orderList;
	}

	public OrderDTO(Status status) {
		this.status = status;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getMenuPrice() {
		return menuPrice;
	}

	public void setMenuPrice(int menuPrice) {
		this.menuPrice = menuPrice;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getOrderDate() {
		return orderDate;
	}

}