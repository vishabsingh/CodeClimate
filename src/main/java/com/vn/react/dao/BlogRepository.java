package com.vn.react.dao;


import com.vn.react.modal.Blog;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface BlogRepository extends ReactiveMongoRepository<Blog, String> {

	@Query("{ 'author' : ?0 }")
	Flux<Blog> findByAuthor(String blogAuthor);
	
	Flux<Blog> findByTitle(String blogTitle);
}
