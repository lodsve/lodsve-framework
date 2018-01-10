package lodsve.test.mock.memcached;

public interface MemcachedServer {

    void start(String host, int port);

    void clean();
}
