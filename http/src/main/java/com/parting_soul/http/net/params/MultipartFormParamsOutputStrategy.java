package com.parting_soul.http.net.params;

import com.parting_soul.http.net.request.BaseRequest;
import com.parting_soul.http.bean.FilePair;
import com.parting_soul.http.utils.ContentType;
import com.parting_soul.http.utils.UrlUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.UUID;

/**
 * @author parting_soul
 * @date 2019-07-23
 */
public class MultipartFormParamsOutputStrategy extends BaseParamsOutputStrategy {
    private static final String END = "\r\n";
    private String boundary = UUID.randomUUID().toString();

    @Override
    protected String getContentType() {
        return ContentType.MULTIPART_FORM_DATA + "; boundary=" + boundary;
    }

    @Override
    protected void onWriteParams(OutputStream writer, BaseRequest request) throws IOException {
        Map<String, Object> params = request.getParams();

        boolean isHasMessageData = false;
        if (params != null && !params.isEmpty()) {
            //写入普通参数
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                addFormField(writer, entry.getKey(), entry.getValue(), boundary);
            }
            isHasMessageData = true;
        }

        Map<String, FilePair> filePairs = request.getFilePairs();
        if (filePairs != null && !filePairs.isEmpty()) {
            //写入文件
            for (Map.Entry<String, FilePair> entry : filePairs.entrySet()) {
                FilePair pair = entry.getValue();
                addFilePart(writer, entry.getKey(), pair.getFilePath(), boundary);
            }
            isHasMessageData = true;
        }

        //写入消息主体结束标志
        if (isHasMessageData) {
            writeMessageEnd(writer, boundary);
        }
    }

    private void writeMessageEnd(OutputStream writer, String boundary) throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append("--")
                .append(boundary)
                .append("--")
                .append(END);
        writer.write(builder.toString().getBytes());
    }

    /**
     * 添加表单字段
     * 每一个部分以--boundary开头，然后换行
     * 之后跟上表单字段的描述信息，然后换行
     * 之后接上一个空行
     * 最后为字段的值
     * <p><br>
     * --{$boundary}<br>
     * Content-Disposition: form-data; name="{$name}"<br>
     * <br>
     * {$value}<br>
     * </p>
     *
     * @param writer
     * @param key
     * @param value
     * @param boundary
     */
    private static void addFormField(OutputStream writer, String key, Object value, String boundary) throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append("--").append(boundary).append(END)
                .append("Content-Disposition: form-data; name=\"").append(key).append("\"").append(END)
                .append("Content-Type: text/plain; charset=").append(UrlUtils.UTF_8).append(END)
                .append(END)
                .append(value).append(END);
        writer.write(builder.toString().getBytes());
    }


    /**
     * 添加文件
     *
     * @param outputStream
     * @param fileKey
     * @param filePath
     * @param boundary
     * @throws IOException
     */
    private static void addFilePart(OutputStream outputStream, String fileKey, String filePath, String boundary) throws IOException {
        File file = new File(filePath);
        StringBuilder builder = new StringBuilder();
        builder.append("--").append(boundary).append(END)
                .append("Content-Disposition: form-data; name=\"").append(fileKey).append("\"; filename=\"").append(file.getName()).append("\"").append(END)
                .append("Content-Type: application/octet-stream").append(END)
                .append("Content-Transfer-Encoding: binary").append(END)
                .append(END);
        outputStream.write(builder.toString().getBytes());
        outputStream.write(getFileData(file));
        outputStream.write(END.getBytes());
    }

    /**
     * 获取文件内容
     *
     * @param file
     * @return
     * @throws IOException
     */
    private static byte[] getFileData(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int len = -1;
        while ((len = fis.read(buffer, 0, buffer.length)) != -1) {
            bos.write(buffer, 0, len);
        }
        fis.close();
        return bos.toByteArray();
    }

}
