package bitProject.cafe.view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import bitProject.cafe.Setting;
import bitProject.cafe.dao.Status;
import bitProject.cafe.dto.LoginDTO;

public class Find extends JFrame implements ActionListener {

	private static final long serialVersionUID = 4926734514679038128L;
	private Login login;
	private JPanel contentPane;
	private JTextField tfId, tfEmailAccount, tfEmailDomain, tfCode;
	private JButton btnSendId, btnSendPw, btnSendCode, btnConfirmCode, btnClear, btnClose;

	public Find(Login login) {
		this.login = login;

		this.setLayout(null);
		setSize(500, 270);
		setLocationRelativeTo(login);
		this.setResizable(false);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(login);
		setResizable(false);

		JLabel lblTitle = new JLabel("ID/PW 찾기");
		lblTitle.setBounds(193, 10, 78, 30);
		contentPane.add(lblTitle);

		JLabel lblPwRe = new JLabel("이메일");
		lblPwRe.setFont(Setting.M_GODIC_B_13);
		lblPwRe.setBounds(37, 59, 77, 15);
		contentPane.add(lblPwRe);

		tfEmailAccount = new JTextField(); // 이메일입력 텍필
		tfEmailAccount.setFont(Setting.M_GODIC_B_13);
		tfEmailAccount.setBounds(120, 55, 100, 21);
		contentPane.add(tfEmailAccount);

		JLabel lblAt = new JLabel("@");
		lblAt.setFont(Setting.M_GODIC_B_13);
		lblAt.setBounds(224, 58, 57, 15);
		contentPane.add(lblAt);

		tfEmailDomain = new JTextField();
		tfEmailDomain.setFont(Setting.M_GODIC_B_13);
		tfEmailDomain.setBounds(240, 55, 85, 21);
		contentPane.add(tfEmailDomain);

		btnSendCode = new JButton("코드 발송");
		btnSendCode.setBounds(337, 53, 108, 23);
		contentPane.add(btnSendCode);

		JLabel lblCode = new JLabel("코드 입력");
		lblCode.setFont(Setting.M_GODIC_B_13);
		lblCode.setBounds(37, 92, 77, 15);
		contentPane.add(lblCode);

		tfCode = new JTextField(""); // 코드입력 텍필
		tfCode.setFont(Setting.M_GODIC_B_13);
		tfCode.setBounds(120, 88, 100, 21);
		contentPane.add(tfCode);

		btnConfirmCode = new JButton("코드 확인");
		btnConfirmCode.setBounds(337, 86, 108, 23);
		contentPane.add(btnConfirmCode);

		JLabel lblId = new JLabel("확인된 이메일로 아이디 발송");
		lblId.setFont(Setting.M_GODIC_B_13);
		lblId.setBounds(37, 125, 177, 15);
		contentPane.add(lblId);

		btnSendId = new JButton("아이디 발송");
		btnSendId.setBounds(337, 119, 108, 23);
		btnSendId.setEnabled(false);
		contentPane.add(btnSendId);

		tfId = new JTextField();
		tfId.setFont(new Font("나눔고딕", Font.BOLD, 12));
		tfId.setBounds(220, 154, 105, 21);
		contentPane.add(tfId);

		JLabel lblPw = new JLabel("아이디 입력 후 비밀번호 발송");
		lblPw.setFont(Setting.M_GODIC_B_13);
		lblPw.setBounds(37, 158, 177, 15);
		contentPane.add(lblPw);

		btnSendPw = new JButton("비밀번호 발송");
		btnSendPw.setBounds(337, 152, 108, 23);
		btnSendPw.setFont(Setting.M_GODIC_B_11);
		btnSendPw.setEnabled(false);
		contentPane.add(btnSendPw);

		btnClear = new JButton("다시입력");
		btnClear.setBounds(110, 195, 120, 23);
		contentPane.add(btnClear);

		btnClose = new JButton("나가기");
		btnClose.setBounds(270, 195, 120, 23);
		contentPane.add(btnClose);

		addEvent();
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.setVisible(true);
	}

