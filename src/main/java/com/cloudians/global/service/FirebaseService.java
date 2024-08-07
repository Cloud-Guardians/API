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
	private final String firebaseBucket = "cloudians-photo.appspot.com";

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
    public String getFileUrl(String fileName) {
        Blob blob = storage.get(bucket().getName(), fileName.toString());
        System.out.println(blob.toString()+"들어왔을까..");
        if (blob != null) {
            // Blob의 media link를 통해 URL을 가져옵니다.
            return blob.getMediaLink();
        }
        return null; // 파일을 찾을 수 없는 경우
    }
    
    
    // delete file
    public String deleteFileUrl(String filePath) throws Exception {
	Blob blob = storage.get(firebaseBucket, filePath);
	blob.delete();
	return blob.getMediaLink();
    }
	


}
