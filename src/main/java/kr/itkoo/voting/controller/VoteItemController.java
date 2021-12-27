package kr.itkoo.voting.controller;

import java.util.List;
import java.util.NoSuchElementException;
import kr.itkoo.voting.data.ResponseData;
import kr.itkoo.voting.data.ResponseMessage;
import kr.itkoo.voting.data.StatusCode;
import kr.itkoo.voting.domain.dto.request.CreateVoteItemRequest;
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

    @PostMapping("/new")
    public ResponseData<CreateVoteItemResponse> saveVoteItem(@RequestBody CreateVoteItemRequest request) {
        ResponseData<CreateVoteItemResponse> responseData;

        try {
            Long voteId = request.getVoteId();
            String name = request.getName();

            Vote vote = voteService.findById(voteId);

            VoteItem voteItem = new VoteItem(vote, name);
            Long voteItemId = voteItemService.save(voteItem);
            CreateVoteItemResponse voteItemResponse = new CreateVoteItemResponse(voteItemId, name);

            responseData = new ResponseData<>(StatusCode.OK, ResponseMessage.SUCCESS, voteItemResponse);
        } catch(NoSuchElementException e) {
            log.error(e.getMessage());
            responseData = new ResponseData<>(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_VOTE, null);
        } catch(IllegalStateException e) {
            log.error(e.getMessage());
            responseData = new ResponseData<>(StatusCode.BAD_REQUEST, ResponseMessage.FAILED_TO_SAVE_VOTEITEM, null);
        }
        return responseData;
    }

    @GetMapping("/{voteId}")
    public ResponseData<VoteWithItemResponse> getVoteItemList(@PathVariable("voteId") Long voteId){
        ResponseData<VoteWithItemResponse> responseData;

        try{
            Vote vote = voteService.findById(voteId);
            List<VoteItem> voteItems = voteItemService.findAllByVoteId(voteId);
            List<VoteItemResponse> result = voteItems.stream()
                    .map(v -> new VoteItemResponse(v))
                    .collect(toList());

            responseData = new ResponseData<>(StatusCode.OK, ResponseMessage.SUCCESS, new VoteWithItemResponse(voteId, vote.getTitle(), result));
        } catch(NoSuchElementException e){
            log.error(e.getMessage());
            responseData = new ResponseData<>(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_VOTE, null);
        }
        return responseData;
    }
}
