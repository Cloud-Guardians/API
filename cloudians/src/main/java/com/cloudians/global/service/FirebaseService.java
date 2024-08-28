package com.cloudians.global.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.cloud.StorageClient;

@Service
public class FirebaseService {
	
	private final Storage storage;

    public FirebaseService() {
        this.storage = StorageOptions.getDefaultInstance().getService();
    }
    
	
	// 사진 업로드
	public String uploadFiles(MultipartFile file, String nameFile) throws IOException, FirebaseAuthException {
String firebaseBucket = "cloudians-photo.appspot.com";
		Bucket bucket = StorageClient.getInstance().bucket(firebaseBucket);
		
		InputStream content = new ByteArrayInputStream(file.getBytes());
		Blob blob = bucket.create(nameFile.toString(), content, file.getContentType());        
		return blob.getMediaLink();
	}
	
	// 프로필 조회는 토큰 있어야 됨.. 

	// 사진 url 얻기
    public String getFileUrl(String fileName) {
    String firebaseBucket = "cloudians-photo.appspot.com";
		Bucket bucket = StorageClient.getInstance().bucket(firebaseBucket);
    	System.out.println(fileName+"서비스에 들어왓공.");
        Blob blob = storage.get(bucket.getName(), fileName.toString());
        System.out.println(blob.toString()+"들어왔을까..");
        if (blob != null) {
            // Blob의 media link를 통해 URL을 가져옵니다.
            return blob.getMediaLink();
        }
        return null; // 파일을 찾을 수 없는 경우
    }
    
    // 사진 삭ㅈㅔ
    public String deleteFileUrl(String filePath) throws Exception {
	String firebaseBucket = "cloudians-photo.appspot.com";
	Bucket bucket = StorageClient.getInstance().bucket(firebaseBucket);
	Blob blob = storage.get("your-project-id.appspot.com", filePath);
	blob.delete();
	return blob.getMediaLink();
    }
	


}
