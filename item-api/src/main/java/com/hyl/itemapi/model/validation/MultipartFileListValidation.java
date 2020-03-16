package com.hyl.itemapi.model.validation;

import com.hyl.itemapi.exception.CustomBadRequestException;
import com.hyl.itemapi.exception.CustomInternalServerErrorException;
import com.hyl.itemapi.model.AuthorizedFileContentType;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class MultipartFileListValidation {


    //************************************************** GLOBAL VALIDATION
    public static void validMultipartFileList(List<MultipartFile> multipartFiles) {
        validContentType(multipartFiles);
        validSize(multipartFiles);
    }


    //************************************************** PARAM
    private static String sizeMax;
    private static Tika tika = new Tika();


    //************************************************** SETTER -> @Value works (doesn't work on static field)
    @Value("${hyl.constraint.subitem.image.maxsize}")
    private void setSizeMax(String sizeMax) {
        MultipartFileListValidation.sizeMax = sizeMax;
    }

    //************************************************** VALIDATIONS
    private static void validSize(List<MultipartFile> multipartFiles) {
        long size = 0;
        for (MultipartFile multipartFile : multipartFiles) {
            size += multipartFile.getSize();
        }
        if (size > Long.parseLong(sizeMax)) {
            throw new CustomBadRequestException("La somme du poids des images par sous-objet " +
                                    "doit être au maximum de "+sizeMax+" octets -> poids calculé -> "+size+" octets.");
        }
    }

    private static void validContentType(List<MultipartFile> multipartFiles) {
        multipartFiles.forEach(multipartFile -> {
          String mimeType = getRealContentType(multipartFile);
            boolean isValid = false;
            for (AuthorizedFileContentType authorizedFileContentType : AuthorizedFileContentType.values()) {
                if (authorizedFileContentType.getMimeType().equals(mimeType)) {
                    isValid = true;
                    break;
                }
            }
            if ( !isValid ) {
                throw new CustomBadRequestException( "Le type '" +mimeType+ "' " +
                        "du fichier '" +multipartFile.getOriginalFilename()+ "' n'est pas autorisé.");
            }
        });
    }


    //************************************************** SHARE
    public static String getRealContentType(MultipartFile multipartFile) {
        String mimeType;
        try {
            InputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(multipartFile.getBytes()));
            mimeType = tika.detect(inputStream);
            inputStream.close();
            return mimeType;
        } catch (IOException e) {
            throw new CustomInternalServerErrorException("La lecture du fichier "+multipartFile.getOriginalFilename()+" a échoué.");
        }

    }

}
