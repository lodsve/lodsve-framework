/*
 * Copyright (C) 2018  Sun.Hao
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

import org.apache.html.dom.HTMLDocumentImpl;
import org.cyberneko.html.parsers.DOMFragmentParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * html的工具类.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0
 * @date 2012-4-30 下午08:41:54
 */
public class HTMLUtils {

    private static final String[] DEFAULT_ATTRS_TO_REMOVE = {"id", "name"};
    private static final String[] DEFAULT_TAGS_TO_REMOVE = {"script", "style"};
    private static final String[] DEFAULT_TAGS_TO_NOT_REMOVE_TAG_NAME = {"object", "embed", "param"};
    private static final Logger logger = LoggerFactory.getLogger(HTMLUtils.class);
    private static final String DEFAULT_ENCODING = "utf-8";

    private static DOMFragmentParser fragmentParser = new DOMFragmentParser();
    private static final Pattern XML_PATTERN = Pattern.compile("(?s)<xml>(.*?)</xml>", Pattern.CASE_INSENSITIVE);
    private static final Pattern STYLE_PATTERN = Pattern.compile("(?s)<style[^>]*>(.*?)</style>", Pattern.CASE_INSENSITIVE);
    private static final Pattern SCRIPT_PATTERN = Pattern.compile("(?s)<script[^>]*>(.*?)</script>", Pattern.CASE_INSENSITIVE);
    private static final Pattern TAGS_PATTERN = Pattern.compile("(?s)<[^>]+>", Pattern.CASE_INSENSITIVE);
    private static final Pattern HTML_CHAR_PATTERN = Pattern.compile("&[\\w]+;", Pattern.CASE_INSENSITIVE);
    private static final Pattern SPACE_PATTERN = Pattern.compile("(?s)[\\s]{2,}");
    private static final Pattern FIST_LETTER_PATTERN = Pattern.compile("^[\\s]+");

    /**
     * 私有化构造器
     */
    private HTMLUtils() {
    }

