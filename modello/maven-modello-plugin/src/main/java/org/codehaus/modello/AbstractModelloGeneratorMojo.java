package org.codehaus.modello;

/*
 * Copyright (c) 2004, Codehaus.org
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

import org.apache.maven.plugin.AbstractPlugin;
import org.apache.maven.plugin.PluginExecutionRequest;
import org.apache.maven.plugin.PluginExecutionResponse;
import org.apache.maven.project.MavenProject;

import org.codehaus.modello.ModelloParameterConstants;
import org.codehaus.modello.core.ModelloCore;

/**
 * @author <a href="mailto:trygvis@inamo.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public abstract class AbstractModelloGeneratorMojo
    extends AbstractPlugin
{
    protected abstract String getGeneratorType();
    
    protected boolean producesCompilableResult()
    {
        return true;
    }

    public void execute( PluginExecutionRequest request, PluginExecutionResponse response )
        throws Exception
    {
        // ----------------------------------------------------------------------
        //
        // ----------------------------------------------------------------------

        String basedir = (String) request.getParameter( "basedir" );

        String outputDirectory = (String) request.getParameter( "outputDirectory" );

        String model = (String) request.getParameter( "model" );

        String version = (String) request.getParameter( "version" );

        String packageWithVersion = (String) request.getParameter( "packageWithVersion" );

        ModelloCore modello = (ModelloCore) request.getParameter( "modelloCore" );

        // ----------------------------------------------------------------------
        //
        // ----------------------------------------------------------------------

        // Just pass a Map in here, no need to make properties up again.

        Properties parameters = new Properties();

        parameters.setProperty( ModelloParameterConstants.OUTPUT_DIRECTORY, outputDirectory );

        parameters.setProperty( ModelloParameterConstants.VERSION, version );

        parameters.setProperty( ModelloParameterConstants.PACKAGE_WITH_VERSION, packageWithVersion );

        modello.generate( modello.loadModel( new FileReader( new File( basedir, model ) ) ), getGeneratorType(), parameters );
        
        if(producesCompilableResult())
        {
            MavenProject project = (MavenProject) request.getParameter( "project" );

            if ( project != null )
            {
                project.addCompileSourceRoot( outputDirectory );
            }
            
        }
    }
}