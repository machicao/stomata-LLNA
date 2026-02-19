package Utils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 *
 * @author Jeaneth Machicao
 */
public class IO {

    private String directory;
    private String[] names;
    private String[] files;

    /**
     * Get the directory
     *
     * @return String directory Eg: /Users/jeaneth/
     */
    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String[] getNames() {
        return names;
    }

    public String[] getNames(String wildcard) {
        return filterRegex(names, wildcard);
    }

    private String[] filterRegex(String[] str, String wildcard) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < str.length; i++) {
            String regex = wildcard.replace("*", ".*?");//regular expression
            if (str[i].matches(regex)) {
                list.add(str[i]);
            }
        }
        String[] filtered = new String[list.size()];
        filtered = list.toArray(filtered);
        return filtered;
    }

    public void setNames(String[] names) {
        this.names = names;
    }

    public int getNFiles() {
        return names.length;
    }

    public IO(String path, final String ext) {
        File dir = new File(path);
        File file[] = null;
        FilenameFilter ff1 = new FilenameFilter() {
            @Override
            public boolean accept(File b, String name) {
                return name.endsWith(ext);
            }
        };
        FilenameFilter ff2 = new FilenameFilter() {
            @Override
            public boolean accept(File b, String name) {
                return b.isDirectory() && !name.contains("DS_Store");
            }
        };

        if (ext != null) {
            names = dir.list(ff1);
            file = dir.listFiles(ff1);

        } else {
            names = dir.list(ff2);
            file = dir.listFiles(ff2);
        }

        files = new String[file.length];
        for (int i = 0; i < file.length; i++) {
            files[i] = file[i].getAbsolutePath();
        }

        directory = path;

        //Arrays.sort(names);
        //Arrays.sort(files);
        //sort file names in ascending order
        Arrays.sort(names, new AlphanumComparator());
        Arrays.sort(files, new AlphanumComparator());
    }

    public String[] getFiles() {
        return files;
    }

    /**
     * Return the list of files, ordered alphanumerically, restricted by the
     * wildcard regular expresion: For example: *_k=4.*txt will return from a
     * directory all the files that match the expression
     *
     * @param wildcard
     * @return
     */
    public String[] getFiles(String wildcard) {
        return filterRegex(files, wildcard);
    }

    public static void mainX(String[] args) {
        //String path = "/Users/jeaneth/Dropbox/doutorado/programas/ComplexSystemsAnalysis/RedesDB/barabasi/";
        String path = "/Users/jeaneth/Dropbox/lifelikeSciReport/datasetContornosGeometricos/networks/circulo/";
        //IO io = new IO(path, null);//full path
        IO io = new IO(path, ".png");

        int nFiles = io.getNFiles();
        //String[] files = io.getFiles("*_k=4_*");
        String[] files = io.getFiles();
        String[] names = io.getNames();
        String directory = io.getDirectory();
        for (int i = 0; i < files.length; i++) {
            System.out.println(names[i]);
            //System.out.println(files[i]);
            //System.out.println(directory);
        }
    }

    public static void main2(String[] args) {
        String files[] = {
            new String("0.175.txt"),
            new String("0.025.txt"),
            new String("0.1.txt"),
            new String("0.4.txt"),
            new String("0.7.txt"),
            new String("0.25.txt"),
            new String("0.55.txt"),
            new String("0.325.txt"),
            new String("0.625.txt"),
            new String("0.775.txt"),
            new String("0.845.txt"),
            new String("0.925.txt"),
            new String("0.475.txt"),
            new String("p0-0.txt"),
            new String("p0-10.txt"),
            new String("p0-11.txt"),
            new String("p0-23.txt"),
            new String("p0-1.txt"),
            new String("p0-4.txt"),
            new String("p0-46.txt"),
            new String("p0-82.txt")
        };
        //Arrays.sort(files);
        //Arrays.sort(files, new AlphanumComparator());// nao ordena decimais
        Arrays.sort(files);//ordena decimais mas nao inteiros!
        String[] names = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            names[i] = files[i];
            System.out.println("" + names[i]);
        }
//        for (File mFile : files) {
//            System.out.println("" + mFile.getName());
//        }
    }
}

/**
 * This is an updated version with enhancements made by Daniel Migowski, Andre
 * Bogus, and David Koelle
 *
 * To convert to use Templates (Java 1.5+): - Change "implements Comparator" to
 * "implements Comparator<String>" - Change "compare(Object o1, Object o2)" to
 * "compare(String s1, String s2)" - Remove the type checking and casting in
 * compare().
 *
 * To use this class: Use the static "sort" method from the
 * java.util.Collections class: Collections.sort(your list, new
 * AlphanumComparator());
 */
class AlphanumComparator implements Comparator<String> {

    private Comparator<String> comparator = new NaturalComparator();

    public AlphanumComparator(Comparator<String> comparator) {
        this.comparator = comparator;
    }

    public AlphanumComparator() {

    }

    private boolean isDigit(char ch) {
        return ch >= 48 && ch <= 57;
    }

    /**
     * Length of string is passed in for improved efficiency (only need to
     * calculate it once) *
     */
    private String getChunk(String s, int slength, int marker) {
        StringBuilder chunk = new StringBuilder();
        char c = s.charAt(marker);
        chunk.append(c);
        marker++;
        if (isDigit(c)) {
            while (marker < slength) {
                c = s.charAt(marker);
                if (!isDigit(c)) {
                    break;
                }
                chunk.append(c);
                marker++;
            }
        } else {
            while (marker < slength) {
                c = s.charAt(marker);
                if (isDigit(c)) {
                    break;
                }
                chunk.append(c);
                marker++;
            }
        }
        return chunk.toString();
    }

    @Override
    public int compare(String s1, String s2) {

        int thisMarker = 0;
        int thatMarker = 0;
        int s1Length = s1.length();
        int s2Length = s2.length();

        while (thisMarker < s1Length && thatMarker < s2Length) {
            String thisChunk = getChunk(s1, s1Length, thisMarker);
            thisMarker += thisChunk.length();

            String thatChunk = getChunk(s2, s2Length, thatMarker);
            thatMarker += thatChunk.length();

            // If both chunks contain numeric characters, sort them numerically
            int result = 0;
            if (isDigit(thisChunk.charAt(0)) && isDigit(thatChunk.charAt(0))) {
                // Simple chunk comparison by length.
                int thisChunkLength = thisChunk.length();
                result = thisChunkLength - thatChunk.length();
                // If equal, the first different number counts
                if (result == 0) {
                    for (int i = 0; i < thisChunkLength; i++) {
                        result = thisChunk.charAt(i) - thatChunk.charAt(i);
                        if (result != 0) {
                            return result;
                        }
                    }
                }
            } else {
                result = comparator.compare(thisChunk, thatChunk);
            }

            if (result != 0) {
                return result;
            }
        }

        return s1Length - s2Length;
    }

    private static class NaturalComparator implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    }
}