    static {
        try {
            fragmentParser.setFeature("http://cyberneko.org/html/features/augmentations", true);
            // balance tags
            fragmentParser.setFeature("http://cyberneko.org/html/features/balance-tags", true);
            fragmentParser.setFeature("http://cyberneko.org/html/features/balance-tags/ignore-outside-content", false);
            // to lower case
            fragmentParser.setProperty("http://cyberneko.org/html/properties/names/elems", "lower");
            // fix the html
            fragmentParser.setFeature("http://cyberneko.org/html/features/document-fragment", false);
            // Specifies whether to ignore the character encoding specified within the <meta http-equiv='Content-Type' content='text/html;charset=...'> tag.
            fragmentParser.setFeature("http://cyberneko.org/html/features/scanner/ignore-specified-charset", false);
            fragmentParser.setFeature("http://cyberneko.org/html/features/insert-doctype", false);
        } catch (SAXNotRecognizedException | SAXNotSupportedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取html中前length个字符，剩余字符用suffix代替，去除html标签
     *
     * @param length 需要截取的长度
     * @param html   html文本
     * @param suffix 代替文本
     * @return 格式化后的文本
     */
    public static String getRawText(final int length, final String html, final String suffix) {
        if (StringUtils.isBlank(html)) {
            return StringUtils.EMPTY;
        }

        StringBuffer result = new StringBuffer(html.length());
        // remove XML tag.
        Matcher matcher = XML_PATTERN.matcher(html);

        while (matcher.find()) {
            matcher.appendReplacement(result, StringUtils.EMPTY);
        }

        matcher.appendTail(result);
        // style tag.
        matcher = STYLE_PATTERN.matcher(result.toString());
        result.setLength(0);

        while (matcher.find()) {
            matcher.appendReplacement(result, StringUtils.EMPTY);
        }

        matcher.appendTail(result);
        // script tag.
        matcher = SCRIPT_PATTERN.matcher(result.toString());
        result.setLength(0);

        while (matcher.find()) {
            matcher.appendReplacement(result, StringUtils.EMPTY);
        }

        matcher.appendTail(result);
        // all the tag.
        matcher = TAGS_PATTERN.matcher(result.toString());
        result.setLength(0);

        while (matcher.find()) {
            matcher.appendReplacement(result, StringUtils.EMPTY);
        }

        matcher.appendTail(result);
        // remove HTML characters, such as: &xxx;
        matcher = HTML_CHAR_PATTERN.matcher(result.toString());
        result.setLength(0);

        while (matcher.find()) {
            matcher.appendReplacement(result, StringUtils.EMPTY);
        }

        matcher.appendTail(result);
        // replace multi-space character with single space.
        matcher = SPACE_PATTERN.matcher(result.toString());
        result.setLength(0);

        while (matcher.find()) {
            matcher.appendReplacement(result, " ");
        }

        matcher.appendTail(result);
        // first letter.
        matcher = FIST_LETTER_PATTERN.matcher(result.toString());
        result.setLength(0);

        if (matcher.find()) {
            matcher.appendReplacement(result, StringUtils.EMPTY);
        }

        matcher.appendTail(result);

        if (length < 1 || result.length() < (length / 2)) {
            return result.toString();
        } else {
            StringBuffer ret = new StringBuffer(length + 2);
            int count = 0;

            for (int i = 0; i < result.length(); i++) {
                count++;
                char c = result.charAt(i);

                // for non-ASCCI characters.
                if (c < 0 || c > 128) {
                    count++;
                }

                if (count > length) {
                    ret.append(suffix);

                    break;
                }

                ret.append(c);
            }

            return ret.toString();
        }
    }

    /**
     * Get raw text from html.
     *
     * @param length Length limit.
     * @param html   HTML fragment.
     * @return Raw text within length limit.
     */
    public static String getRawText(final int length, final String html) {
        return getRawText(length, html, StringUtils.EMPTY);
    }

    public static String getText(String html) {
        return getText(html, DEFAULT_ENCODING);
    }

    /**
     * get pure text.
     *
     * @param html     html for check.
     * @param encoding encoding string
     * @return
     */
    public static String getText(String html, String encoding) {
        if (StringUtils.isBlank(html)) {
            return StringUtils.EMPTY;
        }

        if (StringUtils.isBlank(encoding)) {
            encoding = DEFAULT_ENCODING;
        }

        InputStream is = null;

        try {
            is = new ByteArrayInputStream(html.getBytes(encoding));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return getText(is, encoding);
    }

    /**
     * get pure text.
     *
     * @param is       {@link java.io.InputStream}
     * @param encoding encoding string
     * @return
     */
    public static String getText(InputStream is, String encoding) {
        if (is == null) {
            return StringUtils.EMPTY;
        }

        final StringBuffer result = new StringBuffer();

        loopGetText(createNode(is, encoding), result);

        return result.toString();
    }

    /**
     * create the node.
     *
     * @param is       {@link java.io.InputStream}
     * @param encoding
     * @return
     */
    private static Node createNode(InputStream is, String encoding) {
        DocumentFragment node = new HTMLDocumentImpl().createDocumentFragment();

        setFragmentParserEncoding(encoding);

        try {
            fragmentParser.parse(new InputSource(is), node);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return node;
    }

    /**
     * loop the child nodes.
     *
     * @param node currentNode.
     * @param sb   {@link StringBuffer}
     */
    private static void loopGetText(Node node, StringBuffer sb) {
        if (node.getNodeType() == Node.TEXT_NODE) {
            sb.append(node.getNodeValue());
        } else if ("script".equals(node.getNodeName().toLowerCase())) {
            sb.append("");
        } else if ("style".equals(node.getNodeName().toLowerCase())) {
            sb.append("");
        } else {
            NodeList nodeList = node.getChildNodes();

            if (nodeList != null) {
                for (int i = 0; i < nodeList.getLength(); i++) {
                    loopGetText(nodeList.item(i), sb);
                }
            }
        }
    }

    private static void setFragmentParserEncoding(String encoding) {
        if (StringUtils.isBlank(encoding)) {
            encoding = DEFAULT_ENCODING;
        }

        try {
            fragmentParser.setProperty("http://cyberneko.org/html/properties/default-encoding", encoding);
        } catch (SAXNotRecognizedException e) {
            e.printStackTrace();
        } catch (SAXNotSupportedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param html
     * @param attributesToRemove
     * @param encoding           encoding string
     * @return
     */
    public static String cleanHtml(String html, String[] tagsToRemove, String[] attributesToRemove, String encoding) {
        if (StringUtils.isBlank(html)) {
            return StringUtils.EMPTY;
        }

        if (StringUtils.isBlank(encoding)) {
            encoding = DEFAULT_ENCODING;
        }

        InputStream is = null;

        try {
            is = new ByteArrayInputStream(html.getBytes(encoding));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return cleanHtml(is, tagsToRemove, attributesToRemove, encoding);
    }

    /**
     * clean html and use default settings.
     *
     * @param html
     * @param encoding encoding string
     * @return
     */
    public static String cleanHtml(String html, String encoding) {
        return cleanHtml(html, null, null, encoding);
    }

    /**
     * clean html and use default settings.
     *
     * @param encoding encoding string
     * @param is       InputStream
     * @return 处理后的字符串
     */
    public static String cleanHtml(InputStream is, String encoding) {
        return cleanHtml(is, null, null, encoding);
    }

    /**
     * clean html.
     *
     * @param is
     * @param tagsToRemove
     * @param attributesToRemove
     * @param encoding           encoding string
     * @return
     */
    public static String cleanHtml(InputStream is, String[] tagsToRemove, String[] attributesToRemove,
                                   String encoding) {
        Node node = createNode(is, encoding);

        loopCleanNode(node, node, tagsToRemove, attributesToRemove);

        return convertNodeToString(node);
    }

    /**
     * convertNodeToString
     *
     * @param node
     * @return
     */
    private static String convertNodeToString(Node node) {
        try {
            Source source = new DOMSource(node);
            StringWriter stringWriter = new StringWriter();
            Result result = new StreamResult(stringWriter);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();

            transformer.transform(source, result);
            String tmp = stringWriter.getBuffer().toString();

            return StringUtils.remove(StringUtils.remove(StringUtils.remove(StringUtils.remove(tmp, "<HTML>"),
                    "<BODY>"), "</HTML>"), "</BODY>");
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        return StringUtils.EMPTY;
    }

    /**
     * loop the child nodes.
     */
    private static void loopCleanNode(Node oldNode, Node loopNode, String[] tagsToRemove, String[] attributesToRemove) {
        // remove tags
        if (loopNode.getNodeType() != Node.TEXT_NODE) {
            if (tagsToRemove == null) {
                tagsToRemove = DEFAULT_TAGS_TO_REMOVE;
            }

            for (int i = 0; i < tagsToRemove.length; i++) {
                if (tagsToRemove[i].equalsIgnoreCase(loopNode.getNodeName())) {
                    Node parent = loopNode.getParentNode();

                    if (parent != null) {
                        parent.removeChild(loopNode);
                    }

                    return;
                }
            }
        }

        // remove attributes

        boolean isCleanAttributeTag = true;
        for (int i = 0; i < DEFAULT_TAGS_TO_NOT_REMOVE_TAG_NAME.length; i++) {
            if (DEFAULT_TAGS_TO_NOT_REMOVE_TAG_NAME[i].equalsIgnoreCase(loopNode.getNodeName())) {
                isCleanAttributeTag = false;
                break;
            }
        }

        if (isCleanAttributeTag) {
            NamedNodeMap currentNodeAttrs = loopNode.getAttributes();
            if (currentNodeAttrs != null && currentNodeAttrs.getLength() > 0) {
                if (attributesToRemove == null) {
                    attributesToRemove = DEFAULT_ATTRS_TO_REMOVE;
                }

                for (int i = 0; i < attributesToRemove.length; i++) {
                    for (int j = 0; j < currentNodeAttrs.getLength(); j++) {
                        logger.debug("attrs" + j + ":  " + currentNodeAttrs.item(j).getNodeName());

                        if (attributesToRemove[i].equalsIgnoreCase(currentNodeAttrs.item(j).getNodeName())) {
                            currentNodeAttrs.removeNamedItem(currentNodeAttrs.item(j).getNodeName());
                        }
                    }
                }
            }
        }

        // loop clean node
        NodeList nodeList = loopNode.getChildNodes();

        if (nodeList != null) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                loopCleanNode(oldNode, nodeList.item(i), tagsToRemove, attributesToRemove);
            }
        }
    }

    public static void main(String[] args) {
        String test = "<div class=\"orig-content post-content\">我只中国人</div>";
        String result = HTMLUtils.cleanHtml(test, "utf-8");

        System.out.println(getRawText(3, test));
    }

    public static String escape(String src) {
        int i;
        char j;
        StringBuffer tmp = new StringBuffer();

        tmp.ensureCapacity(src.length() * 6);

        for (i = 0; i < src.length(); i++) {
            j = src.charAt(i);

            if (Character.isDigit(j) || Character.isLowerCase(j) || Character.isUpperCase(j)) {
                tmp.append(j);
            } else if (j < 256) {
                tmp.append("%");

                if (j < 16) {
                    tmp.append("0");
                }

                tmp.append(Integer.toString(j, 16));
            } else {
                tmp.append("%u");
                tmp.append(Integer.toString(j, 16));
            }
        }

        return tmp.toString();
    }

    public static String unescape(String src) {
        StringBuffer tmp = new StringBuffer();

        tmp.ensureCapacity(src.length());
        int lastPos = 0, pos = 0;
        char ch;

        while (lastPos < src.length()) {
            pos = src.indexOf("%", lastPos);

            if (pos == lastPos) {
                if (src.charAt(pos + 1) == 'u') {
                    ch = (char) Integer.parseInt(src.substring(pos + 2, pos + 6), 16);
                    tmp.append(ch);
                    lastPos = pos + 6;
                } else {
                    ch = (char) Integer.parseInt(src.substring(pos + 1, pos + 3), 16);
                    tmp.append(ch);
                    lastPos = pos + 3;
                }
            } else {
                if (pos == -1) {
                    tmp.append(src.substring(lastPos));
                    lastPos = src.length();
                } else {
                    tmp.append(src.substring(lastPos, pos));
                    lastPos = pos;
                }
            }
        }

        return tmp.toString();
    }

    public static String replaceHtmlEntities(String htmlStr) {
        return htmlStr.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&quot;", "\"");
    }

    /**
     * 提取出content中img内容.
     *
     * @param content 内容
     * @return 提取的图片src
     */
    public static List getImageUrls(String content) {
        if (StringUtils.isNotEmpty(content)) {
            List<String> resultList = new ArrayList<>();
            String regxp = "src=\"([^\"]+)\"";

            Pattern p = Pattern.compile(regxp);
            Matcher m = p.matcher(content);

            while (m.find()) {
                if (!m.group(1).contains("fckeditor")) {
                    // 获取被匹配的部分,并且去除fck的表情图片.
                    resultList.add(m.group(1));
                }
            }
            return resultList;
        }
        return Collections.EMPTY_LIST;
    }
}
