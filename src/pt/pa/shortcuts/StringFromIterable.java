package pt.pa.shortcuts;

public class StringFromIterable<T> {
    public String delimit(String delimiter, Iterable<T> iterable) {
        StringBuilder sb = new StringBuilder();

        for (T elem: iterable) {
            sb.append(delimiter + elem);
        }

        return sb.substring(delimiter.length());
    }
}