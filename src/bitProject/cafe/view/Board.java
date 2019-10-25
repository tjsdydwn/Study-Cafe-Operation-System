package bitProject.cafe.view;

// 메인페이지 하단부에 등장하는 게시판
// Board : 메인 패널
// BoardNew : 글쓰기 버튼을 눌렀을 때 새로 뜨는 JFrame, BoardAction으로 줄 생성 데이터를 넘겨줌
// BoardAction : JTable 줄 생성과 삭제
// BoardDTO : JTable 기본 내용
//

import java.awt.Dimension;
import java.awt.FlowLayout;
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

import bitProject.cafe.Setting;
import bitProject.cafe.dao.Status;
import bitProject.cafe.dto.BoardDTO;
import bitProject.cafe.dto.MemberDTO;

public class Board extends JPanel implements ActionListener {
	private static final long serialVersionUID = -672107422565314302L;
	private JButton btnWrite, btnDelete;

	private JTable tableBoardList;
	private JScrollPane scroll;
	private MemberDTO member;
	private Vector<String> vtColName; // 컬럼 설정 벡터
	private Vector<Vector<String>> boardList;
	private DefaultTableModel modelBoardList;

	private CafeNet main;
	private JButton btnUpdate;

	public Board(MemberDTO member, CafeNet main) { // constructor : MainFrame에서 member를 던져주므로 생성자에서 받아야함
		this.member = member;
		this.main = main;

		// 글쓰기 버튼과 삭제 버튼
		btnWrite = new JButton("글쓰기");
		btnWrite.setFont(Setting.M_GODIC_B_13);
		btnWrite.setBounds(1021, 467, 69, 23);

		btnDelete = new JButton("삭제");
		btnDelete.setFont(Setting.M_GODIC_B_13);
		btnDelete.setBounds(1102, 467, 69, 23);

		// 패널 잡기
		JPanel pnlMain = new JPanel();
		JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		pnlMain.setBounds(-5, 0, 1200, 460);
		pnlBottom.setBounds(0, 460, 1200, 40);

		setTable();

		pnlMain.add(scroll);
		pnlBottom.add(btnWrite);

		btnUpdate = new JButton("갱신");
		btnUpdate.setFont(Setting.M_GODIC_B_13);
		pnlBottom.add(btnUpdate);
		pnlBottom.add(btnDelete);

		// MainFrame에서 패널을 불러올 수 있게 최종 레이아웃 잡기
		this.setBounds(0, 0, 1200, 500);
		this.add(pnlMain);
		this.add(pnlBottom);

		// Board 이벤트 생성
		btnWrite.addActionListener(this);
		btnDelete.addActionListener(this);
		btnUpdate.addActionListener(this);
	} // 생성자

	public void setTable() {
		setTableCol();
		getAllList();
		modelBoardList = new DefaultTableModel(boardList, vtColName) {
			private static final long serialVersionUID = -8187460638917297235L;

			@Override
			public boolean isCellEditable(int i, int c) {
				return false;
			}
		};
		tableBoardList = new JTable(modelBoardList);
		tableBoardList.setRowSelectionAllowed(true);
		tableBoardList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableBoardList.setShowVerticalLines(false);
		tableBoardList.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableBoardList.getTableHeader().setReorderingAllowed(false);
		tableBoardList.setShowVerticalLines(false);
		tableBoardList.setRowHeight(25);
		tableBoardList.getColumn("글번호").setPreferredWidth(50);
		tableBoardList.getColumn("내용").setPreferredWidth(800);
		tableBoardList.getColumn("작성자").setPreferredWidth(130);
		tableBoardList.getColumn("작성일").setPreferredWidth(200);
		tableBoardList.setFont(Setting.M_GODIC_B_11);
		setAlignmentCenter(tableBoardList);

		scroll = new JScrollPane(tableBoardList);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setPreferredSize(new Dimension(1200, 450));
	}

	public void setTableCol() {
		// Vector로 열 데이터 입력
		vtColName = new Vector<String>();
		vtColName.addElement("글번호");
		vtColName.addElement("작성자");
		vtColName.addElement("내용");
		vtColName.addElement("작성일");
	}

	public void setAlignmentCenter(JTable table) {
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		TableColumnModel tcm = table.getColumnModel();
		for (int i = 0; i < tcm.getColumnCount(); i++) {
			tcm.getColumn(i).setCellRenderer(dtcr);
		}
	}

	public void getAllList() {
		main.request(new BoardDTO(Status.GET_FROM_DB));
		Object temp = main.response();
		if (temp instanceof BoardDTO) {
			BoardDTO board = (BoardDTO) temp;
			if (board.getStatus() == Status.GET_FROM_DB) {
				boardList = board.getBoardList();
			} else {
				return;
			}
		} else {
			return;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) { // ActionListener 이벤트
		if (e.getSource() == btnWrite) { // 글쓰기 이벤트. Dialog에서 처리.
			new BoardDialog(main, this);
		} else if (e.getSource() == btnDelete) { // 글 삭제

			int rowIdx = tableBoardList.getSelectedRow();
			if (rowIdx == -1) {
				JOptionPane.showMessageDialog(this, "삭제할 게시글을 선택해주세요.");
				return;
			}

			int seq = Integer.parseInt(tableBoardList.getValueAt(rowIdx, 0) + "");
			String id = "" + tableBoardList.getValueAt(rowIdx, 1);

			if (!member.getId().equals(id)) {
				if (!member.isStaff()) {
					JOptionPane.showMessageDialog(this, "자신이 작성한 글만 삭제 가능합니다.");
					return;
				}
			}

			// DB에서 삭제
			BoardDTO boardDTO = new BoardDTO(Status.DELETE);
			boardDTO.setSeq(seq);
			main.request(boardDTO);
			main.response();

			// 테이블에서 삭제
			modelBoardList.removeRow(rowIdx);

		} else if (e.getSource() == btnUpdate) {
			for (int i = 0; i < modelBoardList.getRowCount(); i++) {
				modelBoardList.removeRow(i);
				i--;
			}
			getAllList();
			for (int i = 0; i < boardList.size(); i++) {
				modelBoardList.addRow(boardList.get(i));
			}
		}
	}

	public MemberDTO getMember() {
		return member;
	}

	public Vector<Vector<String>> getBoardList() {
		return boardList;
	}

	public void setBoardList(Vector<Vector<String>> boardList) {
		this.boardList = boardList;
	}

	public DefaultTableModel getModelBoardList() {
		return modelBoardList;
	}

}
