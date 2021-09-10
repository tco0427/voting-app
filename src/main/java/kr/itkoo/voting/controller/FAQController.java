package kr.itkoo.voting.controller;


import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import kr.itkoo.voting.data.ResponseData;
import kr.itkoo.voting.data.ResponseMessage;
import kr.itkoo.voting.data.StatusCode;
import kr.itkoo.voting.domain.dto.response.FAQResponse;
import kr.itkoo.voting.domain.entity.FAQ;
import kr.itkoo.voting.service.FAQService;
import kr.itkoo.voting.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/faq")
public class FAQController {

	private final FAQService faqService;
	private final JwtUtil jwtUtil;

	@GetMapping("")
	public ResponseData<List<FAQResponse>> getFAQList(HttpServletRequest header) {
		ResponseData<List<FAQResponse>> responseData = null;

		try {
			String token = jwtUtil.getTokenByHeader(header);
			jwtUtil.isValidToken(token);

			List<FAQ> faqList = faqService.findAll();
			List<FAQResponse> faqResponseList = faqList.stream().map(FAQ::toFAQResponse)
				.collect(Collectors.toList());
			responseData = new ResponseData<List<FAQResponse>>(
				StatusCode.OK, ResponseMessage.SUCCESS, faqResponseList
			);
		} catch (Exception e) {
			log.error(e.getMessage());
			responseData = new ResponseData<>(StatusCode.INTERNAL_SERVER_ERROR,
				ResponseMessage.FAILED_TO_LOAD_FAQ, null);
		}
		return responseData;
	}
}
