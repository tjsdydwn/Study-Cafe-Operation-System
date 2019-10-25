package bitProject.cafe.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import bitProject.cafe.dao.Status;
import bitProject.cafe.dto.MemberDTO;
import bitProject.cafe.dto.RoomDTO;

public class StaffStudy extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private MemberDTO member;
	private StaffFrame main;

	private JTable tableRoomList;
	private DefaultTableModel modelRoomList;
	private Vector<Vector<String>> roomList;
	private Vector<String> vtColName;

	private JButton btnUpdate;
	private JButton btnCancle;

	public StaffStudy(MemberDTO member, StaffFrame main) {
		this.member = member;
		this.main = main;

		setLayout(null);

		btnUpdate = new JButton("갱신");
		btnUpdate.setBounds(938, 33, 226, 56);
		add(btnUpdate);

		btnCancle = new JButton("예약취소");
		btnCancle.setBounds(938, 120, 226, 56);
		add(btnCancle);
		setTable();
		addEvent();
	}

	public void addEvent() {
		btnCancle.addActionListener(this);
		btnUpdate.addActionListener(this);
	}

	public void setTable() {
		setColName();
		getAllList();
		modelRoomList = new DefaultTableModel(roomList, vtColName) {
			private static final long serialVersionUID = -5093944832684678117L;

			@Override
			public boolean isCellEditable(int i, int c) {
				return false;
			}
		};

		tableRoomList = new JTable(modelRoomList);
		tableRoomList.setRowSelectionAllowed(true);
		tableRoomList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setAlignmentCenter(tableRoomList);

		JScrollPane scroll = new JScrollPane(tableRoomList);
		scroll.setBounds(51, 33, 867, 430);
		add(scroll);
	}

	public void setColName() {
		String[] colNameArr = { "예약번호", "예약자", "방 번호", "예약일", "시작시간", "종료시간" };
		vtColName = new Vector<String>();
		for (int i = 0; i < colNameArr.length; i++) {
			vtColName.add(colNameArr[i]);
		}
	}

	public void getAllList() {
		RoomDTO roomDTO = new RoomDTO();
		roomDTO.setStatus(Status.GET_FROM_DB);
		System.out.println(roomDTO.getStatus());
		main.request(roomDTO);

		Object temp = main.response();
		if (temp instanceof RoomDTO) {
			roomDTO = (RoomDTO) temp;
			if (roomDTO.getStatus() == Status.GET_FROM_DB) {
				roomList = roomDTO.getRoomList();
			}
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

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == btnCancle) { // 취소
			int rowIdx = tableRoomList.getSelectedRow();
			if (rowIdx == -1) {
				JOptionPane.showMessageDialog(this, "먼저 예약을 선택해주세요.");
				return;
			}

			// DB에서 삭제
			int seq = Integer.parseInt("" + tableRoomList.getValueAt(rowIdx, 0));
			System.out.println(seq);
			RoomDTO roomDTO = new RoomDTO();
			roomDTO.setSeq(seq);
			roomDTO.setStatus(Status.DELETE);
			main.request(roomDTO);
			main.response();

			modelRoomList.removeRow(rowIdx);

		} else if (e.getSource() == btnUpdate) { // 갱신
			for (int i = 0; i < modelRoomList.getRowCount(); i++) {
				modelRoomList.removeRow(i);
				i--;
			}
			getAllList();
			for (int i = 0; i < roomList.size(); i++) {
				modelRoomList.addRow(roomList.get(i));
			}
		}

	}
}
