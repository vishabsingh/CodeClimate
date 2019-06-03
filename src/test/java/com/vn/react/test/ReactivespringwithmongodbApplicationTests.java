package com.vn.react.test;

import java.util.Collections;

import javax.xml.ws.RespectBinding;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.vn.react.dao.BlogRepository;
import com.vn.react.modal.Blog;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RunWith(SpringRunner.class)
@SpringBootTest( webEnvironment=  SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ReactivespringwithmongodbApplicationTests {

		@Autowired
		private WebTestClient webTestClient;
	
		@Autowired
		private BlogRepository blogRepository;
		
		@Test
		public void test1CreateBlog(){
			Blog blog = new Blog("Core Java","Blog For Genrices","VISHABSINGH");
			webTestClient.post().uri("/blog")
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.body(Mono.just(blog),Blog.class)
					.exchange()
					.expectStatus().isOk()
					.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
					.expectBody()
					.jsonPath("$.id").isNotEmpty()
					.jsonPath("$.content").isEqualTo("Blog For Genrices");
		}

	@Test
	public void test2GetAllBlogs() {
		webTestClient.get().uri("/blog/find")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
				.expectBodyList(Blog.class);
	}
	
	@Test
	public void test3GetSingleRecord() {
		webTestClient.get().uri("/blog/findTitle/{title}","Core Java")
							.exchange()
							.expectStatus().isOk()
							.expectBody()
							.consumeWith(response -> Assertions.assertThat(response.getResponseBody()).isNotNull());
	}
	@Test
	public void test4GetFindById() {
		Blog blog = blogRepository.findByTitle("Core Java").next().block();
		webTestClient.get()
						.uri("/blog/{id}",blog.getId())
						.exchange()
						.expectStatus().isOk()
						.expectBody()
						.consumeWith(resp -> Assertions.assertThat(resp.getResponseBody()).isNotEmpty());
	}

	@Test
	public void test5CountRecordAndFindByAuthor(){
			long countRecord = blogRepository.count().block().longValue();
			Flux<Blog> fluxBlog = blogRepository.findAll();
			Long totalRecord = fluxBlog.count().block();
			long result = totalRecord !=null ? totalRecord.longValue() : 0;
			Assert.assertEquals(countRecord,result);

			Blog blogDB = blogRepository.findByAuthor("VISHABSINGH").next().block();

			webTestClient.get()
					.uri("/blog/findByAuthor/{author}",blogDB.getAuthor())
					.exchange()
					.expectStatus().isOk()
					.expectBody()
					.consumeWith(resp -> {
						Assertions.assertThat(resp.getResponseBody()).isNotEmpty();
					});
	}

	
	// SONAR CHANGES
	@Test
	public void test10DeleteRecord() {
		Blog blogDB = blogRepository.findByAuthor("VISHABSINGH").next().block();
		webTestClient.delete().uri("/blog/{id}",Collections.singletonMap("id",blogDB.getId()))
		.exchange()
		.expectStatus().isOk();
	}

}
