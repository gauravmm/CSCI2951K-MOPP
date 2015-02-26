/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.brown.csci2951k.util.xml;

/**
 *
 * @author Gaurav Manek
 */
public class XMLSerializingException extends RuntimeException {
    private static final long serialVersionUID = -578242856703570650L;

    public XMLSerializingException() {
    }

    public XMLSerializingException(String message) {
        super(message);
    }

    public XMLSerializingException(String message, Throwable cause) {
        super(message, cause);
    }

    public XMLSerializingException(Throwable cause) {
        super(cause);
    }
    
}
