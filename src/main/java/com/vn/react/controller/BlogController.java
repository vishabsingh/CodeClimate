package com.vn.react.controller;

import com.vn.react.filters.BlogFliter;
import com.vn.react.modal.Blog;
import com.vn.react.service.BlogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/blog")
public class BlogController {

	private static final Logger LOGGER = LoggerFactory.getLogger(BlogController.class);

	@Autowired
	private BlogService blogService;

	@GetMapping("/find")
	public Flux<Blog> findAll(){
		return blogService.findAll();
	}

	@GetMapping("/{id}")
	public Mono<Blog> findOne(@PathVariable final String id){
		LOGGER.info(" id {} ",id);
		return blogService.findOne(id);
	}

	@PostMapping
	public Mono<ResponseEntity<Blog>> save(@RequestBody final BlogFliter blog) {
		return blogService.createBlog(blog.blogFliterToBlog(blog))
				.map(ResponseEntity::ok)
				.defaultIfEmpty(ResponseEntity.notFound().build());

	}

	@PutMapping("/{id}")
	public Mono<ResponseEntity<Blog>> update(@RequestBody BlogFliter blogFliter,@PathVariable final  String id){
		return blogService.updateBlog(blogFliter.blogFliterToBlog(blogFliter),id)
				.map(updatedBlog -> new ResponseEntity<>(updatedBlog,HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Void>> deleteBlog(@PathVariable String id){
		return blogService.delete(id);
	}

	@GetMapping("/findByAuthor/{author}")
	public Flux<Blog> blogFindByAuthor(@PathVariable String author){
		return blogService.findByAuthor(author);
	}

	@GetMapping("/findTitle/{title}")
	public Flux<Blog> findByTitleBlog(@PathVariable String title){
		return blogService.findByTitleOwn(title);
	}
	
	@ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<String> handleWebClientResponseException(WebClientResponseException ex) {
        return ResponseEntity.status(ex.getRawStatusCode()).body(ex.getResponseBodyAsString());
    }

}
