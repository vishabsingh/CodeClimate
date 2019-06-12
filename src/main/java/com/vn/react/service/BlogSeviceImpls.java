package com.vn.react.service;

import com.vn.react.dao.BlogRepository;
import com.vn.react.filters.BlogFliter;
import com.vn.react.modal.Blog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;

@Service
public class BlogSeviceImpls  implements BlogService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BlogSeviceImpls.class);
	@Autowired
	private BlogRepository blogRepo;

	@Override
	public Mono<Blog> createBlog(final Blog blog) {
		return blogRepo.insert(blog);
	}

	@Override
	public Mono<Blog> updateBlog(final Blog blog, final String blogId) {
		return findOne(blogId).doOnSuccess(findBlog ->{
			findBlog.setAuthor(blog.getAuthor());
			findBlog.setTitle(blog.getTitle());
			findBlog.setContent(blog.getContent());
			blogRepo.save(findBlog).subscribe();
		});
	}

	@Override
	public Flux<Blog> findAll() {
		final Flux<Blog> flux = blogRepo.findAll();
	    final Mono<List<BlogFliter>> list = flux.map(blog -> {
			BlogFliter blogFliter = new BlogFliter();
			blogFliter.setId(blog.getId());
			blogFliter.setAuthor(blog.getAuthor());
			blogFliter.setContent(blog.getContent());
			blogFliter.setVersion(blog.getVersion());
			blogFliter.setTitle(blog.getTitle());
			return blogFliter;
					
		}).collectList();
	   list.flux().subscribe(s ->LOGGER.info("{} ",s));
	  return blogRepo.findAll();
	}
	
	@Override
	public Mono<Blog> findOne(final String blogId) {
		return blogRepo.findById(blogId)
				.switchIfEmpty(Mono.error(
						new Exception("No Blog Found With id ".concat(blogId))
				));
	}


	@Override
	public Mono<ResponseEntity<Void>> delete(final String blogId) {
		return blogRepo.findById(blogId)
				.flatMap(existingBlog -> 
							blogRepo.delete(existingBlog)
							.then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
						).defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
		
	}

	@Override
	public Flux<Blog> findByAuthor(final String blogAuthor) {
		return blogRepo.findByAuthor(blogAuthor);
	}

	@Override
	public Flux<Blog> findByTitleOwn(final String blogTitle) {
		return blogRepo.findByTitle(blogTitle);
	}
}
