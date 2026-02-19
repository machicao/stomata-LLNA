package Utils.lempelziv;


public class SiblingTrie {

    Node root_ptr;
    int number_of_nodes_;
    int max_depth_;
    int average_depth_;

    public SiblingTrie() {
        root_ptr = new Node('*');
        number_of_nodes_ = 0;
        max_depth_ = 0;
        average_depth_ = 0;
    }

   private Node AddNodeToBinaryTree(Node start, char character) {
        while (true) {
            if (character < start.data()) {
                if (start.left_sibling() == null) {
                    Node temp = new Node(character);
                    start.set_left_sibling(temp);
                    ++number_of_nodes_;
                    //std::cout << "Left: " << character << std::endl;
                    return start.left_sibling();
                } else {
                    start = start.left_sibling();
                }
            } else if (character > start.data()) {
                if (start.right_sibling() == null) {
                    Node temp = new Node(character);
                    start.set_right_sibling(temp);
                    ++number_of_nodes_;
                    //std::cout << "Right: " << character << std::endl;
                    return start.right_sibling();
                } else {
                    start = start.right_sibling();
                }
            } else {
                return start;
            }
        }
        //return null;
    }

    private Node FindNodeInBinaryTree(Node start, char character) {
        while (true) {
            if (character < start.data()) {
                if (start.left_sibling() == null) {
                    return null;
                } else {
                    start = start.left_sibling();
                }
            } else if (character > start.data()) {
                if (start.right_sibling() == null) {
                    return null;
                } else {
                    start = start.right_sibling();
                }
            } else {
                return start;
            }
        }
        // return null;
    }

    public int AddWord(String word, int flag_type, int lower_limit_index, int upper_limit_index) {
        assert (upper_limit_index <= word.length() && lower_limit_index <= upper_limit_index && lower_limit_index >= 0);
        Node current = root_ptr;
        for (int i = lower_limit_index; i < upper_limit_index; i++) {
            if (current.next_node() == null) {
                //std::cout << "Next: " << word[i] << std::endl;
                Node temp = new Node(word.charAt(i));
                current.set_next_node(temp);
                ++number_of_nodes_;
                current = current.next_node();
            } else if (current.next_node().data() == word.charAt(i)) {
                current = current.next_node();
            } else {
                current = AddNodeToBinaryTree(current.next_node(), word.charAt(i));
            }
        }
        if (current.flag_type() == -1) {
            //std::cout << "flag_type: " << flag_type << std::endl;
            current.set_flag_type(flag_type);
            return flag_type;
        }
        return current.flag_type();
    }

    public int SearchWord(String word, int lower_limit_index, int upper_limit_index) {
        assert (upper_limit_index <= (int) word.length() && lower_limit_index <= upper_limit_index && lower_limit_index >= 0);
        Node current = root_ptr;
        for (int i = lower_limit_index; i < upper_limit_index; i++) {
            if (current.next_node() == null) {
                return -1;
            } else if (current.next_node().data() == word.charAt(i)) {
                current = current.next_node();
            } else {
                current = FindNodeInBinaryTree(current.next_node(), word.charAt(i));
                if (current == null) {
                    return -1;
                }
            }
        }
        return current.flag_type();
    }

//TODO : Not supported yet
    public double GetAverageDepth() {
        return -1.0;
    }

    public int GetNumberOfNodes() {
        return number_of_nodes_;
    }

//TODO : Not supported yet
    public int GetMaxDepth() {
        return -1;
    }

    public int AddWord(String word) {
        return AddWord(word, 0, 0, word.length());
    }

    public int AddWord(String word, int flag_type) {
        return AddWord(word, flag_type, 0, word.length());
    }

    public int AddWord(String word, int flag_type, int lower_limit_index) {
        return AddWord(word, flag_type, lower_limit_index, word.length());
    }

    public int SearchWord(String word) {
        return SearchWord(word, 0, word.length());
    }

    public int SearchWord(String word, int lower_limit_index) {
        return SearchWord(word, lower_limit_index, word.length());
    }

    private class Node {

        char data_;
        int flag_type_; // -1 if not found
        Node left_sibling_;
        Node right_sibling_;
        Node next_node_;

        public Node(char character) {
            data_ = character;
            flag_type_ = -1;
            left_sibling_ = null;
            right_sibling_ = null;
            next_node_ = null;
        }

        char data() {
            return data_;
        }

        int flag_type() {
            return flag_type_;
        }

        public void set_flag_type(int flag_type) {
            flag_type_ = flag_type;
        }

        public void set_left_sibling(Node left) {
            if (left != null) {
                assert (left.data() < data_);
            }
            left_sibling_ = left;
        }

        public void set_right_sibling(Node right) {
            if (right != null) {
                assert (right.data() > data_);
            }
            right_sibling_ = right;
        }

        public void set_next_node(Node next) {
            next_node_ = next;
        }

        public Node left_sibling() {
            return left_sibling_;
        }

        public Node right_sibling() {
            return right_sibling_;
        }

        public Node next_node() {
            return next_node_;
        }
    }

}
