package com.cloudians.global.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.cloud.StorageClient;

@Service
public class FirebaseService {
	
	private final Storage storage;
	private final String firebaseBucket = "cloudians-photo.appspot.com";
	private FirebaseAuth fireAuth = FirebaseAuth.getInstance();
	

	    @Autowired
	    public FirebaseService() {
	        // Ensure FirebaseApp is initialized before accessing services
	        if (FirebaseApp.getApps().isEmpty()) {
	            throw new IllegalStateException("FirebaseApp is not initialized.");
	        }
	        this.storage = StorageOptions.getDefaultInstance().getService();
	        this.fireAuth = FirebaseAuth.getInstance();
	    }
    
    public Bucket bucket() {
	return StorageClient.getInstance().bucket(firebaseBucket);
    }
    
    // user folder
    public String folderPath(String userEmail, String domain,String fileName) {
	 // 사용자 폴더 경로 설정
	 String folderPath = "users/"+userEmail.toString()+"/"+domain+"/"+fileName.toString(); // 사용자 ID에 따라 폴더를 생성
        return folderPath;
    }
	
	// upload file
		public String uploadFile(MultipartFile file, String userEmail, String fileName, String domain) throws IOException, FirebaseAuthException {
		    try (InputStream content = new ByteArrayInputStream(file.getBytes())) {
		        Blob blob = bucket().create(folderPath(userEmail,domain,fileName),content,file.getContentType());
		        return blob.getMediaLink();
		    } catch (StorageException e) {
		        System.err.println("StorageException: " + e.getMessage());
		        throw e;
		    } catch (IOException e) {
		        System.err.println("IOException: " + e.getMessage());
		        throw e;
		    }
		}
	
    
    // delete file
    public String deleteFileUrl(String filePath) throws Exception {
	Blob blob = storage.get(firebaseBucket, filePath);
	blob.delete();
	return blob.getMediaLink();
    }
    
	// get file url
    public String getFileUrl(String fileName) throws Exception {
        Blob blob = storage.get(bucket().getName(), fileName.toString());
        System.out.println(bucket().getName());
        System.out.println(blob.toString()+"들어왔을까..");
            return blob.getMediaLink();
    }
    


}
