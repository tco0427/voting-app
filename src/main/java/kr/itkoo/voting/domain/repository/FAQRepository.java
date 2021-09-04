package kr.itkoo.voting.domain.repository;

import kr.itkoo.voting.domain.entity.FAQ;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FAQRepository extends JpaRepository<FAQ, Long> {
    public List<FAQ> findAllBy();
}
