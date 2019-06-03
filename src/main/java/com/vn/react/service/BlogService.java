package com.vn.react.service;

import org.springframework.http.ResponseEntity;

import com.vn.react.modal.Blog;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service of Blog 
 * @author vishab.singh
 *
 */
public interface BlogService {
	
	/**
	 * 
	 * @param blog
	 * @return
	 */
	Mono<Blog> createBlog(Blog blog);

	/**
	 * 
	 * @param blog
	 * @param id
	 * @return
	 */
    Mono<Blog> updateBlog(Blog blog, String blogId);

    /**
     * Return All Blog
     * @return
     */
    Flux<Blog> findAll();

    /**
     * 
     * @param id
     * @return
     */
    Mono<Blog> findOne(String blogId);

    /**
     * 
     * @param id
     * @return
     */
    Mono<ResponseEntity<Void>> delete(String blogId);

    /**
     * 
     * @param author
     * @return
     */
    Flux<Blog> findByAuthor(String blogAuthor);

    /**
     * 
     * @param title
     * @return
     */
    Flux<Blog> findByTitleOwn(String blogTitle);

}
