package tech.tresearchgroup.microservices.compressor.view;

import io.activej.http.HttpMethod;
import io.activej.http.RoutingServlet;
import io.activej.inject.annotation.Provides;
import io.activej.inject.module.AbstractModule;
import tech.tresearchgroup.microservices.compressor.controller.endpoints.FileController;
import tech.tresearchgroup.schemas.compression.model.CompressionMethodEnum;

public class FileEndpoints extends AbstractModule {
    @Provides
    public RoutingServlet servlet() {
        return RoutingServlet.create()
            .map(HttpMethod.POST, "/v1/:compressDecompress/brotli", response -> FileController.handleUpload(CompressionMethodEnum.BROTLI, response))
            .map(HttpMethod.POST, "/v1/:compressDecompress/bzip2", response -> FileController.handleUpload(CompressionMethodEnum.BZIP2, response))
            .map(HttpMethod.POST, "/v1/:compressDecompress/deflate", response -> FileController.handleUpload(CompressionMethodEnum.DEFLATE, response))
            .map(HttpMethod.POST, "/v1/:compressDecompress/gzip", response -> FileController.handleUpload(CompressionMethodEnum.GZIP, response))
            .map(HttpMethod.POST, "/v1/:compressDecompress/lz4/block", response -> FileController.handleUpload(CompressionMethodEnum.LZ4_BLOCK, response))
            .map(HttpMethod.POST, "/v1/:compressDecompress/lz4/framed", response -> FileController.handleUpload(CompressionMethodEnum.LZ4_FRAMED, response))
            .map(HttpMethod.POST, "/v1/:compressDecompress/lzma", response -> FileController.handleUpload(CompressionMethodEnum.LZMA, response))
            .map(HttpMethod.POST, "/v1/:compressDecompress/snappy/framed", response -> FileController.handleUpload(CompressionMethodEnum.SNAPPY_FRAMED, response))
            .map(HttpMethod.POST, "/v1/:compressDecompress/xz", response -> FileController.handleUpload(CompressionMethodEnum.XZ, response))
            .map(HttpMethod.POST, "/v1/:compressDecompress/zstd", response -> FileController.handleUpload(CompressionMethodEnum.ZSTD, response))
            .map(HttpMethod.POST, "/v1/:compressDecompress/lzo", response -> FileController.handleUpload(CompressionMethodEnum.LZO, response))
            .map(HttpMethod.POST, "/v1/best", FileController::handleUploadGetBest);
    }
}
