package com.vn.react.modal;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;



/**
 * 
 * @author vishab.singh
 *
 */
@Document(collection = "BLOG")
public class Blog implements Serializable{ 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	private String id;
	
	@Version
	private Long version;
	
	@TextIndexed
	private String title;
	
	private String content;
	
	@Indexed
	private String author;
	
	/**
	 * Default COns
	 */
	public Blog() {
	}
	/**
	 * 
	 * @param title
	 * @param content
	 * @param author
	 */
	public Blog(final String title, final String content, final String author) {
		this.title = title;
		this.content = content;
		this.author = author;
	}
	
	
	public Long getVersion() {
		return version;
	}

	public String getId() {
		return id;
	}


	public void setId(final String id) {
		this.id = id;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(final String title) {
		this.title = title;
	}


	public String getContent() {
		return content;
	}


	public void setContent(final String content) {
		this.content = content;
	}


	public String getAuthor() {
		return author;
	}


	public void setAuthor(final String author) {
		this.author = author;
	}


}
