package com.vn.react;

import com.vn.react.modal.Blog;
import com.vn.react.service.BlogService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;


@RunWith(SpringRunner.class)
@SpringBootTest( webEnvironment=  SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReactivespringwithmongodbApplicationTests {

		@Autowired
		private WebTestClient webTestClient;

		@Autowired
		private BlogService blogService;

		@Test
		public void testCreateBlog(){
			Blog blog = new Blog("TestWebClient","CreateBlog","VISHABSINGH");

			webTestClient.post().uri("/blog")
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.body(Mono.just(blog),Blog.class)
					.exchange()
					.expectStatus().isOk()
					.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
					.expectBody()
					.jsonPath("$.id").isNotEmpty()
					.jsonPath("$.content").isEqualTo("CreateBlog");
		}

	@Test
	public void testGetAllBlogs() {
		webTestClient.get().uri("/blog/find")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
				.expectBodyList(Blog.class);
	}


}
