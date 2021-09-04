package kr.itkoo.voting.data;

public class ResponseMessage {

	public static final String SUCCESS = "성공";
	public static final String NOT_FOUND_USER = "회원 정보 조회 실패";
	public static final String NOT_FOUND_VOTE = "투표 정보 조회 실패";
	public static final String FAILED_TO_SAVE_VOTE_PARTICIPANT = "투표 참여자 저장 실패";
	public static final String FAILED_TO_SAVE_VOTE = "투표 생성 실패";
	public static final String EXPIRED_TOKEN = "유효기간 만료된 토큰";
	public static final String INVALID_TOKEN = "유효하지 않은 토큰 정보";
	public static final String INVALID_HEADER = "잘못된 헤더 정보";
	public static final String FAILED_TO_LOAD_FAQ = "FAQ 조회 실패";
}
