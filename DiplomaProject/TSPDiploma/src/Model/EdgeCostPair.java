/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author Krzysztof
 */
  
    public class EdgeCostPair
    {
        private int fromIndex;
        private int toIndex;
        private float cost;
        public int getfomIndex () {return fromIndex;}
        public int gettoIndex () {return toIndex;}
        public float getCost(){return cost;}
        public EdgeCostPair(int from, int to, float cost)
        {
            this.fromIndex = from;
            this.toIndex = to;
            this.cost = cost;
        }
        public void setCost(float cost){this.cost = cost;}
        public void setfromIndex(int index){this.fromIndex = index;}
        public void settoIndex(int index){this.toIndex = index;}
    }