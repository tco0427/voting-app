package kr.itkoo.voting.domain.repository;

import java.util.List;
import kr.itkoo.voting.domain.entity.FAQ;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FAQRepository extends JpaRepository<FAQ, Long> {

	List<FAQ> findAllByIsVisible(boolean isVisible);
}
