package lodsve.core.io;

import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * zookeeper中读取InputStream.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 2018-1-2-0002 14:37
 */
public class InputStreamSerializer implements ZkSerializer {
    private static final Logger logger = LoggerFactory.getLogger(InputStreamSerializer.class);

    @Override
    public byte[] serialize(Object data) throws ZkMarshallingError {
        if (!(data instanceof InputStream)) {
            return new byte[0];
        }

        try {
            return IOUtils.toByteArray((InputStream) data);
        } catch (IOException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage(), e);
            }
            return new byte[0];
        }
    }

    @Override
    public Object deserialize(byte[] bytes) throws ZkMarshallingError {
        return new ByteArrayInputStream(bytes);
    }
}
