package mobi.monaca.framework.template;

import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;

/** This interface represent resource map for getting a template code. */
public interface TemplateResource {

    public boolean exists(String path);

    public Reader get(String path);

    /** Resolve relative path to absolute path. */
    public String resolve(String path, String from);

    /** Simple stub implementation. */
    public class SimpleString implements TemplateResource {

        protected HashMap<String, String> map = new HashMap<String, String>();

        public void put(String path, String template) {
            map.put(path, template);
        }

        @Override
        public boolean exists(String path) {
            return map.containsKey(path);
        }

        @Override
        public Reader get(String path) {
            if (!exists(path)) {
                throw new RuntimeException("unavailable path: " + path);
            }
            return new StringReader(map.get(path));
        }

        public String resolve(String path, String from) {
            return path;
        }
    }

}
