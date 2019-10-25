package bitProject.cafe.net;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import bitProject.cafe.dao.BoardDAO;
import bitProject.cafe.dao.MemberDAO;
import bitProject.cafe.dao.OrderDAO;
import bitProject.cafe.dao.RoomDAO;
import bitProject.cafe.dao.SalesDAO;
import bitProject.cafe.dao.Status;
import bitProject.cafe.dto.BoardDTO;
import bitProject.cafe.dto.CafeDTO;
import bitProject.cafe.dto.LoginDTO;
import bitProject.cafe.dto.MemberDTO;
import bitProject.cafe.dto.OrderDTO;
import bitProject.cafe.dto.RoomDTO;
import bitProject.cafe.dto.SalesDTO;
import bitProject.cafe.view.CafeNet;

public class CafeServerHandler extends Thread implements CafeNet {

	private Socket socket;
	private ArrayList<CafeServerHandler> list;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private String code;

	public CafeServerHandler(Socket socket, ArrayList<CafeServerHandler> list) {
		this.socket = socket;
		this.list = list;

		try {
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			while (true) {
				Object temp = response();
				// ----------------------------------------로그인, 아이디 중복검사 처리
				if (temp instanceof LoginDTO) {
					LoginDTO login = (LoginDTO) temp;
					if (login.getStatus() == Status.LOGIN_CLIENT) {
						MemberDTO memberDTO = MemberDAO.getInstance().tryLogin(login.getId(), login.getPw());
						if (memberDTO == null) {
							request(new MemberDTO("", Status.FAILURE));
						} else {
							memberDTO.setStatus(Status.LOGIN_CLIENT);
							memberDTO.setStaff(false);
							list.remove(this);
							request(memberDTO);
							disconnect();
							break;
						}
					} else if (login.getStatus() == Status.LOGIN_STAFF) {
						MemberDTO memberDTO = MemberDAO.getInstance().tryLogin(login.getId(), login.getPw());
						if (memberDTO == null) {
							request(new MemberDTO("", Status.FAILURE));
						} else {
							memberDTO.setStatus(Status.LOGIN_STAFF);
							memberDTO.setStaff(true);
							list.remove(this);
							request(memberDTO);
							disconnect();
							break;
						}
					} else if (login.getStatus() == Status.CHECK_MY_ID) {
						if (MemberDAO.getInstance().hasSameId(login)) {
							login.setStatus(Status.FAILURE);
						}
						request(login);
					} else if (login.getStatus() == Status.FAILURE) {
						list.remove(this);
						request(login);
						disconnect();
						break;
					} else if (login.getStatus() == Status.LOGOUT) {
						list.remove(this);
						request(login);
						disconnect();
						break;
					} else if (login.getStatus() == Status.SEND_EMAIL) {
						code = createEmailCode();
						String addr = login.getEmailAccount() + "@" + login.getEmailDomain();
						String title = "스터디 카페 메일인증 코드입니다";
						String content = "아래의 코드를 입력해주세요.\t" + code;
						sendEmail(new String[] { addr }, "aquavictoria2@gmail.com", "Study Cafe", title, content);
						request(login);
					} else if (login.getStatus() == Status.JOIN) { // 인증코드 확인
						if (!login.getId().equals(code)) {
							login.setStatus(Status.FAILURE);
						}
						request(login);
					} else if (login.getStatus() == Status.STAFF) { // 스태프인지 확인
						if (!MemberDAO.getInstance().isStaff(login)) {
							login.setStatus(Status.FAILURE);
						}
						request(login);
					} else if (login.getStatus() == Status.LEAVE) { // 회원탈퇴
						if (!MemberDAO.getInstance().delete(login.getId())) {
							login.setStatus(Status.FAILURE);
						}
						request(login);
					} else if (login.getStatus() == Status.FIND_EMAIL) {
						boolean hasEmail = MemberDAO.getInstance().hasSameMail(login);
						if (hasEmail) {
							code = createEmailCode();
							String addr = login.getEmailAccount() + "@" + login.getEmailDomain();
							String title = "스터디 카페 메일인증 코드입니다";
							String content = "아래의 코드를 입력해주세요.\t" + code;
							sendEmail(new String[] { addr }, "aquavictoria2@gmail.com", "Study Cafe", title, content);
						} else {
							login.setStatus(Status.FAILURE);
						}
						request(login);
					} else if (login.getStatus() == Status.SEND_MY_ID) {
						String id = MemberDAO.getInstance().findId(login); // 디비에서 아이디 가져오기
						String addr = login.getEmailAccount() + "@" + login.getEmailDomain();
						String title = "스터디 카페 아이디 찾기입니다";
						String content = "해당 메일로 가입하신 아이디는\t[" + id + "] 입니다.";
						sendEmail(new String[] { addr }, "aquavictoria2@gmail.com", "Study Cafe", title, content);
						request(login);
					} else if (login.getStatus() == Status.SEND_MY_PW) {
						login.setPw(createEmailCode());
						String pw = MemberDAO.getInstance().findPw(login); // 디비에 패스워드 입력 후 가져오기
						String addr = login.getEmailAccount() + "@" + login.getEmailDomain();
						String title = "스터디 카페 비밀번호 발송입니다";
						String content = "임시 비밀번호를 생성하여 발송드리오니, 내정보에서 변경하여 주시기 바랍니다.\n" + "임시 비밀번호는 [" + pw + "] 입니다.";
						sendEmail(new String[] { addr }, "aquavictoria2@gmail.com", "Study Cafe", title, content);
						request(login);
					}
				}
				// -----------------------------------------매출처리
				if (temp instanceof SalesDTO) {
					SalesDTO salesDTO = (SalesDTO) temp;
					if (salesDTO.getStatus() == Status.GET_FROM_DB) {
						salesDTO.setSalesList(SalesDAO.getInstance().getSalesList());
						request(salesDTO);
					} else if (salesDTO.getStatus() == Status.DELETE) {
						SalesDAO.getInstance().delete(salesDTO.getSeq());
					} else if (salesDTO.getStatus() == Status.DELETE_SUB) {
						SalesDAO.getInstance().delete(salesDTO);
					}
				}

				// -----------------------------------------주문처리
				if (temp instanceof OrderDTO) {
					OrderDTO orderDTO = (OrderDTO) temp;
					if (orderDTO.getStatus() == Status.GET_FROM_DB) {
						orderDTO.setOrderList(OrderDAO.getInstance().getFromDB());
						request(orderDTO);
					} else if (orderDTO.getStatus() == Status.INSERT) {
						ArrayList<OrderDTO> list = OrderDAO.getInstance().getFromDB(orderDTO);
						OrderDAO.getInstance().insert(list);
						OrderDAO.getInstance().delete();
					} else if (orderDTO.getStatus() == Status.DELETE) {
						OrderDAO.getInstance().delete(orderDTO.getSeq());
						request(orderDTO);
					} else if (orderDTO.getStatus() == Status.INSERT_SALES) {
						SalesDAO.getInstance().insert(orderDTO);
						request(orderDTO);
					} else if (orderDTO.getStatus() == Status.INSERT_TEMP) {
						OrderDAO.getInstance().insert(orderDTO, 1);
					} else if (orderDTO.getStatus() == Status.DELETE_SUB) {
						OrderDAO.getInstance().delete(orderDTO);
					}
				}

				// ----------------------------------------게시판처리
				if (temp instanceof BoardDTO) {
					BoardDTO board = (BoardDTO) temp;
					if (board.getStatus() == Status.WRITE_BOARD) { // 게시판 글쓰기 처리
						board = BoardDAO.getInstance().writeBoard(board);
						if (board == null) {
							System.out.println("글쓰기 작성 실패");
						} else {
							board.setBoardList(BoardDAO.getInstance().getAllBoardContents());
							request(board);
						}
					} else if (board.getStatus() == Status.GET_FROM_DB) { // 게시판 갱신 처리
						board.setBoardList(BoardDAO.getInstance().getAllBoardContents());
						request(board);
					} else if (board.getStatus() == Status.DELETE) { // 글 삭제처리
						int cnt = BoardDAO.getInstance().delete(board);
						if (cnt == 0) {
							board.setStatus(Status.FAILURE);
						}
						request(board);
					}
				}

				// ----------------------------------------스터디룸 처리
				if (temp instanceof RoomDTO) {
					RoomDTO room = (RoomDTO) temp;
					if (room.getStatus() == Status.RESERVATION) { // 예약 메시지일 때 처리
						if (RoomDAO.getInstance().hasSameReservation(room)) { // 중복 예약 확인.
							room.setStatus(Status.FAILURE);
							request(room);
						} else {
							int subseq = RoomDAO.getInstance().insert(room);
							room.setSubseq(subseq);
							request(room);
						}
					} else if (room.getStatus() == Status.CHECK_MY_RESERVATION) { // 조회처리
						ArrayList<RoomDTO> roomArrayList = RoomDAO.getInstance().check(room);
						room = new RoomDTO();
						room.setRoomArrayList(roomArrayList);
						room.setStatus(Status.CHECK_MY_RESERVATION);
						request(room);
					} else if (room.getStatus() == Status.GET_MINE) { // 예약 조회 처리
						room.setRoomList(RoomDAO.getInstance().getMyList(room.getId()));
						request(room);
					} else if (room.getStatus() == Status.DELETE) { // 예약 취소 처리
						int cnt = RoomDAO.getInstance().delete(room);
						if (cnt == 0) {
							room.setStatus(Status.FAILURE);
						}
						request(room);
					} else if (room.getStatus() == Status.GET_FROM_DB) { // 모든 예약 불러오기
						room.setRoomList(RoomDAO.getInstance().getAllList());
						request(room);
					}
				}

				// ----------------------------------------입장/종료/회원가입
				if (temp instanceof MemberDTO) {
					MemberDTO member = (MemberDTO) temp;
					if (member.getStatus() == Status.WELCOME) {
						System.out.println(member.getName() + " 님이 접속하셨습니다.");
						System.out.println("현재 접속한 사용자 수 : " + list.size());
					} else if (member.getStatus() == Status.LOGOUT) {
						list.remove(this);
						System.out.println(member.getName() + " 님이 나가셨습니다.");
						request(member);
						disconnect();
						break;
					} else if (member.getStatus() == Status.JOIN) { // 회원가입처리
						int cnt = MemberDAO.getInstance().insert(member);
						if (cnt != 0) {
							request(member);
						} else {
							member.setStatus(Status.FAILURE);
							request(member);
						}
					} else if (member.getStatus() == Status.CHANGE_MY_INFO) { // 개인정보 수정
						int cnt = MemberDAO.getInstance().update(member);
						if (cnt == 0) {
							member.setStatus(Status.FAILURE);
						}
						request(member);
					} else if (member.getStatus() == Status.GET_FROM_DB) { // 회원 내역 모두 가져오기.
						member.setMemberList(MemberDAO.getInstance().getMemberList());
						request(member);
					} else if (member.getStatus() == Status.UPDATE) { // 회원 권한 변경
						if (member.isStaff()) {
							MemberDAO.getInstance().update(member.getId(), 0);
						} else {
							MemberDAO.getInstance().update(member.getId(), 1);
						}
						request(member);
					}
				}
			}
		} finally {
			disconnect();
		}
	}

