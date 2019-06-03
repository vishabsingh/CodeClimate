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
	 * create new blog
	 */
	Mono<Blog> createBlog(Blog blog);

	/**
	 Updated Blog.
	 */
    Mono<Blog> updateBlog(Blog blog, String blogId);

    /**
     * Return All Blog.
     */
    Flux<Blog> findAll();

    /**
     * Find One Records.
     */
    Mono<Blog> findOne(String blogId);

    /**
      Delete the blog.
     */
    Mono<ResponseEntity<Void>> delete(String blogId);

    /*
     Find Blog By Author.
     */
    Flux<Blog> findByAuthor(String blogAuthor);

    /**
     *
     * @param blogTitle
     * {@return FindByTile in Blog.}
     */
    Flux<Blog> findByTitleOwn(String blogTitle);

}
