/*******************************************************************************
 * Copyright (c) 2014 - 2016 Andre Bossert.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Andre Bossert - initial API and implementation and/or initial documentation
 *******************************************************************************/

package de.anbos.eclipse.easyshell.plugin.types;

public enum Tokenizer {
    tokenizerUnknown(-1, "Unknown"),
	tokenizerNo(0, "No"),
	tokenizerYes(1, "Yes");
    // attributes
    private final int id;
    private final String name;
    // construct
    Tokenizer(int id, String mode) {
        this.id = id;
        this.name = mode;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public static Tokenizer getFromId(int id) {
    	Tokenizer ret = tokenizerUnknown;
        for(int i = 0; i < Tokenizer.values().length; i++) {
            if (Tokenizer.values()[i].getId() == id) {
                ret = Tokenizer.values()[i];
                break;
            }
        }
        return ret;
    }
    public static Tokenizer getFromName(String name) {
        Tokenizer ret = tokenizerUnknown;
        for(int i = 0; i < Tokenizer.values().length; i++) {
            if (Tokenizer.values()[i].getName().equals(name)) {
                ret = Tokenizer.values()[i];
                break;
            }
        }
        return ret;
    }
    public static Tokenizer getFromEnum(String name) {
        return Tokenizer.valueOf(name);
    }
};
