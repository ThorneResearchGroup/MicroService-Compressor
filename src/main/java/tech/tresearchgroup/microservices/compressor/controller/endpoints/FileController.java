package tech.tresearchgroup.microservices.compressor.controller.endpoints;

import io.activej.csp.file.ChannelFileWriter;
import io.activej.http.HttpRequest;
import io.activej.http.HttpResponse;
import io.activej.http.MultipartDecoder;
import io.activej.inject.annotation.Provides;
import io.activej.promise.Promisable;
import org.jetbrains.annotations.NotNull;
import tech.tresearchgroup.libraries.compression.controller.ByteCompressionController;
import tech.tresearchgroup.schemas.compression.model.CompressDecompressEnum;
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
            CompressDecompressEnum compressDecompressEnum = CompressDecompressEnum.valueOf(httpRequest.getPathParameter("compressDecompress").toUpperCase());
            return httpRequest.handleMultipart(MultipartDecoder.MultipartDataHandler.file(fileName ->
                    ChannelFileWriter.open(executor(), file)))
                .map($ -> run(compressDecompressEnum, method, file));
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

    private static HttpResponse run(CompressDecompressEnum compressDecompressEnum, CompressionMethodEnum method, Path path) throws IOException {
        FileInputStream fis = new FileInputStream(path.toFile());
        byte[] data = null;
        if(compressDecompressEnum.equals(CompressDecompressEnum.COMPRESS)) {
            data = ByteCompressionController.compress(fis, method);
        } else if(compressDecompressEnum.equals(CompressDecompressEnum.DECOMPRESS)) {
            data = ByteCompressionController.decompress(fis, method);
        }
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
            Map<String, Double> results = ByteCompressionController.getBestCompressor(path.toFile());
            return HttpResponse.ok200().withBody(results.toString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return HttpResponse.ofCode(500);
    }
}
