package com.vn.react.service;

import com.vn.react.modal.Blog;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service of Blog.
 * @author vishab.singh
 *
 */
public interface BlogService {
	
	/**
	 * 
	 * @param blog
	 * {@return Create Blog.}
	 */
	Mono<Blog> createBlog(Blog blog);

	/**
	 * 
	 * @param blog
	 * @param blogId
	 * @return List<Mono> Updated Blog.
	 */
    Mono<Blog> updateBlog(Blog blog, String blogId);

    /**
     * Return All Blog
     * @return Flux<Blog>Find All Blog.
     */
    Flux<Blog> findAll();

    /**
     * 
     * @param blogId
     * @return Mono<Blog>Find One Records.
     */
    Mono<Blog> findOne(String blogId);

    /**
     * 
     * @param blogId
     * @return Mono<ResponseEntity> Delete .
     */
    Mono<ResponseEntity<Void>> delete(String blogId);

    /**
     * 
     * @param blogAuthor
     * @return Flux<Blog> FindBy Author.
     */
    Flux<Blog> findByAuthor(String blogAuthor);

    /**
     *
     * @param blogTitle
     * @return Flux<Blog> FindByTile in Blog.
     */
    Flux<Blog> findByTitleOwn(String blogTitle);

}
