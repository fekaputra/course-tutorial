package org.semsys.implementation;

import org.semsys.helper.Tutorial;
import org.semsys.helper.Utility;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import static org.semsys.helper.Utility.readFile;

public class HelloJena implements Tutorial {

    private static final Logger log = LoggerFactory.getLogger(HelloJena.class);

    //    private Model model;
    private OntModel ontModel;

    public HelloJena() {
        createRepository();
    }

    @Override public void createRepository() {
        //        model = ModelFactory.createDefaultModel();
        ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RDFS_INF);
    }

    @Override public void createInstances() {
        Individual individual = ontModel.createIndividual(Utility.NS_EXAMPLE + "john", FOAF.Person);
        individual.addLiteral(FOAF.name, "john");
    }

    @Override public void modifyInstances() {
        Individual individual = ontModel.getIndividual(Utility.NS_EXAMPLE + "john");
        ontModel.removeAll(individual, FOAF.name, null);
        individual.addLiteral(FOAF.name, "John Lennon");
    }

    @Override public void iteratingRdfData() {
        ontModel.listStatements().forEachRemaining(statement -> {
            System.out.println(statement);
        });
    }

    @Override public void selectQuery(String queryFile) {
        try {
            String queryString = readFile(queryFile, Charset.forName("UTF-8"));
            QueryExecution execution = QueryExecutionFactory.create(queryString, ontModel);
            ResultSet resultSet = execution.execSelect();
            ResultSetFormatter.out(resultSet);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @Override public void constructQuery(String queryFile) {
        try {
            String queryString = readFile(queryFile, Charset.forName("UTF-8"));
            QueryExecution execution = QueryExecutionFactory.create(queryString, ontModel);
            Model model = execution.execConstruct();
            RDFDataMgr.write(System.out, model, Lang.TURTLE);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @Override public void loadRdfFile(String inputFile) throws FileNotFoundException, IOException {
        RDFDataMgr.read(ontModel, inputFile);
    }

    @Override public void writeRdfFile(String outputFile) throws FileNotFoundException, IOException {
        FileOutputStream fos = new FileOutputStream(outputFile);
        RDFDataMgr.write(fos, ontModel, Lang.TURTLE);
    }
}
