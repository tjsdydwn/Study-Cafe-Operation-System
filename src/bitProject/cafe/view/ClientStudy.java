package bitProject.cafe.view;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import bitProject.cafe.Setting;
import bitProject.cafe.dao.Status;
import bitProject.cafe.dto.MemberDTO;
import bitProject.cafe.dto.OrderDTO;
import bitProject.cafe.dto.RoomDTO;

public class ClientStudy extends JPanel implements ActionListener {
	private static final long serialVersionUID = -1594693393272223975L;

	private JTextField tfRoomNum;
	private JTextField tfCapacity;
	private JTextField tfFacility;
	private JTextField tfPrice;

	private JButton[] btnRoomArr;

	private JComboBox<String> cbxYear;
	private JComboBox<String> cbxMonth;
	private JComboBox<String> cbxDate;

	private JComboBox<String> cbxInHour;
	private JComboBox<String> cbxOutHour;

	private JButton btnReserve;
	private JButton btnCheck;

	private MemberDTO member;
	private ClientFrame main;

	public ClientStudy(MemberDTO member, ClientFrame main) {
		this.member = member;
		this.main = main;

		setBackground(Color.WHITE);
		setBounds(new Rectangle(0, 0, 1200, 500));
		setLayout(null);

		btnRoomArr = new JButton[6];

		JPanel pnlRoom1 = new JPanel();
		pnlRoom1.setBounds(12, 10, 239, 206);
		add(pnlRoom1);
		pnlRoom1.setLayout(new GridLayout(0, 1, 0, 0));

		JLabel lblRoom1 = new JLabel("1번 방");
		lblRoom1.setHorizontalAlignment(SwingConstants.CENTER);
		pnlRoom1.add(lblRoom1);

		btnRoomArr[0] = new JButton("예약불가");
		pnlRoom1.add(btnRoomArr[0]);

		JPanel pnlRoom2 = new JPanel();
		pnlRoom2.setBounds(275, 10, 239, 206);
		add(pnlRoom2);
		pnlRoom2.setLayout(new GridLayout(0, 1, 0, 0));

		JLabel lblRoom2 = new JLabel("2번 방");
		lblRoom2.setHorizontalAlignment(SwingConstants.CENTER);
		pnlRoom2.add(lblRoom2);

		btnRoomArr[1] = new JButton("예약불가");
		pnlRoom2.add(btnRoomArr[1]);

		JPanel pnlRoom3 = new JPanel();
		pnlRoom3.setBounds(536, 10, 239, 206);
		add(pnlRoom3);
		pnlRoom3.setLayout(new GridLayout(0, 1, 0, 0));

		JLabel lblRoom3 = new JLabel("3번 방");
		lblRoom3.setHorizontalAlignment(SwingConstants.CENTER);
		pnlRoom3.add(lblRoom3);

		btnRoomArr[2] = new JButton("예약불가");
		pnlRoom3.add(btnRoomArr[2]);

		JPanel pnlRoom4 = new JPanel();
		pnlRoom4.setBounds(12, 257, 239, 206);
		add(pnlRoom4);
		pnlRoom4.setLayout(new GridLayout(0, 1, 0, 0));

		JLabel lblRoom4 = new JLabel("4번 방");
		lblRoom4.setHorizontalAlignment(SwingConstants.CENTER);
		pnlRoom4.add(lblRoom4);

		btnRoomArr[3] = new JButton("예약불가");
		pnlRoom4.add(btnRoomArr[3]);

		JPanel pnlRoom5 = new JPanel();
		pnlRoom5.setBounds(275, 257, 239, 206);
		add(pnlRoom5);
		pnlRoom5.setLayout(new GridLayout(0, 1, 0, 0));

		JLabel lblRoom5 = new JLabel("5번 방");
		lblRoom5.setHorizontalAlignment(SwingConstants.CENTER);
		pnlRoom5.add(lblRoom5);

		btnRoomArr[4] = new JButton("예약불가");
		pnlRoom5.add(btnRoomArr[4]);

		JPanel pnlRoom6 = new JPanel();
		pnlRoom6.setBounds(536, 257, 239, 206);
		add(pnlRoom6);
		pnlRoom6.setLayout(new GridLayout(0, 1, 0, 0));

		JLabel lblRoom6 = new JLabel("6번 방");
		lblRoom6.setHorizontalAlignment(SwingConstants.CENTER);
		pnlRoom6.add(lblRoom6);

		btnRoomArr[5] = new JButton("예약불가");
		pnlRoom6.add(btnRoomArr[5]);

		JPanel pnlStatus = new JPanel();
		pnlStatus.setBounds(826, 10, 362, 480);
		add(pnlStatus);
		pnlStatus.setLayout(null);

		JLabel lblCapacity = new JLabel("수용인원");
		lblCapacity.setHorizontalAlignment(SwingConstants.LEFT);
		lblCapacity.setFont(Setting.M_GODIC_B_13);
		lblCapacity.setBounds(191, 276, 55, 28);
		pnlStatus.add(lblCapacity);

		JLabel lblRoomNum = new JLabel("방 번호");
		lblRoomNum.setHorizontalAlignment(SwingConstants.LEFT);
		lblRoomNum.setFont(Setting.M_GODIC_B_13);
		lblRoomNum.setBounds(45, 276, 43, 28);
		pnlStatus.add(lblRoomNum);

		JLabel lblFacility = new JLabel("보유시설");
		lblFacility.setHorizontalAlignment(SwingConstants.LEFT);
		lblFacility.setFont(Setting.M_GODIC_B_13);
		lblFacility.setBounds(45, 352, 55, 28);
		pnlStatus.add(lblFacility);

		JLabel lblPrice = new JLabel("가격");
		lblPrice.setHorizontalAlignment(SwingConstants.LEFT);
		lblPrice.setFont(Setting.M_GODIC_B_13);
		lblPrice.setBounds(45, 314, 45, 28);
		pnlStatus.add(lblPrice);

		JLabel lblReserveTime = new JLabel("예약시간");
		lblReserveTime.setHorizontalAlignment(SwingConstants.LEFT);
		lblReserveTime.setFont(Setting.M_GODIC_B_13);
		lblReserveTime.setBounds(45, 105, 55, 28);
		pnlStatus.add(lblReserveTime);

		cbxInHour = new JComboBox<String>();
		cbxInHour.setBounds(110, 110, 45, 21);
		pnlStatus.add(cbxInHour);

		cbxOutHour = new JComboBox<String>();
		cbxOutHour.setBounds(215, 110, 45, 21);
		pnlStatus.add(cbxOutHour);

		JLabel lblInHour = new JLabel("시 부터");
		lblInHour.setHorizontalAlignment(SwingConstants.LEFT);
		lblInHour.setFont(Setting.M_GODIC_B_13);
		lblInHour.setBounds(160, 105, 43, 28);
		pnlStatus.add(lblInHour);

		JLabel lblOutHour = new JLabel("시 까지");
		lblOutHour.setHorizontalAlignment(SwingConstants.LEFT);
		lblOutHour.setFont(Setting.M_GODIC_B_13);
		lblOutHour.setBounds(270, 105, 45, 28);
		pnlStatus.add(lblOutHour);

		tfRoomNum = new JTextField();
		tfRoomNum.setEditable(false);
		tfRoomNum.setFont(Setting.M_GODIC_N_13);
		tfRoomNum.setBounds(101, 279, 78, 21);
		pnlStatus.add(tfRoomNum);
		tfRoomNum.setColumns(10);

		tfCapacity = new JTextField();
		tfCapacity.setEditable(false);
		tfCapacity.setFont(Setting.M_GODIC_N_13);
		tfCapacity.setColumns(10);
		tfCapacity.setBounds(247, 279, 78, 21);
		pnlStatus.add(tfCapacity);

		tfFacility = new JTextField();
		tfFacility.setEditable(false);
		tfFacility.setFont(Setting.M_GODIC_N_13);
		tfFacility.setColumns(10);
		tfFacility.setBounds(101, 355, 224, 21);
		pnlStatus.add(tfFacility);

		tfPrice = new JTextField();
		tfPrice.setEditable(false);
		tfPrice.setFont(Setting.M_GODIC_N_13);
		tfPrice.setColumns(10);
		tfPrice.setBounds(102, 318, 78, 21);
		pnlStatus.add(tfPrice);

		JLabel lblReserveDate = new JLabel("예약날짜");
		lblReserveDate.setHorizontalAlignment(SwingConstants.LEFT);
		lblReserveDate.setFont(Setting.M_GODIC_B_13);
		lblReserveDate.setBounds(45, 42, 55, 28);
		pnlStatus.add(lblReserveDate);

		JLabel lblYear = new JLabel("년");
		lblYear.setHorizontalAlignment(SwingConstants.LEFT);
		lblYear.setFont(Setting.M_GODIC_B_13);
		lblYear.setBounds(170, 42, 22, 28);
		pnlStatus.add(lblYear);

		JLabel lblMonth = new JLabel("월");
		lblMonth.setHorizontalAlignment(SwingConstants.LEFT);
		lblMonth.setFont(Setting.M_GODIC_B_13);
		lblMonth.setBounds(240, 42, 22, 28);
		pnlStatus.add(lblMonth);

		JLabel lblDate = new JLabel("일");
		lblDate.setHorizontalAlignment(SwingConstants.LEFT);
		lblDate.setFont(Setting.M_GODIC_B_13);
		lblDate.setBounds(315, 42, 22, 28);
		pnlStatus.add(lblDate);

		cbxYear = new JComboBox<String>();
		cbxYear.setBounds(110, 45, 55, 21);
		pnlStatus.add(cbxYear);

		cbxMonth = new JComboBox<String>();
		cbxMonth.setBounds(191, 45, 45, 21);
		pnlStatus.add(cbxMonth);

		cbxDate = new JComboBox<String>();
		cbxDate.setBounds(263, 45, 45, 21);
		pnlStatus.add(cbxDate);

		btnReserve = new JButton("예약하기");
		btnReserve.setBounds(114, 424, 161, 28);
		btnReserve.setEnabled(false);
		pnlStatus.add(btnReserve);

		btnCheck = new JButton("조회하기");
		btnCheck.setBounds(114, 159, 161, 28);
		pnlStatus.add(btnCheck);

		JLabel label = new JLabel("원 / 시간");
		label.setHorizontalAlignment(SwingConstants.LEFT);
		label.setFont(Setting.M_GODIC_B_11);
		label.setBounds(191, 314, 45, 28);
		pnlStatus.add(label);

		setCbxForReservation();
		cbxYear.setSelectedIndex(0);
		addEvent();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Calendar cal = Calendar.getInstance();
		int currYear = cal.get(Calendar.YEAR);
		int currMonth = cal.get(Calendar.MONTH);
		int currDate = cal.get(Calendar.DATE);
		int currHour = cal.get(Calendar.HOUR_OF_DAY);

		int selectedRoomNum = 0;
		try {
			selectedRoomNum = Integer.parseInt(tfRoomNum.getText());
		} catch (NumberFormatException e1) {
			selectedRoomNum = 0;
		}
		int selectedYear = Integer.parseInt((String) cbxYear.getSelectedItem());
		int selectedMonth = 0;
		int selectedDate = 0;
		int selectedInHour = 0;
		int selectedOutHour = 0;

		if (cbxMonth.getSelectedItem() != null) {
			selectedMonth = Integer.parseInt("" + cbxMonth.getSelectedItem()) - 1;
		}
		if (cbxDate.getSelectedItem() != null) {
			selectedDate = Integer.parseInt("" + cbxDate.getSelectedItem());
		}
		if (cbxInHour.getSelectedItem() != null) {
			selectedInHour = Integer.parseInt("" + cbxInHour.getSelectedItem());
		}
		if (cbxOutHour.getSelectedItem() != null) {
			selectedOutHour = Integer.parseInt("" + cbxOutHour.getSelectedItem());
		}

		if (e.getSource() == cbxYear) { // 년도에 변화가 있을 때.
			allRoomClose();
			if (currYear == selectedYear) {
				cbxMonth.removeAllItems();
				for (int i = currMonth + 1; i <= 12; i++) {
					cbxMonth.addItem(i + "");
				}
			} else {
				cbxMonth.removeAllItems();
				for (int i = 1; i <= 12; i++) {
					cbxMonth.addItem(i + "");
				}
			}
			cbxMonth.setSelectedIndex(0);
			selectedMonth = Integer.parseInt("" + cbxMonth.getSelectedItem()) - 1;
		} else if (e.getSource() == cbxMonth) { // 월에 변화가 있을 때.
			allRoomClose();
			if (currYear == selectedYear && currMonth == selectedMonth) {
				cbxDate.removeAllItems();
				cal.set(selectedYear, currMonth, 1);
				int lastDate = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
				for (int i = currDate; i <= lastDate; i++) {
					cbxDate.addItem(i + "");
				}
			} else {
				cbxDate.removeAllItems();
				cal.set(selectedYear, selectedMonth, 1);
				int lastDate = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
				for (int i = 1; i <= lastDate; i++) {
					cbxDate.addItem(i + "");
				}
			}
			cbxDate.setSelectedIndex(0);
			selectedDate = Integer.parseInt("" + cbxDate.getSelectedItem());
		} else if (e.getSource() == cbxDate) { // 일에 변화가 있을 때
			allRoomClose();
			cbxInHour.removeAllItems();
			cbxOutHour.removeAllItems();
			if (currYear == selectedYear && currMonth == selectedMonth && currDate == selectedDate) {
				if (currHour >= 23) {
					cbxInHour.addItem("0");
					cbxOutHour.addItem("-1");
					btnCheck.setEnabled(false);
					btnReserve.setEnabled(false);
					return;
				} else {
					btnCheck.setEnabled(true);
					for (int i = currHour + 1; i < 23; i++) {
						cbxInHour.addItem(i + "");
						cbxOutHour.addItem((i + 1) + "");
					}
				}
			} else {
				btnCheck.setEnabled(true);
				for (int i = 9; i < 23; i++) {
					cbxInHour.addItem(i + "");
					cbxOutHour.addItem((i + 1) + "");
				}
			}
			cbxInHour.setSelectedIndex(0);
			selectedInHour = Integer.parseInt("" + cbxInHour.getSelectedItem());
		} else if (e.getSource() == cbxInHour) { // 시작 시간에 변화가 있을 때
			allRoomClose();
			cbxOutHour.removeAllItems();
			for (int i = selectedInHour + 1; i <= 23; i++) {
				cbxOutHour.addItem(i + "");
			}
			cbxOutHour.setSelectedIndex(0);
			selectedOutHour = Integer.parseInt("" + cbxOutHour.getSelectedItem());
		}

		if (e.getSource() == btnRoomArr[0]) { // 1번 룸
			btnReserve.setEnabled(true);
			tfRoomNum.setText("1");
			tfCapacity.setText("4");
			tfPrice.setText("1000");
			tfFacility.setText("빔 프로젝트, 칠판");
			btnColorChange(0);
		} else if (e.getSource() == btnRoomArr[1]) { // 2번 룸
			btnReserve.setEnabled(true);
			tfRoomNum.setText("2");
			tfCapacity.setText("8");
			tfPrice.setText("2000");
			tfFacility.setText("빔 프로젝트, 노트북, 칠판");
			btnColorChange(1);
		} else if (e.getSource() == btnRoomArr[2]) { // 3번 룸
			btnReserve.setEnabled(true);
			tfRoomNum.setText("3");
			tfCapacity.setText("8");
			tfPrice.setText("2000");
			tfFacility.setText("빔 프로젝트, 노트북, 칠판");
			btnColorChange(2);
		} else if (e.getSource() == btnRoomArr[3]) { // 4번 룸
			btnReserve.setEnabled(true);
			tfRoomNum.setText("4");
			tfCapacity.setText("8");
			tfPrice.setText("2000");
			tfFacility.setText("빔 프로젝트, 노트북, 칠판");
			btnColorChange(3);
		} else if (e.getSource() == btnRoomArr[4]) { // 5번 룸
			btnReserve.setEnabled(true);
			tfRoomNum.setText("5");
			tfCapacity.setText("8");
			tfPrice.setText("2000");
			tfFacility.setText("빔 프로젝트, 노트북, 칠판");
			btnColorChange(4);
		} else if (e.getSource() == btnRoomArr[5]) { // 6번 룸
			btnReserve.setEnabled(true);
			tfRoomNum.setText("6");
			tfCapacity.setText("8");
			tfPrice.setText("2000");
			tfFacility.setText("빔 프로젝트, 노트북, 칠판");
			btnColorChange(5);
		}

		if (e.getSource() == btnReserve) { // 예약하기 버튼 눌렀을 때.
			int price = Integer.parseInt(tfPrice.getText());
			RoomDTO room = new RoomDTO(selectedRoomNum, member.getId(), selectedYear, selectedMonth + 1, selectedDate,
					selectedInHour, selectedOutHour, price);
			room.setStatus(Status.RESERVATION);
			main.request(room);
			Object temp = main.response();

			int subseq = 0;

			if (temp instanceof RoomDTO) {
				room = (RoomDTO) temp;
				if (room.getStatus() == Status.RESERVATION) {
					subseq = room.getSubseq();
					JOptionPane.showMessageDialog(this, "예약이 완료되었습니다.");
				} else if (room.getStatus() == Status.FAILURE) {
					JOptionPane.showMessageDialog(this, "예약 실패!!!!");
					return;
				}
			}

			OrderDTO orderDTO = new OrderDTO("", "스터디" + room.getRoomNum() + "번 방", 1, price);
			orderDTO.setStatus(Status.INSERT_SALES);
			orderDTO.setSeq(subseq);
			main.request(orderDTO);
			temp = main.response();

			reset();
		} else if (e.getSource() == btnCheck) { // 조회하기 버튼 눌렀을 때.

			if (cbxMonth.getSelectedIndex() == -1) {
				JOptionPane.showMessageDialog(this, "날짜를 선택해주세요.");
				return;
			}

			RoomDTO room = new RoomDTO(selectedRoomNum, member.getId(), selectedYear, selectedMonth + 1, selectedDate,
					selectedInHour, selectedOutHour, 0);
			room.setStatus(Status.CHECK_MY_RESERVATION);
			main.request(room);
			Object temp = main.response();
			if (temp instanceof RoomDTO) {
				room = (RoomDTO) temp;
				if (room.getStatus() == Status.CHECK_MY_RESERVATION) {
					ArrayList<RoomDTO> roomList = room.getRoomArrayList();
					if (roomList.size() == 0) {
						for (int i = 0; i < btnRoomArr.length; i++) {
							btnRoomArr[i].setEnabled(true);
							btnRoomArr[i].setText("예약가능");
						}
					} else {
						for (int i = 0; i < btnRoomArr.length; i++) {
							btnRoomArr[i].setEnabled(true);
							btnRoomArr[i].setText("예약가능");
						}
						for (RoomDTO roomDTO : roomList) {
							if (selectedInHour < roomDTO.getOutHour() && selectedInHour >= roomDTO.getInHour()) {
								// 선택한 inHour가 room에서 가져온 값 사이에 있을 때
								btnRoomArr[roomDTO.getRoomNum() - 1].setEnabled(false);
								btnRoomArr[roomDTO.getRoomNum() - 1].setText("예약불가");
							}
							if (selectedOutHour <= roomDTO.getOutHour() && selectedOutHour > roomDTO.getInHour()) {
								// 선택한 outHour가 room에서 가져온 값 사이에 있을 때
								btnRoomArr[roomDTO.getRoomNum() - 1].setEnabled(false);
								btnRoomArr[roomDTO.getRoomNum() - 1].setText("예약불가");
							}
							if (selectedInHour <= roomDTO.getInHour() && selectedOutHour >= roomDTO.getOutHour()) {
								// 선택한 inHour ~ outHour 가 room의 in/out을 감싸고 있을 때.
								btnRoomArr[roomDTO.getRoomNum() - 1].setEnabled(false);
								btnRoomArr[roomDTO.getRoomNum() - 1].setText("예약불가");
							}
						}
					}
				}
			}
		}
	}

