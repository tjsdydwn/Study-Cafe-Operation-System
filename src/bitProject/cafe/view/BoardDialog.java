package bitProject.cafe.view;
// 이미 존재하는 JFrame에서 또 JFrame을 사용하는 것보다는 JDialog를 사용하는 것이 더 낫다고 판단했습니다

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import bitProject.cafe.Setting;
import bitProject.cafe.dao.Status;
import bitProject.cafe.dto.BoardDTO;

public class BoardDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 19310253510009307L;
	private JPanel pnlMain, btnPane;
	private JLabel lblId, lblId2, lblText;
	private JTextField tfText;
	private JButton btnWrite, btnCancle;

	private CafeNet main;
	private Board board;

	public BoardDialog(CafeNet main, Board board) {
		this.main = main;
		this.board = board;

		// 라벨
		lblId = new JLabel("작성자");
		lblId.setFont(Setting.M_GODIC_B_13);
		lblId.setBounds(43, 26, 57, 15);

		lblText = new JLabel("내용");
		lblText.setFont(Setting.M_GODIC_B_13);
		lblText.setBounds(53, 63, 57, 15);

		lblId2 = new JLabel(board.getMember().getId()); // MemberDTO의 ID를 받아온다
		lblId2.setFont(Setting.M_GODIC_B_13);
		lblId2.setBounds(117, 26, 57, 15);

		// 게시글 내용 입력 받기
		tfText = new JTextField();
		tfText.setFont(Setting.M_GODIC_B_11);
		tfText.setBounds(117, 57, 282, 27);
		tfText.setColumns(10);

		// 패널 처리
		pnlMain = new JPanel();
		pnlMain.setLayout(null);
		pnlMain.setBounds(0, 0, 434, 101);

		pnlMain.add(tfText);
		pnlMain.add(lblId);
		pnlMain.add(lblText);
		pnlMain.add(lblId2);

		// 작성/취소 버튼
		btnWrite = new JButton("작성");
		btnWrite.setFont(Setting.M_GODIC_B_13);
		btnWrite.setActionCommand("OK");
		btnCancle = new JButton("취소");
		btnCancle.setFont(Setting.M_GODIC_B_13);
		btnCancle.setActionCommand("Cancel");

		btnPane = new JPanel();
		btnPane.add(btnWrite);
		btnPane.add(btnCancle);
		btnPane.setBounds(0, 99, 434, 38);
		btnPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		getContentPane().setLayout(null);
		getContentPane().add(pnlMain);
		getContentPane().add(btnPane);

		setBounds(100, 100, 445, 176);
		setResizable(false);
		setVisible(true);

		getRootPane().setDefaultButton(btnWrite); // Enter를 치면 btnConfirm을 입력한 것으로 처리(엔터치면 작성 됩니다)
		btnWrite.addActionListener(this);
		btnCancle.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String writeTime = sdf.format(cal.getTime());

		if (e.getSource() == btnWrite) { // 쓰기 버튼 클릭했을 때 처리.
			String text = tfText.getText();
			if (text.length() < 1) {
				JOptionPane.showMessageDialog(this, "글을 입력해주세요.");
			} else {
				BoardDTO boardDTO = new BoardDTO(board.getMember().getId(), text, writeTime);
				boardDTO.setStatus(Status.WRITE_BOARD);
				main.request(boardDTO);

				Object temp = main.response();
				if (temp instanceof BoardDTO) {
					boardDTO = (BoardDTO) temp;
					if (boardDTO.getStatus() == Status.WRITE_BOARD) {
						for (int i = 0; i < board.getModelBoardList().getRowCount(); i++) { // 전부삭제
							board.getModelBoardList().removeRow(i);
							i--;
						}
						board.setBoardList(boardDTO.getBoardList());
						for (int i = 0; i < board.getBoardList().size(); i++) { // Board쪽에 DB에서 가져온 모든 것을 업데이트.
							board.getModelBoardList().addRow(board.getBoardList().get(i));
						}
					}
					this.dispose();
					return;
				} else {
					return;
				}
			}
		} else if (e.getSource() == btnCancle) {
			this.dispose();
		}
	}
}
