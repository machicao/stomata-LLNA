package Utils.lempelziv;

import java.util.ArrayList;

public class LempelZiv {

    private static final double scaling_value_ = 0.0005d;

    public static double CalculateVectorInformation(ArrayList<Double> vector_values) {
        
        int num_elems = vector_values.size();
        StringBuilder sequence = new StringBuilder();
        boolean is_already_binary = true;
        for (int ind = 0; ind < num_elems; ++ind) {
            if (Math.abs(vector_values.get(ind) - 1) < 1e-11) {
                sequence.append("1");
            } else if (Math.abs(vector_values.get(ind)) < 1e-11) {
                sequence.append("0");
            } else {
                is_already_binary = false;
            }           
        }
        if (is_already_binary) {
            return CalculateVectorInformation(sequence.toString());
        }
        sequence = new StringBuilder();

        boolean entre = false;
        for (int ind = 1; ind < num_elems - 1; ++ind) {
            int acres = (int) Math.abs(Math.round((vector_values.get(ind) - vector_values.get(ind - 1)) / scaling_value_));
            if (vector_values.get(ind) > vector_values.get(ind - 1)) {
                sequence.setCharAt(acres, '1');
            } else {
                sequence.setCharAt(acres, '0');
            }            
            entre = true;
        }
        return (CalculateVectorInformation(sequence.toString()) * Math.log(num_elems)) / num_elems;
    }

    public static double CalculateVectorInformation(String sequence) {        
        SiblingTrie binary_trie = new SiblingTrie();
        int n = sequence.length();
        String seq_to_be_analyzed = "";
        int lempel_ziv_complexity = 0;
        boolean is_open = false;
        for (int ind = 0; ind < n; ++ind) {
            if (sequence.charAt(ind) == '0' || sequence.charAt(ind) == '1' || sequence.charAt(ind) == '2') {
                seq_to_be_analyzed += sequence.charAt(ind);
                if (binary_trie.SearchWord(seq_to_be_analyzed) == -1) {
                    binary_trie.AddWord(seq_to_be_analyzed);
                    System.out.print(seq_to_be_analyzed+"|");
                    seq_to_be_analyzed = "";
                    lempel_ziv_complexity++;
                    
                    is_open = false;                    
                } else {
                    is_open = true;                    
                }     
                
            } else {
                System.err.println("String sequence must contain only 0 and 1 digits, ex: 100010011");
            }
        }
        //System.out.print("lempel_ziv_complexity: " + lempel_ziv_complexity + " " + is_open);
        //System.out.print("lempel_ziv_complexity: " + lempel_ziv_complexity +"\t");
        int val = is_open == true ? 1 : 0;
        // C * log_a(n)/n = C *(log(n)/log(a)*n)        
        return (lempel_ziv_complexity + val) * Math.log(n) / (n);//Rayner usado en LLNA
        
 

    }
 
    
    public static void main1(String[] args) {
        
        System.out.println("Lempel-ziv: " + LempelZiv.CalculateVectorInformation("1"));
        System.out.println("Lempel-ziv: " + LempelZiv.CalculateVectorInformation("11"));
        System.out.println("Lempel-ziv: " + LempelZiv.CalculateVectorInformation("10"));
        System.out.println("Lempel-ziv: " + LempelZiv.CalculateVectorInformation("101"));        
        System.out.println("Lempel-ziv: " + LempelZiv.CalculateVectorInformation("11100010101010111"));
        System.out.println("Lempel-ziv: " + LempelZiv.CalculateVectorInformation("0101010100101010101010100101110100101"));
        System.out.println("Lempel-ziv: " + LempelZiv.CalculateVectorInformation("111000101010101110101010100101010101010100101110100101"));
        System.out.println("Lempel-ziv: " + LempelZiv.CalculateVectorInformation("11100010101010111010101010010101010101010010111010010100101001000100010100100101010000101"));
        System.out.println("Lempel-ziv: " + LempelZiv.CalculateVectorInformation("1110001010101101010101010100101110100101000111010101010010101010101010010111010010100101001000100010100100101010000101"));
//        Lempel-ziv: 1.0
//        Lempel-ziv: 2.0
//        Lempel-ziv: 2.0
//        Lempel-ziv: 3.0
//        Lempel-ziv: 9.0
//        Lempel-ziv: 12.0
//        Lempel-ziv: 17.0
    }
 
    public static void mainX(String[] args) {
        System.out.println("Lempel-Ziv: " + LempelZiv.CalculateVectorInformation("00010110101"));  
        
    }
 

    public static int[] fromIntToByte(int valor) { // Esta funcion es usada cuando el valor del Byte fue un entero
        int SIZEBYTE = 8;
        int[] bits = new int[SIZEBYTE];
        //value = valor; // esto aveces no conviene pq luego en bits cambia de valor
        int ascii = valor;
        String mybyte = Integer.toBinaryString(ascii);

        int size = mybyte.length();
        int j = 0;
        for (int i = SIZEBYTE - 1; i >= 0; i--) {
            if (j < size) {
                bits[i] = String.valueOf(mybyte.charAt(size - 1 - j)).equals("1") ? 1 : 0;
            } else {
                break;
            }
            j++;
        }
        return bits;
    }

    public static String fromIntToStringByte(int valor) {
        int[] bytes = fromIntToByte(valor);
        int SIZEBYTE = bytes.length;
        String byteString = "";
        for (int j = 0; j < SIZEBYTE; j++) {

            String val = bytes[j] == 1 ? "1" : "0";
            byteString += val;

        }
        return byteString;
    }
}
