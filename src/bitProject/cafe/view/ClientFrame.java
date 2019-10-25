package bitProject.cafe.view;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import bitProject.cafe.Setting;
import bitProject.cafe.dao.Status;
import bitProject.cafe.dto.CafeDTO;
import bitProject.cafe.dto.LoginDTO;
import bitProject.cafe.dto.MemberDTO;

public class ClientFrame extends JFrame implements ActionListener, Runnable, CafeNet {
	private static final long serialVersionUID = 6815773521340214556L;
	private MemberDTO member;
	private JPanel mainPanel; // 전체를 감싸는 패널

	// 오른쪽 상단 슬라이더
	private JPanel pnlSlider; // 실제로 움직이는 슬라이드 패널
	private JButton btnMenuArr[]; // 슬라이더 패널 내에 담을 메뉴들의 배열
	private JButton btnPrev; // 메뉴 위치를 앞으로.
	private JButton btnNext; // 메뉴 위치를 뒤로.
	private int sliderX; // 슬라이더 X의 위치.
	private int sizeBtn; // 버튼 가로세로 크기.

	// 스테이터스 창
	private JLabel lblId; // 로그인 한 계정의 아이디를 표시해줌.
	private JButton btnLogOut; // 로그아웃 버튼

	// 카드레이아웃 컨트롤
	private JPanel pnlMenuWrap; // Card 레이아웃 잡혀있는 패널
	private CardLayout card; // Card 컨트롤
	private ClientStudy roomReservation;
	private MyInfomation myInfomation;
	private Board board;
	private ChatRoom chatRoom;
	private ClientOrder order;

