package com.ss.utils;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.cxf.transport.http.HTTPConduit;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by juneday on 2017/9/25.
 */
public class WebSUtil {


    /**
     * 获取XML最小节点的数据 ,由于数据是放在map中,
     * 注意 :如果有多个叶子节点名称相同,则会发生覆盖.最后得到值会和预想的不一样
     * @param element 参数
     * @param mp      用于接受节点数据的map,理论上要求是个空的map
     * @return 节点数据map
     */
    public static Map<String, String> getXMLElementMap(Element element, Map<String, String> mp) {
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
                getXMLElementMap(elem, mp);
            }
        }
        return mp;
    }

    /**
     * 注意 :如果有多个叶子节点名称相同,则会发生覆盖.最后得到值会和预想的不一样
     * @param xmlString xml String
     * @return elementMap
     */
    public static Map getXMLElementMap(String xmlString) {
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
        return getXMLElementMap(rootElement, mp);
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
     * 获取XML报文
     * @param data       节点Map参数
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

    /**
     * cxf 调用servicel 例子
     * @throws Exception ex
     */
    public static void invokeService() throws Exception {

        System.out.println(new Date() + "初始化开始...");

        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        String url = "https://swsim.testing.stamps.com/swsim/swsimv62.asmx?wsdl";
        Client client = dcf.createClient(url);
        HTTPConduit http = (HTTPConduit) client.getConduit();
      /*  HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
        httpClientPolicy.setConnectionTimeout(200000);
        httpClientPolicy.setReceiveTimeout(600000);
        http.setClient(httpClientPolicy);
*/

        String xmlInput = "<input></input>";


        /****接口调用测试****/
        //=========CXF========//
        System.out.println(new Date() + "业务执行开始...");
        String methodStr = "AuthenticateUser";
        Object[] objects;
        objects = client.invoke(methodStr, xmlInput);
        String rexml = "";
        for (final Object object : objects) {
            rexml += object.toString();
        }
        System.out.println(objects[0].toString());
        System.out.println(rexml);
        System.out.println(new Date() + "业务执行结束!");
    }

}