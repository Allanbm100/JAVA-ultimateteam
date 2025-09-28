package br.com.fiap.ultimateteam.config;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class FileUploadService {

    private final Path root = Paths.get("src/main/resources/static/uploads");

    public FileUploadService() {
        try {
            // Garante que o diretório de uploads exista na inicialização
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível criar a pasta de upload!", e);
        }
    }

    public String save(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        // Garante que o diretório exista (redundância segura)
        Files.createDirectories(root);

        // Gera um nome de arquivo único para evitar conflitos
        String originalFilename = file.getOriginalFilename();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String newFilename = timestamp + "_" + originalFilename;

        // Copia o arquivo para o destino final
        Files.copy(file.getInputStream(), this.root.resolve(newFilename));

        return newFilename;
    }
}
