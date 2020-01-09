package id.semantics;

import id.semantics.helper.Tutorial;
import id.semantics.implementation.HelloJena;

import java.io.IOException;

import static id.semantics.helper.Utility.*;

public class Main {

    public static void main(String[] args) throws IOException {

        Tutorial tutorial = new HelloJena();
        //        Tutorial tutorial = new HelloRDF4J();

        tutorial.createInstances();
        tutorial.modifyInstances();

        tutorial.loadRdfFile(INPUT_FILE);
        tutorial.loadRdfFile(INSTANCE_FILE);

        tutorial.selectQuery(Q1_SELECT);
        tutorial.selectQuery(Q2_SELECT);
        tutorial.constructQuery(Q3_CONSTRUCT);
        tutorial.writeRdfFile(OUTPUT_FILE);
        tutorial.iteratingRdfData();
    }
}
