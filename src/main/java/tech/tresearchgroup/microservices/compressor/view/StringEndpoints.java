package tech.tresearchgroup.microservices.compressor.view;

import io.activej.http.HttpMethod;
import io.activej.http.RoutingServlet;
import io.activej.inject.annotation.Provides;
import io.activej.inject.module.AbstractModule;
import tech.tresearchgroup.microservices.compressor.controller.endpoints.StringController;
import tech.tresearchgroup.schemas.compression.model.CompressionMethodEnum;

public class StringEndpoints extends AbstractModule {
    @Provides
    public RoutingServlet servlet() {
        return RoutingServlet.create()
            .map(HttpMethod.GET, "/v1/:compressDecompress/brotli", response -> StringController.run(CompressionMethodEnum.BROTLI, response))
            .map(HttpMethod.GET, "/v1/:compressDecompress/bzip2", response -> StringController.run(CompressionMethodEnum.BZIP2, response))
            .map(HttpMethod.GET, "/v1/:compressDecompress/deflate", response -> StringController.run(CompressionMethodEnum.DEFLATE, response))
            .map(HttpMethod.GET, "/v1/:compressDecompress/gzip", response -> StringController.run(CompressionMethodEnum.GZIP, response))
            .map(HttpMethod.GET, "/v1/:compressDecompress/lz4/block", response -> StringController.run(CompressionMethodEnum.LZ4_BLOCK, response))
            .map(HttpMethod.GET, "/v1/:compressDecompress/lz4/framed", response -> StringController.run(CompressionMethodEnum.LZ4_FRAMED, response))
            .map(HttpMethod.GET, "/v1/:compressDecompress/lzma", response -> StringController.run(CompressionMethodEnum.LZMA, response))
            .map(HttpMethod.GET, "/v1/:compressDecompress/snappy/framed", response -> StringController.run(CompressionMethodEnum.SNAPPY_FRAMED, response))
            .map(HttpMethod.GET, "/v1/:compressDecompress/xz", response -> StringController.run(CompressionMethodEnum.XZ, response))
            .map(HttpMethod.GET, "/v1/:compressDecompress/zstd", response -> StringController.run(CompressionMethodEnum.ZSTD, response))
            .map(HttpMethod.GET, "/v1/:compressDecompress/lzo", response -> StringController.run(CompressionMethodEnum.LZO, response))
            .map(HttpMethod.GET, "/v1/:compressDecompress/smaz", StringController::smazCompress)
            .map(HttpMethod.GET, "/v1/best", StringController::getBestCompressor);
    }
}