	@Override
	public Object response() {
		Object objectRecieved = null;
		while (true) {
			try {
				objectRecieved = ois.readObject();
				break;
			} catch (EOFException e) {
				objectRecieved = null;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return objectRecieved;
	}

	@Override
	public void request(CafeDTO cafeDTO) {
		try {
			oos.writeObject(cafeDTO);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String createEmailCode() { // 난수 코드 발행
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i <= 6; i++) {
			if ((int) (Math.random() * 2) == 0) {
				buffer.append((char) ((int) (Math.random() * 10) + 48));
			} else {
				buffer.append((char) ((int) (Math.random() * 26) + 97));
			}
		}
		return buffer.toString();
	}

	@Override
	public void disconnect() {
		try {
			if (oos != null)
				oos.close();
			if (ois != null)
				ois.close();
			if (socket != null)
				socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendEmail(String[] addr, String serverAddr, String serverName, String title, String content) {
		Properties props = System.getProperties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.host", "smtp.gmail.com"); // 지메일
		props.put("mail.smtp.port", "587"); // tls용 지메일 포트번호
		props.put("mail.smtp.user", "bitProject.cafe"); // 보내는 사람 이름? 역할이 뭔지 모르겠음
		props.put("mail.smtp.auth", "true");

		try {
			Authenticator cafeAccount = new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("aquavictoria2", "fkatptm4"); // 보낼 지메일 계정
				}
			};

			Session mailSession = Session.getInstance(props, cafeAccount);
			Message msg = new MimeMessage(mailSession);
			msg.setFrom(new InternetAddress(serverAddr, MimeUtility.encodeText(serverName, "B", "UTF-8"))); // 보내는 사람 설정

			int mailCount = addr.length;
			InternetAddress[] addresses = new InternetAddress[mailCount];
			for (int i = 0; mailCount > i; i++) {
				addresses[i] = new InternetAddress(addr[i]);
			}
			msg.setRecipients(Message.RecipientType.TO, addresses);
			msg.setSubject(title);
			msg.setSentDate(new Date()); // 보내는 날짜 = 현재 시간
			msg.setContent(content, "text/html;charset=euc-kr"); // html 형식 내용설정
			Transport.send(msg); // 메일 보내기
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void connectToServer() {

	}
}
