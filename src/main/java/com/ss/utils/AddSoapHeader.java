package com.ss.utils;

import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.Phase;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.namespace.QName;
import java.util.List;


/**
 * @Title:在发送消息前，封装Soap Header 信息
 */

public class AddSoapHeader extends AbstractSoapInterceptor {

	/**名称空间*/
	private String nameURI;   
	/**用户*/
    private String userName;
    /**密码*/    
    private String passWord;

    public AddSoapHeader(){   
        super(Phase.WRITE);   
    }   
    public AddSoapHeader(String nameURI, String userName, String passWord) {
        super(Phase.WRITE);
    	this.nameURI  = nameURI;
    	this.userName = userName;
    	this.passWord = passWord;

    }
    @Override
    public void handleMessage(SoapMessage message) throws Fault {
    	
        QName qname=new QName("RequestSOAPHeader");   
        Document doc=DOMUtils.createDocument();  
        
        //用户名
        Element el_username=doc.createElement("userName");   
        el_username.setTextContent(userName);   
        //密码
        Element el_password=doc.createElement("passWord");          
        el_password.setTextContent(passWord);

        Element root=doc.createElementNS(nameURI, "in:system");
        root.appendChild(el_username);   
        root.appendChild(el_password);   

        SoapHeader head=new SoapHeader(qname,root);   
        List<Header> headers=message.getHeaders();   
        headers.add(head);   
    }
}

