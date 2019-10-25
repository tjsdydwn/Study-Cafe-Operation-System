package bitProject.cafe.view;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import bitProject.cafe.Setting;
import bitProject.cafe.dao.Status;
import bitProject.cafe.dto.MemberDTO;
import bitProject.cafe.dto.OrderDTO;

public class ClientOrder extends JPanel implements ActionListener {
	private static final long serialVersionUID = -5436347122697860687L;
	private JComboBox<String> cbxBevOrDes; // 음료/디저트 선택 콤보박스
	private JComboBox<ImageIcon> cbxMenuSelect; // 음료메뉴/디저트메뉴 선택 콤보박스
	private ImageIcon[] bevArr; // 음료메뉴 아이콘
	private ImageIcon[] desArr; // 디저트 메뉴 아이콘

	private JTextArea taPrice; // 금액
	private JTextField tfAmount; // 수량
	private JTextField tfTotPrice; // 총 결제금액

	private JButton btnAdd; // 주문 추가 버튼
	private JButton btnConfirm; // 주문 확정 버튼
	private JButton btnCancel; // 주문 취소 버튼

	private JTable tableOrderList; // Jtable
	private DefaultTableModel modelOrderList; // tableModel
	private Vector<String> vtColName;
//	private Vector<OrderDTO> list;

	private MemberDTO member;
	private ClientFrame main;
	private int totPrice;
	private int amount;
	private String menuName;

	public ClientOrder(MemberDTO member, ClientFrame main) {
		this.member = member;
		this.main = main;

		setLayout(null);
		setBounds(new Rectangle(0, 0, 1200, 500));

		JLabel lblBevOrDes = new JLabel("음료/디저트");
		JLabel lblMenuSelect = new JLabel("메뉴 선택");
		JLabel lblPrice = new JLabel("금액");
		JLabel lblAmount = new JLabel("수량");
		JLabel lblStatus = new JLabel("주문현황");
		JLabel lblTotPrice = new JLabel("총 결제 금액");

		String[] bevOrDesArr = { "음료", "디저트" };
		cbxBevOrDes = new JComboBox<String>(bevOrDesArr);

		bevArr = new ImageIcon[] { new ImageIcon("img/coffee_1.png"), new ImageIcon("img/coffee_2.png"),
				new ImageIcon("img/coffee_3.png"), new ImageIcon("img/coffee_4.png"), new ImageIcon("img/coffee_5.png"),
				new ImageIcon("img/coffee_6.png"), };

		desArr = new ImageIcon[] { new ImageIcon("img/cake_1.png"), new ImageIcon("img/cake_2.png"),
				new ImageIcon("img/cake_3.png"), new ImageIcon("img/cake_4.png"), };

		cbxMenuSelect = new JComboBox<ImageIcon>();

		taPrice = new JTextArea(0, 0);
		taPrice.setEditable(false);

		tfAmount = new JTextField("0");
		tfAmount.setColumns(10);

		tfTotPrice = new JTextField();
		tfTotPrice.setColumns(10);
		tfTotPrice.setEditable(false);

		btnConfirm = new JButton("주문 확정");
		btnAdd = new JButton("주문 추가");
		btnCancel = new JButton("주문 취소");

		setTable();

		// 위치 / 글꼴 설정 후 붙이기
		lblBevOrDes.setBounds(12, 10, 120, 20);
		lblMenuSelect.setBounds(213, 10, 100, 20);
		lblPrice.setBounds(378, 10, 80, 20);
		lblAmount.setBounds(519, 10, 80, 20);
		lblStatus.setBounds(940, 10, 100, 20);
		lblTotPrice.setBounds(616, 473, 80, 20);
		cbxBevOrDes.setBounds(12, 47, 100, 30);
		cbxMenuSelect.setBounds(200, 47, 100, 30);
		taPrice.setBounds(378, 51, 80, 30);
		tfAmount.setBounds(505, 51, 80, 30);
		tfTotPrice.setBounds(708, 468, 100, 30);
		btnAdd.setBounds(820, 468, 100, 30);
		btnCancel.setBounds(940, 468, 100, 30);
		btnConfirm.setBounds(1059, 468, 100, 30);

		lblBevOrDes.setFont(Setting.M_GODIC_B_13);
		lblMenuSelect.setFont(Setting.M_GODIC_B_13);
		lblPrice.setFont(Setting.M_GODIC_B_13);
		lblAmount.setFont(Setting.M_GODIC_B_13);
		lblStatus.setFont(Setting.M_GODIC_B_13);
		lblTotPrice.setFont(Setting.M_GODIC_B_13);
		cbxBevOrDes.setFont(Setting.M_GODIC_B_13);
		cbxMenuSelect.setFont(Setting.M_GODIC_B_13);
		taPrice.setFont(Setting.M_GODIC_B_13);
		tfTotPrice.setFont(Setting.M_GODIC_B_13);
		btnAdd.setFont(Setting.M_GODIC_B_13);
		btnConfirm.setFont(Setting.M_GODIC_B_13);
		btnCancel.setFont(Setting.M_GODIC_B_13);

		add(lblBevOrDes);
		add(lblMenuSelect);
		add(lblPrice);
		add(lblAmount);
		add(lblStatus);
		add(lblTotPrice);
		add(cbxBevOrDes);
		add(cbxMenuSelect);
		add(taPrice);
		add(tfAmount);
		add(tfTotPrice);
		add(btnAdd);
		add(btnCancel);
		add(btnConfirm);

		// ActionEvent
		cbxBevOrDes.addActionListener(this);
		btnAdd.addActionListener(this);
		btnCancel.addActionListener(this);
		btnConfirm.addActionListener(this);

	}// 생성자

