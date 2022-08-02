package processor.memorysystem;

public class Cacheline {
    int data;
    int tag;
    int wordsPerLine;
    int word_size;

    public Cacheline(int wordsPerLine, int wordSize) {
        this.wordsPerLine  = wordsPerLine;
        this.word_size = wordSize;
        data = -1;
        tag = -1;
    }

    public int get_data() {
        return this.data;
    }

    public void set_data(int data) {
        this.data = data;
    }

    public int get_tag() {
        return this.tag;
    }

    public void set_tag(int tag) {
        this.tag = tag;
    }
}