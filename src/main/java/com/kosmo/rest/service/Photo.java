package com.kosmo.rest.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Photo {
	public int albumId;
	public int id;
	public String title;
	public String url;
	public String thumbnailUrl;
}