	// 네트워킹
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	public ClientFrame(MemberDTO member) {
		this.member = member;

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setSize(1280, 800);
		setResizable(false);
		setLocationRelativeTo(null);

		mainPanel = new JPanel();
		pnlSlider = new JPanel();
		JPanel pnlStatus = new JPanel();
		JPanel pnlBtns = new JPanel();

		mainPanel.setBackground(Color.WHITE);
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(mainPanel);
		mainPanel.setLayout(null);

		pnlStatus.setBounds(25, 25, 265, 200);
		pnlStatus.setLayout(null);

		pnlBtns.setBounds(374, 65, 810, 160);
		pnlBtns.setLayout(null);

		lblId = new JLabel(member.getName());
		lblId.setHorizontalAlignment(SwingConstants.CENTER);
		lblId.setFont(Setting.M_GODIC_B_17);
		lblId.setBounds(12, 23, 161, 52);

		JLabel lblExpression = new JLabel("님 로그인");
		lblExpression.setHorizontalAlignment(SwingConstants.CENTER);
		lblExpression.setFont(Setting.M_GODIC_B_13);
		lblExpression.setBounds(154, 23, 99, 52);

		btnLogOut = new JButton("Log Out");
		btnLogOut.setBounds(88, 152, 97, 23);

		pnlStatus.add(lblExpression);
		pnlStatus.add(lblId);
		pnlStatus.add(btnLogOut);

		// 슬라이더 구현
		sizeBtn = 160;

		String btnMenusName[] = { "홈", "스터디예약", "주문하기", "관리자문의", "개인정보", };
		btnMenuArr = new JButton[btnMenusName.length];
		for (int i = 0; i < btnMenuArr.length; i++) {
			btnMenuArr[i] = new JButton(btnMenusName[i]);
			btnMenuArr[i].setPreferredSize(new Dimension(sizeBtn - 5, sizeBtn - 5));
			btnMenuArr[i].addActionListener(this);
			btnMenuArr[i].setBackground(Color.WHITE);
			btnMenuArr[i].setForeground(Color.BLACK);
			pnlSlider.add(btnMenuArr[i]);
		}
		pnlSlider.setBounds(0, 0, sizeBtn * btnMenuArr.length, 160);
		sliderX = pnlSlider.getLocation().x;
		pnlBtns.add(pnlSlider);

		// 이전, 다음 버튼
		btnPrev = new JButton("prev");
		btnPrev.setBounds(325, 65, 35, 160);
		btnPrev.setVisible(false);

		btnNext = new JButton("prev");
		btnNext.setBounds(1200, 65, 35, 160);
		btnNext.setVisible(false);

		LblClock lblClock = new LblClock();
		lblClock.setHorizontalAlignment(SwingConstants.RIGHT);
		lblClock.setFont(Setting.M_GODIC_B_17);
		lblClock.setBounds(1099, 25, 136, 30);

		mainPanel.add(pnlStatus);
		mainPanel.add(pnlBtns);
		mainPanel.add(btnPrev);
		mainPanel.add(btnNext);
		mainPanel.add(lblClock);

		btnPrev.addActionListener(this);
		btnNext.addActionListener(this);
		btnLogOut.addActionListener(this);
		// 서버 연결하고, 카드레이아웃에 모든 패널을 붙임.
		connectToServer();
		addCardPanels();

		// 프로그램 강제종료 처리.
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				chatRoom.closeChatRoom();
				request(new MemberDTO(member.getName(), Status.LOGOUT));
				Object temp = response();
				if (temp instanceof MemberDTO) {
					MemberDTO memberDTO = (MemberDTO) temp;
					if (memberDTO.getStatus() == Status.LOGOUT) {
						disconnect();
						System.exit(0);
					}
				} else {
					return;
				}
			}
		});
	}

	public void connectToServer() {
		try {
			socket = new Socket(Setting.SERVER_IP, Setting.PORT);
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Thread t = new Thread(this);
		t.start();
	}

	public void addCardPanels() {
		pnlMenuWrap = new JPanel();
		card = new CardLayout();
		pnlMenuWrap.setLayout(card);
		pnlMenuWrap.setBounds(25, 250, 1210, 500);
		myInfomation = new MyInfomation(member, this);
		roomReservation = new ClientStudy(member, this);
		board = new Board(member, this);
		chatRoom = new ChatRoom(member);
		order = new ClientOrder(member, this);

		pnlMenuWrap.add(board, "board");
		pnlMenuWrap.add(roomReservation, "roomReservation");
		pnlMenuWrap.add(order, "order");
		pnlMenuWrap.add(chatRoom, "chatRoom");
		pnlMenuWrap.add(myInfomation, "myInfomation");

		mainPanel.add(pnlMenuWrap);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnPrev) { // Prev 버튼 이벤트
			sliderX += sizeBtn; // slider의 위치를 버튼 사이즈 만큼 이동.
			if (sliderX >= 0) { // slider의 위치가 0이라면 이동 불가.
				sliderX = 0;
				btnPrev.setVisible(false);
			}
			if (sliderX > -sizeBtn * (btnMenuArr.length - 5)) {
				btnNext.setVisible(true);
			}
			pnlSlider.setLocation(sliderX, 0);

		} else if (e.getSource() == btnNext) { // Next버튼 이벤트
			sliderX -= sizeBtn;
			if (sliderX <= -sizeBtn * (btnMenuArr.length - 5)) {
				sliderX = -sizeBtn * (btnMenuArr.length - 5);
				btnNext.setVisible(false);
			}
			if (sliderX < 0) {
				btnPrev.setVisible(true);
			}
			pnlSlider.setLocation(sliderX, 0);
		} else if (e.getSource() == btnMenuArr[0]) { // 홈으로 가기
			btnColorChange(0);
			card.show(pnlMenuWrap, "board");
		} else if (e.getSource() == btnMenuArr[1]) { // 스터디룸 예약
			btnColorChange(1);
			card.show(pnlMenuWrap, "roomReservation");
		} else if (e.getSource() == btnMenuArr[2]) { // 주문 하러 가기
			btnColorChange(2);
			card.show(pnlMenuWrap, "order");
		} else if (e.getSource() == btnMenuArr[3]) { // 문의하기
			btnColorChange(3);
			card.show(pnlMenuWrap, "chatRoom");
			chatRoom.callChatRoom(member);
		} else if (e.getSource() == btnMenuArr[4]) { // 개인정보
			btnColorChange(4);
			card.show(pnlMenuWrap, "myInfomation");
		} else if (e.getSource() == btnLogOut) { // 로그아웃
			chatRoom.closeChatRoom();
			request(new LoginDTO(member.getName(), Status.LOGOUT));
			Object temp = response();
			if (temp instanceof LoginDTO) {
				LoginDTO login = (LoginDTO) temp;
				if (login.getStatus() == Status.LOGOUT) {
					disconnect();
					new Login();
					this.dispose();
				}
			}
		}
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void run() {
		request(member);
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
				break;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				break;
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
		return objectRecieved;
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

	@Override
	public void request(CafeDTO cafeDTO) {
		try {
			oos.writeObject(cafeDTO);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void btnColorChange(int idx) {
		for (int i = 0; i < btnMenuArr.length; i++) {
			if (i == idx) {
				btnMenuArr[i].setBackground(Color.BLACK);
				btnMenuArr[i].setForeground(Color.WHITE);
			} else {
				btnMenuArr[i].setBackground(Color.WHITE);
				btnMenuArr[i].setForeground(Color.BLACK);
			}
		}
	}

}
