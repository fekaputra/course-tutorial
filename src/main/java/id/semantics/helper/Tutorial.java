package id.semantics.helper;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface Tutorial {

    public void createRepository();

    public void createInstances();

    public void modifyInstances();

    public void iteratingRdfData();

    public void selectQuery(String queryFile);

    public void constructQuery(String queryFile);

    public void loadRdfFile(String inputFile) throws FileNotFoundException, IOException;

    public void writeRdfFile(String outputFile) throws FileNotFoundException, IOException;

}
