package com.myspring.pro28.ex02;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import net.coobird.thumbnailator.Thumbnails;

//@Controller
public class FileDownloadController {
	private static String CURR_IMAGE_REPO_PATH = "c:\\spring\\image_repo";
	// 파일 저장 위치를 지정한다

//	@RequestMapping("/download")
	protected void download(@RequestParam("imageFileName") String imageFileName, HttpServletResponse response)
			throws Exception {
		// @RequestParam 사용하여 다운로드할 이미지 파일 이름을 전달한다
		OutputStream out = response.getOutputStream();
		String enfileName = URLEncoder.encode(imageFileName, "utf-8");
		// 한글 Filter를 걸었으나 해더(저장공간같은것)에는 적용이 안되어 받아온 파일이름이 한글일 경우
		// 다운로드가 안되고 깨진 문구로 출력되어 다시 한번 encoding을 해줌
		String filePath = CURR_IMAGE_REPO_PATH + "\\" + imageFileName;
		System.out.println("imageFileName = " + imageFileName);
		File image = new File(filePath);
		
		// 확장자를 제외한 원본 이미지 파일의 이름을 가져온다.
		int lastIndex = imageFileName.lastIndexOf(".");
		String fileName = imageFileName.substring(0, lastIndex);
		
		File thumbnail = new File(CURR_IMAGE_REPO_PATH + "\\" + "thumbnail" + "\\" + fileName + ".png");
		// 원본 이미지 파일 이름과 같은 이름의 썸네일 파일에 대한 file 객체를 생성한다.
//		response.addHeader("Content-disposition", "attachment; fileName = " + enfileName);

		// 원본 이미지 파일을 가로세로가 50px인 png 형식의 썸네일 이미지 파일로 생성한다.
		if (image.exists()) {
			thumbnail.getParentFile().mkdirs();
			Thumbnails.of(image).size(50, 50).outputFormat("png").toFile(thumbnail);
		}

		// 생성된 이미지 파일을 브라우저로 전송한다
		FileInputStream in = new FileInputStream(thumbnail);
		byte[] buffer = new byte[1024 * 8];
		while (true) {
			int count = in.read(buffer); // 버퍼에 읽어들인 문자개수
			if (count == -1) // 버퍼의 마지막에 도달했는지 체크
				break;
			out.write(buffer, 0, count);
		}
		
		in.close();
		out.close();
	}

}