	public void setTable() {
		setColName();

		modelOrderList = new DefaultTableModel(vtColName, 0) {
			private static final long serialVersionUID = -8826602990608097920L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tableOrderList = new JTable(modelOrderList);
		setAlignmentCenter(tableOrderList);

		JScrollPane scroll = new JScrollPane(tableOrderList);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setBounds(812, 53, 300, 400);
		add(scroll);

	}

	public void setColName() {
		String colNameArr[] = new String[] { "선택 메뉴", "수량", "금액" };
		vtColName = new Vector<String>();
		for (int i = 0; i < colNameArr.length; i++) {
			vtColName.add(colNameArr[i]);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		// 음료/디저트 선택 콤보박스에서, 선택한 옵션에 맞추어 메뉴 콤보박스(음료메뉴/디저트메뉴) 를 보여준다.
		if (e.getSource() == cbxBevOrDes) {
			selectBevOrDes();
			taPrice.setText("");
			cbxMenuSelect.addActionListener(this);

			// 음료/디저트 메뉴 콤보박스에서, 선택한 메뉴에 맞추어 메뉴의 가격을 보여준다.
		} else if (e.getSource() == cbxMenuSelect) {
			int idx = cbxMenuSelect.getSelectedIndex();
			int menuPrice = 0;
			if (cbxBevOrDes.getSelectedIndex() == 0)
				menuPrice = setBevPrice(idx);
			else if (cbxBevOrDes.getSelectedIndex() == 1)
				menuPrice = setDesPrice(idx);
			taPrice.setText(menuPrice + "");
			taPrice.setEditable(false);

			// 주문 추가 버튼을 누르면, 선택한 메뉴, 수량, 금액을 JTable에 보여준다.
		} else if (e.getSource() == btnAdd) { // 주문추가

			// 수량
			try {
				amount = Integer.parseInt(tfAmount.getText());
				if (amount <= 0) {
					JOptionPane.showMessageDialog(this, "잘못된 수량을 입력하셨습니다.");
					return;
				}
			} catch (NumberFormatException e1) {
				JOptionPane.showMessageDialog(this, "수량을 입력해주세요.");
				return;
			}

			if (taPrice.getText().length() < 1) {
				JOptionPane.showMessageDialog(this, "가격이 표시되지 않았습니다.");
				return;
			}

			// 메뉴 이름
			if (cbxBevOrDes.getSelectedIndex() == 0)
				menuName = setBevName();
			else if (cbxBevOrDes.getSelectedIndex() == 1)
				menuName = setDesName();

			OrderDTO orderDTO = new OrderDTO(member.getId(), menuName, amount, Integer.parseInt(taPrice.getText()));
			orderDTO.setStatus(Status.INSERT_TEMP);
			main.request(orderDTO);

			Vector<String> order = new Vector<String>();
			order.add(menuName);
			order.add("" + amount);
			order.add(taPrice.getText());

			// JTable에는 열 별로 vector로 추가한다.
			modelOrderList.addRow(order);
			int tot = 0;
			for (int i = 0; i < tableOrderList.getRowCount(); i++) {
				amount = Integer.parseInt("" + tableOrderList.getValueAt(i, 1));
				int price = Integer.parseInt("" + tableOrderList.getValueAt(i, 2));
				tot += amount * price;
			}
			totPrice = tot;
			tfTotPrice.setText("" + totPrice);
			tfAmount.setText("0");
		} else if (e.getSource() == btnCancel) { // 삭제
			if (modelOrderList.getRowCount() < 1) {
				JOptionPane.showMessageDialog(this, "현재 주문한 건이 없습니다.");
				return;
			}

			if (tableOrderList.getSelectedRow() == -1) {
				JOptionPane.showMessageDialog(this, "취소할 주문을 선택해주세요.");
				return;
			}

			int rawIdx = tableOrderList.getSelectedRow();
			String menu = "" + tableOrderList.getValueAt(rawIdx, 0);
			int amount = Integer.parseInt("" + tableOrderList.getValueAt(rawIdx, 1));
			int menuPrice = Integer.parseInt("" + tableOrderList.getValueAt(rawIdx, 2));

			OrderDTO orderDTO = new OrderDTO(member.getId(), menu, amount, menuPrice);
			orderDTO.setStatus(Status.DELETE_SUB);
			main.request(orderDTO);

			totPrice -= amount * menuPrice;
			tfTotPrice.setText(totPrice + "");

			modelOrderList.removeRow(rawIdx);
		} else if (e.getSource() == btnConfirm) { // 주문확정
			if (modelOrderList.getRowCount() < 1) {
				JOptionPane.showMessageDialog(this, "현재 주문한 건이 없습니다.");
				return;
			}

			OrderDTO orderDTO = new OrderDTO(member.getId(), menuName, amount, Integer.parseInt(taPrice.getText()));
			orderDTO.setStatus(Status.INSERT_TEMP);
			main.request(orderDTO);

			orderDTO = new OrderDTO();
			orderDTO.setStatus(Status.INSERT);
			main.request(orderDTO);

			for (int i = 0; i < modelOrderList.getRowCount(); i++) {
				modelOrderList.removeRow(i);
				i--;
			}

			cbxBevOrDes.setSelectedIndex(0);
			cbxMenuSelect.setSelectedIndex(0);
			taPrice.setText("0");
			tfTotPrice.setText("0");

			totPrice = 0;
			tfTotPrice.setText(totPrice + "");
		}
	}

	public void clear() {
		for (int i = 0; i < modelOrderList.getRowCount(); i++) {
			modelOrderList.removeRow(i);
			i--;
		}
		cbxBevOrDes.setSelectedIndex(0);
		cbxMenuSelect.setSelectedIndex(0);
		taPrice.setText("");
		tfTotPrice.setText("");
	}

	public int setDesPrice(int idx) {
		int menuPrice = 0;
		int[] priceArr = { 1500, 2000, 2000, 2500 };
		for (int i = 0; i < priceArr.length; i++) {
			if (idx == i)
				menuPrice = priceArr[i];
		}
		return menuPrice;
	}

	public int setBevPrice(int idx) {
		int menuPrice = 0;
		int[] priceArr = { 1000, 2000, 3000, 3000, 2000, 2500 };
		for (int i = 0; i < priceArr.length; i++) {
			if (idx == i)
				menuPrice = priceArr[i];
		}
		return menuPrice;
	}

	public String setDesName() {
		String menuName = null;
		if (cbxMenuSelect.getSelectedIndex() == 0) {
			menuName = "초코케익";
		} else if (cbxMenuSelect.getSelectedIndex() == 1) {
			menuName = "치즈케익";
		} else if (cbxMenuSelect.getSelectedIndex() == 2) {
			menuName = "딸기케익";
		} else if (cbxMenuSelect.getSelectedIndex() == 3) {
			menuName = "블루베리케익";
		}

		return menuName;
	}

	public String setBevName() {
		// 음료 메뉴 중 하나를 선택
		String menuName = null;
		if (cbxMenuSelect.getSelectedIndex() == 0) {
			menuName = "아메리카노";
		} else if (cbxMenuSelect.getSelectedIndex() == 1) {
			menuName = "카페라떼";
		} else if (cbxMenuSelect.getSelectedIndex() == 2) {
			menuName = "카푸치노";
		} else if (cbxMenuSelect.getSelectedIndex() == 3) {
			menuName = "녹차라떼";
		} else if (cbxMenuSelect.getSelectedIndex() == 4) {
			menuName = "핫초코";
		} else if (cbxMenuSelect.getSelectedIndex() == 5) {
			menuName = "레모네이드";
		}

		return menuName;
	}

	public void selectBevOrDes() {
		cbxMenuSelect.setVisible(false); // 생성자에서 만들었던 빈칸의 콤보박스를 setVisible(false) 하고
		int idx = cbxBevOrDes.getSelectedIndex(); // 음료/디저트 중 하나를 선택
		ImageIcon[] menuArr = setImageIcon(idx); // 선택한 옵션에 맞추어 ImageIcon 를 대입.
		setCbxMenu(menuArr);

		cbxMenuSelect.setBounds(200, 47, 100, 100);
		add(cbxMenuSelect);
		cbxMenuSelect.setVisible(true); // ImageIcon을 받은 콤보박스로 setVisible(true)
	}

	private void setCbxMenu(ImageIcon[] menuArr) {
		cbxMenuSelect = new JComboBox<ImageIcon>(menuArr);
	}

	public ImageIcon[] setImageIcon(int idx) {
		ImageIcon[] menuArr = bevArr;
		if (idx == 0) {
			menuArr = bevArr;
		} else if (idx == 1) {
			menuArr = desArr;
		}

		return menuArr;
	}

	public void setAlignmentCenter(JTable table) { // JTable의 내용 가운데 정렬
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		TableColumnModel tcm = table.getColumnModel();
		for (int i = 0; i < tcm.getColumnCount(); i++) {
			tcm.getColumn(i).setCellRenderer(dtcr);
		}
	}
}
