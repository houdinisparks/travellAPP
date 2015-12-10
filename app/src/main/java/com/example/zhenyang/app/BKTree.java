package com.example.zhenyang.app;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yanyee on 11/7/2015.
 */
public class BKTree {

    private Node root;
    private HashMap<String, Integer> matches;   //Hashmap stores the each vertices as strings, and edges as LevenDist
    private LevenshteinDistance distance;
    private String bestTerm;


    //**************************CONSTRUCTORS***************************/
    public BKTree(LevenshteinDistance distance) {
        root = null;
        this.distance = distance;
    }

    /**
     * Create a new BKtree with a LevenshteinDistance
     */
    public BKTree() {
        root = null;
        this.distance = new LevenshteinDistance();
    }


    public static void simpleMethod() {
        System.out.println("simpleMethod Executed.");
    }
    /*public BKTree(String filename) {
        root = null;
        this.distance = new LevenshteinDistance();
        String line;

        try {
            FileReader filereader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(filereader);
            while ((line = bufferedReader.readLine()) != null) {
                this.add(line);
                System.out.println(line);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open file " + filename);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }*/


    //***************************METHODS******************************/

    //ADD STRING TERM AS NODE
    public void add(String term) {
        if (root != null) {
            root.add(term);
        } else {
            //System.out.println("Inside else");
            root = new Node(term);


        }

    }

    /**
     * Remove the complete content of the tree
     */
    public void removeAll() {
        if (root != null) root = null;
    }

    /**
     * This method will find all the close matching Objects within
     * a certain threshold.  For instance, for search for similar
     * strings, threshold set to 1 will return all the strings that
     * are off by 1 edit distance.
     *
     * @param searchObject
     * @param threshold
     * @return
     */

    //
    public HashMap<String, Integer> query(String searchObject, int threshold) {
        matches = new HashMap<String, Integer>();
        root.query(searchObject, threshold, matches);
        return matches;
    }

    /**
     * Attempts to find the closest match to the search term.
     *
     * @param term
     * @return the edit distance of the best match
     */
    public int find(String term) {
        return root.findBestMatch(term, Integer.MAX_VALUE);
    }

    /**
     * Attempts to find the closest match to the search term.
     *
     * @param term
     * @return a match that is within the best edit distance of the search term.
     */
    public String findBestWordMatch(String term) {
        root.findBestMatch(term, Integer.MAX_VALUE);
        return root.getBestTerm();
    }

    /**
     * Attempts to find the closest match to the search term.
     *
     * @param term
     * @return a match that is within the best edit distance of the search term.
     */
    public HashMap<String, Integer> findBestWordMatchWithDistance(String term) {
        int distance = root.findBestMatch(term, Integer.MAX_VALUE);
        HashMap<String, Integer> returnMap = new HashMap<String, Integer>();
        returnMap.put(root.getBestTerm(), distance);
        return returnMap;
    }

    private class Node {

        String term;
        HashMap<Integer, Node> children;

        public Node(String term) {
            this.term = term;
            children = new HashMap<Integer, Node>();
        }

        //ADD ANOTHER TERM/NODE AS A CHILD OF THIS TERM/NODE
        public void add(String term) {

            //System.out.println("LevenDist: 0");//gets the LevenDist between this term and term to add

            int score = distance.getDistance(term, this.term);
            //System.out.println("LevenDist: " + score);//gets the LevenDist between this term and term to add


            //********CHECKS IF THIS NODES CHILDREN CONTAINS THE TERM*************/
            Node child = children.get(score);                   //gets the Node of the score.
            if (child != null) {                                //if children contains score
                child.add(term);                                //recursive function, check the children of this term's children*/
            } else {
                children.put(score, new Node(term));
            }
        }

        public int findBestMatch(String term, int bestDistance) {

            int distanceAtNode = distance.getDistance(term, this.term);     //gets the LevelDist between this term and arg term

//			System.out.println("term = " + term + ", this.term = " + this.term + ", distance = " + distanceAtNode);

//			if(distanceAtNode == 1) {
//				return distanceAtNode;
//			}

            if (distanceAtNode < bestDistance) {
                bestDistance = distanceAtNode;
                bestTerm = this.term;
            }

            int possibleBest = bestDistance;

            for (Integer score : children.keySet()) {
                if (score < distanceAtNode + bestDistance) {
                    possibleBest = children.get(score).findBestMatch(term, bestDistance);
                    if (possibleBest < bestDistance) {
                        bestDistance = possibleBest;
                    }
                }
            }
            return bestDistance;
        }

        public String getBestTerm() {
            return bestTerm;
        }

        public void query(String term, int threshold, HashMap<String, Integer> collected) {
            int distanceAtNode = distance.getDistance(term, this.term);

            if (distanceAtNode == threshold) {
                collected.put(this.term, distanceAtNode);
                return;
            }

            if (distanceAtNode < threshold) {
                collected.put(this.term, distanceAtNode);
            }

            for (int score = distanceAtNode - threshold; score <= threshold + distanceAtNode; score++) {
                Node child = children.get(score);
                if (child != null) {
                    child.query(term, threshold, collected);
                }
            }
        }
    }
}

class LevenshteinDistance {

    //LevenshteinDistance(){};/**/

    public static int getDistance(String a, String b) {
        //recursive function comes from wikipedia
        //base case, when a.length || b.length == 0;

        if (a.length() == 0)
            return b.length();
        if (b.length() == 0)
            return a.length();
        if (a == b)
            return 0;
        int cost;

        int[] vect1 = new int[b.length() + 1];
        int[] vect2 = new int[b.length() + 1];

        for (int i = 0; i < vect1.length; i++) {
            vect1[i] = i;
        }

        for (int i = 0; i < a.length(); i++) {
            vect2[0] = i + 1;

            for (int j = 0; j < b.length(); j++) {
                cost = (a.toCharArray()[i] == b.toCharArray()[j]) ? 0 : 1;
                vect2[j + 1] = Math.min(vect2[j] + 1, Math.min(vect1[j] + cost, vect1[j + 1] + 1));
            }

            for (int j = 0; j < vect1.length; j++) {
                vect1[j] = vect2[j];
            }

        }

        return vect2[b.length()];
        //System.out.println(newa);
        //System.out.println(newb);


        //Use a BK Tree to come up a list of suggestions
        //Let Tolerance N = 2
    }
}




