/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License. 
 *  
 */

package org.apache.directory.shared.ldap.aci;


import java.io.StringReader;
import java.text.ParseException;
import java.util.Map;

import org.apache.directory.shared.i18n.I18n;
import org.apache.directory.shared.ldap.name.NameComponentNormalizer;
import org.apache.directory.shared.ldap.schema.normalizers.OidNormalizer;

import antlr.RecognitionException;
import antlr.TokenStreamException;


/**
 * A reusable wrapper around the antlr generated parser for an ACIItem as
 * defined by X.501. This class enables the reuse of the antlr parser/lexer pair
 * without having to recreate them every time.
 * 
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev: 928938 $
 */
public class ACIItemParser
{
    /** the antlr generated parser being wrapped */
    private ReusableAntlrACIItemParser parser;

    /** the antlr generated lexer being wrapped */
    private ReusableAntlrACIItemLexer lexer;

    private final boolean isNormalizing;


    /**
     * Creates a ACIItem parser.
     */
    public ACIItemParser( Map<String, OidNormalizer> oidsMap )
    {
        this.lexer = new ReusableAntlrACIItemLexer( new StringReader( "" ) );
        this.parser = new ReusableAntlrACIItemParser( lexer );

        this.parser.init( oidsMap ); // this method MUST be called while we cannot do
        // constructor overloading for antlr generated parser
        this.isNormalizing = false;
    }


    /**
     * Creates a normalizing ACIItem parser.
     */
    public ACIItemParser( NameComponentNormalizer normalizer, Map<String, OidNormalizer> oidsMap )
    {
        this.lexer = new ReusableAntlrACIItemLexer( new StringReader( "" ) );
        this.parser = new ReusableAntlrACIItemParser( lexer );

        this.parser.setNormalizer( normalizer );
        this.parser.init( oidsMap ); // this method MUST be called while we cannot do
        // constructor overloading for antlr generated parser
        this.isNormalizing = true;
    }


    /**
     * Initializes the plumbing by creating a pipe and coupling the parser/lexer
     * pair with it. param spec the specification to be parsed
     */
    private synchronized void reset( String spec )
    {
        StringReader in = new StringReader( spec );
        this.lexer.prepareNextInput( in );
        this.parser.resetState();
    }


    /**
     * Parses an ACIItem without exhausting the parser.
     * 
     * @param spec
     *            the specification to be parsed
     * @return the specification bean
     * @throws ParseException
     *             if there are any recognition errors (bad syntax)
     */
    public synchronized ACIItem parse( String spec ) throws ParseException
    {
        ACIItem aCIItem = null;

        if ( spec == null || spec.trim().equals( "" ) )
        {
            return null;
        }

        reset( spec ); // reset and initialize the parser / lexer pair

        try
        {
            aCIItem = this.parser.wrapperEntryPoint();
        }
        catch ( TokenStreamException e )
        {
            throw new ParseException( I18n.err( I18n.ERR_00004, spec, e.getLocalizedMessage() ), 0 );
        }
        catch ( RecognitionException e )
        {
            throw new ParseException( I18n.err( I18n.ERR_00004, spec, e.getLocalizedMessage() ), e.getColumn() );
        }

        return aCIItem;
    }


    /**
     * Tests to see if this parser is normalizing.
     * 
     * @return true if it normalizes false otherwise
     */
    public boolean isNormizing()
    {
        return this.isNormalizing;
    }
}
