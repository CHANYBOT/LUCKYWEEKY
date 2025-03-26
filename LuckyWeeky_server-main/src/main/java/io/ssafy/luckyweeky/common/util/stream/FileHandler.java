package io.ssafy.luckyweeky.common.util.stream;

import io.ssafy.luckyweeky.user.application.validator.FileValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.util.UUID;

public class FileHandler {
    private static final String DEFAULT_PROFILE_IMAGE = "profile-images/default.png";

    public static Part getFilePart(HttpServletRequest request, String partName) throws IOException, ServletException {
        if (request.getContentType() != null && request.getContentType().startsWith("multipart/form-data")) {
            return request.getPart(partName);
        }
        return null;
    }

    public static String processFilePart(Part filePart) {
        if (filePart != null && filePart.getSize() > 0) {
            FileValidator.getInstance().isValid(filePart);
            String uniqueFileName = UUID.randomUUID() + getExtension(filePart);
            return "profile-images/" + uniqueFileName;
        }
        return DEFAULT_PROFILE_IMAGE;
    }

    private static String getExtension(Part filePart) {
        String fileName = filePart.getSubmittedFileName();
        int lastDotIndex = fileName.lastIndexOf(".");
        return (lastDotIndex != -1) ? fileName.substring(lastDotIndex) : "";
    }
}
