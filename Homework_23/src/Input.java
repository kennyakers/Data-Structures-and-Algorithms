
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class Input {

    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static Integer[] convertIntegers(String[] values, int lo, int hi) {
        Integer[] result = new Integer[values.length];
        for (int i = lo; i <= hi; i++) {
            result[i] = Integer.parseInt(values[i]);
        }
        return result;
    }

    public static Integer[] convertIntegers(String[] values) {
        return convertIntegers(values, 0, values.length - 1);
    }

    public static Long[] convertLongs(String[] values, int lo, int hi) {
        Long[] result = new Long[values.length];
        for (int i = lo; i <= hi; i++) {
            result[i] = Long.parseLong(values[i]);
        }
        return result;
    }

    public static Long[] convertLongs(String[] values) {
        return convertLongs(values, 0, values.length - 1);
    }

    public static Double[] convertDoubles(String[] values, int lo, int hi) {
        Double[] result = new Double[values.length];
        for (int i = lo; i <= hi; i++) {
            result[i] = Double.parseDouble(values[i]);
        }
        return result;
    }

    public static Double[] convertDoubles(String[] values) {
        return convertDoubles(values, 0, values.length - 1);
    }

    public static int[] convertInts(String[] values, int lo, int hi) {
        int[] result = new int[values.length];
        for (int i = lo; i <= hi; i++) {
            result[i] = Integer.parseInt(values[i]);
        }
        return result;
    }

    public static int[] convertInts(String[] values) {
        return convertInts(values, 0, values.length - 1);
    }

    public static String readString() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    public static Integer readInteger() {
        return Integer.parseInt(readString());
    }

    public static Long readLong() {
        return Long.parseLong(readString());
    }

    public static Double readDouble() {
        return Double.parseDouble(readString());
    }

    public static int readInt() {
        return readInteger();
    }

    public static String[] readStrings() {
        ArrayList<String> array = new ArrayList<>();
        String line = "";

        try {
            while ((line = reader.readLine()) != null) {
                array.add(line);
            }
        } catch (Exception e) {
            return null;
        }

        String[] result = new String[array.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = array.get(i);
        }
        return result;
    }

    public static Integer[] readIntegers() {
        return convertIntegers(readStrings());
    }

    public static Long[] readLongs() {
        return convertLongs(readStrings());
    }

    public static Double[] readDoubles() {
        return convertDoubles(readStrings());
    }

    public static int[] readInts() {
        return convertInts(readStrings());
    }

    public static int[] getInts(String[] args) {
        return (args.length > 0) ? convertInts(args) : readInts();
    }

    public static Integer[] getIntegers(String[] args) {
        return (args.length > 0) ? convertIntegers(args) : readIntegers();
    }

    public static Long[] getLongs(String[] args) {
        return (args.length > 0) ? convertLongs(args) : readLongs();
    }

    public static Double[] getDoubles(String[] args) {
        return (args.length > 0) ? convertDoubles(args) : readDoubles();
    }
}
