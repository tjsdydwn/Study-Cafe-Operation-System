package bitProject.cafe.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import bitProject.cafe.Setting;
import bitProject.cafe.dao.Status;
import bitProject.cafe.dto.LoginDTO;
import bitProject.cafe.dto.MemberDTO;

public class Join extends JFrame implements ActionListener {

	private static final long serialVersionUID = 2867185402144811174L;
	private JPanel contentPane;
	private JLabel lblAgree;
	private JTextField tfId;
	private JPasswordField ptfPw;
	private JPasswordField ptfPwRe;
	private JTextField tfName, tfEmailAccount, tfEmailDomain, tfEmailCode, tfTel1, tfTel2, tfTel3;
	private JCheckBox cbAgree;
	private JButton btnId, btnPw, btnEmail, btnConfirm, btnClear, btnClose, btnEmailCode;
	private JComboBox<String> cbxYear, cbxMonth, cbxDate;

	private Login login;

	public Join(Login login) {
		this.login = login;

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(500, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(login);
		setResizable(false);

		JLabel lblTitle = new JLabel("회원가입");
		lblTitle.setBounds(193, 10, 78, 30);
		contentPane.add(lblTitle);

		JLabel lblId = new JLabel("아이디");
		lblId.setFont(Setting.M_GODIC_B_13);
		lblId.setBounds(37, 59, 57, 15);
		contentPane.add(lblId);

		JLabel lblPw = new JLabel("비밀번호");
		lblPw.setFont(Setting.M_GODIC_B_13);
		lblPw.setBounds(37, 108, 57, 15);
		contentPane.add(lblPw);

		JLabel lblPwRe = new JLabel("비밀번호 재확인");
		lblPwRe.setFont(Setting.M_GODIC_B_13);
		lblPwRe.setBounds(37, 137, 100, 15);
		contentPane.add(lblPwRe);

		JLabel lblName = new JLabel("이름");
		lblName.setFont(Setting.M_GODIC_B_13);
		lblName.setBounds(37, 187, 57, 15);
		contentPane.add(lblName);

		JLabel lblEmail = new JLabel("이메일");
		lblEmail.setFont(Setting.M_GODIC_B_13);
		lblEmail.setBounds(37, 222, 57, 15);
		contentPane.add(lblEmail);

		JLabel lblTel = new JLabel("전화번호");
		lblTel.setFont(Setting.M_GODIC_B_13);
		lblTel.setBounds(37, 292, 57, 15);
		contentPane.add(lblTel);

		JLabel lblBirthday = new JLabel("생년월일");
		lblBirthday.setFont(Setting.M_GODIC_B_13);
		lblBirthday.setBounds(37, 324, 57, 15);
		contentPane.add(lblBirthday);

		lblAgree = new JLabel("개인정보 동의 안내 (필수)");
		lblAgree.setFont(Setting.M_GODIC_B_11);
		lblAgree.setBounds(37, 369, 148, 15);
		contentPane.add(lblAgree);

		tfId = new JTextField();
		tfId.setBounds(143, 56, 116, 21);
		contentPane.add(tfId);
		tfId.setColumns(10);

		ptfPw = new JPasswordField();
		ptfPw.setBounds(143, 105, 116, 21);
		contentPane.add(ptfPw);

		ptfPwRe = new JPasswordField();
		ptfPwRe.setBounds(143, 134, 116, 21);
		contentPane.add(ptfPwRe);

		tfName = new JTextField();
		tfName.setColumns(10);
		tfName.setBounds(143, 184, 116, 21);
		contentPane.add(tfName);

		tfEmailAccount = new JTextField();
		tfEmailAccount.setColumns(10);
		tfEmailAccount.setBounds(143, 219, 75, 21);
		contentPane.add(tfEmailAccount);

		tfEmailDomain = new JTextField();
		tfEmailDomain.setColumns(10);
		tfEmailDomain.setBounds(250, 219, 75, 21);
		contentPane.add(tfEmailDomain);

		JLabel lblAt = new JLabel("@");
		lblAt.setBounds(230, 222, 17, 15);
		contentPane.add(lblAt);

		tfEmailCode = new JTextField();
		tfEmailCode.setColumns(10);
		tfEmailCode.setBounds(143, 252, 75, 21);
		contentPane.add(tfEmailCode);

		tfTel1 = new JTextField();
		tfTel1.setColumns(10);
		tfTel1.setBounds(143, 289, 63, 21);
		contentPane.add(tfTel1);

		tfTel2 = new JTextField();
		tfTel2.setColumns(10);
		tfTel2.setBounds(228, 289, 63, 21);
		contentPane.add(tfTel2);

		tfTel3 = new JTextField();
		tfTel3.setColumns(10);
		tfTel3.setBounds(313, 289, 63, 21);
		contentPane.add(tfTel3);

		JScrollPane scroll = new JScrollPane();
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setBounds(37, 390, 409, 67);
		contentPane.add(scroll);

		JTextArea taExpression = new JTextArea();
		taExpression.setText(
				" 1. 수집하는 개인정보 : 이름, 이메일, 생년월일, 전화번호 \n 2. 목적 :  예약자 확인, 예약확인 안내메일 발송 \n   * 동의하지 않을 경우 회원가입 불가 ");
		taExpression.setFont(Setting.M_GODIC_N_13);
		taExpression.setEditable(false);
		scroll.setViewportView(taExpression);

		cbAgree = new JCheckBox("동의");
		cbAgree.setBounds(390, 365, 70, 23);
		contentPane.add(cbAgree);

		btnId = new JButton("아이디 확인");
		btnId.setBounds(297, 55, 128, 23);
		contentPane.add(btnId);

		btnPw = new JButton("비밀번호 확인");
		btnPw.setBounds(297, 133, 128, 23);
		contentPane.add(btnPw);

		btnEmail = new JButton("코드발송");
		btnEmail.setBounds(337, 218, 88, 23);
		contentPane.add(btnEmail);

		cbxYear = new JComboBox<String>();
		cbxYear.setBounds(143, 324, 63, 21);
		contentPane.add(cbxYear);

		cbxMonth = new JComboBox<String>();
		cbxMonth.setBounds(230, 324, 63, 21);
		contentPane.add(cbxMonth);

		cbxDate = new JComboBox<String>();
		cbxDate.setBounds(323, 324, 63, 21);
		contentPane.add(cbxDate);

		btnConfirm = new JButton("완료");
		btnConfirm.setBounds(81, 490, 72, 23);
		contentPane.add(btnConfirm);

		btnClear = new JButton("다시입력");
		btnClear.setBounds(193, 490, 90, 23);
		contentPane.add(btnClear);

		btnClose = new JButton("취소");
		btnClose.setBounds(329, 490, 72, 23);
		contentPane.add(btnClose);

		JLabel lblHyphen = new JLabel("-");
		lblHyphen.setBounds(213, 292, 17, 15);
		contentPane.add(lblHyphen);

		JLabel lblHyphen2 = new JLabel("-");
		lblHyphen2.setBounds(297, 292, 17, 15);
		contentPane.add(lblHyphen2);

		JLabel lblYear = new JLabel("년");
		lblYear.setBounds(213, 327, 17, 15);
		contentPane.add(lblYear);

		JLabel lblMonth = new JLabel("월");
		lblMonth.setBounds(297, 327, 17, 15);
		contentPane.add(lblMonth);

		JLabel lblDate = new JLabel("일");
		lblDate.setBounds(396, 324, 17, 15);
		contentPane.add(lblDate);

		JLabel lblCode = new JLabel("인증코드");
		lblCode.setFont(Setting.M_GODIC_B_13);
		lblCode.setBounds(37, 255, 57, 15);
		contentPane.add(lblCode);

		btnEmailCode = new JButton("인증");
		btnEmailCode.setBounds(337, 251, 88, 23);
		contentPane.add(btnEmailCode);

		JLabel lblIdExpression = new JLabel("아이디는 영문, 숫자로 이뤄진 5 ~ 12자만 가능합니다.");
		lblIdExpression.setFont(Setting.M_GODIC_N_11);
		lblIdExpression.setBounds(37, 84, 388, 15);
		contentPane.add(lblIdExpression);

		JLabel lblPwExepression = new JLabel("비밀번호는 영문, 숫자로 이루어진 7 ~ 15자만 가능합니다.");
		lblPwExepression.setFont(Setting.M_GODIC_N_11);
		lblPwExepression.setBounds(37, 162, 388, 15);
		contentPane.add(lblPwExepression);

		setCalendar();
		addEvent();
		setVisible(true);
	}

	public void addEvent() {
		btnId.addActionListener(this);
		btnPw.addActionListener(this);
		btnEmail.addActionListener(this);
		btnConfirm.addActionListener(this);
		btnClear.addActionListener(this);
		btnClose.addActionListener(this);
		cbxMonth.addActionListener(this);
		btnEmailCode.addActionListener(this);
	}

	public void setCalendar() {
		cbxYear.removeAllItems();
		cbxMonth.removeAllItems();
		cbxDate.removeAllItems();

		Calendar cal = Calendar.getInstance();
		int startYear = cal.get(Calendar.YEAR) - 90;
		int lastYear = cal.get(Calendar.YEAR);

		for (int i = lastYear; i >= startYear; i--) {
			cbxYear.addItem(i + "");
		}
		for (int i = 1; i <= 12; i++) {
			cbxMonth.addItem(i + "");
		}
		for (int i = 1; i <= 31; i++) {
			cbxDate.addItem(i + "");
		}
	}

	public void clear() {
		tfId.setEditable(true);
		tfId.setText("");
		ptfPw.setEditable(true);
		ptfPw.setText("");
		ptfPwRe.setEditable(true);
		ptfPwRe.setText("");
		btnPw.setEnabled(true);
		btnPw.setText("비밀번호 확인");
		btnId.setEnabled(true);
		btnId.setText("아이디 확인");
		btnEmail.setEnabled(true);
		btnEmail.setText("코드발송");
		btnEmailCode.setEnabled(true);
		btnEmailCode.setText("인증");
		tfName.setText("");
		tfEmailAccount.setEditable(true);
		tfEmailAccount.setText("");
		tfEmailDomain.setEditable(true);
		tfEmailDomain.setText("");
		tfEmailCode.setEditable(true);
		tfEmailCode.setText("");
		tfTel1.setText("");
		tfTel2.setText("");
		tfTel3.setText("");
		setCalendar();
		cbAgree.setSelected(false);
	}

	public boolean isSamePw(String pw, String pwRe) {
		boolean result = false;
		if (pw.equals(pwRe))
			result = true;
		return result;
	}

	public boolean isValidId(String id) {
		String regex = "^[a-zA-Z]{1}[a-zA-Z0-9]{4,11}$";
		// 시작은 영문으로만, 특수문자 안되며 영문, 숫자로 이루어진 5 ~ 12자 이하
		boolean result = Pattern.matches(regex, id);
		return result;
	}

	public boolean isValidPw(String pw) {
		String regex = "^[a-zA-Z]{1}[a-zA-Z0-9]{6,14}$";
		boolean result = Pattern.matches(regex, pw);
		// 시작은 영문으로만, 특수문자 안되며 영문, 숫자로 이루어진 7 ~ 15자
		return result;
	}

	public boolean hasSameID(String id) {
		boolean result = false;
		LoginDTO loginDTO = new LoginDTO(id, Status.CHECK_MY_ID);
		login.request(loginDTO);
		Object temp = login.response();
		if (temp instanceof LoginDTO) {
			loginDTO = (LoginDTO) temp;
			if (loginDTO.getStatus() == Status.FAILURE) {
				result = true;
			}
		}
		return result;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == cbxMonth) {
			cbxDate.removeAllItems();
			if (cbxYear.getSelectedItem() == null)
				return;
			int selectedYear = Integer.parseInt("" + cbxYear.getSelectedItem());
			int selectedMonth = cbxMonth.getSelectedIndex();
			Calendar cal = Calendar.getInstance();
			cal.set(selectedYear, selectedMonth, 1);

			int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			for (int i = 1; i <= lastDay; i++) {
				cbxDate.addItem("" + i);
			}
		} else if (e.getSource() == btnPw) { // 비밀번호 검사
			String pw = String.valueOf(ptfPw.getPassword());
			String pwRe = String.valueOf(ptfPwRe.getPassword());

			if (pw.length() < 1 || pwRe.length() < 1) {
				JOptionPane.showMessageDialog(this, "비밀번호를 입력해주세요.");
				return;
			}
			if (!isSamePw(pw, pwRe)) {
				JOptionPane.showMessageDialog(this, "비밀번호가 일치하지 않습니다.");
				return;
			}

			if (!isValidPw(pwRe)) {
				JOptionPane.showMessageDialog(this, "유효하지 않은 비밀번호 입니다.");
				return;
			}

			btnPw.setText("완료");
			btnPw.setEnabled(false);
			ptfPw.setEditable(false);
			ptfPwRe.setEditable(false);
		} else if (e.getSource() == btnClear) { // 다시 입력
			clear();
		} else if (e.getSource() == btnId) { // 아이디 검사
			String id = tfId.getText();

			if (id.length() < 1) {
				JOptionPane.showMessageDialog(this, "아이디를 입력해주세요.");
				return;
			}

			if (!isValidId(id)) {
				JOptionPane.showMessageDialog(this, "유효하지 않은 아이디입니다.");
				return;
			}

			if (hasSameID(id)) {
				JOptionPane.showMessageDialog(this, "이미 등록된 아이디입니다.");
				return;
			}

			tfId.setEditable(false);
			btnId.setEnabled(false);
			btnId.setText("완료");
		} else if (e.getSource() == btnEmail) { // 인증코드 발송
			String emailAccount = tfEmailAccount.getText();
			String emailDomain = tfEmailDomain.getText();

			if (emailAccount.length() < 1 || emailDomain.length() < 1) {
				JOptionPane.showMessageDialog(this, "이메일을 입력해주세요.");
				return;
			}

			LoginDTO loginDTO = new LoginDTO(emailAccount, emailDomain);
			login.request(loginDTO);
			Object temp = login.response();
			if (temp instanceof LoginDTO) {
				loginDTO = (LoginDTO) temp;
				if (loginDTO.getStatus() == Status.SEND_EMAIL) {
					tfEmailAccount.setEditable(false);
					tfEmailDomain.setEditable(false);
					btnEmail.setEnabled(false);
					btnEmail.setText("발송완료");
					JOptionPane.showMessageDialog(this, "입력하신 메일주소로 인증코드를 발송했습니다.");
				} else {
					JOptionPane.showMessageDialog(this, "발송 실패");
				}
			}

		} else if (e.getSource() == btnEmailCode) { // 인증코드 확인
			String id = tfEmailCode.getText();

			LoginDTO loginDTO = new LoginDTO(id, Status.JOIN);
			login.request(loginDTO);

			Object temp = login.response();
			if (temp instanceof LoginDTO) {
				loginDTO = (LoginDTO) temp;
				if (loginDTO.getStatus() == Status.JOIN) {
					JOptionPane.showMessageDialog(this, "인증코드 확인을 완료했습니다.");
					tfEmailCode.setEditable(false);
					btnEmailCode.setEnabled(false);
					btnEmailCode.setText("완료");
				} else {
					JOptionPane.showMessageDialog(this, "인증코드를 다시 확인해주세요.");
				}
			}
		} else if (e.getSource() == btnConfirm) { // 가입 완료
			if (btnId.isEnabled()) {
				JOptionPane.showMessageDialog(this, "아이디를 확인해주세요.");
				return;
			}
			if (btnPw.isEnabled()) {
				JOptionPane.showMessageDialog(this, "비밀번호를 확인해주세요.");
				return;
			}
			if (btnEmail.isEnabled() || btnEmailCode.isEnabled()) {
				JOptionPane.showMessageDialog(this, "이메일을 인증해주세요.");
				return;
			}
			if (!cbAgree.isSelected()) {
				JOptionPane.showMessageDialog(this, "개인정보제공 미동의시 회원가입이 불가합니다.");
				return;
			}
			String id = tfId.getText();
			String pw = String.valueOf(ptfPwRe.getPassword());
			String name = tfName.getText();
			String emailAccount = tfEmailAccount.getText();
			String emailDomain = tfEmailDomain.getText();
			String tel1 = tfTel1.getText();
			String tel2 = tfTel2.getText();
			String tel3 = tfTel3.getText();
			int birthYear = Integer.parseInt("" + cbxYear.getSelectedItem());
			int birthMonth = Integer.parseInt("" + cbxMonth.getSelectedItem());
			int birthDate = Integer.parseInt("" + cbxDate.getSelectedItem());

			MemberDTO memberDTO = new MemberDTO(id, pw, name, emailAccount, emailDomain, tel1, tel2, tel3, birthYear,
					birthMonth, birthDate);
			memberDTO.setStatus(Status.JOIN);
			login.request(memberDTO);

			Object temp = login.response();
			if (temp instanceof MemberDTO) {
				memberDTO = (MemberDTO) temp;
				if (memberDTO.getStatus() == Status.JOIN) {
					JOptionPane.showMessageDialog(this, "회원가입이 성공적으로 이루어졌습니다.");
				} else if (memberDTO.getStatus() == Status.FAILURE) {
					JOptionPane.showMessageDialog(this, "회원가입에 실패했습니다.");
				}
			}
			this.dispose();
		} else if (e.getSource() == btnClose) { // 회원가입 취소.
			this.dispose();
		}
	}
}
