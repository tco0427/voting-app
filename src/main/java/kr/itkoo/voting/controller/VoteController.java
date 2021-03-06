package kr.itkoo.voting.controller;


import static java.util.stream.Collectors.toList;

import io.jsonwebtoken.JwtException;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import kr.itkoo.voting.data.ResponseData;
import kr.itkoo.voting.data.ResponseMessage;
import kr.itkoo.voting.data.StatusCode;
import kr.itkoo.voting.domain.dto.request.CreateVoteRequest;
import kr.itkoo.voting.domain.dto.request.UpdateVoteRequest;
import kr.itkoo.voting.domain.dto.response.CreateVoteResponse;
import kr.itkoo.voting.domain.dto.response.DeleteVoteResponse;
import kr.itkoo.voting.domain.dto.response.UpdateVoteResponse;
import kr.itkoo.voting.domain.dto.response.VoteByUserResponse;
import kr.itkoo.voting.domain.dto.response.VoteItemResponse;
import kr.itkoo.voting.domain.dto.response.VoteResponse;
import kr.itkoo.voting.domain.dto.response.VoteWithItemResponse;
import kr.itkoo.voting.domain.entity.User;
import kr.itkoo.voting.domain.entity.Vote;
import kr.itkoo.voting.domain.entity.VoteItem;
import kr.itkoo.voting.exception.NotFoundUserException;
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
	 * ?????? ?????? ??????
	 */
	@ApiOperation(value = "?????? ?????? ??????", notes = "id????????? vote ????????? ???????????????.")
	@GetMapping("/{id}")
	public ResponseData<VoteResponse> getVoteById(@PathVariable("id") Long id) {
		ResponseData<VoteResponse> responseData = null;

		VoteResponse voteResponse = null;
		try {
			Vote vote = voteService.findById(id);
			voteResponse = new VoteResponse(vote.getId(), vote.getTitle());
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
	 * ?????? ??????
	 */
	@ApiOperation(value = "?????? ??????", notes = "????????? vote??? ?????? ?????????.")
	@PostMapping("/new")
	public ResponseData<CreateVoteResponse> saveVote(
		@RequestBody @Valid CreateVoteRequest request, HttpServletRequest httpServletRequest) {

		ResponseData<CreateVoteResponse> responseData = null;
		CreateVoteResponse createVoteResponse;

		try {
			String token = jwtUtil.getTokenByHeader(httpServletRequest);
			jwtUtil.isValidToken(token);
			Long userId = jwtUtil.getUserIdByToken(token);

      		User user = userService.findById(userId);

			Vote vote = new Vote(user, request.getTitle());

			Long id = voteService.join(vote);

			createVoteResponse = new CreateVoteResponse(id, vote.getTitle());
			responseData = new ResponseData<>(StatusCode.OK, ResponseMessage.SUCCESS,
				createVoteResponse);
			log.info(responseData.toString());
		} catch (JwtException je) {
			responseData = new ResponseData<>(StatusCode.BAD_REQUEST, ResponseMessage.INVALID_TOKEN,
				null);
		} catch (NotFoundUserException e) {
			responseData = new ResponseData<>(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER,
					null);
			log.error("Optional Error" + e.getMessage());
		} catch (Exception e) {
			responseData = new ResponseData<>(StatusCode.INTERNAL_SERVER_ERROR,
				ResponseMessage.FAILED_TO_SAVE_VOTE, null);
			log.error(e.getMessage());
		}

		return responseData;
	}

	/**
	 * ?????? ?????? ??????
	 */
	@ApiOperation(value = "?????? ?????? ??????", notes = "vote??? id??? ?????? ??? ???????????????.")
	@PutMapping("/{id}")
	public ResponseData<UpdateVoteResponse> updateVote(@PathVariable("id") Long id,
		@RequestBody @Valid UpdateVoteRequest request) {
		ResponseData<UpdateVoteResponse> responseData;
		UpdateVoteResponse updateVoteResponse = null;
		try {
			String title = request.getTitle();
			voteService.update(id, title);

			updateVoteResponse = new UpdateVoteResponse(id, title);
			responseData = new ResponseData<>(StatusCode.OK, ResponseMessage.SUCCESS, updateVoteResponse);

		} catch (NoSuchElementException e) {
			responseData = new ResponseData<>(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_VOTE, null);
			log.error(e.getMessage());
		}
		return responseData;
	}

	/**
	 * ?????? ??????
	 */
	@ApiOperation(value = "?????? ??????", notes = "vote??? id?????? ?????? ???????????????.")
	@DeleteMapping("/{id}")
	public ResponseData<DeleteVoteResponse> deleteVote(@PathVariable("id") Long id) {
		ResponseData<DeleteVoteResponse> responseData;
		try {
			voteService.deleteById(id);
			responseData = new ResponseData<>(StatusCode.OK, ResponseMessage.SUCCESS, new DeleteVoteResponse(id));

		} catch (Exception e) {
			responseData = new ResponseData<>(StatusCode.BAD_REQUEST,
				ResponseMessage.NOT_FOUND_VOTE, null);
			log.error(e.getMessage());
		}
		return responseData;
	}

	/**
	 * ?????? ?????? ?????? ?????? ??????
	 */
	@ApiOperation(value = "????????? ?????? ?????? ??????", notes = "?????? ?????? ?????? ?????? ??????")
	@GetMapping("/myVote")
	public ResponseData<VoteByUserResponse> getVoteListByMember(HttpServletRequest httpServletRequest) {
		ResponseData<VoteByUserResponse> responseData;

		try {
			String token = jwtUtil.getTokenByHeader(httpServletRequest);
			jwtUtil.isValidToken(token);
			Long userId = jwtUtil.getUserIdByToken(token);

			User user = userService.findById(userId);

			List<Vote> voteList = user.getVotes();

			List<VoteWithItemResponse> voteWithItemResponseList = new ArrayList<>();

			for (Vote vote : voteList) {
				List<VoteItem> voteItems = vote.getVoteItems();
				List<VoteItemResponse> voteItemResponseList = voteItems.stream()
					.map(vi -> new VoteItemResponse(vi))
					.collect(toList());

				VoteWithItemResponse voteWithItemResponse = new VoteWithItemResponse(vote.getId(),
					vote.getTitle(), voteItemResponseList);

				voteWithItemResponseList.add(voteWithItemResponse);
			}

			responseData = new ResponseData<>(StatusCode.OK, ResponseMessage.SUCCESS,
				new VoteByUserResponse(user.getId(), voteWithItemResponseList));

		} catch (NotFoundUserException e) {
			log.error(e.getMessage());
			responseData = new ResponseData<>(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER, null);
		}
		return responseData;
	}
}
