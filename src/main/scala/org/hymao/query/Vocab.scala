package org.hymao.query

import org.phenoscape.scowl.OWL._
import com.hp.hpl.jena.vocabulary.RDF
import com.hp.hpl.jena.vocabulary.RDFS
import com.hp.hpl.jena.vocabulary.OWL2
import com.hp.hpl.jena.graph.NodeFactory
import com.hp.hpl.jena.graph.Node

object Vocab {

  val bearer_of = ObjectProperty("http://purl.obolibrary.org/obo/BFO_0000053")
  val inheres_in = ObjectProperty("http://purl.obolibrary.org/obo/BFO_0000052")
  val has_part = ObjectProperty("http://purl.obolibrary.org/obo/BFO_0000051")
  val part_of = ObjectProperty("http://purl.obolibrary.org/obo/BFO_0000050")
  val denotes = ObjectProperty("http://purl.obolibrary.org/obo/IAO_0000219")

  val rdfType = RDF.`type`.asNode
  val rdfsSubClassOf = RDFS.subClassOf.asNode
  val rdfsLabel = RDFS.label.asNode
  val owlOnProperty = OWL2.onProperty.asNode
  val owlAllValuesFrom = OWL2.allValuesFrom.asNode

  val Head = Class("http://purl.obolibrary.org/obo/HAO_0000397")
  val Color = Class("http://purl.obolibrary.org/obo/PATO_0000014")
}