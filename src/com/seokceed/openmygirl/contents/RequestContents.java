package com.seokceed.openmygirl.contents;

public class RequestContents {
	
	private ContentsData contents_name;
	private String image_path;
	
	private String[] args;
	
	public RequestContents(ContentsData content, String imagePath) {
		contents_name = content;
		image_path = imagePath;
	}
	
	public RequestContents(ContentsData content, String imagePath, String ... args) {
		contents_name = content;
		image_path = imagePath;
		this.args = args;
	}
	
	public String[] getArgs() {
		return args;
	}
	
	public void setImage_path(String image_path) {
		this.image_path = image_path;
	}
	
	public ContentsData getContents_name() {
		return contents_name;
	}

	public String getImage_path() {
		return image_path;
	}

}
