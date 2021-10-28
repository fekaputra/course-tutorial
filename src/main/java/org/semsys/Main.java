package org.semsys;

import org.semsys.helper.Tutorial;
import org.semsys.implementation.HelloJena;
import org.semsys.helper.Utility;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        Tutorial tutorial = new HelloJena();
        //        Tutorial tutorial = new HelloRDF4J();

        tutorial.createInstances();
        tutorial.modifyInstances();

        tutorial.loadRdfFile(Utility.INPUT_FILE);
        tutorial.loadRdfFile(Utility.INSTANCE_FILE);

        tutorial.selectQuery(Utility.Q1_SELECT);
        tutorial.selectQuery(Utility.Q2_SELECT);
        tutorial.constructQuery(Utility.Q3_CONSTRUCT);
        tutorial.writeRdfFile(Utility.OUTPUT_FILE);
        tutorial.iteratingRdfData();
    }
}
