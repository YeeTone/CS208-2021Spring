package poj;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class P3487StableMatching {
    public static void main(String[] args) {
        FastReader fastReader=new FastReader(System.in);
        int t= fastReader.nextInt();
        for (int i = 0; i < t; i++) {
            int n= fastReader.nextInt();

            HashMap<Character,Man>man_Name_HashMap=new HashMap<Character,Man>();
            Man[]men=new Man[n];
            HashMap<Character,Woman>woman_Name_HashMap=new HashMap<Character,Woman>();

            for (int j = 0; j < n; j++) {
                char name=fastReader.next().charAt(0);
                men[j]=new Man(name);
                man_Name_HashMap.put(name,men[j]);
            }

            for (int j = 0; j < n; j++) {
                char name=fastReader.next().charAt(0);
                woman_Name_HashMap.put(name,new Woman(name));
            }

            for (int j = 0; j < n; j++) {
                String[]informs=fastReader.next().split(":");
                char manName=informs[0].charAt(0);
                char[] manPrefers=informs[1].toCharArray();

                HashMap<Integer,Woman>manPreferHashMap=new HashMap<Integer,Woman>();
                //Woman[]prefers=new Woman[n];
                for (int k = 0; k < n; k++) {
                    manPreferHashMap.put(k,woman_Name_HashMap.get(manPrefers[k]));
                }
                Man man=man_Name_HashMap.get(manName);
                man.preferenceList=manPreferHashMap;
            }

            for (int j = 0; j < n; j++) {
                String[]informs=fastReader.next().split(":");
                char womanName =informs[0].charAt(0);
                char[] womanPrefers =informs[1].toCharArray();

                HashMap<Man,Integer>womanRankHashMap=new HashMap<Man,Integer>();
                //Man[]prefers=new Man[n];
                for (int k = 0; k < n; k++) {
                    womanRankHashMap.put(man_Name_HashMap.get(womanPrefers[k]),k);
                }
                Woman woman=woman_Name_HashMap.get(womanName);
                woman.rankList=womanRankHashMap;
            }

            Queue<Man>manQueue=new LinkedList<Man>();
            for (int j = 0; j < n; j++) {
                manQueue.offer(men[j]);
            }

            while (!manQueue.isEmpty()){
                Man firstMan=manQueue.peek();
                Woman firstWoman=firstMan.preferenceList.get(firstMan.visitedIndex);
                if(!firstWoman.paired){
                    engage(firstMan,firstWoman);
                    manQueue.poll();
                }else{
                    int rankCur=firstWoman.rankList.get(firstWoman.currentPartner);
                    int rankNew=firstWoman.rankList.get(firstMan);

                    if (rankNew < rankCur) {
                        Man curPartner=firstWoman.currentPartner;
                        engage(firstMan,firstWoman);
                        curPartner.paired=false;
                        curPartner.currentPartner=null;
                        //curPartner.visitedIndex++;
                        manQueue.offer(curPartner);
                        manQueue.poll();
                    }else {
                        manQueue.poll();
                        manQueue.offer(firstMan);
                    }
                }
                firstMan.visitedIndex++;
            }
            Arrays.sort(men, new Comparator<Man>() {
                @Override
                public int compare(Man o1, Man o2) {
                    return o1.manName-o2.manName;
                }
            });

            for (int j = 0; j < n; j++) {
                System.out.println(men[j].manName+" "+men[j].currentPartner.womanName);
            }
            System.out.println();
        }

        fastReader.close();
    }
    private static void engage(Man man,Woman woman){
        man.paired=true;
        woman.paired=true;
        man.currentPartner=woman;
        woman.currentPartner=man;
    }

    private static class Man{
        private HashMap<Integer,Woman>preferenceList=new HashMap<Integer,Woman>();
        private final char manName;
        private boolean paired=false;
        private Woman currentPartner=null;
        private int visitedIndex;

        public Man(char name){
            this.manName=name;
        }
    }
    private static class Woman{
        private HashMap<Man,Integer>rankList=new HashMap<Man,Integer>();
        private final char womanName;
        private boolean paired=false;
        private Man currentPartner=null;

        public Woman(char name){
            this.womanName=name;
        }
    }
    private static class FastReader implements Closeable {
        private final BufferedReader br;
        private StringTokenizer st;

        public FastReader(InputStream in) {
            br = new BufferedReader(new InputStreamReader(in), 16384);
            eat("");
        }

        private void eat(String s) {
            st = new StringTokenizer(s);
        }

        public String nextLine() {
            try {
                return br.readLine();
            } catch (IOException e) {
                return null;
            }
        }

        public boolean hasNext() {
            while(!st.hasMoreTokens()) {
                String s = nextLine();
                if(s==null) return false;
                eat(s);
            }
            return true;
        }

        public String next() {
            hasNext();
            return st.nextToken();
        }

        public boolean nextBoolean(){
            return Boolean.parseBoolean(next());
        }


        public int nextInt() {
            return Integer.parseInt(next());
        }

        public long nextLong() {
            return Long.parseLong(next());
        }

        public float nextFloat(){
            return Float.parseFloat(next());
        }

        public double nextDouble(){
            return Double.parseDouble(next());
        }

        public BigInteger nextBigInteger(){
            return new BigInteger(next());
        }

        public BigDecimal nextBigDecimal(){
            return new BigDecimal(next());
        }

        public void close(){
            try{
                st=null;
                br.close();
            }catch (IOException e){
                e.printStackTrace();
                System.exit(1);
            }

        }
    }
}
