package bitProject.cafe.dto;

import java.io.Serializable;
import java.util.Vector;

import bitProject.cafe.dao.Status;

public class BoardDTO implements Serializable, CafeDTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8559845052902967529L;
	private int seq;
	private String id;
	private String text;
	private String writeTime;
	private Vector<Vector<String>> boardList;
	private Status status;

	public BoardDTO(Status status) {
		this.status = status;
	}

	public BoardDTO(String id, String text, String writeTime) {
		this.id = id;
		this.text = text;
		this.writeTime = writeTime;
	}

	public Vector<Vector<String>> getBoardList() {
		return boardList;
	}

	public void setBoardList(Vector<Vector<String>> boardList) {
		this.boardList = boardList;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	public String getWriteTime() {
		return writeTime;
	}

	public void setWriteTime(String writeTime) {
		this.writeTime = writeTime;
	}

}
