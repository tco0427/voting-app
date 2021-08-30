package kr.itkoo.voting.controller;

import java.util.List;
import java.util.NoSuchElementException;
import kr.itkoo.voting.data.ResponseData;
import kr.itkoo.voting.data.ResponseMessage;
import kr.itkoo.voting.data.StatusCode;
import kr.itkoo.voting.domain.dto.response.CreateVoteItemResponse;
import kr.itkoo.voting.domain.dto.response.VoteItemResponse;
import kr.itkoo.voting.domain.dto.response.VoteWithItemResponse;
import kr.itkoo.voting.domain.entity.Vote;
import kr.itkoo.voting.domain.entity.VoteItem;
import kr.itkoo.voting.service.VoteItemService;
import kr.itkoo.voting.service.VoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static java.util.stream.Collectors.toList;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/vote/item")
public class VoteItemController {

    private final VoteService voteService;
    private final VoteItemService voteItemService;

    @PostMapping("/{voteId}")
    public ResponseData<CreateVoteItemResponse> saveVoteItem(@PathVariable("voteId") Long voteId,
        @RequestParam("name") String name) {
        ResponseData<CreateVoteItemResponse> responseData = null;

        try {
            // 1. 투표 아이디 유효성 체크
            // 1-2. 없을겨우 에러 처리
            Vote vote = voteService.findById(voteId);

            // 2. 투표 아이템 저장
            VoteItem voteItem = new VoteItem(vote, name);
            Long voteItemId = voteItemService.save(voteItem);
            CreateVoteItemResponse voteItemResponse = new CreateVoteItemResponse(voteItemId, name);

            responseData = new ResponseData<>(StatusCode.OK, ResponseMessage.SUCCESS, voteItemResponse);
        } catch(NoSuchElementException e){
            responseData = new ResponseData<>(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_VOTE, null);
        } catch(Exception e) {
            responseData = new ResponseData<>(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.NOT_FOUND_USER, null);
            return responseData;
        }
        return responseData;
    }

    @GetMapping("/{voteId}")
    public ResponseData<VoteWithItemResponse> getVoteItemList(@PathVariable("voteId") Long voteId){
        ResponseData<VoteWithItemResponse> responseData = null;

        try{
            Vote vote = voteService.findById(voteId);
            List<VoteItem> voteItems = voteItemService.findAllByVoteId(voteId);
            List<VoteItemResponse> result = voteItems.stream()
                    .map(v -> new VoteItemResponse(v))
                    .collect(toList());

            responseData = new ResponseData<>(StatusCode.OK, ResponseMessage.SUCCESS, new VoteWithItemResponse(voteId, vote.getTitle(), result));
        }catch(Exception e){
            log.error(e.getMessage());
        }
        return responseData;
    }
}
