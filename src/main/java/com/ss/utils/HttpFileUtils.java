package com.ss.utils;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by juneday on 2017/11/15.
 */
public class HttpFileUtils {

    /**
     * 上传文件
     * @param request
     * @param savePath 保存路径
     * @return 数据集
     */
    public static Map<String, Object> uploadfile(HttpServletRequest request, String savePath) {
        /* 保存表单数据*/
        Map<String, Object> map = new HashMap<>(); /* 保存文件路径*/
        List<String> files = new ArrayList<>();
        File photo = new File(savePath); /* 判断上传文件的保存目录是否存在*/
        if (!photo.exists() && !photo.isDirectory()) {
            System.out.println(savePath + "目录不存在，需要创建"); /* 创建目录*/
            photo.mkdir();
        }
        try { /* 1、创建一个DiskFileItemFactory工厂*/
            DiskFileItemFactory factory = new DiskFileItemFactory();
            /* 2、创建一个文件上传解析器*/
            ServletFileUpload upload = new ServletFileUpload(factory);
            /* 解决上传文件名的中文乱码*/
            upload.setHeaderEncoding("UTF-8");
            /* 3、判断提交上来的数据是否是上传表单的数据*/
            if (!ServletFileUpload.isMultipartContent(request)) {
            }
            /* 4、使用ServletFileUpload解析器解析上传数据，解析结果返回的是一个List<FileItem>集合，每一个FileItem对应一个Form表单的输入项*/
            List<FileItem> list = upload.parseRequest(request);
            for (FileItem item : list) { /* 如果fileitem中封装的是普通输入项的数据*/
                if (item.isFormField()) { /* 解决普通输入项的数据的中文乱码问题 添加表单项*/
                    if (null == item.getFieldName())
                        continue;
                    if (0 < item.getSize()) map.put(item.getFieldName(), new String(item.getString()));
                } else {
                    /* 如果fileitem中封装的是上传文件 得到上传的文件名称，*/
                    long size = item.getSize();
                    String filename = item.getName();
                    if (null == filename || filename.trim().equals("")) {
                        continue;
                    }
                    /* 注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件名是带有路径的，如： c:\a\b\1.txt，而有些只是单纯的文件名，如：1.txt 处理获取到的上传文件的文件名的路径部分，只保留文件名部分*/
                    filename = filename.substring(filename.lastIndexOf("\\") + 1);
                    filename = System.currentTimeMillis() + filename.substring(filename.lastIndexOf("."), filename.length()); /* 获取item中的上传文件的输入流*/
                    InputStream in = item.getInputStream(); /* 创建一个文件输出流*/
                    FileOutputStream out = new FileOutputStream(savePath + filename); /* 创建一个缓冲区*/
                    byte buffer[] = new byte[1024]; /* 判断输入流中的数据是否已经读完的标识*/
                    int len = 0; /* 循环将输入流读入到缓冲区当中，(len=in.read(buffer))>0就表示in里面还有数据*/
                    while ((len = in.read(buffer)) > 0) { /* 使用FileOutputStream输出流将缓冲区的数据写入到指定的目录(savePath + "\\" + filename)当中*/
                        out.write(buffer, 0, len);
                    } /* 关闭输入流*/
                    in.close(); /* 关闭输出流*/
                    out.close(); /* 删除处理文件上传时生成的临时文件*/
                    item.delete();
                    if (size > 200000) {
                        String newfile = System.currentTimeMillis() + filename.substring(filename.lastIndexOf("."), filename.length());
                        if (newfile.equals(filename))
                            newfile = System.currentTimeMillis() + filename.substring(filename.lastIndexOf("."), filename.length());
                        System.out.println(savePath + filename);
                        System.out.println(savePath + newfile);
                        files.add(newfile);
                    } else
                        files.add(filename);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            map = null;
            return map;
        }
        map.put("file", files);
        return map;
    }


    /**
     * 上传文件
     * @param request
     * @param savePath 保存路径
     * @return 数据集
     */
    public static Map<String, Object> uploadfile(HttpServletRequest request, String savePath, String filename) {
        // 保存表单数据
        Map<String, Object> map = new HashMap<String, Object>();
        // 保存文件路径
        List<String> files = new ArrayList<String>();

        File photo = new File(savePath);
        // 判断上传文件的保存目录是否存在
        if (!photo.exists() && !photo.isDirectory()) {
            System.out.println(savePath + "目录不存在，需要创建");
            // 创建目录
            photo.mkdir();
        }
        try {
            // 1、创建一个DiskFileItemFactory工厂
            DiskFileItemFactory factory = new DiskFileItemFactory();
            // 2、创建一个文件上传解析器
            ServletFileUpload upload = new ServletFileUpload(factory);
            // 解决上传文件名的中文乱码
            upload.setHeaderEncoding("UTF-8");
            // 3、判断提交上来的数据是否是上传表单的数据
            if (!ServletFileUpload.isMultipartContent(request)) {

            }
            // 4、使用ServletFileUpload解析器解析上传数据，解析结果返回的是一个List<FileItem>集合，每一个FileItem对应一个Form表单的输入项
            List<FileItem> list = upload.parseRequest(request);
            for (FileItem item : list) {
                // 如果fileitem中封装的是普通输入项的数据
                if (item.isFormField()) {
                    // 解决普通输入项的数据的中文乱码问题
                    // 添加表单项
                    if (null == item.getFieldName()) {
                        continue;
                    }
                    if (0 < item.getSize()) {
                        map.put(item.getFieldName(), new String(item.getString().getBytes("ISO8859_1"), "utf-8"));
                    }
                } else {// 如果fileitem中封装的是上传文件
                    // 得到上传的文件名称，
                    // 注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件名是带有路径的，如：
                    // c:\a\b\1.txt，而有些只是单纯的文件名，如：1.txt
                    // 处理获取到的上传文件的文件名的路径部分，只保留文件名部分
                    filename = filename.substring(filename.lastIndexOf("\\") + 1);
                    // 获取item中的上传文件的输入流
                    InputStream in = item.getInputStream();
                    // 创建一个文件输出流
                    FileOutputStream out = new FileOutputStream(savePath + filename);
                    // 创建一个缓冲区
                    byte buffer[] = new byte[1024];
                    // 判断输入流中的数据是否已经读完的标识
                    int len = 0;
                    // 循环将输入流读入到缓冲区当中，(len=in.read(buffer))>0就表示in里面还有数据
                    while ((len = in.read(buffer)) > 0) {
                        // 使用FileOutputStream输出流将缓冲区的数据写入到指定的目录(savePath + "\\"
                        // + filename)当中
                        out.write(buffer, 0, len);
                    }
                    // 关闭输入流
                    in.close();
                    // 关闭输出流
                    out.close();
                    // 删除处理文件上传时生成的临时文件
                    item.delete();

                    files.add(filename);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            map = null;
            return map;
        }
        map.put("file", files);
        return map;
    }


    /**
     * 模拟FORM表单请求
     * @param urlstr 请求地址
     * @param files  文件
     * @param texts  文本
     * @return
     * @throws Exception
     */
    public static String postFormData(String urlstr, Map<String, File> files, Map<String, Object> texts) throws Exception {
        URL url = new URL(urlstr);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setConnectTimeout(5000);
        con.setReadTimeout(30000);
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false); // post方式不能使用缓存
        con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式
        // 设置请求头信息
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", "UTF-8");
        // 设置边界,这里的boundary是http协议里面的分割符，不懂的可惜百度(http 协议 boundary)，这里boundary
        // 可以是任意的值(111,2222)都行
        String BOUNDARY = "----------" + System.currentTimeMillis();
        con.setRequestProperty("Content-Type", "multipart/form-data" + BOUNDARY);
        // 获得输出流
        OutputStream conOut = new DataOutputStream(con.getOutputStream());
        if (null != texts && !texts.isEmpty()) {// 文本
            StringBuffer strBuf = new StringBuffer();
            for (Map.Entry<String, Object> text : texts.entrySet()) {
                strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                strBuf.append("Content-Disposition: form-data; name=\"" + text.getKey() + "\"\r\n\r\n");
                strBuf.append(text.getValue());
            }
            conOut.write(strBuf.toString().getBytes("utf-8"));
        }
        if (null != files && !files.isEmpty()) {// 文件
            for (Map.Entry<String, File> entry : files.entrySet()) {
                // 请求正文信息
                StringBuilder sb = new StringBuilder();
                sb.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                // 这里是media参数相关的信息，这里是否能分开下我没有试，感兴趣的可以试试
                sb.append("Content-Disposition: form-data;name=\""
                        + entry.getKey() + "\"; filename=\"" + entry.getKey()
                        + "\"\r\n");
                sb.append("Content-Type:application/octet-stream\r\n\r\n");
                // 输出表头
                conOut.write(sb.toString().getBytes("utf-8"));
                // 文件正文部分
                // 把文件以流文件的方式 推入到url中
                DataInputStream imgin = new DataInputStream(
                        new FileInputStream(entry.getValue()));
                int bytes = 0;
                byte[] bufferOut = new byte[1024];
                while ((bytes = imgin.read(bufferOut)) != -1) {
                    conOut.write(bufferOut, 0, bytes);
                }
                imgin.close();
            }
        }
        // 结尾部分，这里结尾表示整体的参数的结尾，结尾要用"--"作为结束，这些都是http协议的规定
        byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
        conOut.write(foot);
        conOut.flush();
        conOut.close();
        StringBuffer imgbuffer = new StringBuffer();
        BufferedReader reader = null;
        // 定义BufferedReader输入流来读取URL的响应
        reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String line = null;
        while ((line = reader.readLine()) != null) {
            imgbuffer.append(line);
        }
        return imgbuffer.toString();
    }

    /**
     * 下载网络资源
     * @param urlstr   请求地址
     * @param savepath 保存路径
     * @return String 资源路径
     * @throws Exception
     */
    public static String loadResourceByUrl(String urlstr, String savepath) throws Exception {
        URL url = new URL(urlstr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // 设置超时间为3秒
        conn.setConnectTimeout(3 * 1000);
        // 防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        String filename = urlstr.substring(urlstr.lastIndexOf("/") + 1);
        // 得到输入流
        InputStream inputStream = conn.getInputStream();
        // 创建一个文件输出流
        FileOutputStream out = new FileOutputStream(savepath + filename);
        // 创建一个缓冲区
        byte buffer[] = new byte[1024];
        // 判断输入流中的数据是否已经读完的标识
        int len = 0;
        // 循环将输入流读入到缓冲区当中，(len=in.read(buffer))>0就表示in里面还有数据
        while ((len = inputStream.read(buffer)) > 0) {
            // 使用FileOutputStream输出流将缓冲区的数据写入到指定的目录(savePath + "\\" +
            // filename)当中
            out.write(buffer, 0, len);
        }
        // 关闭输入流
        inputStream.close();
        // 关闭输出流
        out.close();
        return filename;
    }

}
