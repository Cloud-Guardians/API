package com.cloudians.global.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudians.domain.user.entity.User;
import com.cloudians.domain.user.repository.UserRepository;
import com.cloudians.global.Message;
import com.cloudians.global.exception.FirebaseException;
import com.cloudians.global.exception.FirebaseExceptionType;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class FirebaseService {
    private final String firebaseBucket = "cloudians-photo.appspot.com";
    private final UserRepository userRepository;

    public Bucket bucket() {
        return StorageClient.getInstance().bucket(firebaseBucket);
    }

   
    // user folder
    public String folderPath(String userEmail, String domain, String fileName) {
        // 사용자 폴더 경로 설정
        String folderPath = "users/" + userEmail + "/" + domain + "/" + fileName.toString(); // 사용자 ID에 따라 폴더를 생성
        return folderPath;
    }

    // upload file
    public String uploadFile(MultipartFile file, String userEmail, String domain, String fileName) {
        try (InputStream content = new ByteArrayInputStream(file.getBytes())) {
            Blob blob = bucket().create(folderPath(userEmail, domain, fileName), content, file.getContentType());
            return blob.getMediaLink();
        } catch (Exception e) {
            Message message = new Message(e, HttpStatus.BAD_REQUEST.value());
            return message.toString();
        }
    }

    // delete file
    public void deleteFileUrl(String userEmail, String domain, String fileName) {
        System.out.println(fileName);
        String folderPath = folderPath(userEmail, domain, fileName);
        Blob blob = bucket().get(folderPath);
        if (blob != null) {
            blob.delete();
        } else throw new FirebaseException(FirebaseExceptionType.PHOTO_VALUE_NOT_FOUND);
    }

    // get file url
    public String getFileUrl(String userEmail, String domain, String fileName) {
        String folderPath = folderPath(userEmail, domain, fileName);
        Blob blob = bucket().get(folderPath);
        if (blob != null) {
            String urlPath = folderPath.split("/")[0] + "%2F" + folderPath.split("/")[1] + "%2F" + folderPath.split("/")[2] + "%2F" + folderPath.split("/")[3];
            System.out.println(folderPath);
            System.out.println("mediaLink:" + blob.getMediaLink());
            System.out.println("selfLink:" + blob.getSelfLink());
            System.out.println("blob:" + blob.toString());
            Map<String, String> metadata = blob.getMetadata();
            String tokens = metadata.get("firebaseStorageDownloadTokens");

            String dap = "https://firebasestorage.googleapis.com/v0/b/cloudians-photo.appspot.com/o/" + urlPath + "?alt=media&token=" + tokens;
            return dap;
        } else throw new FirebaseException(FirebaseExceptionType.PHOTO_VALUE_NOT_FOUND);
    }

    public Blob getBlob(String userEmail, String domain, String fileName) {
        String folderPath = folderPath(userEmail, domain, fileName);
        return bucket().get(folderPath);
    }
}

