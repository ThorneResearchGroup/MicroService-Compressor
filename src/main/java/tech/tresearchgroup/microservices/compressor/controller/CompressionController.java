package tech.tresearchgroup.microservices.compressor.controller;

import com.aayushatharva.brotli4j.Brotli4jLoader;
import com.aayushatharva.brotli4j.encoder.BrotliOutputStream;
import com.aayushatharva.brotli4j.encoder.Encoder;
import com.github.luben.zstd.ZstdOutputStream;
import net.jpountz.lz4.LZ4BlockOutputStream;
import net.jpountz.lz4.LZ4FrameOutputStream;
import org.anarres.lzo.LzoAlgorithm;
import org.anarres.lzo.LzoCompressor;
import org.anarres.lzo.LzoLibrary;
import org.anarres.lzo.LzoOutputStream;
import org.tukaani.xz.LZMA2Options;
import org.tukaani.xz.LZMAOutputStream;
import org.tukaani.xz.XZ;
import org.tukaani.xz.XZOutputStream;
import org.xbib.io.compress.bzip2.Bzip2OutputStream;
import org.xerial.snappy.SnappyOutputStream;
import tech.tresearchgroup.schemas.compression.model.CompressionMethodEnum;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPOutputStream;

public class CompressionController {
    public static byte[] compress(InputStream data, CompressionMethodEnum method) throws IOException {
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        switch (method) {
            case BROTLI -> {
                try {
                    Brotli4jLoader.ensureAvailability();
                } catch (UnsatisfiedLinkError e) {
                    return null;
                }
                Encoder.Parameters brotliParams = new Encoder.Parameters().setQuality(11);
                BrotliOutputStream compressionStream = new BrotliOutputStream(byteOutputStream, brotliParams);
                while ((len = data.read(buffer)) != -1) {
                    compressionStream.write(buffer, 0, len);
                }
                compressionStream.close();
            }
            case BZIP2 -> {
                Bzip2OutputStream compressionStream = new Bzip2OutputStream(byteOutputStream);
                while ((len = data.read(buffer)) != -1) {
                    compressionStream.write(buffer, 0, len);
                }
                compressionStream.close();
            }
            case DEFLATE -> {
                DeflaterOutputStream compressionStream = new DeflaterOutputStream(byteOutputStream);
                while ((len = data.read(buffer)) != -1) {
                    compressionStream.write(buffer, 0, len);
                }
                compressionStream.close();
            }
            case GZIP -> {
                GZIPOutputStream compressionStream = new GZIPOutputStream(byteOutputStream);
                while ((len = data.read(buffer)) != -1) {
                    compressionStream.write(buffer, 0, len);
                }
                compressionStream.close();
            }
            case LZ4_BLOCK -> {
                LZ4BlockOutputStream compressionStream = new LZ4BlockOutputStream(byteOutputStream);
                while ((len = data.read(buffer)) != -1) {
                    compressionStream.write(buffer, 0, len);
                }
                compressionStream.close();
            }
            case LZ4_FRAMED -> {
                LZ4FrameOutputStream compressionStream = new LZ4FrameOutputStream(byteOutputStream);
                while ((len = data.read(buffer)) != -1) {
                    compressionStream.write(buffer, 0, len);
                }
                compressionStream.close();
            }
            case LZMA -> {
                LZMA2Options options = new LZMA2Options();
                options.setPreset(9);
                LZMAOutputStream compressionStream = new LZMAOutputStream(byteOutputStream, options, data.available());
                while ((len = data.read(buffer)) != -1) {
                    compressionStream.write(buffer, 0, len);
                }
                compressionStream.close();
            }
            case SNAPPY_FRAMED -> {
                SnappyOutputStream compressionStream = new SnappyOutputStream(byteOutputStream);
                while ((len = data.read(buffer)) != -1) {
                    compressionStream.write(buffer, 0, len);
                }
                compressionStream.close();
            }
            case XZ -> {
                LZMA2Options options = new LZMA2Options();
                options.setPreset(9);
                XZOutputStream compressionStream = new XZOutputStream(byteOutputStream, options, XZ.CHECK_SHA256);
                while ((len = data.read(buffer)) != -1) {
                    compressionStream.write(buffer, 0, len);
                }
                compressionStream.close();
            }
            case ZSTD -> {
                ZstdOutputStream compressionStream = new ZstdOutputStream(byteOutputStream, 22);
                while ((len = data.read(buffer)) != -1) {
                    compressionStream.write(buffer, 0, len);
                }
                compressionStream.close();
            }
            case LZO -> {
                LzoAlgorithm algorithm = LzoAlgorithm.LZO1X;
                LzoCompressor compressor = LzoLibrary.getInstance().newCompressor(algorithm, null);
                LzoOutputStream compressionStream = new LzoOutputStream(byteOutputStream, compressor, 256);
                while ((len = data.read(buffer)) != -1) {
                    compressionStream.write(buffer, 0, len);
                }
                compressionStream.close();
            }
        }

        byteOutputStream.close();
        return byteOutputStream.toByteArray();
    }

    public static Map<String, Double> getBestCompressor(FileInputStream fileInputStream) throws IOException {
        Map<String, Double> results = new HashMap<>();
        for (CompressionMethodEnum method : CompressionMethodEnum.values()) {
            if (!method.equals(CompressionMethodEnum.SMAZ)) {
                byte[] data = CompressionController.compress(fileInputStream, method);
                if(data != null) {
                    results.put(method.name().toLowerCase(), (double) data.length);
                }
            }
        }
        return results;
    }

    public static Map<String, Double> getBestCompressor(ByteArrayInputStream byteArrayInputStream) throws IOException {
        Map<String, Double> results = new HashMap<>();
        for (CompressionMethodEnum method : CompressionMethodEnum.values()) {
            if (!method.equals(CompressionMethodEnum.SMAZ)) {
                byte[] data = CompressionController.compress(byteArrayInputStream, method);
                if(data != null) {
                    results.put(method.name().toLowerCase(), (double) data.length);
                }
            }
        }
        return results;
    }
}
