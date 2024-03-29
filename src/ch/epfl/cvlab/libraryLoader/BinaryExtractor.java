package ch.epfl.cvlab.libraryLoader;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;


//TODO automatically extract all binaries within jar

public class BinaryExtractor {
  
    public static ArrayList<URI> extractBinaries(String[] args) throws URISyntaxException, ZipException, IOException{
        final URI uri;
        URI exe;
        ArrayList<URI> exes = new ArrayList<URI>();
        
        uri = getJarURI();
        for(int i = 0; i < args.length; i++){
          System.out.println("Looking for executable " + args[i] + " URI");
          exe = getFile(uri, args[i]);
          exes.add(exe);
          System.out.println("Executable URI: " +  exe.toString() + " found.");
        }
        return exes;
    }

    private static URI getJarURI()
        throws URISyntaxException
    {
        final ProtectionDomain domain;
        final CodeSource       source;
        final URL              url;
        final URI              uri;

        domain = BinaryExtractor.class.getProtectionDomain();
        source = domain.getCodeSource();
        url    = source.getLocation();
        uri    = url.toURI();

        return (uri);
    }

    private static URI getFile(URI    where,
                               String fileName)
        throws ZipException,
               IOException
    {
        final File location;
        final URI  fileURI;

        location = new File(where);

        // not in a JAR, just return the path on disk
        if(location.isDirectory())
        {
            //TODO fix this when not in jar
            fileURI = URI.create(where.toString() + fileName);
        }
        else
        {
            final ZipFile zipFile;

            zipFile = new ZipFile(location);

            try
            {
                fileURI = extract(zipFile, fileName);
            }
            finally
            {
                zipFile.close();
            }
        }

        return (fileURI);
    }

    private static URI extract(ZipFile zipFile,
                               String  fileName)
        throws IOException
    {
        final File         tempFile;
        final ZipEntry     entry;
        final InputStream  zipStream;
        OutputStream       fileStream;

        String tDir = System.getProperty("java.io.tmpdir");
        tempFile = new File(tDir + "/" + fileName);
        //TODO do I want to leave the binary traces?
        //tempFile.deleteOnExit();
        entry    = zipFile.getEntry(fileName);

        if(entry == null)
        {
            throw new FileNotFoundException("cannot find file: " + fileName + " in archive: " + zipFile.getName());
        }

        zipStream  = zipFile.getInputStream(entry);
        fileStream = null;

        try
        {
            final byte[] buf;
            int          i;

            fileStream = new FileOutputStream(tempFile);
            buf        = new byte[1024];
            i          = 0;

            while((i = zipStream.read(buf)) != -1)
            {
                fileStream.write(buf, 0, i);
            }
        }
        finally
        {
            close(zipStream);
            close(fileStream);
        }

        if(tempFile.setExecutable(true) == false) {
          System.err.println("Error setting executable permission. Do you have write permission?");
        }
        return (tempFile.toURI());
    }

    private static void close(Closeable stream)
    {
        if(stream != null)
        {
            try
            {
                stream.close();
            }
            catch(final IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }
}