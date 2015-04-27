/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.models.meaning;

/**
 *
 * @author Gaurav Manek
 */
public final class Meanings {

    public enum PP {

        LEFT(1), RIGHT(1), NEAR(1), FRONT(1), BETWEEN(2);

        private final int bindingNum;

        PP(int bindingNum) {
            this.bindingNum = bindingNum;
        }

        public boolean isUnary() {
            return this.bindingNum == 1;
        }

        public boolean isBinary() {
            return this.bindingNum == 2;
        }
        
        public int getNumChildren() {
            return this.bindingNum + 1;
        }
    }

}
