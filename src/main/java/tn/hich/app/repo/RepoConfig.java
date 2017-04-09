package tn.hich.app.repo;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories
@EnableCaching
public class RepoConfig {
	
	@Bean(name="opCacheManager")
	public CacheManager OperationsCache(){
		return new ConcurrentMapCacheManager("opCache");
	}

}
