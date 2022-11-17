package org.semsys;

import org.semsys.helper.Tutorial;
import org.semsys.helper.Utility;
import org.semsys.implementation.HelloJena;
import org.semsys.implementation.HelloRDF4J;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        Tutorial tutorialJena = new HelloJena();
        Tutorial tutorialRDF4J = new HelloRDF4J();

        runTutorial(tutorialJena);
        runTutorial(tutorialRDF4J);
    }

    private static void runTutorial(Tutorial tutorial) throws IOException {


        tutorial.createInstances();
        tutorial.modifyInstances();
        tutorial.iteratingRdfData();

        tutorial.loadRdfFile(Utility.INPUT_FILE);
        tutorial.loadRdfFile(Utility.INSTANCE_FILE);

        tutorial.selectQuery(Utility.Q1_SELECT);
        tutorial.selectQuery(Utility.Q2_SELECT);
        tutorial.constructQuery(Utility.Q3_CONSTRUCT);
        tutorial.writeRdfFile(Utility.OUTPUT_FILE);
    }
}
