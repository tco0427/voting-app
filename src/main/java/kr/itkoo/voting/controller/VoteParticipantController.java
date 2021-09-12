package kr.itkoo.voting.controller;

import io.swagger.annotations.ApiOperation;
import kr.itkoo.voting.data.ResponseData;
import kr.itkoo.voting.data.ResponseMessage;
import kr.itkoo.voting.data.StatusCode;
import kr.itkoo.voting.domain.dto.response.*;
import kr.itkoo.voting.domain.entity.User;
import kr.itkoo.voting.domain.entity.Vote;
import kr.itkoo.voting.domain.entity.VoteParticipant;
import kr.itkoo.voting.exception.NotFoundUserException;
import kr.itkoo.voting.service.UserService;
import kr.itkoo.voting.service.VoteParticipantService;
import kr.itkoo.voting.util.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/voteParticipant")
@RequiredArgsConstructor
@Slf4j
public class VoteParticipantController {

    private final VoteParticipantService voteParticipantService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

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

        User user = userService.findById(userId);

        // 2. 투표 참여 정보 객체 저장
        VoteParticipant voteParticipant = new VoteParticipant(user, voteId, voteItemId);

        // 3. 투표 참여 정보 테이블에 저장
        try {
            Long voteParticipantId = voteParticipantService.save(voteParticipant);
            // 3-1. 응답 객체 생성 후 리턴
            responseData = new ResponseData<>(StatusCode.OK, ResponseMessage.SUCCESS,
                    new VoteParticipateResponse(voteParticipantId));
        } catch (Exception e) {
            // 3-2. DB 에러시 처리(try-catch 또는 exception 처리)
            responseData = new ResponseData<>(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.FAILED_TO_SAVE_VOTE_PARTICIPANT, null);
            log.error(e.toString());
        }
        return responseData;
    }

    @ApiOperation(value = "내가 참여한 투표 목록 조회", notes = "내가 참여한 투표 목록 조회 with Paging")
    @GetMapping("/{page}")
    public ResponseData<VoteParticipantListResponse> getVoteListByMember(@PathVariable("page") Integer page, HttpServletRequest httpServletRequest) {
        ResponseData<VoteParticipantListResponse> responseData = null;
        VoteParticipantListResponse voteParticipantListResponse = null;

        try {
            String token = jwtUtil.getTokenByHeader(httpServletRequest);

            jwtUtil.isValidToken(token);

            Long userId = jwtUtil.getUserIdByToken(token);

            PageRequest pageRequest = PageRequest.of(page,10,Sort.by(Sort.Direction.DESC, "createdDate"));

            Slice<VoteParticipant> slice = voteParticipantService.getVoteParticipantByUserId(userId, pageRequest);

            List<VoteParticipant> content = slice.getContent();

            List<VoteParticipantResponse> voteParticipantResponseList = new ArrayList<>();

            for (VoteParticipant voteParticipant : content) {
                Long id = voteParticipant.getId();
                User user = voteParticipant.getUser();
                Long voteId = voteParticipant.getVoteId();

                VoteParticipantResponse voteParticipantResponse = new VoteParticipantResponse(id, voteId);

                voteParticipantResponseList.add(voteParticipantResponse);
            }

            voteParticipantListResponse = new VoteParticipantListResponse(voteParticipantResponseList);
            responseData = new ResponseData<>(StatusCode.OK, ResponseMessage.SUCCESS, voteParticipantListResponse);
        } catch (NotFoundUserException e) {
            log.error(e.getMessage());
            responseData = new ResponseData<>(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER,
                    null);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return responseData;
    }

    @Data
    @AllArgsConstructor
    static class VoteParticipantListResponse{
        List<VoteParticipantResponse> voteParticipantResponses;
    }

    @Data
    @AllArgsConstructor
    static class VoteParticipantResponse{
        private Long id;
        private Long voteId;
    }
}
