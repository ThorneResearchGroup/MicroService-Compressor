package tech.tresearchgroup.microservices.compressor.view;

import io.activej.http.HttpMethod;
import io.activej.http.RoutingServlet;
import io.activej.inject.annotation.Provides;
import io.activej.inject.module.AbstractModule;
import tech.tresearchgroup.microservices.compressor.controller.endpoints.compression.StringController;
import tech.tresearchgroup.schemas.compression.model.CompressionMethodEnum;

public class StringEndpoints extends AbstractModule {
    @Provides
    public RoutingServlet servlet() {
        return RoutingServlet.create()
            .map(HttpMethod.GET, "/v1/brotli", response -> StringController.compress(CompressionMethodEnum.BROTLI, response))
            .map(HttpMethod.GET, "/v1/bzip2", response -> StringController.compress(CompressionMethodEnum.BZIP2, response))
            .map(HttpMethod.GET, "/v1/deflate", response -> StringController.compress(CompressionMethodEnum.DEFLATE, response))
            .map(HttpMethod.GET, "/v1/gzip", response -> StringController.compress(CompressionMethodEnum.GZIP, response))
            .map(HttpMethod.GET, "/v1/lz4/block", response -> StringController.compress(CompressionMethodEnum.LZ4_BLOCK, response))
            .map(HttpMethod.GET, "/v1/lz4/framed", response -> StringController.compress(CompressionMethodEnum.LZ4_FRAMED, response))
            .map(HttpMethod.GET, "/v1/lzma", response -> StringController.compress(CompressionMethodEnum.LZMA, response))
            .map(HttpMethod.GET, "/v1/snappy/framed", response -> StringController.compress(CompressionMethodEnum.SNAPPY_FRAMED, response))
            .map(HttpMethod.GET, "/v1/xz", response -> StringController.compress(CompressionMethodEnum.XZ, response))
            .map(HttpMethod.GET, "/v1/zstd", response -> StringController.compress(CompressionMethodEnum.ZSTD, response))
            .map(HttpMethod.GET, "/v1/lzo", response -> StringController.compress(CompressionMethodEnum.LZO, response))
            .map(HttpMethod.GET, "/v1/smaz", StringController::smazCompress)
            .map(HttpMethod.GET, "/v1/best", StringController::getBestCompressor);
    }
}
