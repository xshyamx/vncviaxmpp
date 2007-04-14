package org.codehaus.mojo.izpack;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.izforge.izpack.ant.IzPackTask;
import com.izforge.izpack.compiler.CompilerConfig;

/**
 * Build an IzPack installer
 * 
 * TODO it'd be nice to create a default install.xml file if it does not exist, or maybe a different goal
 * @goal izpack
 * @phase package
 * @requiresDependencyResolution package
 * @version $Id:  $
 * @author Miguel Griffa
 */
public class IzPackMojo
    extends AbstractMojo
{

    /**
     * The izpack default configuration file
     * @parameter expression="${project.basedir}/src/izpack/install.xml" default-value="${project.basedir}/src/izpack/install.xml"
     * @optional
     */
    private String izpackConfig;

    /**
     * Name of the installer file to be generated
     * @parameter expression="${basedir}/target/${project.groupId}-${project.artifactId}-${project.version}-installer.jar"
     * @optional
     */
    private String installerFile;

    /**
     * Basedir for the the izpack project
     * @parameter expression="${project.basedir}" default-value="basedir"
     * @optional
     */
    private File basedir;

    /**
     * kind argument for izpack, standard or web
     * @parameter expression="standard" default-value="standard"
     * @optional
     */
    private String kind;

    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        IzPackTask task = new IzPackTask();
        task.setInput( izpackConfig );
        task.setOutput( installerFile );
        try
        {
            checkOutputDirectory( installerFile );
        }
        catch ( IOException e )
        {
            getLog().error( "Error verifying directory for installer file: " + installerFile, e );
        }
        task.setBasedir( basedir.getAbsolutePath() );

        // TODO it would be nice to pass properties to compiler, somehow
        try
        {
            buildInstaller();
        }
        catch ( Exception e )
        {
            throw new MojoExecutionException( "Error building installer", e );
        }
    }

    private void checkOutputDirectory( String file )
        throws IOException
    {
        File f = new File( file );
        File dir = f.getParentFile();
        if ( !dir.exists() )
        {
            if ( !dir.mkdirs() )
            {
                throw new IOException( "Could not create directory " + dir );
            }
        }
    }

    private void buildInstaller()
        throws Exception
    {
        // else use external configuration referenced by the input attribute
        CompilerConfig c = new CompilerConfig( this.izpackConfig, this.basedir.getCanonicalPath(), this.kind, this.installerFile );

        c.executeCompiler();

        if ( c.wasSuccessful() )
        {
            getLog().info( "IzPack compilation successfull" );
        }
        else
        {
            getLog().warn( "IzPack compilation ERROR" );
        }
    }

}
