package tn.hich.app.repo;

import java.util.Collection;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import tn.hich.app.model.Operation;

public interface OperationRepository extends PagingAndSortingRepository<Operation, Long>{
	
	Collection<Operation> findByCheckInOrderByTaskCard(Collection<String> checks);
	
	@Cacheable(cacheNames="opCache",cacheManager="opCacheManager")
	Page<Operation> findAll(Pageable pageable);
	
	@Cacheable(cacheNames="opCache",cacheManager="opCacheManager")
	long count();

}
