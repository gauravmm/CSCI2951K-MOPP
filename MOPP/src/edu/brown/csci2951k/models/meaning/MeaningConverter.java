/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.models.meaning;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;
import ontopt.pen.SemanticNode;

/**
 *
 * @author Gaurav Manek
 */
public final class MeaningConverter {

    public static MeaningNode convert(SemanticNode s) {
        switch (s.getLabel()) {
            case "RAIZ":
                // RAIZ ::= NP
                return convert(s.getChildren().getFirst());

            case "SNP":
                // Terminal productions.
                return new TerminalNode(s.getTerminals().stream().map((n) -> n.getLabel()).collect(Collectors.toList()));

            case "NP":
                // NP ::= SNP
                // NP ::= SNP <&> PP

                MeaningNode rhs1 = convert(s.getChildren().getFirst());
                if (s.getChildCount() == 1) {
                    return rhs1;
                } else if (s.getChildCount() == 2) {
                    return parsePP(s.getChildren().get(1), rhs1);
                } else {
                    throw new RuntimeException();
                }

            default:
                throw new RuntimeException("Unrecognized Node Type");
        }
    }

    private static MeaningNode parsePP(SemanticNode rhs2, MeaningNode rhs1) {
        if (!rhs2.getLabel().equals("PP")) {
            throw new RuntimeException();
        }

        LinkedList<SemanticNode> children = rhs2.getChildren();
        if (rhs2.getChildren().getFirst().getLabel().equals("PREP_UNARY")) {
            if (rhs2.getChildCount() != 2) {
                throw new RuntimeException();
            }
            return new NonTerminalNode(getPPType(rhs2.getChildren().getFirst()), Arrays.asList(rhs1, convert(children.get(1))));
        } else if (rhs2.getChildren().getFirst().getLabel().equals("PREP_BINARY")) {
            LinkedList<SemanticNode> nps = rhs2.getNodes("NP");
            if (nps.size() != 2) {
                throw new RuntimeException();
            }

            return new NonTerminalNode(getPPType(rhs2.getChildren().getFirst()), Arrays.asList(rhs1, convert(nps.get(0)), convert(nps.get(1))));
        } else {
            throw new RuntimeException();
        }
    }

    private static Meanings.PP getPPType(SemanticNode get) {
        if (get.getLabel().equals("PREP_UNARY")) {
            LinkedList<SemanticNode> dir = get.getNodes("DIRECTION");
            String search = dir.isEmpty() ? get.getChildren().get(0).getLabel() : dir.getFirst().getChildren().getLast().getLabel();

            switch (search) {
                case "near":
                    return Meanings.PP.NEAR;

                case "left":
                    return Meanings.PP.LEFT;

                case "right":
                    return Meanings.PP.RIGHT;

                case "front":
                    return Meanings.PP.FRONT;

                default:
                    throw new RuntimeException();
            }
        } else if (get.getLabel()
                .equals("PREP_BINARY")) {
            if (get.getChildren().getFirst().getLabel().equals("between")) {
                return Meanings.PP.BETWEEN;
            }
            throw new RuntimeException("PREP_BINARY not BETWEEN");
        } else {
            throw new RuntimeException();
        }

    }
}
