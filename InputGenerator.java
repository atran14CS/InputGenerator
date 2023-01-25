import java.util.*;

public class InputGenerator {

    public static void main (String[] args) {
        //creates arrays of 100by100 all the way up to 2000by2000 increment by 100.
        double mRankTotal = 0.0;
        double wRankTotal = 0.0;
        int size = 0;
        for (int n = 100; n <= 2000; n += 100) {
            int[][] mp = new int[n][n]; // preference lists for men
            int[][] wp = new int[n][n]; // preference lists for women
            // generate random preference list for men
            size += (n/2);
            for (int i = 0; i < n; i++) {
                List<Integer> list = new ArrayList<>();
                for (int j = 0; j < n; j++) {
                    list.add(j);
                }
                Collections.shuffle(list);
                for (int j = 0; j < n; j++) {
                    mp[i][j] = list.get(j);
                }
            }
            // generate random preference list for women
            for (int i = 0; i < n; i++) {
                List<Integer> list = new ArrayList<>();
                for (int j = 0; j < n; j++) {
                    list.add(j);
                }
                Collections.shuffle(list);
                for (int j = 0; j < n; j++) {
                    wp[i][j] = list.get(j);
                }
            }
            System.out.println("mRank: " + mRank(matching(mp, wp), mp));
            mRankTotal += mRank(matching(mp, wp), mp);
            System.out.println("wrank: " + wRank(matching(mp, wp), wp));
            wRankTotal += wRank(matching(mp, wp), wp);
        }
        System.out.println("The total size for m and w each is " + size);
        System.out.println("The total mRank is " + mRankTotal);
        System.out.println("The total mRank is " + wRankTotal);
        System.out.println("mRank total goodness Score: " + mRankTotal/(size));
        System.out.println("wRank total goodness Score: " + wRankTotal/(size));
    }

    //Matching algo used to find stable matching between m and w from mp and wp.
    //returns map of pairs.
    public static Map<Integer,Integer> matching (int[][]mp, int[][]wp) {
        Map<Integer, Integer> matches = new HashMap<>();
        Queue<Integer> q = new LinkedList<>();
        int index = 0;
        int counter = 0;
        for(int i = 0; i < mp.length; i++) {
            q.add(i);
        }
        //checks if there is still a free man and to not go past
        //the number of choices for one m.
        while(q.size() > 0 && index < 10) {
            int personM = q.remove();
            //checks when need to move on to second, third, fourth... choice
            if(counter == mp.length) {
                index++;
                counter = 0;
            }
            //possible woman that is being paired
            int pontenitalW = mp[personM][index];
            //Checks if there is another pair that has this woman
            if(matches.containsValue(pontenitalW)) {
                wPreference(pontenitalW, personM, wp, matches, q);
            } else {
                matches.put(personM, pontenitalW);
            }
            counter++;
        }
        return matches;
    }

    //When woman is already paired with another man have to find out which one the woman prefers more
    //This means going through potenitalW preference list. and Updating the pairing accordingly.
    private static void wPreference(int pontenitalW, int currentM, int[][]wp,
                                    Map<Integer, Integer> matches, Queue<Integer> q) {
        int assignedM = getKey(matches, pontenitalW);
        for(int i = 0; i < wp[pontenitalW].length; i++) {
            if(wp[pontenitalW][i] == assignedM) {
                q.add(currentM);
                break;
            } else if (wp[pontenitalW][i] == currentM) {
                matches.remove(assignedM);
                matches.put(currentM, pontenitalW);
                q.add(assignedM);
                break;
            }
        }
    }

    //This is used to get the man attach to the potentialW.
    public static int getKey (Map<Integer, Integer> matches, int value) {
        for (Map.Entry<Integer, Integer> entry : matches.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return -1;
    }

    //finding mRank
    public static double mRank(Map<Integer, Integer> matches, int[][]mp) {
        int counter = 1;
        double total = 0;
        for(int key : matches.keySet()) {
            int match = matches.get(key);
            for (int i = 0; i < mp[key].length; i++) {
                if (mp[key][i] == match) {
                    counter = 1;
                    break;
                }
                counter++;
                total += counter;
            }
        }
        return total / mp.length;
    }

    //finding w rank
    public static double wRank(Map<Integer, Integer> matching, int[][]wp) {
        double total = 0;
        int counter = 1;
        for(int value : matching.values()) {
            int match = getKey(matching, value);
            for (int i = 0; i < wp[value].length; i++) {
                if (wp[value][i] == match) {
                    counter = 1;
                    break;
                }
                counter++;
                total += counter;
            }
        }
        return total / wp.length;
    }
}

