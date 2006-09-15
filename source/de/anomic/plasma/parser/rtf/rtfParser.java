//rtfParser.java 
//------------------------
//part of YaCy
//(C) by Michael Peter Christen; mc@anomic.de
//first published on http://www.anomic.de
//Frankfurt, Germany, 2005
//
//this file is contributed by Martin Thelian
//last major change: 16.05.2005
//
//This program is free software; you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation; either version 2 of the License, or
//(at your option) any later version.
//
//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.
//
//You should have received a copy of the GNU General Public License
//along with this program; if not, write to the Free Software
//Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
//Using this software in any meaning (reading, learning, copying, compiling,
//running) means that you agree that the Author(s) is (are) not responsible
//for cost, loss of data or any harm that may be caused directly or indirectly
//by usage of this softare or this documentation. The usage of this software
//is on your own risk. The installation and usage (starting/running) of this
//software may allow other people or application to access your computer and
//any attached devices and is highly dependent on the configuration of the
//software which must be done by the user of the software; the author(s) is
//(are) also not responsible for proper configuration and usage of the
//software, even if provoked by documentation provided together with
//the software.
//
//Any changes to this file according to the GPL as documented in the file
//gpl.txt aside this file in the shipment you received can be done to the
//lines that follows this copyright notice here, but changes must not be
//done inside the copyright notive above. A re-distribution must contain
//the intact and unchanged copyright notice.
//Contributions and changes to the program code must be marked as such.

package de.anomic.plasma.parser.rtf;

import java.io.InputStream;
import de.anomic.net.URL;
import java.util.Hashtable;

import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.rtf.RTFEditorKit;

import de.anomic.plasma.plasmaParserDocument;
import de.anomic.plasma.parser.AbstractParser;
import de.anomic.plasma.parser.Parser;
import de.anomic.plasma.parser.ParserException;

public class rtfParser
extends AbstractParser
implements Parser {

    /**
     * a list of mime types that are supported by this parser class
     * @see #getSupportedMimeTypes()
     */    
    public static final Hashtable SUPPORTED_MIME_TYPES = new Hashtable();    
    static { 
        SUPPORTED_MIME_TYPES.put("application/rtf","rtf"); 
        SUPPORTED_MIME_TYPES.put("text/rtf","rtf");
    } 
    
    /**
     * a list of library names that are needed by this parser
     * @see Parser#getLibxDependences()
     */
    private static final String[] LIBX_DEPENDENCIES = new String[] {};    
    
	public rtfParser() {
		super(LIBX_DEPENDENCIES);
        parserName = "Rich Text Format Parser";  
	}

	public plasmaParserDocument parse(URL location, String mimeType, String charset,
			InputStream source) throws ParserException, InterruptedException {

        
		try {	
            DefaultStyledDocument doc = new DefaultStyledDocument();
            
            RTFEditorKit theRtfEditorKit = new RTFEditorKit();               
            theRtfEditorKit.read(source, doc, 0);            
            
            String bodyText = doc.getText(0, doc.getLength());
            
            plasmaParserDocument theDoc = new plasmaParserDocument(
                    location,
                    mimeType,
                    "UTF-8",
                    null,
                    ((bodyText.length() > 80)? bodyText.substring(0, 80):bodyText.trim()).
                        replaceAll("\r\n"," ").
                        replaceAll("\n"," ").
                        replaceAll("\r"," ").
                        replaceAll("\t"," "),
                    null,
                    null,
                    null,
                    bodyText.getBytes(),
                    null,
                    null);
            
            return theDoc;             
		}
		catch (Exception e) {			
            if (e instanceof InterruptedException) throw (InterruptedException) e;
			throw new ParserException("Unable to parse the rdf content. " + e.getMessage());
		}        
	}

	public java.util.Hashtable getSupportedMimeTypes() {
		return rtfParser.SUPPORTED_MIME_TYPES;
	}

	public void reset() {
        // Nothing todo here at the moment
	}

}
