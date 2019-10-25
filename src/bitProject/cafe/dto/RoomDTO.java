package bitProject.cafe.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

import bitProject.cafe.dao.Status;

public class RoomDTO implements Serializable, CafeDTO {
	private static final long serialVersionUID = 7774346678492384119L;
	private int seq;
	private int roomNum;
	private String id;
	private int year;
	private int month;
	private int date;
	private int inHour;
	private int outHour;
	private int price;
	private int subseq;
	private Status status;
	private ArrayList<RoomDTO> roomArrayList;
	private Vector<Vector<String>> roomList;

	public RoomDTO(int roomNum, String id, int year, int month, int date, int inHour, int outHour, int price) {
		super();
		this.roomNum = roomNum;
		this.id = id;
		this.year = year;
		this.month = month;
		this.date = date;
		this.inHour = inHour;
		this.outHour = outHour;
		this.price = price;
	}

	public int getSubseq() {
		return subseq;
	}

	public void setSubseq(int subseq) {
		this.subseq = subseq;
	}

	public RoomDTO() {
		super();
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public int getRoomNum() {
		return roomNum;
	}

	public void setRoomNum(int roomNum) {
		this.roomNum = roomNum;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public int getInHour() {
		return inHour;
	}

	public void setInHour(int inHour) {
		this.inHour = inHour;
	}

	public int getOutHour() {
		return outHour;
	}

	public void setOutHour(int outHour) {
		this.outHour = outHour;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public ArrayList<RoomDTO> getRoomArrayList() {
		return roomArrayList;
	}

	public void setRoomArrayList(ArrayList<RoomDTO> roomArrayList) {
		this.roomArrayList = roomArrayList;
	}

	public Vector<Vector<String>> getRoomList() {
		return roomList;
	}

	public void setRoomList(Vector<Vector<String>> roomList) {
		this.roomList = roomList;
	}
}
