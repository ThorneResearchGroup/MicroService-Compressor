package tech.tresearchgroup.microservices.compressor.controller.endpoints.compression;

import io.activej.csp.file.ChannelFileWriter;
import io.activej.http.HttpRequest;
import io.activej.http.HttpResponse;
import io.activej.http.MultipartDecoder;
import io.activej.inject.annotation.Provides;
import io.activej.promise.Promisable;
import org.jetbrains.annotations.NotNull;
import tech.tresearchgroup.microservices.compressor.controller.CompressionController;
import tech.tresearchgroup.schemas.compression.model.CompressionMethodEnum;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executor;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

public class FileController {
    @Provides
    static Executor executor() {
        return newSingleThreadExecutor();
    }

    public static @NotNull Promisable<HttpResponse> handleUpload(CompressionMethodEnum method, HttpRequest httpRequest) {
        try {
            UUID uuid = UUID.randomUUID();
            Path file = new File("temp/" + uuid + ".tmp").toPath();
            return httpRequest.handleMultipart(MultipartDecoder.MultipartDataHandler.file(fileName ->
                    ChannelFileWriter.open(executor(), file)))
                .map($ -> compress(method, file));
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResponse.ofCode(500);
        }
    }

    public static @NotNull Promisable<HttpResponse> handleUploadGetBest(HttpRequest httpRequest) {
        try {
            UUID uuid = UUID.randomUUID();
            Path file = new File("temp/" + uuid + ".tmp").toPath();
            return httpRequest.handleMultipart(MultipartDecoder.MultipartDataHandler.file(fileName ->
                    ChannelFileWriter.open(executor(), file)))
                .map($ -> getBestCompressor(file));
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResponse.ofCode(500);
        }
    }

    private static HttpResponse compress(CompressionMethodEnum method, Path path) throws IOException {
        FileInputStream fis = new FileInputStream(path.toFile());
        byte[] data = CompressionController.compress(fis, method);
        fis.close();
        if(data == null) {
            return HttpResponse.ofCode(500);
        }
        if (!path.toFile().delete()) {
            System.err.println("Failed to delete: " + path);
        }
        return HttpResponse.ok200().withBody(data);
    }

    public static HttpResponse getBestCompressor(Path path) {
        try {
            FileInputStream fis = new FileInputStream(path.toFile());
            Map<String, Double> results = CompressionController.getBestCompressor(fis);
            fis.close();
            return HttpResponse.ok200().withBody(results.toString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return HttpResponse.ofCode(500);
    }
}
