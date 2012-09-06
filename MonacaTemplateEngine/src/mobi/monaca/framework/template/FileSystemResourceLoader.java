package mobi.monaca.framework.template;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class FileSystemResourceLoader implements TemplateResource {

    @Override
    public boolean exists(String path) {
        return new File(path).exists();
    }

    @Override
    public Reader get(String path) {
        try {
            return new FileReader(new File(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new CompilerError();
        }
    }

    @Override
    public String resolve(String path, String from) {
        try {
            return new File(from + "/../" + path).getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
            throw new CompilerError();
        }
    }

    protected boolean isRelativePath(String path) {
        return path.startsWith(".");
    }

    protected String getAbsolutePath(String path) {
        return new File(path).getAbsolutePath();
    }

}