	public void addEvent() {
		btnConfirmCode.addActionListener(this);
		btnSendCode.addActionListener(this);
		btnSendId.addActionListener(this);
		btnSendPw.addActionListener(this);
		btnClear.addActionListener(this);
		btnClose.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnSendCode) {
			String emailAccount = tfEmailAccount.getText();
			String emailDomain = tfEmailDomain.getText();

			if (emailAccount.length() < 1 || emailDomain.length() < 1) {
				JOptionPane.showMessageDialog(this, "이메일을 입력해주세요.");
				return;
			}
			LoginDTO loginDTO = new LoginDTO(emailAccount, emailDomain);
			loginDTO.setStatus(Status.FIND_EMAIL);
			login.request(loginDTO);
			Object temp = login.response();
			if (temp instanceof LoginDTO) {
				loginDTO = (LoginDTO) temp;
				if (loginDTO.getStatus() == Status.FIND_EMAIL) {
					tfEmailAccount.setEditable(false);
					tfEmailDomain.setEditable(false);
					btnSendCode.setEnabled(false);
					btnSendCode.setText("발송완료");
					JOptionPane.showMessageDialog(this, "입력하신 메일주소로 인증코드를 발송했습니다.");
				} else {
					JOptionPane.showMessageDialog(this, "입력하신 이메일이 등록되어 있지 않습니다.");
				}
			}
		} else if (e.getSource() == btnConfirmCode) {
			String inputCode = tfCode.getText();
			LoginDTO loginDTO = new LoginDTO(inputCode, Status.JOIN);
			login.request(loginDTO);

			Object temp = login.response();
			if (temp instanceof LoginDTO) {
				loginDTO = (LoginDTO) temp;
				if (loginDTO.getStatus() == Status.JOIN) {
					JOptionPane.showMessageDialog(this, "인증코드 확인을 완료했습니다.");
					tfCode.setEditable(false);
					btnSendCode.setEnabled(false);
					btnConfirmCode.setEnabled(false);
					btnConfirmCode.setText("완료");

					btnSendId.setEnabled(true);
					btnSendPw.setEnabled(true);

				} else {
					JOptionPane.showMessageDialog(this, "인증코드를 다시 확인해주세요.");
				}
			}
		} else if (e.getSource() == btnSendId) {

			if (btnConfirmCode.isEnabled()) {
				JOptionPane.showMessageDialog(this, "이메일을 인증해주세요.");
				return;
			}

			String emailAccount = tfEmailAccount.getText();
			String emailDomain = tfEmailDomain.getText();
			LoginDTO loginDTO = new LoginDTO(emailAccount, emailDomain);
			loginDTO.setStatus(Status.SEND_MY_ID);

			login.request(loginDTO);
			Object temp = login.response();
			if (temp instanceof LoginDTO) {
				loginDTO = (LoginDTO) temp;
				if (loginDTO.getStatus() == Status.SEND_MY_ID) {
					JOptionPane.showMessageDialog(this, "메일로 아이디 발송을 완료했습니다.");
				} else {
					JOptionPane.showMessageDialog(this, "메일 발송을 실패했습니다.");
				}
			}

		} else if (e.getSource() == btnSendPw) {

			if (btnConfirmCode.isEnabled()) {
				JOptionPane.showMessageDialog(this, "이메일을 인증해주세요.");
				return;
			}
			if (tfId.getText().equals("")) {
				JOptionPane.showMessageDialog(this, "아이디를 입력해주세요.");
			}

			String emailAccount = tfEmailAccount.getText();
			String emailDomain = tfEmailDomain.getText();
			LoginDTO loginDTO = new LoginDTO(emailAccount, emailDomain);
			loginDTO.setId(tfId.getText());
			;
			loginDTO.setStatus(Status.SEND_MY_PW);

			login.request(loginDTO);
			Object temp = login.response();
			if (temp instanceof LoginDTO) {
				loginDTO = (LoginDTO) temp;
				if (loginDTO.getStatus() == Status.SEND_MY_PW) {
					JOptionPane.showMessageDialog(this, "메일로 임시 비밀번호 발송을 완료했습니다.");
				} else {
					JOptionPane.showMessageDialog(this, "메일 발송을 실패했습니다.");
				}
			}
		} else if (e.getSource() == btnClear) {
			tfCode.setText("");
			tfEmailAccount.setText("");
			tfEmailDomain.setText("");
			tfId.setText("");

			btnSendCode.setEnabled(true);
			btnConfirmCode.setEnabled(true);
			btnSendId.setEnabled(false);
			btnSendPw.setEnabled(false);
		} else if (e.getSource() == btnClose) {
			this.dispose();
		}

	}

}
