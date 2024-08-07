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
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.cloud.StorageClient;

@Service
public class FirebaseService {
	
	private final Storage storage;
	private String firebaseBucket = "cloudians-photo.appspot.com";

    public FirebaseService() {
        this.storage = StorageOptions.getDefaultInstance().getService();
    }
    
    public Bucket bucket() {
	return StorageClient.getInstance().bucket(firebaseBucket);
    }
	
	// upload file
	public String uploadFiles(MultipartFile file, String nameFile) throws IOException, FirebaseAuthException {
		InputStream content = new ByteArrayInputStream(file.getBytes());
		Blob blob = bucket().create(nameFile.toString(), content, file.getContentType());        
		return blob.getMediaLink();
	}
	

	// get file url
    public String getFileUrl(String fileName) throws Exception {
        Blob blob = storage.get(bucket().getName(), fileName.toString());
        System.out.println(bucket().getName());
        System.out.println(blob.toString()+"들어왔을까..");
            return blob.getMediaLink();
    }
    
    
    // delete file
    public String deleteFileUrl(String filePath) throws Exception {
	Blob blob = storage.get(firebaseBucket, filePath);
	blob.delete();
	return blob.getMediaLink();
    }
    
    // user folder
    public String folderPath(String userEmail, String domain) throws Exception {
	 // 사용자 폴더 경로 설정
        String folderPath = "user_photos/" + userEmail.toString() + "/"+domain+"/"; // 사용자 ID에 따라 폴더를 생성
        return folderPath;
    }
	


}
