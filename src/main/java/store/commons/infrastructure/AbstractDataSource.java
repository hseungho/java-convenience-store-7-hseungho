package store.commons.infrastructure;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import store.commons.lang.DataLoaderException;

public abstract class AbstractDataSource<T> {

    public abstract void initialize();

    protected abstract String getPath();

    protected abstract boolean isSkipFirstLine();

    protected abstract T map(String line);

    private BufferedReader getBufferedReader() {
        try {
            File file = new File(this.getPath());
            if (!file.exists()) {
                throw new DataLoaderException(this.getPath() + " 위치에 파일을 찾을 수 없습니다.");
            }
            return new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        } catch (FileNotFoundException e) {
            throw new DataLoaderException(this.getPath() + " 위치에 파일을 찾을 수 없습니다.");
        }
    }

    protected List<T> read() {
        try {
            BufferedReader br = this.getBufferedReader();
            if (isSkipFirstLine()) {
                br.readLine();
            }
            return br.lines().map(this::map).toList();
        } catch (IOException e) {
            throw new DataLoaderException(this.getPath() + " 파일을 읽어들이는 과정에서 오류가 발생하였습니다.");
        }
    }
}
