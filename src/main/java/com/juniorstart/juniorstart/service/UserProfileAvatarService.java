package com.juniorstart.juniorstart.service;

import com.juniorstart.juniorstart.exception.BadRequestException;
import com.juniorstart.juniorstart.exception.ResourceNotFoundException;
import com.juniorstart.juniorstart.model.UserProfile;
import com.juniorstart.juniorstart.repository.UserProfileRepository;
import com.juniorstart.juniorstart.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import java.io.ByteArrayOutputStream;

@RequiredArgsConstructor
@Service
public class UserProfileAvatarService {

    final private UserProfileRepository userProfileRepository;

    public ResponseEntity<?> getUserAvatar(UserPrincipal currentUser) {

        Optional<UserProfile> foundUser = findUser(currentUser.getId());

        return ResponseEntity.ok(decompressBytes(foundUser.get().getUserAvatar()));
    }

    public ResponseEntity<?> addUserAvatar(MultipartFile file, UserPrincipal currentUser) throws IOException {

        Optional<UserProfile> foundUser = findUser(currentUser.getId());

        foundUser.get().setUserAvatar(compressBytes(file.getBytes()));

        return ResponseEntity.ok(userProfileRepository.save(foundUser.get()));
    }

    public ResponseEntity<?> updateUserAvatar(MultipartFile file, UserPrincipal currentUser) throws IOException {

        Optional<UserProfile> foundUser = findUser(currentUser.getId());
        foundUser.get().setUserAvatar(compressBytes(file.getBytes()));

        return ResponseEntity.ok(userProfileRepository.save(foundUser.get()));
    }

    public ResponseEntity<?> deleteUserAvatar(UserPrincipal currentUser) {

        Optional<UserProfile> foundUser = findUser(currentUser.getId());
        foundUser.get().setUserAvatar(null);

        return ResponseEntity.ok(userProfileRepository.save(foundUser.get()));
    }

    public static byte[] decompressBytes(byte[] data) {

        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException | DataFormatException ignored) {
            throw new BadRequestException("Couldn't load image");
        }
        return outputStream.toByteArray();
    }

    public static byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException ignored) {
            throw new BadRequestException("Bad Image Format");
        }
        return outputStream.toByteArray();
    }

    public Optional<UserProfile> findUser(UUID id) {
        return Optional.ofNullable(userProfileRepository.findByPrivateId(id).orElseThrow(() ->
                new ResourceNotFoundException("UserProfile", "ID", id)));
    }
}
