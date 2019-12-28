package org.andrea.jockey.predict;

public class FirstFourStrategy {
    int [] seq1;
    int [] seq2;
    int [] fixed;
    String name;
    boolean twoParts;
    int cntFromSeq1;

    public int getCntFromSeq1() {
        return cntFromSeq1;
    }

    public int[] getSeq1() {
        return seq1;
    }

    public int[] getSeq2() {
        return seq2;
    }

    public int[] getFixed() { return fixed; }

    public String getName() {
        return name;
    }

    public boolean isTwoParts() {
        return twoParts;
    }



    public FirstFourStrategy(int[] seq){
        StringBuilder sb= new StringBuilder();
        sb.append("{");
        for(int i: seq){
            sb.append(i).append("~");
        }
        sb.append("}");
        this.name= sb.toString();
        this.seq1=seq;
        this.twoParts=false;
    }
    public FirstFourStrategy(int[] seq1,int[] seq2, int cntFromSeq1){
        this.seq1=seq1;
        this.seq2=seq2;
        this.twoParts=true;
        this.cntFromSeq1=cntFromSeq1;
        StringBuilder sb= new StringBuilder();
        sb.append("{");
        for(int i: seq1){
            sb.append(i).append("~");
        }
        sb.append("}/").append(cntFromSeq1);
        sb.append("{");
        for(int i: seq2){
            sb.append(i).append("~");
        }
        sb.append("}/").append(4-cntFromSeq1);
        this.name= sb.toString();

    }
    public FirstFourStrategy(int[] seq1,int[] seq2, int [] fixed,int cntFromSeq1){

        this.seq1=seq1;
        this.seq2=seq2;
        this.fixed = fixed;
        this.twoParts=true;
        this.cntFromSeq1=cntFromSeq1;


        StringBuilder sb= new StringBuilder();
        sb.append("[");
        for(int i: fixed){
            sb.append(i).append("~");
        }
        sb.append("]");
        sb.append("{");
        for(int i: seq1){
            sb.append(i).append("~");
        }
        sb.append("}/").append(cntFromSeq1);
        sb.append("+{");
        for(int i: seq2){
            sb.append(i).append("~");
        }
        sb.append("}/").append(3-cntFromSeq1);


    }
}
