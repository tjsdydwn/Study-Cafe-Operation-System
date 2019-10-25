package bitProject.cafe.view;

import java.awt.Container;
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

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import bitProject.cafe.Setting;
import bitProject.cafe.dao.Status;
import bitProject.cafe.dto.CafeDTO;
import bitProject.cafe.dto.LoginDTO;
import bitProject.cafe.dto.MemberDTO;

public class Login extends JFrame implements ActionListener, CafeNet {

	private static final long serialVersionUID = -883731442213980503L;

	private JButton btnLogin;
	private JButton btnClear;
	private JButton btnJoin;
	private JButton btnFind;

	private ButtonGroup btnGroupLogin;
	private JRadioButton rbtnStaff;
	private JRadioButton rbtnClient;

	private JTextField tfId;
	private JPasswordField ptfPw;

	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	public Login() {
		// 제목, 틀 생성 및 배치 시작
		this.setLayout(null);
		this.setSize(475, 200);
		this.setLocationRelativeTo(null);
		Container c = this.getContentPane();
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		// 제목, 틀 생성 및 배치 종료
		JLabel lblId;
		JLabel lblPw;

		// 로그인 라벨, 필드 생성 및 배치 시작
		btnGroupLogin = new ButtonGroup();

		btnLogin = new JButton("로그인");
		btnClear = new JButton("다시입력");
		btnJoin = new JButton("회원가입");
		btnFind = new JButton("ID/PW찾기");

		lblId = new JLabel("●  아이디");
		lblPw = new JLabel("●  비밀번호");

		tfId = new JTextField(30);
		ptfPw = new JPasswordField(30);
		rbtnStaff = new JRadioButton("Staff", false);
		rbtnClient = new JRadioButton("Client", true);

		btnGroupLogin.add(rbtnStaff);
		btnGroupLogin.add(rbtnClient);

		btnLogin.setBounds(30, 115, 95, 30);
		btnClear.setBounds(135, 115, 95, 30);
		btnJoin.setBounds(240, 115, 95, 30);
		btnFind.setBounds(345, 115, 95, 30);
		lblId.setBounds(40, 30, 100, 30);
		lblPw.setBounds(40, 60, 100, 30);
		tfId.setBounds(135, 30, 200, 30);
		ptfPw.setBounds(135, 60, 200, 30);
		rbtnStaff.setBounds(370, 30, 100, 30);
		rbtnClient.setBounds(370, 60, 100, 30);

		c.add(lblId);
		c.add(lblPw);
		c.add(tfId);
		c.add(ptfPw);
		c.add(rbtnStaff);
		c.add(rbtnClient);
		c.add(btnLogin);
		c.add(btnClear);
		c.add(btnJoin);
		c.add(btnFind);
		connectToServer();

		// 로그인 라벨, 필드 생성 및 배치 종료
		this.setVisible(true);
		addEvent();
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				request(new LoginDTO("", Status.FAILURE));
				Object temp = response();
				if (temp instanceof LoginDTO) {
					LoginDTO loginDTO = (LoginDTO) temp;
					if (loginDTO.getStatus() == Status.FAILURE) {
						disconnect();
						System.exit(0);
					}
				}
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnLogin) {
			String id = tfId.getText();
			String pw = String.valueOf(ptfPw.getPassword());

			if (id.length() < 1 || pw.length() < 1) {
				JOptionPane.showMessageDialog(this, "아이디, 비밀번호를 입력해주세요.");
			}

			if (rbtnStaff.isSelected()) { // 스태프일 때
				LoginDTO login = new LoginDTO(id, pw, Status.LOGIN_STAFF);
				request(login);
				Object temp = response();
				if (temp instanceof MemberDTO) {
					MemberDTO memberDTO = (MemberDTO) temp;
					if (memberDTO.getStatus() == Status.LOGIN_STAFF) {
						disconnect();
						StaffFrame sf = new StaffFrame(memberDTO);
						sf.setVisible(true);
						this.dispose();
					} else if (memberDTO.getStatus() == Status.FAILURE) {
						JOptionPane.showMessageDialog(this, "아이디, 또는 비밀번호가 일치하지 않습니다.");
					}
				} else {
					return;
				}
			} else { // 클라이언트일 때.
				LoginDTO login = new LoginDTO(id, pw, Status.LOGIN_CLIENT);
				request(login);
				Object temp = response();
				if (temp instanceof MemberDTO) {
					MemberDTO member = (MemberDTO) temp;
					if (member.getStatus() == Status.LOGIN_CLIENT) {
						disconnect();
						ClientFrame cf = new ClientFrame(member);
						cf.setVisible(true);
						this.dispose();
					} else if (member.getStatus() == Status.FAILURE) {
						JOptionPane.showMessageDialog(this, "아이디, 또는 비밀번호가 일치하지 않습니다.");
					}
				} else {
					return;
				}
			}
		} else if (e.getSource() == btnJoin) {
			new Join(this);
		} else if (e.getSource() == rbtnStaff) {
			String id = tfId.getText();

			if (id.length() < 1) {
				JOptionPane.showMessageDialog(this, "존재하지 않는 스태프 아이디입니다.");
				rbtnClient.setSelected(true);
				return;
			}

			LoginDTO loginDTO = new LoginDTO(id, Status.STAFF);
			request(loginDTO);
			Object temp = response();
			if (temp instanceof LoginDTO) {
				loginDTO = (LoginDTO) temp;
				if (loginDTO.getStatus() == Status.FAILURE) {
					JOptionPane.showMessageDialog(this, "존재하지 않는 스태프 아이디입니다.");
					rbtnClient.setSelected(true);
					return;
				}
			}
		} else if (e.getSource() == btnClear) {
			clear();
		} else if (e.getSource() == btnFind) {
			new Find(this);
		}
	}

	@Override
	public void connectToServer() {
		try {
			socket = new Socket(Setting.SERVER_IP, Setting.PORT);
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addEvent() {
		btnLogin.addActionListener(this);
		btnClear.addActionListener(this);
		btnJoin.addActionListener(this);
		btnFind.addActionListener(this);
		rbtnStaff.addActionListener(this);
	}

	public void clear() {
		tfId.setText("");
		ptfPw.setText("");
		rbtnClient.setSelected(true);
	}

	@Override
	public void request(CafeDTO cafe) {
		try {
			oos.writeObject(cafe);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
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

}
