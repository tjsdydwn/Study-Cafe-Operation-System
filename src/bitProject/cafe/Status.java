package bitProject.cafe;

public enum Status {
	// 로그인, 로그아웃, 회원가입, 개인정보 수정
	LOGIN_STAFF, LOGIN_CLIENT, WELCOME, LOGOUT, JOIN, CHECK_MY_ID, CHANGE_MY_INFO, SEND_EMAIL, STAFF, LEAVE,
	// 예약처리에 관한 메시지
	RESERVATION, CHECK_MY_RESERVATION, GET_FROM_DB, DELETE,
	// 게시판 처리에 관한 메시지
	WRITE_BOARD, DELETE_BOARD,
	// 실패할 경우 메시지
	FAILURE,
	// 주문메시지
	INSERT, INSERT_SALES
}
