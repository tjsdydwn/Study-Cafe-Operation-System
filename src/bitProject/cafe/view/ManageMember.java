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
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import bitProject.cafe.dao.Status;
import bitProject.cafe.dto.LoginDTO;
import bitProject.cafe.dto.MemberDTO;

public class ManageMember extends JPanel implements ActionListener {
	private static final long serialVersionUID = -2927782707786259862L;

	private JTable tableMemberList;
	private DefaultTableModel modelMemberList;
	private Vector<String> vtColName;
	private Vector<Vector<String>> memberList;

	private JButton btnAuthorize, btnUpdate, btnRemove;
	private StaffFrame main;

	public ManageMember(StaffFrame main) {
		this.main = main;

		setLayout(null);

		tableMemberList = new JTable();
		add(tableMemberList);

		btnAuthorize = new JButton("계정권한변경");
		btnAuthorize.setBounds(956, 30, 185, 50);
		add(btnAuthorize);

		btnRemove = new JButton("삭제");
		btnRemove.setBounds(956, 105, 185, 50);
		add(btnRemove);

		btnUpdate = new JButton("갱신");
		btnUpdate.setBounds(956, 180, 185, 50);
		add(btnUpdate);
		setTable();
		addEvent();
	}

	public void addEvent() {
		btnAuthorize.addActionListener(this);
		btnRemove.addActionListener(this);
		btnUpdate.addActionListener(this);
	}

	public void setTable() {
		setColName();
		getMemberList();
		modelMemberList = new DefaultTableModel(memberList, vtColName) {
			private static final long serialVersionUID = 2862875727074595670L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tableMemberList = new JTable(modelMemberList);
		tableMemberList.setRowSelectionAllowed(true);
		tableMemberList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 하나의 행만 선택
		setAlignmentCenter(tableMemberList);
		JScrollPane scroll = new JScrollPane(tableMemberList);
		scroll.setBounds(52, 32, 858, 419);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		add(scroll);
	}

	public void setColName() {
		String[] colNameArr = { "아이디", "이름", "이메일", "전화번호", "생년월일", "권한" };
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

	public void getMemberList() {
		MemberDTO memberDTO = new MemberDTO("", Status.GET_FROM_DB);
		main.request(memberDTO);
		Object temp = main.response();
		if (temp instanceof MemberDTO) {
			memberDTO = (MemberDTO) temp;
			if (memberDTO.getStatus() == Status.GET_FROM_DB) {
				memberList = memberDTO.getMemberList();
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int rowIdx = tableMemberList.getSelectedRow();

		if (e.getSource() == btnAuthorize) { // 권한변경
			if (rowIdx == -1) {
				JOptionPane.showMessageDialog(this, "회원을 선택하세요.");
				return;
			}

			String id = "" + tableMemberList.getValueAt(rowIdx, 0);
			String authority = "" + tableMemberList.getValueAt(rowIdx, 5);

			MemberDTO memberDTO = new MemberDTO();
			memberDTO.setId(id);
			memberDTO.setStaff(authority.length() > 1 ? true : false);
			memberDTO.setStatus(Status.UPDATE);
			main.request(memberDTO);
			Object temp = main.response();
			if (temp instanceof MemberDTO) {
				memberDTO = (MemberDTO) temp;
				if (memberDTO.getStatus() == Status.UPDATE) {
					JOptionPane.showMessageDialog(this, "해당 이용자의 권한이 변경되었습니다.");
				}
			}

			if (authority.length() > 1) {
				tableMemberList.setValueAt("", rowIdx, 5);
			} else {
				tableMemberList.setValueAt("스태프", rowIdx, 5);
			}

		} else if (e.getSource() == btnRemove) { // 삭제하기.
			if (rowIdx == -1) {
				JOptionPane.showMessageDialog(this, "회원을 선택하세요.");
				return;
			}

			int answer = JOptionPane.showConfirmDialog(this, "정말 삭제하시겠습니까?", "확인", JOptionPane.OK_CANCEL_OPTION);
			if (answer != JOptionPane.OK_OPTION) {
				return;
			}

			// DB에서 삭제
			String id = "" + tableMemberList.getValueAt(rowIdx, 0);
			LoginDTO loginDTO = new LoginDTO(id, Status.LEAVE);
			main.request(loginDTO);
			Object temp = main.response();
			if (temp instanceof LoginDTO) {
				loginDTO = (LoginDTO) temp;
				if (loginDTO.getStatus() == Status.LEAVE) {
					JOptionPane.showMessageDialog(this, "해당 회원을 삭제하였습니다.");
				}
			}

			// 테이블에서 삭제
			modelMemberList.removeRow(rowIdx);

		} else if (e.getSource() == btnUpdate) { // 갱신
			for (int i = 0; i < modelMemberList.getRowCount(); i++) {
				modelMemberList.removeRow(i);
				i--;
			}
			getMemberList();
			for (int i = 0; i < memberList.size(); i++) {
				modelMemberList.addRow(memberList.get(i));
			}
		}
	}
}
