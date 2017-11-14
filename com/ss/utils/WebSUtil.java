package com.ss.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.*;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by juneday on 2017/9/25.
 */
public class WebSUtil {


    /**
     * 获取最小节点的数据 ,由于数据是放在map中,
     * 注意 :如果有多个叶子节点名称相同,则会发生覆盖.最后得到值会和预想的不一样
     * @param element 参数
     * @param mp      用于接受节点数据的map,理论上要求是个空的map
     * @return 节点数据map
     */
    public static Map<String, String> getElementMap(Element element, Map<String, String> mp) {
        List elements = element.elements();
        // 没有子元素,叶子节点
        if (elements.isEmpty()) {
            String xpath = element.getName();
            String value = element.getTextTrim();
            mp.put(xpath, value);
        } else {
            // 有子元素(不是最小(叶子)节点)
            for (final Object element1 : elements) {
                Element elem = (Element) element1;
                // 递归遍历
                getElementMap(elem, mp);
            }
        }
        return mp;
    }

    /**
     * 注意 :如果有多个叶子节点名称相同,则会发生覆盖.最后得到值会和预想的不一样
     * @param xmlString
     * @return
     */
    public static Map getElementMap(String xmlString) {
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(xmlString);
        } catch (DocumentException e) {
            e.printStackTrace();
            return null;
        }

        // 获取XML根元素
        Element rootElement = doc.getRootElement();
        Map mp = new HashMap<>();
        return getElementMap(rootElement, mp);
    }


    /**
     * @param soapXml 请求报文
     * @param url     服务地址
     * @return 返回报文
     * @throws IOException 异常
     */
    public static String execteHttpServer(String soapXml, String url) throws IOException {
        Map<String, String> propertyMap = new HashMap();
        propertyMap.put("Content-Type", "text/xml");
        propertyMap.put("chartset", "UTF-8");
        return execteHttpServer(url, soapXml, propertyMap);
    }


    /**
     * @param url         WebService服务的请求地址
     * @param soapXml     请求报文
     * @param propertyMap 请求参数
     * @return 返回报文
     * @throws IOException 异常
     */
    private static String execteHttpServer(String url, String soapXml, Map<String, String> propertyMap) throws IOException {
        URL serUrl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) serUrl.openConnection(); //建立连接，并将连接强转为Http连接
        conn.setRequestMethod("POST"); // 设置请求方式
        conn.setConnectTimeout(50000);
        conn.setDoOutput(true);   //是否有出参
        for (Map.Entry<String, String> entry : propertyMap.entrySet()) {
            conn.setRequestProperty(entry.getKey(), entry.getValue());
        }
        if (null != soapXml) {
            byte[] entitydata = soapXml.getBytes();
            conn.setRequestProperty("Content-length", String.valueOf(entitydata.length));
            OutputStreamWriter outputStream = new OutputStreamWriter(conn.getOutputStream(), "ISO-8859-1");
            outputStream.write(soapXml);
            outputStream.flush();
            outputStream.close();
        }
        int code = conn.getResponseCode();
        String responString = "";
        InputStream inputStream;
        if (code == 200) {
            inputStream = conn.getInputStream();
        } else {
            inputStream = conn.getErrorStream();
        }
        String sCurrentLine;
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "ISO-8859-1"));

        while ((sCurrentLine = reader.readLine()) != null) {
            responString += sCurrentLine;
        }
        System.out.println(responString);
        conn.disconnect();
        return responString;
    }


    /**
     * soap转换xml报文
     * @param source soapSource
     * @return xml报文
     * @throws Exception 异常
     */
    private static String soap2string(Source source) throws Exception {
        if (source != null) {
            Node root = null;
            if (source instanceof DOMSource) {
                root = ((DOMSource) source).getNode();
            } else if (source instanceof SAXSource) {
                InputSource insource = ((SAXSource) source).getInputSource();
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                dbf.setNamespaceAware(true);
                org.w3c.dom.Document doc = dbf.newDocumentBuilder().parse(insource);
                root = doc.getDocumentElement();
            }
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            StringWriter sw = new StringWriter();
            transformer.transform(new DOMSource(root), new StreamResult(sw));
            return sw.toString();

        }
        return null;
    }

    /**
     * 获取报文
     * @param data       参数
     * @param SOAPAction API接口方法
     * @return 请求报文
     * @throws Exception 异常
     */
    public static String getSoapXML(Map<String, Object> data, String SOAPAction) throws Exception {
        String AUTH_PREFIX = "tns";
        String AUTH_NS = "http://stamps.com/xml/namespace/2017/04/swsim/swsimv62";
        SOAPMessage soapmessage = MessageFactory.newInstance().createMessage();
        SOAPPart soap = soapmessage.getSOAPPart();
        SOAPEnvelope soapEnvelope = soap.getEnvelope();
        soapEnvelope.setPrefix("soap");
        soapEnvelope.getHeader().setPrefix("soap");
        soapEnvelope.removeNamespaceDeclaration("SOAP-ENV");
        soapEnvelope.getHeader().removeNamespaceDeclaration("SOAP-ENV");
        soapEnvelope.addNamespaceDeclaration(AUTH_PREFIX, AUTH_NS);
        soapEnvelope.addNamespaceDeclaration("xsd", "http://www.w3.org/2001/XMLSchema");
        soapEnvelope.addNamespaceDeclaration("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        SOAPBody soapbody = soapEnvelope.getBody();
        soapbody.setPrefix("soap");
        SOAPElement actionElement = soapbody.addChildElement(SOAPAction, AUTH_PREFIX);
        SOAPFactory soapfactory = SOAPFactory.newInstance();

        setSaopElement(data, AUTH_PREFIX, AUTH_NS, actionElement, soapfactory);

        String xml = soap2string(soap.getContent());
        return xml.replace("<soap:Header/>", "");
    }

    /**
     * 生成XML 格式报文
     * @param dataMap       请求数据
     * @param AUTH_PREFIX   前缀
     * @param AUTH_NS       url
     * @param actionElement element
     * @param soapfactory   soap工厂
     * @throws SOAPException
     */
    private static void setSaopElement(final Map<String, Object> dataMap, final String AUTH_PREFIX, final String AUTH_NS,
                                       final SOAPElement actionElement, final SOAPFactory soapfactory) throws SOAPException {
        SOAPElement element;
        for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
            if (entry.getValue() instanceof Map) {
                Map<String, Object> childMap = (Map) entry.getValue();
                element = soapfactory.createElement(entry.getKey(), AUTH_PREFIX, AUTH_NS);
                setSaopElement(childMap, AUTH_PREFIX, AUTH_NS, element, soapfactory);
            } else {
                element = soapfactory.createElement(entry.getKey(), AUTH_PREFIX, AUTH_NS);
                element.setTextContent(entry.getValue() + "");
            }
            actionElement.addChildElement(element);
        }
    }


}