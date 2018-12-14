package com.amx.jax.utils;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.amx.jax.postman.model.File;

public class PostManUtil {

	public static ResponseEntity<byte[]> download(File file) {
		return ResponseEntity.ok().contentLength(file.getBody().length)
				.header("Content-Disposition", "attachment; filename=" + file.getName())
				.contentType(MediaType.valueOf(file.getType().getContentType())).body(file.getBody());
	}

	public static ResponseEntity<byte[]> render(File file) {
		return ResponseEntity.ok().contentLength(file.getBody().length)
				.contentType(MediaType.valueOf(file.getType().getContentType())).body(file.getBody());
	}

}
