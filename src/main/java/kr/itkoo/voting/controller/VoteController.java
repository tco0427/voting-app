package kr.itkoo.voting.controller;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.NoSuchElementException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import kr.itkoo.voting.data.ResponseData;
import kr.itkoo.voting.data.ResponseMessage;
import kr.itkoo.voting.data.StatusCode;
import kr.itkoo.voting.domain.dto.request.CreateVoteRequest;
import kr.itkoo.voting.domain.dto.request.UpdateVoteRequest;
import kr.itkoo.voting.domain.dto.response.*;
import kr.itkoo.voting.domain.entity.User;
import kr.itkoo.voting.domain.entity.Vote;
import kr.itkoo.voting.domain.entity.VoteParticipant;
import kr.itkoo.voting.service.UserService;
import kr.itkoo.voting.service.VoteParticipantService;
import kr.itkoo.voting.service.VoteService;
import kr.itkoo.voting.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vote")
@RequiredArgsConstructor
@Slf4j
public class VoteController {

    private final UserService userService;
    private final VoteService voteService;
    private final VoteParticipantService voteParticipantService;
    private final JwtUtil jwtUtil;

	/**
	 * 투표 정보 조회
	 */
	@ApiOperation(value = "투표 정보 조회", notes = "id값으로 vote 정보를 조회합니다.")
	@GetMapping("/{id}")
	public ResponseData<VoteResponse> getVoteById(
		@ApiParam(name = "투표 id", required = true, example = "1") @PathVariable("id") Long id) {
		log.info("getVoteById : " + id);
		ResponseData<VoteResponse> responseData = null;

		VoteResponse voteResponse = null;
		try {
			Vote vote = voteService.findById(id).get();
			voteResponse = new VoteResponse(vote.getTitle());
			responseData = new ResponseData<>(StatusCode.OK, ResponseMessage.SUCCESS, voteResponse);
			log.info(responseData.toString());
		} catch (NoSuchElementException e) {
			responseData = new ResponseData<>(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_VOTE,
				null);
			log.error("Optional Error" + e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
		}

        return responseData;
    }

	/**
	 * 투표 생성
	 */
	@ApiOperation(value = "투표 생성", notes = "새로운 vote를 생성 합니다.")
	@PostMapping("/new")
	public ResponseData<CreateVoteResponse> saveVote(
		@RequestBody @Valid CreateVoteRequest request) {

        ResponseData<CreateVoteResponse> responseData = null;
        CreateVoteResponse createVoteResponse;
        try {
            Vote vote = new Vote();
            User user = userService.findById(request.getUserId()).get();

            vote.setUser(user);
            vote.setTitle(request.getTitle());

            Long id = voteService.join(vote);

			createVoteResponse = new CreateVoteResponse(id, vote.getTitle());
			responseData = new ResponseData<>(StatusCode.OK, ResponseMessage.SUCCESS,
				createVoteResponse);
			log.info(responseData.toString());
		} catch (NoSuchElementException e) {
			responseData = new ResponseData<>(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER,
				null);
			log.error("Optional Error" + e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
		}

        return responseData;
    }

	/**
	 * 투표 정보 수정
	 */
	@ApiOperation(value = "투표 정보 수정", notes = "vote를 id로 조회 후 수정합니다.")
	@PutMapping("/{id}")
	public ResponseData<UpdateVoteResponse> updateVote(@PathVariable("id") Long id,
													   @RequestBody @Valid UpdateVoteRequest request) {
		ResponseData<UpdateVoteResponse> responseData = null;
		UpdateVoteResponse updateVoteResponse = null;
		try {
			voteService.update(id, request.getTitle());
			Vote vote = voteService.findById(id).get();

            updateVoteResponse = new UpdateVoteResponse(vote.getId(), vote.getTitle());
            responseData = new ResponseData<>(StatusCode.OK, ResponseMessage.SUCCESS,
                updateVoteResponse);

		} catch (NoSuchElementException e) {
			responseData = new ResponseData<>(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_VOTE,
				null);
			log.error("Optional Error" + e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return responseData;
	}

	/**
	 * 투표 삭제
	 */
	@ApiOperation(value = "투표 삭제", notes = "vote를 id값을 통해 삭제합니다.")
	@DeleteMapping("/{id}")
	public ResponseData<DeleteVoteResponse> deleteVote(@PathVariable("id") Long id) {
		ResponseData<DeleteVoteResponse> responseData = null;
		try {
			voteService.deleteById(id);
			responseData = new ResponseData<>(StatusCode.OK, ResponseMessage.SUCCESS,
				new DeleteVoteResponse(id));
		} catch (Exception e) {
			responseData = new ResponseData<>(StatusCode.BAD_REQUEST,
				ResponseMessage.NOT_FOUND_VOTE, null);
			log.error(e.getMessage());
		}
		return responseData;
	}

	@PostMapping("/{voteId}/item/{voteItemId}")
	public ResponseData<VoteParticipateResponse> participateVote(
		@PathVariable("voteId") Long voteId, @PathVariable("voteItemId") Long voteItemId,
		HttpServletRequest request) {
		ResponseData<VoteParticipateResponse> responseData;
		Long userId = null;
		String authenticationHeader = request.getHeader("Authorization");

        // 1. 헤더에서 토큰값이 있는지 체크 & userId 가져오기
        if (authenticationHeader != null && authenticationHeader.startsWith("Bearer")) {
            String token = authenticationHeader.replace("Bearer", "");
            userId = jwtUtil.getUserIdByToken(token);
        }

        // 1-2. 없을경우 에러 처리
        if (userId == null) {
            responseData = new ResponseData<>(StatusCode.UNAUTHORIZED,
                ResponseMessage.NOT_FOUND_USER, null);
            return responseData;
        }

        // 2. 투표 참여 정보 객체 저장
        VoteParticipant voteParticipant = new VoteParticipant(userId, voteId, voteItemId);

		// 3. 투표 참여 정보 테이블에 저장
		try {
			Long voteParticipantId = voteParticipantService.save(voteParticipant);
			// 3-1. 응답 객체 생성 후 리턴
			responseData = new ResponseData<>(StatusCode.OK, ResponseMessage.SUCCESS,
				new VoteParticipateResponse(voteParticipantId));
		} catch (Exception e) {
			// 3-2. DB 에러시 처리(try-catch 또는 exception 처리)
			responseData = new ResponseData<>(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.FAILED_TO_SAVE_VOTE_PARTICIPANT, null);
			log.error(e.toString());
		}
		return responseData;
	}
}
