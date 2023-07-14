package com.myspring.pro28.ex01;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import net.coobird.thumbnailator.Thumbnails;

@Controller
public class FileDownloadController {

	private static String CURR_IMAGE_REPO_PATH = "c:\\spring\\image_repo"; // 파일 저장 위치를 지정한다

	@RequestMapping("/download") // 업로드한 파일을 보여주기위한 메서드
	protected void download(@RequestParam("imageFileName") String imageFileName, HttpServletResponse response)
			throws Exception {
		// @RequestParam 사용하여 다운로드할 이미지 파일 이름을 전달한다
		OutputStream out = response.getOutputStream();
		String downFile = CURR_IMAGE_REPO_PATH + "\\" + imageFileName;
		System.out.println("imageFileName = " + imageFileName);
		File file = new File(downFile);

		// 확장자를 제외한 원본 이미지 파일의 이름을 가져온다.
		int lastIndex = imageFileName.lastIndexOf(".");
		String fileName = imageFileName.substring(0, lastIndex);
//	String enfileName = URLEncoder.encode(fileName,"utf-8"); 해더를 사용하지 않았음으로 필요없음
		File thumbnail = new File(CURR_IMAGE_REPO_PATH + "\\" + "thumbnail" + "\\" + fileName + ".png");
		// 원본 이미지 파일 이름과 같은 이름의 썸네일 파일에 대한 file 객체를 생성한다.

		if (file.exists()) {
			// thumbnail.getParentFile().mkdirs();
			// 썸네일 이미지 파일로 생성하는 기능, 썸네일 이미지가 있으면 새로업로드할 필요없음으로 주석처리

			Thumbnails.of(file).size(50, 50).outputFormat("png").toOutputStream(out);
			// 원본 이이지에 댛나 썸네일 이미지를 생성 한 후 OutputStream 객체에 할당한다
		} else {
			return;
		}

		// 썸네일 이미지를 OutputStream 객체를 이용해 브라우저로 전송한다.
		byte[] buffer = new byte[1024 * 8];
		out.write(buffer);

		out.close();
	}

//	
//  @RequestMapping("/download")
//	protected void download(@RequestParam("imageFileName") String imageFileName, HttpServletResponse response)  throws Exception {
//							// @RequestParam 사용하여 다운로드할 이미지 파일 이름을 전달한다
//		OutputStream out = response.getOutputStream();
//		String enfileName = URLEncoder.encode(imageFileName,"utf-8");
//		// 한글 Filter를 걸었으나 해더에는 적용이 안되어 받아온 파일이름이 한글일 경우 
//		// 다운로드가 안되고 깨진 문구로 출력되어 다시 한번 encoding을 해줌
//		String downFile = CURR_IMAGE_REPO_PATH + "\\" + imageFileName;
//		System.out.println("imageFileName = " + imageFileName);
//		File file = new File(downFile); // 다운로드할 파일 객체를 생성한다.
//		response.setHeader("Cache-Control", "no-cache");
//		response.addHeader("Content-disposition", "attachment; fileName = " + enfileName);
//		// 해더(저장공간같은것)에 파일 이름을 설정한다
//		
//		FileInputStream in = new FileInputStream(file);
//		byte[] buffer = new byte[1024 * 8];// 버퍼를 이용해 한 번에 8Kbyte씩 브라우저로 전송한다.
//		while (true) {
//			int count = in.read(buffer);
//			if (count == -1) break;
//			out.write(buffer, 0, count);
//		}
//		
//		in.close();
//		out.close();
//		
//	}
}
