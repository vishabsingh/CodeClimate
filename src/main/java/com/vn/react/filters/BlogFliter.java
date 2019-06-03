package com.vn.react.filters;

import com.vn.react.modal.Blog;

public class BlogFliter {
	private String id;
	
	private Long version;
	
	private String title;
	
	private String content;
	
	private String author;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Blog blogFliterToBlog(BlogFliter blogFliter) {
		Blog blog = new Blog();
		blog.setAuthor(blogFliter.getAuthor());
		blog.setContent(blogFliter.getContent());
		blog.setTitle(blogFliter.getTitle());
		return blog;
		
	}

	@Override
	public String toString() {
		return "BlogFliter [id=" + id + ", version=" + version + ", title=" + title + ", content=" + content
				+ ", author=" + author + "]";
	}
	
	
}
