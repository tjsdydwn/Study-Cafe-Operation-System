package bitProject.cafe.view;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import bitProject.cafe.dao.Status;
import bitProject.cafe.dto.MemberDTO;
import bitProject.cafe.dto.OrderDTO;

public class StaffOrder extends JPanel implements ActionListener {
	private static final long serialVersionUID = 824222095702920744L;

	private JTable tableOrderList;
	private DefaultTableModel modelOrderList;

	private JButton btnConfirm;
	private JButton btnUpdate;

	private Vector<String> vtColName;
	private Vector<Vector<String>> orderList;

	private MemberDTO member;
	private StaffFrame main;

	public StaffOrder(MemberDTO member, StaffFrame main) {
		this.member = member;
		this.main = main;

		setLayout(null);
		setBounds(new Rectangle(0, 0, 1200, 500));

		JLabel lblOrderDisp = new JLabel("주문내역 확인");
		lblOrderDisp.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		lblOrderDisp.setBounds(28, 21, 178, 36);
		add(lblOrderDisp);

		btnConfirm = new JButton("주문확정");
		btnConfirm.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		btnConfirm.setBounds(962, 84, 158, 77);
		add(btnConfirm);

		btnUpdate = new JButton("주문내용 갱신");
		btnUpdate.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		btnUpdate.setBounds(962, 190, 158, 77);
		add(btnUpdate);
		setTabel();
		addEvent();
	}

	public void setTabel() {
		setTableCol();
		getOrderList();
		modelOrderList = new DefaultTableModel(orderList, vtColName) {
			private static final long serialVersionUID = 2862875727074595670L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tableOrderList = new JTable(modelOrderList);
		tableOrderList.getTableHeader().setReorderingAllowed(false); // 이동 불가
		tableOrderList.getColumnModel().getColumn(0).setPreferredWidth(10);
		tableOrderList.getColumnModel().getColumn(2).setPreferredWidth(180);
		tableOrderList.getColumnModel().getColumn(3).setPreferredWidth(20);
		tableOrderList.getColumnModel().getColumn(4).setPreferredWidth(30);
		tableOrderList.getColumnModel().getColumn(5).setPreferredWidth(30);
		tableOrderList.getTableHeader().setResizingAllowed(false); // 크기 조절 불가
		tableOrderList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 하나의 행만 선택
		setAlignmentCenter(tableOrderList);
		JScrollPane scroll = new JScrollPane(tableOrderList);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		scroll.setBounds(82, 84, 801, 377);
		add(scroll);
	}

	public void getOrderList() {
		OrderDTO orderDTO = new OrderDTO(Status.GET_FROM_DB);
		main.request(orderDTO);
		Object temp = main.response();
		if (temp instanceof OrderDTO) {
			orderDTO = (OrderDTO) temp;
			if (orderDTO.getStatus() == Status.GET_FROM_DB) {
				orderList = orderDTO.getOrderList();
				return;
			}
		} else {
			return;
		}
	}

	public void setTableCol() {
		String[] colNameArr = { "주문번호", "주문자", "주문 메뉴", "수량", "단가", "금액" };
		vtColName = new Vector<String>();
		for (int i = 0; i < colNameArr.length; i++) {
			vtColName.add(colNameArr[i]);
		}
	}

	public void setAlignmentCenter(JTable table) {
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		TableColumnModel tcm = table.getColumnModel();
		for (int i = 0; i < tcm.getColumnCount(); i++) {
			tcm.getColumn(i).setCellRenderer(dtcr);
		}
	}

	public void addEvent() {
		btnConfirm.addActionListener(this);
		btnUpdate.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnConfirm) { // 주문확정
			int rowIdx = tableOrderList.getSelectedRow();

			if (rowIdx == -1) {
				JOptionPane.showMessageDialog(this, "확정할 주문을 선택해주세요.");
				return;
			}

			int orderNum = Integer.parseInt(tableOrderList.getValueAt(rowIdx, 0) + "");

			// 매출 DB에 넣는다.
			String menuName = "" + tableOrderList.getValueAt(rowIdx, 2);
			int amount = Integer.parseInt("" + tableOrderList.getValueAt(rowIdx, 3));
			int menuPrice = Integer.parseInt("" + tableOrderList.getValueAt(rowIdx, 4));
			OrderDTO orderDTO = new OrderDTO("", menuName, amount, menuPrice);
			orderDTO.setStatus(Status.INSERT_SALES);
			main.request(orderDTO);
			Object temp = main.response();
			if (temp instanceof OrderDTO) {
				orderDTO = (OrderDTO) temp;
				if (orderDTO.getStatus() != Status.INSERT_SALES) {
					JOptionPane.showMessageDialog(this, "매출에 정상 기록 되지 않았습니다.");
				}
			}

			// table에서 삭제한다.
			modelOrderList.removeRow(rowIdx);

			// orderDB에서 삭제한다.
			orderDTO = new OrderDTO();
			orderDTO.setSeq(orderNum);
			orderDTO.setStatus(Status.DELETE);
			main.request(orderDTO);
			temp = main.response();
			if (temp instanceof OrderDTO) {
				orderDTO = (OrderDTO) temp;
				if (orderDTO.getStatus() != Status.DELETE) {
					JOptionPane.showMessageDialog(this, "이미 삭제된 건 입니다.");
				}
			}
		} else if (e.getSource() == btnUpdate) {
			// db 오더 데이터를 다 꺼내온다.
			getOrderList();
			for (int i = 0; i < modelOrderList.getRowCount(); i++) {
				modelOrderList.removeRow(i);
				i--;
			}
			for (int i = 0; i < orderList.size(); i++) {
				modelOrderList.addRow(orderList.get(i));
			}
		}
	}

}
