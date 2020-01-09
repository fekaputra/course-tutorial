package id.semantics.implementation;

import id.semantics.helper.Tutorial;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.vocabulary.*;
import org.eclipse.rdf4j.query.*;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;

import static id.semantics.helper.Utility.*;

public class HelloRDF4J implements Tutorial {

    private static final Logger log = LoggerFactory.getLogger(HelloRDF4J.class);

    private Repository repo;

    public HelloRDF4J() {
        createRepository();
    }

    @Override public void createRepository() {
        //        repo = new SailRepository(new MemoryStore());
        //        repo = new SailRepository(new MemoryStore(new File("storage/store.ttl")));
        //        repo = new SailRepository(new NativeStore(new File("storage/store")));
        repo = new HTTPRepository(REMOTE_SPARQL_ENDPOINT, REMOTE_REPO_ID);
        repo.init();
    }

    @Override public void createInstances() {

        ValueFactory valueFactory = repo.getValueFactory();
        IRI john = valueFactory.createIRI(NS_EXAMPLE, "john");

        try (RepositoryConnection conn = repo.getConnection()) {
            conn.add(john, RDF.TYPE, FOAF.PERSON);
            conn.add(john, RDFS.LABEL, valueFactory.createLiteral("john"));
        }
    }

    @Override public void modifyInstances() {

        ValueFactory valueFactory = repo.getValueFactory();
        IRI john = valueFactory.createIRI(NS_EXAMPLE, "john");

        try (RepositoryConnection conn = repo.getConnection()) {
            conn.remove(john, RDFS.LABEL, null);
            conn.add(john, RDFS.LABEL, valueFactory.createLiteral("John Lennon"));
        }

    }

    @Override public void iteratingRdfData() {

        try (RepositoryConnection conn = repo.getConnection()) {
            RepositoryResult<Statement> statements = conn.getStatements(null, null, null);
            for (int i = 0; i < 10; i++) {
                if (statements.hasNext()) {
                    Statement statement = statements.next();
                    System.out.println(statement.getSubject());
                    System.out.println("  " + statement.getPredicate());
                    System.out.println("    " + statement.getObject());
                }
            }
        }
    }

    @Override public void selectQuery(String queryFile) {

        try (RepositoryConnection conn = repo.getConnection()) {
            String queryString = readFile(queryFile, Charset.forName("UTF-8"));
            TupleQuery query = conn.prepareTupleQuery(queryString);
            TupleQueryResult queryResult = query.evaluate();

            while (queryResult.hasNext()) {
                BindingSet bindings = queryResult.next();
                System.out.println(bindings);
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @Override public void constructQuery(String queryFile) {

        try (RepositoryConnection conn = repo.getConnection()) {
            String queryString = readFile(queryFile, Charset.forName("UTF-8"));
            GraphQuery query = conn.prepareGraphQuery(queryString);
            GraphQueryResult queryResult = query.evaluate();

            Model model = QueryResults.asModel(queryResult);
            Rio.write(model, System.out, RDFFormat.TURTLE);

        } catch (IOException e) {
            log.error(e.getMessage());
        }

    }

    @Override public void loadRdfFile(String inputFile) throws IOException {
        FileReader reader = new FileReader(inputFile);
        Model model = Rio.parse(reader, NS_EXAMPLE, RDFFormat.TURTLE);
        try (RepositoryConnection conn = repo.getConnection()) {
            conn.add(model);
        }

    }

    @Override public void writeRdfFile(String outputFile) throws IOException {
        FileWriter fileWriter = new FileWriter(outputFile);
        try (RepositoryConnection conn = repo.getConnection()) {

            RepositoryResult<Statement> statements = conn.getStatements(null, null, null);

            Model model = QueryResults.asModel(statements);
            model.setNamespace("rdf", RDF.NAMESPACE);
            model.setNamespace("rdfs", RDFS.NAMESPACE);
            model.setNamespace("owl", OWL.NAMESPACE);
            model.setNamespace("foaf", FOAF.NAMESPACE);
            model.setNamespace("ex", NS_EXAMPLE);
            model.setNamespace("mv", NS_MOVIE);
            model.setNamespace("xsd", XMLSchema.NAMESPACE);

            Rio.write(model, fileWriter, RDFFormat.TURTLE);
        }
    }
}
