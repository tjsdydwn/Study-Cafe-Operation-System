package bitProject.cafe.view;

import java.awt.Font;
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
import bitProject.cafe.dto.SalesDTO;
import javax.swing.JTextField;

public class Sales extends JPanel implements ActionListener {

	private static final long serialVersionUID = -3675947320972014766L;
	private JTable tableSalesList;
	private DefaultTableModel modelSalesList;
	private JButton btnUpdate, btnCancel;

	private Vector<Vector<String>> salesList;
	private Vector<String> vtColName;
	private MemberDTO member;
	private StaffFrame main;
	private JTextField tfTotal;

	public Sales(MemberDTO member, StaffFrame main) {

		this.member = member;
		this.main = main;

		setLayout(null);

		JLabel lblOrderDisp = new JLabel("매출 확인");
		lblOrderDisp.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		lblOrderDisp.setBounds(28, 21, 178, 36);
		add(lblOrderDisp);

		btnUpdate = new JButton("갱신");
		btnUpdate.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		btnUpdate.setBounds(965, 81, 158, 40);
		add(btnUpdate);

		btnCancel = new JButton("취소");
		btnCancel.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		btnCancel.setBounds(965, 150, 158, 40);
		add(btnCancel);
		JLabel lblTotal = new JLabel("원");
		lblTotal.setBounds(887, 42, 37, 28);
		add(lblTotal);
		
		tfTotal = new JTextField();
		tfTotal.setEditable(false);
		tfTotal.setBounds(746, 40, 135, 30);
		add(tfTotal);
		tfTotal.setColumns(10);
		
		setTable();
		btnUpdate.addActionListener(this);
		btnCancel.addActionListener(this);
	} // 생성자

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnUpdate) { // 갱신
			for (int i = 0; i < modelSalesList.getRowCount(); i++) {
				modelSalesList.removeRow(i);
				i--;
			}
			getSalesList();
			for (int i = 0; i < salesList.size(); i++) {
				modelSalesList.addRow(salesList.get(i));
			}
		} else if (e.getSource() == btnCancel) { // 취소
			int rowIdx = tableSalesList.getSelectedRow();
			if (rowIdx == -1) {
				JOptionPane.showMessageDialog(this, "매출항목을 선택해주세요.");
				return;
			}
			int seq = Integer.parseInt("" + tableSalesList.getValueAt(rowIdx, 0));
			System.out.println("시퀀스 번호" + seq);

			// DB에서 삭제
			SalesDTO salesDTO = new SalesDTO(Status.DELETE);
			salesDTO.setSeq(seq);
			main.request(salesDTO);

			// 테이블에서 삭제
			modelSalesList.removeRow(rowIdx);
		}
		setTotal();
	}

	public void setTable() {
		setColName();
		getSalesList();
		modelSalesList = new DefaultTableModel(salesList, vtColName) {
			private static final long serialVersionUID = -5853850537091940716L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tableSalesList = new JTable(modelSalesList);
		tableSalesList.getTableHeader().setReorderingAllowed(false); // 이동 불가
		tableSalesList.getColumnModel().getColumn(0).setPreferredWidth(30);
		tableSalesList.getColumnModel().getColumn(1).setPreferredWidth(180);
		tableSalesList.getColumnModel().getColumn(2).setPreferredWidth(30);
		tableSalesList.getColumnModel().getColumn(3).setPreferredWidth(30);
		tableSalesList.getColumnModel().getColumn(4).setPreferredWidth(30);
		tableSalesList.getTableHeader().setResizingAllowed(false); // 크기 조절 불가
		tableSalesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 하나의 행만 선택
		setAlignmentCenter(tableSalesList);

		JScrollPane scroll = new JScrollPane(tableSalesList);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setBounds(83, 80, 836, 364);
		add(scroll);
		setTotal();
	}

	public void setColName() {
		String[] colName = { "매출번호", "주문메뉴", "수량", "단가", "금액", "매출일자" };
		vtColName = new Vector<String>();
		for (int i = 0; i < colName.length; i++) {
			vtColName.add(colName[i]);
		}
	}

	public void getSalesList() {
		SalesDTO salesDTO = new SalesDTO(Status.GET_FROM_DB);
		main.request(salesDTO);
		Object temp = main.response();
		if (temp instanceof SalesDTO) {
			salesDTO = (SalesDTO) temp;
			salesList = salesDTO.getSalesList();
		}
	}

	public void setAlignmentCenter(JTable table) { // JTable의 내용 가운데 정렬
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		TableColumnModel tcm = table.getColumnModel();
		for (int i = 0; i < tcm.getColumnCount(); i++) {
			tcm.getColumn(i).setCellRenderer(dtcr);
		}
	}

	public void setTotal() {
		int total = 0;
		for (int i = 0; i < modelSalesList.getRowCount(); i++) {
			total += Integer.parseInt("" + tableSalesList.getValueAt(i, 4));
		}
		tfTotal.setText("" + total);
	}
}