	public void addEvent() {
		cbxYear.addActionListener(this);
		cbxMonth.addActionListener(this);
		cbxDate.addActionListener(this);
		cbxInHour.addActionListener(this);
		cbxOutHour.addActionListener(this);

		for (JButton btnRoom : btnRoomArr) {
			btnRoom.addActionListener(this);
			btnRoom.setEnabled(false);
			btnRoom.setBackground(Color.WHITE);
			btnRoom.setFocusPainted(false);
		}

		btnReserve.addActionListener(this);
		btnCheck.addActionListener(this);
	}

	public void allRoomClose() {
		for (int i = 0; i < btnRoomArr.length; i++) {
			btnRoomArr[i].setEnabled(false);
			btnRoomArr[i].setText("예약불가");
			btnRoomArr[i].setBackground(Color.WHITE);
		}
	}

	public void btnColorChange(int idx) {
		for (int i = 0; i < btnRoomArr.length; i++) {
			if (i == idx) {
				btnRoomArr[i].setBackground(Color.BLACK);
				btnRoomArr[i].setForeground(Color.WHITE);
			} else {
				btnRoomArr[i].setBackground(Color.WHITE);
				btnRoomArr[i].setForeground(Color.BLACK);
			}
		}
	}

	public void reset() {
		tfRoomNum.setText("");
		tfPrice.setText("");
		tfCapacity.setText("");
		tfFacility.setText("");
		cbxMonth.removeAllItems();
		cbxDate.removeAllItems();
		cbxInHour.removeAllItems();
		cbxOutHour.removeAllItems();
		cbxYear.setSelectedIndex(0);
		btnReserve.setEnabled(false);
		for (JButton btn : btnRoomArr) {
			btn.setEnabled(false);
			btn.setText("예약불가");
			btn.setBackground(Color.WHITE);
			btn.setForeground(Color.BLACK);
		}
	}

	public void setCbxForReservation() {
		Calendar cal = Calendar.getInstance();
		int currYear = cal.get(Calendar.YEAR);

		// 년도 세팅
		String[] yearArr = { "" + currYear, "" + (currYear + 1) };
		for (int i = 0; i < yearArr.length; i++) {
			cbxYear.addItem(yearArr[i]);
		}
	}

}
