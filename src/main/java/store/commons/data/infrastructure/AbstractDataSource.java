package store.commons.data.infrastructure;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public abstract class AbstractDataSource<T> {

    public abstract void initialize();

    protected abstract String getPath();

    protected abstract boolean isIgnoreColumnHeader();

    protected abstract boolean isPresentIdColumn();

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
            if (isIgnoreColumnHeader()) {
                br.readLine();
            }
            return br.lines().map(this::map).toList();
        } catch (IOException e) {
            throw new DataLoaderException(this.getPath() + " 파일을 읽어들이는 과정에서 오류가 발생하였습니다.");
        }
    }

    protected String[] parseToColumns(Class<T> clazz, String line) {
        String[] columns = line.split(",");
        int length = clazz.getDeclaredFields().length;
        if (isPresentIdColumn()) {
            --length;
        }
        if (columns.length != length) {
            throw new DataLoaderException(this.getPath() + " 파일에 올바르지 않은 형식으로 데이터가 저장되어 있습니다.");
        }
        return columns;
    }

    protected String getStringOrThrow(String property, String value) {
        if (value == null) {
            throw new DataLoaderException(property + "에 해당하는 값이 존재하지 않습니다.");
        }
        return value;
    }

    protected Integer getIntegerOrThrow(String property, String value) {
        if (value == null) {
            throw new DataLoaderException(property + "에 해당하는 값이 존재하지 않습니다.");
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new DataLoaderException(property + "에 해당하는 값이 숫자가 아닙니다.");
        }
    }

    protected Long getLongOrThrow(String property, String value) {
        if (value == null) {
            throw new DataLoaderException(property + "에 해당하는 값이 존재하지 않습니다.");
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new DataLoaderException(property + "에 해당하는 값이 숫자가 아닙니다.");
        }
    }
}
