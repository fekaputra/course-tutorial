from rdflib import Graph, URIRef, BNode, Literal
from rdflib.namespace import FOAF, RDF

import owlrl 
import pprint
import os

example_ns = "http://semantics/id/ns/example#"

# -- create repository
g = Graph()
g.bind("foaf", FOAF)
g.bind("ex", example_ns)

# -- create instance
john = URIRef(example_ns + "john")
g.add((john, RDF.type, FOAF.Person)) 
g.add((john, FOAF.name, Literal("John")))

# -- modify instance
g.set((john, FOAF.name, Literal("John Doe")))

# -- iterating RDF data / print all triples
for stmt in g:
    pprint.pprint(stmt)

# -- load ontology + instances (via URL)
# g.parse("http://semantics.id/ns/example/film")

# -- load ontology (file)
g.parse("input/graph/film.ttl")

# -- load instances (file)
g.parse("input/graph/film-instances.ttl")

# -- load ontology with deductive closure - RDFS only
owlrl.DeductiveClosure(owlrl.RDFS_Semantics).expand(g)

# -- load ontology with deductive closure - OWL2-RL + RDFS
# owlrl.DeductiveClosure(owlrl.RDFS_OWLRL_Semantics).expand(g)

# -- QUERIES
for query in os.listdir('input/query'):
    q = open('input/query/' + query).read()
    result = g.query(q)
    print("--- "+query+" ---")
    for row in result:
        print(row)

# -- write into an RDF file
g.serialize(format="json-ld", destination="export.jsonld")
g.serialize(format="pretty-xml", destination="export.xml")
g.serialize(format="turtle", destination="export.ttl")
