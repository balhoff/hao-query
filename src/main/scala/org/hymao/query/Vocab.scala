package org.hymao.query

import org.phenoscape.scowl.OWL._
import com.hp.hpl.jena.vocabulary.RDF
import com.hp.hpl.jena.vocabulary.RDFS
import com.hp.hpl.jena.vocabulary.OWL2
import com.hp.hpl.jena.graph.NodeFactory
import com.hp.hpl.jena.graph.Node
import org.semanticweb.owlapi.model.IRI

object Vocab {

  val bearer_of = ObjectProperty("http://purl.obolibrary.org/obo/BFO_0000053")
  val inheres_in = ObjectProperty("http://purl.obolibrary.org/obo/BFO_0000052")
  val has_part = ObjectProperty("http://purl.obolibrary.org/obo/BFO_0000051")
  val part_of = ObjectProperty("http://purl.obolibrary.org/obo/BFO_0000050")
  val denotes = ObjectProperty("http://purl.obolibrary.org/obo/IAO_0000219")
  val has_state = ObjectProperty("http://purl.obolibrary.org/obo/CDAO_0000184")
  val belongs_to_TU = ObjectProperty("http://purl.obolibrary.org/obo/CDAO_0000191")
  val in_subset = IRI.create("http://www.geneontology.org/formats/oboInOwl#inSubset")
  val attribute_slim = IRI.create("http://purl.obolibrary.org/obo/pato#attribute_slim")

  val rdfType = RDF.`type`.asNode
  val rdfsSubClassOf = RDFS.subClassOf.asNode
  val rdfsLabel = RDFS.label.asNode
  val owlOnProperty = OWL2.onProperty.asNode
  val owlAllValuesFrom = OWL2.allValuesFrom.asNode

  val AnatomicalEntity = Class("http://purl.obolibrary.org/obo/HAO_0000000")
  val Head = Class("http://purl.obolibrary.org/obo/HAO_0000397")
  val Color = Class("http://purl.obolibrary.org/obo/PATO_0000014")
}