/*
 * Copyright (C) 2019 Sun.Hao(https://www.crazy-coder.cn/)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package lodsve.core.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.util.Assert;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * xml工具类.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2015-11-18 13:58
 */
public final class XmlUtils {
    private static final SAXReader SAX_READER = new SAXReader();

    private XmlUtils() {
    }

    /**
     * 解析xml
     *
     * @param in
     * @return
     */
    public static Document parseXML(InputStream in) {
        Assert.notNull(in);

        try {
            return SAX_READER.read(in);
        } catch (DocumentException e) {
            throw new RuntimeException("XML解析发生错误");
        }
    }

    /**
     * xml 2 string
     *
     * @param document xml document
     * @return
     */
    public static String parseXMLToString(Document document) {
        Assert.notNull(document);

        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
        StringWriter writer = new StringWriter();
        XMLWriter xmlWriter = new XMLWriter(writer, format);
        try {
            xmlWriter.write(document);
            xmlWriter.close();
        } catch (IOException e) {
            throw new RuntimeException("XML解析发生错误");
        }
        return writer.toString();
    }

    /**
     * 获取元素下指定节点名的子元素集合
     *
     * @param element 元素
     * @param tagName 子元素
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<Element> getChildren(Element element, String tagName) {
        Assert.notNull(element);
        Assert.hasText(tagName);

        return element.elements(tagName);
    }

    /**
     * 获取元素下指定节点名的子元素
     *
     * @param element 元素
     * @param tagName 子元素
     * @return
     */
    public static Element getChild(Element element, String tagName) {
        Assert.notNull(element);
        Assert.hasText(tagName);

        return element.element(tagName);
    }

    /**
     * 获取元素下的全部子元素
     *
     * @param element 元素
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<Element> getChildren(Element element) {
        Assert.notNull(element);

        return element.elements();
    }

    /**
     * 获取元素中包含的内容
     *
     * @param element 元素
     * @return
     */
    public static String getElementBody(Element element) {
        Assert.notNull(element);

        return element.getTextTrim();
    }

    /**
     * 获取元素属性
     *
     * @param element 元素
     * @param attr    属性名
     * @return
     */
    public static String getElementAttr(Element element, String attr) {
        Assert.notNull(element);
        Assert.hasText(attr);

        return element.attributeValue(attr);
    }

    /**
     * 获取元素下子元素的属性
     *
     * @param element 元素
     * @param attr    属性名
     * @return
     */
    public static String getChildAttr(Element element, String tagName, String attr) {
        Assert.notNull(element);
        Assert.hasText(tagName);
        Assert.hasText(attr);

        Element child = element.element(tagName);
        return child.attributeValue(attr);
    }

    /**
     * 获取指定路径节点
     *
     * @param root 根
     * @param path 路径（相对于给定的根元素）,eg: root/node1/node2...
     * @return
     */
    public static Element getElement(Element root, String path) {
        Assert.notNull(root);
        Assert.hasText(path);

        String[] paths = StringUtils.split(path, "/");
        Element element = null;
        for (String p : paths) {
            element = getChild(root, p);
        }

        return element;
    }

    /**
     * 获取指定路径节点的属性
     *
     * @param root        根
     * @param pathAndAttr 路径（相对于给定的根元素）,eg: root/node1/node2/attr3:attrName
     * @return
     */
    public static String getAttrValue(Element root, String pathAndAttr) {
        Assert.notNull(root);
        Assert.hasText(pathAndAttr);

        String[] temp = StringUtils.split(pathAndAttr, ":");
        if (temp.length != 2) {
            return StringUtils.EMPTY;
        }

        String path = temp[0];
        String attr = temp[1];

        Element element = getElement(root, path);
        if (element == null) {
            return StringUtils.EMPTY;
        }

        return getElementAttr(element, attr);
    }

    /**
     * 判断元素是否含有子节点
     *
     * @param ele      元素
     * @param nodeName 子节点
     * @return true/false
     */
    public static boolean hasChildNode(Element ele, String nodeName) {
        List<Element> eles = getChildren(ele, nodeName);
        return eles != null && eles.size() > 0;
    }

    /**
     * 判断元素是否含有属性
     *
     * @param ele      元素
     * @param attrName 属性名
     * @return true/false
     */
    public static boolean hasAttr(Element ele, String attrName) {
        return ele.attributeValue(attrName) != null;
    }

    public static Map<String, String> parse(InputStream body) {
        try {
            InputSource source = new InputSource(new InputStreamReader(body, "UTF-8"));
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            org.w3c.dom.Document document = dbf.newDocumentBuilder().parse(source);
            NodeList childNodes = document.getElementsByTagName("xml").item(0).getChildNodes();
            Map<String, String> requestMap = new HashMap<>(childNodes.getLength());
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node item = childNodes.item(i);
                requestMap.put(item.getNodeName(), item.getTextContent());
            }
            return requestMap;
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }

    public static String toXML(Object obj) {
        try {
            JAXBContext context = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            StringWriter writer = new StringWriter();
            marshaller.marshal(obj, writer);
            return writer.toString();
        } catch (JAXBException e) {
            e.printStackTrace();
            return "";
        }
    }
}
