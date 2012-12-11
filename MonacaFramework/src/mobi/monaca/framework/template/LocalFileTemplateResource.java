package mobi.monaca.framework.template;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;

import mobi.monaca.framework.template.TemplateResource;

public class LocalFileTemplateResource implements TemplateResource {
    
    public LocalFileTemplateResource() {
    }
    
    @Override
    public boolean exists(String path) {
        path = normalize(path);
        return new File(path).exists();
    }
    
    @Override
    public Reader get(String path) {
        path = normalize(path);
        try {
            return new FileReader(new File(path));
        } catch (Exception e) {
            return new StringReader("File not found: " + path);
        }
    }
    
    @Override
    public String resolve(String relativePath, String base) {
        base = normalize(base);
        try {
            return new File(base + "/../" + relativePath).getCanonicalPath();
        } catch (Exception e) {
            return base + "/../" + relativePath;
        }
    }
    
    protected String normalize(String path) {
        if (path.startsWith("file://")) {
            return path.substring(7);
        }
        return path;
    }
    
}
